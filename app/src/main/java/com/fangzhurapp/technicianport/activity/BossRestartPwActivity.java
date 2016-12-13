package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossRestartPwActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_title_back)
    ImageView imgTitleBack;
    @Bind(R.id.tv_title_text)
    TextView tvTitleText;
    @Bind(R.id.et_boss_respw_code)
    EditText etBossRespwCode;
    @Bind(R.id.btn_boss_respw_getcode)
    Button btnBossRespwGetcode;
    @Bind(R.id.et_boss_respw_password)
    EditText etBossRespwPassword;
    @Bind(R.id.et_boss_respw_confirmpw)
    EditText etBossRespwConfirmpw;
    @Bind(R.id.ib_boss_respw_confirm)
    ImageButton ibBossRespwConfirm;

    private static final String TAG = "BossRestartPwActivity";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_restart_pw);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgTitleBack.setOnClickListener(this);
        ibBossRespwConfirm.setOnClickListener(this);
        btnBossRespwGetcode.setOnClickListener(this);
    }

    private void initView() {
        tvTitleText.setText("重置密码");

        if (getIntent() != null){
            id = getIntent().getStringExtra("id");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_title_back:
                BossRestartPwActivity.this.finish();
                break;

            case R.id.btn_boss_respw_getcode:
                getCode();
                break;

            case R.id.ib_boss_respw_confirm:
                retrievePw();
                break;
        }
    }

    private void retrievePw() {
        if (
                !TextUtils.isEmpty(etBossRespwCode.getText().toString()) &&
                !TextUtils.isEmpty(etBossRespwPassword.getText().toString()) &&
                !TextUtils.isEmpty(etBossRespwConfirmpw.getText().toString())){

            if (etBossRespwPassword.getText().toString().equals(etBossRespwConfirmpw.getText().toString()
             )){

                if (etBossRespwConfirmpw.getText().length() < 6){

                    Toast.makeText(BossRestartPwActivity.this, "密码长度不够", Toast.LENGTH_SHORT).show();
                }else{
                    Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.RETRIEVE_PW, RequestMethod.POST);
                    jsonObjectRequest.add("phone",SpUtil.getString(BossRestartPwActivity.this,"phone",""));
                    jsonObjectRequest.add("code",etBossRespwCode.getText().toString());
                    jsonObjectRequest.add("passwd",etBossRespwConfirmpw.getText().toString());
                    CallServer.getInstance().add(BossRestartPwActivity.this,jsonObjectRequest,callback, UrlTag.RETRIEVE_PW,true,false,true);
                }


            }else{
                Toast.makeText(BossRestartPwActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            }

        }else{
             if (TextUtils.isEmpty(etBossRespwCode.getText().toString())){
                Toast.makeText(BossRestartPwActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(etBossRespwPassword.getText().toString())){
                Toast.makeText(BossRestartPwActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(etBossRespwConfirmpw.getText().toString())){
                Toast.makeText(BossRestartPwActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCode() {
        String phone = SpUtil.getString(BossRestartPwActivity.this, "phone", "");
        if (!TextUtils.isEmpty(phone) &&
                phone.length() == 11){

            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.GET_CODE, RequestMethod.POST);
            jsonObjectRequest.add("phone",phone);
            CallServer.getInstance().add(BossRestartPwActivity.this,jsonObjectRequest,callback, UrlTag.GET_CODE,true,false,true);

        }else{
            Toast.makeText(BossRestartPwActivity.this, "请输入合法的手机号", Toast.LENGTH_SHORT).show();
        }
    }
    
    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            
            if (what == UrlTag.GET_CODE){
                LogUtil.d(TAG,response.toString());
                Toast.makeText(BossRestartPwActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                TimeTimer timeTimer = new TimeTimer(60000, 1000);
                timeTimer.start();
            }else if (what == UrlTag.RETRIEVE_PW){

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        String isok = data.getString("isok");
                        if (isok.equals("1")){
                            Toast.makeText(BossRestartPwActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            BossRestartPwActivity.this.finish();
                        }else{
                            Toast.makeText(BossRestartPwActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        JSONObject msg = jsonObject.getJSONObject("msg");

                        String fanhui = msg.getString("fanhui");
                        Toast.makeText(BossRestartPwActivity.this, fanhui, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            
        }

        @Override
        public void onFailed(int what,  Response<JSONObject> response) {

        }
    };


    /**
     * 倒计时
     */

    class TimeTimer extends CountDownTimer {

        public TimeTimer(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {//计时
            btnBossRespwGetcode.setEnabled(false);
            btnBossRespwGetcode.setBackgroundResource(R.drawable.btn_gray);
            btnBossRespwGetcode.setText(l / 1000 + "s");
        }

        @Override
        public void onFinish() {//计时完毕
            btnBossRespwGetcode.setEnabled(true);
            btnBossRespwGetcode.setText("获取验证码");
            btnBossRespwGetcode.setBackgroundResource(R.drawable.select_getcode);
        }
    }
}
