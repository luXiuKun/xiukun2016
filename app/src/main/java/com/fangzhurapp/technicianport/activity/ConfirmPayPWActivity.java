package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
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
import com.jungly.gridpasswordview.GridPasswordView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ConfirmPayPWActivity extends AppCompatActivity {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.gpv_confirmpaypw)
    GridPasswordView gpvConfirmpaypw;
    private String firstPw;

    private static final String TAG = "ConfirmPayPWActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pay_pw);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {

        gpvConfirmpaypw.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (!TextUtils.isEmpty(firstPw)){

                    if (psw.length() == 6){

                        if (firstPw.equals(psw)){
                            setPayPw();

                        }else{

                            Toast.makeText(ConfirmPayPWActivity.this, "两次密码不一致,请重新设置", Toast.LENGTH_SHORT).show();
                            ConfirmPayPWActivity.this.finish();
                        }

                    }
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
    }

    private void setPayPw() {
        if (SpUtil.getString(ConfirmPayPWActivity.this,"ident","").equals("js")){

            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.SET_PAYPW, RequestMethod.POST);
            jsonObjectRequest.add("password",firstPw);
            jsonObjectRequest.add("staff_id", SpUtil.getString(ConfirmPayPWActivity.this,"id",""));
            CallServer.getInstance().add(ConfirmPayPWActivity.this,jsonObjectRequest,callback, UrlTag.SET_PAYPW,true,false,true);

        }else if (SpUtil.getString(ConfirmPayPWActivity.this,"ident","").equals("boss")){

            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_CHANGE_PAY_PW, RequestMethod.POST);
            jsonObjectRequest.add("password",firstPw);
            jsonObjectRequest.add("id", SpUtil.getString(ConfirmPayPWActivity.this,"id",""));
            CallServer.getInstance().add(ConfirmPayPWActivity.this,jsonObjectRequest,callback, UrlTag.BOSS_CHANGE_PAY_PW,true,false,true);

        }

    }

    private void initView() {

        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("确认支付密码");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        if (getIntent() != null){
            firstPw = getIntent().getStringExtra("psw");

        }
    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {

            if (what == UrlTag.SET_PAYPW){
                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        Toast.makeText(ConfirmPayPWActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConfirmPayPWActivity.this, TXPriceActivity.class);
                        startActivity(intent);
                        ConfirmPayPWActivity.this.finish();
                        SetPayPWActivity.SETPAYPWACTIVITY.finish();

                    }else{
                        Toast.makeText(ConfirmPayPWActivity.this, "设置失败", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (what == UrlTag.BOSS_CHANGE_PAY_PW){

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        Toast.makeText(ConfirmPayPWActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConfirmPayPWActivity.this, BossTXPriceActivity.class);
                        startActivity(intent);
                        ConfirmPayPWActivity.this.finish();
                        SetPayPWActivity.SETPAYPWACTIVITY.finish();

                    }else{
                        Toast.makeText(ConfirmPayPWActivity.this, "设置失败", Toast.LENGTH_SHORT).show();

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
