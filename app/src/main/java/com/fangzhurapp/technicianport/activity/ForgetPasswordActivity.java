package com.fangzhurapp.technicianport.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ForgetPasswordActivity extends Activity implements View.OnClickListener{

    @Bind(R.id.img_title_back)
    ImageView imgTitleBack;
    @Bind(R.id.tv_title_text)
    TextView tvTitleText;
    @Bind(R.id.et_forgetpw_phone)
    EditText etForgetpwPhone;
    @Bind(R.id.et_forgetpw_code)
    EditText etForgetpwCode;
    @Bind(R.id.btn_forgetpw_getcode)
    Button btnForgetpwGetcode;
    @Bind(R.id.et_forgetpw_password)
    EditText etForgetpwPassword;
    @Bind(R.id.et_forgetpw_confirmpw)
    EditText etForgetpwConfirmpw;
    @Bind(R.id.ib_fotgetpw_confirm)
    ImageButton ibFotgetpwConfirm;
    private static final String TAG = "ForgetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgTitleBack.setOnClickListener(this);
        btnForgetpwGetcode.setOnClickListener(this);
        ibFotgetpwConfirm.setOnClickListener(this);
    }

    private void initView() {
        tvTitleText.setText("找回密码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_forgetpw_getcode:
                if (!TextUtils.isEmpty(etForgetpwPhone.getText().toString()) &&
                        etForgetpwPhone.getText().length() == 11){

                    Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.GET_CODE, RequestMethod.POST);
                    jsonObjectRequest.add("phone",etForgetpwPhone.getText().toString());
                    CallServer.getInstance().add(ForgetPasswordActivity.this,jsonObjectRequest,callback,UrlTag.GET_CODE,true,false,true);

                }else{
                    Toast.makeText(ForgetPasswordActivity.this, "请输入合法的手机号", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.img_title_back:
                ForgetPasswordActivity.this.finish();
                break;

            case R.id.ib_fotgetpw_confirm:
                retrievePw();
                break;
        }
    }

    private void retrievePw() {
        if (!TextUtils.isEmpty(etForgetpwPhone.getText().toString()) &&
            !TextUtils.isEmpty(etForgetpwCode.getText().toString()) &&
            !TextUtils.isEmpty(etForgetpwPassword.getText().toString()) &&
            !TextUtils.isEmpty(etForgetpwConfirmpw.getText().toString())){

            if (etForgetpwPhone.getText().length() == 11){

                if (etForgetpwPassword.getText().toString().equals(etForgetpwConfirmpw.getText().toString())
                        ){
                    if (etForgetpwPassword.getText().length() <6){
                        Toast.makeText(ForgetPasswordActivity.this, "密码长度不够", Toast.LENGTH_SHORT).show();
                    }else{

                        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.RETRIEVE_PW, RequestMethod.POST);
                        jsonObjectRequest.add("phone",etForgetpwPhone.getText().toString());
                        jsonObjectRequest.add("code",etForgetpwCode.getText().toString());
                        jsonObjectRequest.add("passwd",etForgetpwConfirmpw.getText().toString());
                        CallServer.getInstance().add(ForgetPasswordActivity.this,jsonObjectRequest,callback, UrlTag.RETRIEVE_PW,true,false,true);
                    }

                }else{
                    Toast.makeText(ForgetPasswordActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
            }else{

                Toast.makeText(ForgetPasswordActivity.this, "请输入合法手机号", Toast.LENGTH_SHORT).show();
            }


        }else{
            if (TextUtils.isEmpty(etForgetpwPhone.getText().toString())){
                Toast.makeText(ForgetPasswordActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(etForgetpwCode.getText().toString())){
                Toast.makeText(ForgetPasswordActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(etForgetpwPassword.getText().toString())){
                Toast.makeText(ForgetPasswordActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(etForgetpwConfirmpw.getText().toString())){
                Toast.makeText(ForgetPasswordActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.RETRIEVE_PW){

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        String isok = data.getString("isok");
                        if (isok.equals("1")){
                            Toast.makeText(ForgetPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            ForgetPasswordActivity.this.finish();
                        }else{
                            Toast.makeText(ForgetPasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        JSONObject msg = jsonObject.getJSONObject("msg");
                        String fanhui = msg.getString("fanhui");

                        Toast.makeText(ForgetPasswordActivity.this, fanhui, Toast.LENGTH_SHORT).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (what == UrlTag.GET_CODE){
                LogUtil.d(TAG,response.toString());
                Toast.makeText(ForgetPasswordActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                TimeTimer timeTimer = new TimeTimer(60000, 1000);
                timeTimer.start();
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

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
            btnForgetpwGetcode.setEnabled(false);
            btnForgetpwGetcode.setBackgroundResource(R.drawable.btn_gray);
            btnForgetpwGetcode.setText(l / 1000 + "s");
        }

        @Override
        public void onFinish() {//计时完毕
            btnForgetpwGetcode.setEnabled(true);
            btnForgetpwGetcode.setText("获取验证码");
            btnForgetpwGetcode.setBackgroundResource(R.drawable.select_getcode);
        }
    }
}
