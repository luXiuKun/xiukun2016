package com.fangzhurapp.technicianport.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

import com.fangzhurapp.technicianport.BossMainActivity;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.MainActivity;
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


public class LoginActivity extends Activity implements View.OnClickListener{

    @Bind(R.id.et_login_phone)
    EditText etLoginPhone;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.img_login_seepw)
    ImageView imgLoginSeepw;
    @Bind(R.id.login_tv_forget_pw)
    TextView loginTvForgetPw;
    @Bind(R.id.ib_login)
    ImageButton ibLogin;
    private Boolean seePassword = true;
    private static final String TAG = "LoginActivity";
    private String ident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
        initEvent();

    }

    private void initView() {



        ident = SpUtil.getString(LoginActivity.this,"ident","");
    }

    private void initEvent() {
        if (!TextUtils.isEmpty(SpUtil.getString(LoginActivity.this,"phone",""))){
            etLoginPhone.setText(SpUtil.getString(LoginActivity.this,"phone",""));
        }
        imgLoginSeepw.setOnClickListener(this);
        ibLogin.setOnClickListener(this);
        loginTvForgetPw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_login_seepw:

                if (seePassword){
                    etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    etLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                seePassword = !seePassword;

                CharSequence charSequence = etLoginPassword.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
                break;
            case R.id.ib_login:
                login();
                break;
            case R.id.login_tv_forget_pw:
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;


        }

    }

    private void login() {

        if (!TextUtils.isEmpty(etLoginPhone.getText().toString()) &&
                !TextUtils.isEmpty(etLoginPassword.getText().toString())){
            if (etLoginPhone.getText().toString().trim().length() == 11 && etLoginPassword.length() >= 6){

                if (ident.equals("1")){
                    Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.LOGIN, RequestMethod.POST);
                    jsonObjectRequest.add("phone",etLoginPhone.getText().toString());
                    jsonObjectRequest.add("passwd",etLoginPassword.getText().toString());
                    CallServer.getInstance().add(LoginActivity.this,jsonObjectRequest,callback, UrlTag.LOGIN,true,false,true);
                }else if (ident.equals("2")){

                    Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_LOGIN, RequestMethod.POST);
                    jsonObjectRequest.add("phone",etLoginPhone.getText().toString());
                    jsonObjectRequest.add("passwd",etLoginPassword.getText().toString());
                    CallServer.getInstance().add(LoginActivity.this,jsonObjectRequest,callback, UrlTag.BOSS_LOGIN,true,false,true);

                    /*Intent intent = new Intent(LoginActivity.this, BossMainActivity.class);
                    startActivity(intent);*/
                }


            }else{
                if (etLoginPhone.getText().toString().trim().length() != 11){

                    Toast.makeText(LoginActivity.this, "请输入合法的手机号", Toast.LENGTH_SHORT).show();
                }else if (etLoginPassword.length() < 6){

                    Toast.makeText(LoginActivity.this, "密码长度不够", Toast.LENGTH_SHORT).show();
                }
            }

        }else{
            Toast.makeText(LoginActivity.this, "手机号或者密码不能为空!", Toast.LENGTH_SHORT).show();
        }



    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.LOGIN){
                LogUtil.d(TAG,response.toString());
                try {
                    JSONObject jsonObject = response.get();
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        String id = data.getString("id");
                        String sid = data.getString("sid");
                        String id_number = data.getString("id_number");
                        String name = data.getString("name");
                        String sex = data.getString("sex");

                        SpUtil.putString(LoginActivity.this,"id",id);
                        SpUtil.putString(LoginActivity.this,"sid",sid);
                        SpUtil.putString(LoginActivity.this,"id_number",id_number);
                        SpUtil.putString(LoginActivity.this,"name",name);
                        SpUtil.putString(LoginActivity.this,"sex",sex);



                        String is_chongzhi = data.getString("is_chongzhi");
                        if (is_chongzhi.equals("1")){
                            //首次登陆充值密码
                            SpUtil.putString(LoginActivity.this,"phone",etLoginPhone.getText().toString());
                            SpUtil.putString(LoginActivity.this,"password",etLoginPassword.getText().toString());

                            Intent intent = new Intent(LoginActivity.this, RestartPWActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }else{
                            SpUtil.putString(LoginActivity.this,"phone",etLoginPhone.getText().toString());
                            SpUtil.putString(LoginActivity.this,"ident","js");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }

                    }else{
                        Toast.makeText(LoginActivity.this, "登录失败!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (what == UrlTag.BOSS_LOGIN){

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")){
                        SpUtil.putString(LoginActivity.this,"phone",etLoginPhone.getText().toString());
                        JSONObject data = jsonObject.getJSONObject("data");
                        String id = data.getString("id");
                        String is_chongzhi = data.getString("is_chongzhi");
                        String name = data.getString("name");
                        String shenfen = data.getString("shenfen");
                        String sid = data.getString("sid");

                        SpUtil.putString(LoginActivity.this,"shenfen",shenfen);
                        SpUtil.putString(LoginActivity.this,"id",id);
                        SpUtil.putString(LoginActivity.this,"sid",sid);
                        SpUtil.putString(LoginActivity.this,"name",name);
                        if (is_chongzhi.equals("1")){
                            Intent intent = new Intent(LoginActivity.this, BossRestartPwActivity.class);
                            startActivity(intent);
                        }else{
                            SpUtil.putString(LoginActivity.this,"ident","boss");
                            Intent intent = new Intent(LoginActivity.this, BossMainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }

                    }else{
                        Toast.makeText(LoginActivity.this, "登录失败,请检查账号和密码是否正确", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }
    };
}
