package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
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
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 重置密码
 */
public class RestartPWActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_title_back)
    ImageView imgTitleBack;
    @Bind(R.id.tv_title_text)
    TextView tvTitleText;
    @Bind(R.id.et_newpw)
    EditText etNewpw;
    @Bind(R.id.img_restartpw_newpwsee)
    ImageView imgRestartpwNewpwsee;
    @Bind(R.id.et_confirmpw)
    EditText etConfirmpw;
    @Bind(R.id.img_restartpw_confirmpwsee)
    ImageView imgRestartpwConfirmpwsee;
    @Bind(R.id.ib_restart_confirm)
    ImageButton ibRestartConfirm;

    private Boolean SEE_NEWPW = true;
    private Boolean SEE_CONFIRMPW = true;

    private static final String TAG = "RestartPWActivity";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restart_pw);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initEvent();
    }

    private void initEvent() {
        imgTitleBack.setOnClickListener(this);
        tvTitleText.setText("重置密码");
        imgRestartpwNewpwsee.setOnClickListener(this);
        imgRestartpwConfirmpwsee.setOnClickListener(this);
        ibRestartConfirm.setOnClickListener(this);
        if (getIntent() != null){

            id = getIntent().getStringExtra("id");



        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_title_back:
                RestartPWActivity.this.finish();
                break;
            case R.id.ib_restart_confirm:
                confirm();
                break;

            case R.id.img_restartpw_newpwsee:
                seeNewpw();
                break;
            case R.id.img_restartpw_confirmpwsee:
                seeConfirmPw();
                break;
        }

    }

    private void seeConfirmPw() {

        if (SEE_CONFIRMPW){
            etConfirmpw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            etConfirmpw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        SEE_CONFIRMPW = !SEE_CONFIRMPW;

        CharSequence charSequence = etConfirmpw.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    private void seeNewpw() {

        if (SEE_NEWPW){
            etNewpw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else {
            etNewpw.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        SEE_NEWPW = !SEE_NEWPW;

        CharSequence charSequence = etNewpw.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    private void confirm() {

        if (!TextUtils.isEmpty(etNewpw.getText().toString()) &&
                !TextUtils.isEmpty(etConfirmpw.getText().toString())){

            if (etNewpw.getText().toString().equals(etConfirmpw.getText().toString())){

                if (etConfirmpw.getText().toString().length() < 6){

                    Toast.makeText(RestartPWActivity.this, "密码长度不够", Toast.LENGTH_SHORT).show();
                }else {
                    Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.CHANGE_PW, RequestMethod.POST);
                    jsonObjectRequest.add("phone", SpUtil.getString(RestartPWActivity.this,"phone",""));
                    jsonObjectRequest.add("passwd", SpUtil.getString(RestartPWActivity.this,"password",""));
                    jsonObjectRequest.add("passwds", etConfirmpw.getText().toString());
                    jsonObjectRequest.add("id",id);
                    CallServer.getInstance().add(RestartPWActivity.this,jsonObjectRequest,callback, UrlTag.CHANGE_PW,true,false,true);
                }


            }else{
                Toast.makeText(RestartPWActivity.this, "两次密码不一致!", Toast.LENGTH_SHORT).show();
            }


        }else{

            if (TextUtils.isEmpty(etNewpw.getText().toString())){
                Toast.makeText(RestartPWActivity.this, "请填写新密码!", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(etConfirmpw.getText().toString())){
                Toast.makeText(RestartPWActivity.this, "请填写确认密码!", Toast.LENGTH_SHORT).show();
            }

        }


    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.CHANGE_PW){

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        String isok = data.getString("isok");
                        if (isok.equals("1")){
                            Toast.makeText(RestartPWActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RestartPWActivity.this, LoginActivity.class);
                            startActivity(intent);
                            RestartPWActivity.this.finish();
                        }else{

                        }


                    }else {
                        Toast.makeText(RestartPWActivity.this, "修改失败", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {

        }
    };
}
