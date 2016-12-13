package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.fangzhurapp.technicianport.BossMainActivity;
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

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {

        timer = new Timer(2000, 1000);
        timer.start();

    }


    class Timer extends CountDownTimer{



        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {


        }

        @Override
        public void onFinish() {




            if (SpUtil.getBoolean(SplashActivity.this,"firstlogin",true)){


                Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();

            }else {
                if (!TextUtils.isEmpty(SpUtil.getString(SplashActivity.this,"selectident",""))){
                    //技师
                    if (SpUtil.getString(SplashActivity.this,"selectident","").equals("1")){

                        if (!TextUtils.isEmpty(SpUtil.getString(SplashActivity.this,"phone",""))
                                && !TextUtils.isEmpty(SpUtil.getString(SplashActivity.this,"password",""))){
                            timer.cancel();
                            jsLogin();

                        }else{
                            timer.cancel();
                            Intent intent = new Intent(SplashActivity.this, SelectIdent.class);
                            startActivity(intent);
                            SplashActivity.this.finish();

                        }

                    }else if (SpUtil.getString(SplashActivity.this,"selectident","").equals("2")){

                        if (!TextUtils.isEmpty(SpUtil.getString(SplashActivity.this,"phone",""))
                                && !TextUtils.isEmpty(SpUtil.getString(SplashActivity.this,"password",""))){
                            timer.cancel();
                            bossLogin("1");

                        }else{
                            timer.cancel();
                            Intent intent = new Intent(SplashActivity.this, SelectIdent.class);
                            startActivity(intent);
                            SplashActivity.this.finish();

                        }
                    }else if (SpUtil.getString(SplashActivity.this,"selectident","").equals("3")){

                        if (!TextUtils.isEmpty(SpUtil.getString(SplashActivity.this,"phone",""))
                                && !TextUtils.isEmpty(SpUtil.getString(SplashActivity.this,"password",""))){
                            timer.cancel();
                            bossLogin("2");

                        }else{
                            timer.cancel();
                            Intent intent = new Intent(SplashActivity.this, SelectIdent.class);
                            startActivity(intent);
                            SplashActivity.this.finish();

                        }

                    }

                }else{
                    timer.cancel();
                    Intent intent = new Intent(SplashActivity.this, SelectIdent.class);
                    startActivity(intent);
                    SplashActivity.this.finish();

                }

            }

        }
    }

    private void partnerLogin() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.PARTNER_LOGIN, RequestMethod.POST);
        jsonObjectRequest.add("account", SpUtil.getString(SplashActivity.this,"phone",""));
        jsonObjectRequest.add("password", SpUtil.getString(SplashActivity.this,"password",""));
        CallServer.getInstance().add(SplashActivity.this, jsonObjectRequest, callback, UrlTag.PARTNER_LOGIN, true, false, true);
    }

    /**
     * boss自动登录
     */
    private void bossLogin(String ident) {

        if (ident.equals("1")){

            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_LOGIN, RequestMethod.POST);
            jsonObjectRequest.add("account",SpUtil.getString(SplashActivity.this,"phone",""));
            jsonObjectRequest.add("password",SpUtil.getString(SplashActivity.this,"password",""));
            jsonObjectRequest.add("identity",ident);
            CallServer.getInstance().add(SplashActivity.this,jsonObjectRequest,callback, UrlTag.BOSS_LOGIN,false,false,true);
        }else{
            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_LOGIN, RequestMethod.POST);
            jsonObjectRequest.add("account",SpUtil.getString(SplashActivity.this,"phone",""));
            jsonObjectRequest.add("password",SpUtil.getString(SplashActivity.this,"password",""));
            jsonObjectRequest.add("identity",ident);
            CallServer.getInstance().add(SplashActivity.this,jsonObjectRequest,callback, UrlTag.PARTNER_LOGIN,false,false,true);
        }

    }

    /**
     * 技师自动登录
     */
    private void jsLogin() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.LOGIN, RequestMethod.POST);
        jsonObjectRequest.add("phone",SpUtil.getString(SplashActivity.this,"phone",""));
        jsonObjectRequest.add("passwd",SpUtil.getString(SplashActivity.this,"password",""));
        CallServer.getInstance().add(SplashActivity.this,jsonObjectRequest,callback, UrlTag.LOGIN,false,false,true);

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

                        SpUtil.putString(SplashActivity.this,"id",id);
                        SpUtil.putString(SplashActivity.this,"sid",sid);
                        SpUtil.putString(SplashActivity.this,"id_number",id_number);
                        SpUtil.putString(SplashActivity.this,"name",name);
                        SpUtil.putString(SplashActivity.this,"sex",sex);



                        String is_chongzhi = data.getString("is_chongzhi");
                        if (is_chongzhi.equals("1")){
                            //首次登陆充值密码
                            timer.cancel();
                            Intent intent = new Intent(SplashActivity.this, RestartPWActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }else{
                            timer.cancel();
                            SpUtil.putString(SplashActivity.this,"ident","js");
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }

                    }else{
                        timer.cancel();
                        Toast.makeText(SplashActivity.this, "请检查手机号或密码是否正确!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashActivity.this, SelectIdent.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
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
                        JSONObject data = jsonObject.getJSONObject("data");
                        String id = data.getString("id");
                        String is_chongzhi = data.getString("is_chongzhi");
                        String name = data.getString("name");
                        String shenfen = data.getString("shenfen");
                        String sid = data.getString("sid");

                        SpUtil.putString(SplashActivity.this,"shenfen",shenfen);
                        SpUtil.putString(SplashActivity.this,"id",id);
                        SpUtil.putString(SplashActivity.this,"sid",sid);
                        SpUtil.putString(SplashActivity.this,"name",name);
                        if (is_chongzhi.equals("1")){
                            timer.cancel();
                            Intent intent = new Intent(SplashActivity.this, BossRestartPwActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }else{
                            timer.cancel();
                            SpUtil.putString(SplashActivity.this,"ident","boss");
                            Intent intent = new Intent(SplashActivity.this, BossMainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }

                    }else{
                        timer.cancel();
                        Toast.makeText(SplashActivity.this, "请检查手机号或密码是否正确!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashActivity.this, SelectIdent.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }else if (what == UrlTag.PARTNER_LOGIN){


                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    if (jsonObject.getString("sucess").equals("1")){

                        SpUtil.putString(SplashActivity.this, "ident", "partner");
                        JSONObject data = jsonObject.getJSONObject("data");


                        SpUtil.putString(SplashActivity.this,"uname",data.getString("account"));
                        SpUtil.putString(SplashActivity.this,"gold",data.getString("identity_gold"));
                        SpUtil.putString(SplashActivity.this,"shenfen",data.getString("identity_admin_type"));//1boss2股东
                        SpUtil.putString(SplashActivity.this,"city",data.getString("identity_city"));
                        SpUtil.putString(SplashActivity.this,"partnerid",data.getString("id"));
                        SpUtil.putString(SplashActivity.this,"cityname",data.getString("city"));
                        Intent intent = new Intent(SplashActivity.this, PartnerActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();

                    }else{
                        timer.cancel();
                        Toast.makeText(SplashActivity.this, "请检查手机号或密码是否正确!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SplashActivity.this, SelectIdent.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
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
}
