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
import com.fangzhurapp.technicianport.PartnerActivity;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.utils.ToastUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoginActivity extends Activity implements View.OnClickListener {

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
    @Bind(R.id.tv_login_port)
    TextView tvLoginPort;
    @Bind(R.id.tv_login_changeident)
    TextView tvLoginChangeident;
    @Bind(R.id.tv_login_register)
    TextView tvLoginRegister;
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


        ident = SpUtil.getString(LoginActivity.this, "selectident", "");

        if (ident.equals("1")) {
            tvLoginPort.setText("技师端");
        } else if (ident.equals("2")) {
            tvLoginPort.setText("Boss端");
        } else {
            tvLoginPort.setText("合伙人");
        }
    }

    private void initEvent() {
        if (!TextUtils.isEmpty(SpUtil.getString(LoginActivity.this, "phone", ""))) {
            etLoginPhone.setText(SpUtil.getString(LoginActivity.this, "phone", ""));
        }
        imgLoginSeepw.setOnClickListener(this);
        ibLogin.setOnClickListener(this);
        loginTvForgetPw.setOnClickListener(this);
        tvLoginChangeident.setOnClickListener(this);
        tvLoginRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_login_seepw:

                if (seePassword) {
                    etLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
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

            case R.id.tv_login_changeident:

                Intent intent1 = new Intent(LoginActivity.this, SelectIdent.class);
                startActivity(intent1);
                LoginActivity.this.finish();


                break;

            case R.id.tv_login_register:
                Intent reg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(reg);
                break;


        }

    }

    private void login() {

        if (!TextUtils.isEmpty(etLoginPhone.getText().toString()) &&
                !TextUtils.isEmpty(etLoginPassword.getText().toString())) {
            if (etLoginPhone.getText().toString().trim().length() == 11 && etLoginPassword.length() >= 6) {

                if (ident.equals("1")) {
                    Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.LOGIN, RequestMethod.POST);
                    jsonObjectRequest.add("phone", etLoginPhone.getText().toString());
                    jsonObjectRequest.add("passwd", etLoginPassword.getText().toString());
                    CallServer.getInstance().add(LoginActivity.this, jsonObjectRequest, callback, UrlTag.LOGIN, true, false, true);
                } else if (ident.equals("2")) {

                    Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_LOGIN, RequestMethod.POST);
                    jsonObjectRequest.add("account", etLoginPhone.getText().toString());
                    jsonObjectRequest.add("password", etLoginPassword.getText().toString());
                    jsonObjectRequest.add("identity", "1");
                    CallServer.getInstance().add(LoginActivity.this, jsonObjectRequest, callback, UrlTag.BOSS_LOGIN, true, false, true);


                }else if (ident.equals("3")){

                    //合伙人
                    Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_LOGIN, RequestMethod.POST);
                    jsonObjectRequest.add("account", etLoginPhone.getText().toString());
                    jsonObjectRequest.add("password", etLoginPassword.getText().toString());
                    jsonObjectRequest.add("identity", "2");
                    CallServer.getInstance().add(LoginActivity.this, jsonObjectRequest, callback, UrlTag.PARTNER_LOGIN, true, false, true);
                }


            } else {
                if (etLoginPhone.getText().toString().trim().length() != 11) {

                    Toast.makeText(LoginActivity.this, "请输入合法的手机号", Toast.LENGTH_SHORT).show();
                } else if (etLoginPassword.length() < 6) {

                    Toast.makeText(LoginActivity.this, "密码长度不够", Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            Toast.makeText(LoginActivity.this, "手机号或者密码不能为空!", Toast.LENGTH_SHORT).show();
        }


    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.LOGIN) {
                LogUtil.d(TAG, response.toString());

                try {
                    JSONObject jsonObject = response.get();
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")) {
                        SpUtil.putString(LoginActivity.this, "phone", etLoginPhone.getText().toString());
                        JSONObject data = jsonObject.getJSONObject("data");
                        String id = data.getString("id");
                        String sid = data.getString("sid");
                        String id_number = data.getString("id_number");
                        String name = data.getString("name");
                        String sex = data.getString("sex");

                        SpUtil.putString(LoginActivity.this, "id", id);
                        SpUtil.putString(LoginActivity.this, "sid", sid);
                        SpUtil.putString(LoginActivity.this, "id_number", id_number);
                        SpUtil.putString(LoginActivity.this, "name", name);
                        SpUtil.putString(LoginActivity.this, "sex", sex);


                        String is_chongzhi = data.getString("is_chongzhi");
                        if (is_chongzhi.equals("1")) {
                            //首次登陆充值密码

                            SpUtil.putString(LoginActivity.this, "password", etLoginPassword.getText().toString());

                            Intent intent = new Intent(LoginActivity.this, RestartPWActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        } else {
                            SpUtil.putString(LoginActivity.this, "password", etLoginPassword.getText().toString());
                            SpUtil.putString(LoginActivity.this, "ident", "js");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "请检查手机号或密码是否正确!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == UrlTag.BOSS_LOGIN) {

                LogUtil.d(TAG, response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")) {
                        SpUtil.putString(LoginActivity.this, "phone", etLoginPhone.getText().toString());
                        JSONObject data = jsonObject.getJSONObject("data");
                        String id = data.getString("id");
                        String is_chongzhi = data.getString("is_chongzhi");
                        String name = data.getString("name");
                        String shenfen = data.getString("shenfen");
                        String sid = data.getString("sid");

                        SpUtil.putString(LoginActivity.this, "shenfen", shenfen);
                        SpUtil.putString(LoginActivity.this, "id", id);
                        SpUtil.putString(LoginActivity.this, "sid", sid);
                        SpUtil.putString(LoginActivity.this, "name", name);
                        if (is_chongzhi.equals("1")) {
                            SpUtil.putString(LoginActivity.this, "password", etLoginPassword.getText().toString());
                            Intent intent = new Intent(LoginActivity.this, BossRestartPwActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        } else {
                            SpUtil.putString(LoginActivity.this, "ident", "boss");
                            SpUtil.putString(LoginActivity.this, "password", etLoginPassword.getText().toString());
                            Intent intent = new Intent(LoginActivity.this, BossMainActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, "请检查手机号或密码是否正确!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else if (what == UrlTag.PARTNER_LOGIN){


                LogUtil.d(TAG,response.toString());



                JSONObject jsonObject = response.get();
                try {
                    if (jsonObject.getString("sucess").equals("1")){
                        SpUtil.putString(LoginActivity.this, "phone", etLoginPhone.getText().toString());
                        SpUtil.putString(LoginActivity.this, "password", etLoginPassword.getText().toString());
                        SpUtil.putString(LoginActivity.this, "ident", "partner");
                        JSONObject data = jsonObject.getJSONObject("data");


                        SpUtil.putString(LoginActivity.this,"uname",data.getString("account"));
                        SpUtil.putString(LoginActivity.this,"invite_people",data.getString("invite_people"));
                        SpUtil.putString(LoginActivity.this,"gold",data.getString("identity_gold"));
                        SpUtil.putString(LoginActivity.this,"shenfen",data.getString("identity_admin_type"));//1boss2股东
                        SpUtil.putString(LoginActivity.this,"city",data.getString("identity_city"));
                        SpUtil.putString(LoginActivity.this,"partnerid",data.getString("id"));
                        SpUtil.putString(LoginActivity.this,"cityname",data.getString("city"));
                        Intent intent = new Intent(LoginActivity.this, PartnerActivity.class);
                        startActivity(intent);


                    }else{
                        ToastUtil.showToast(LoginActivity.this,"登录失败，请检查用户名密码");
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
