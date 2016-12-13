package com.fangzhurapp.technicianport.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.view.SetPayPwDialog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossChangePayPwActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.et_changepaypw_name)
    EditText etChangepaypwName;
    @Bind(R.id.et_changepaypw_newpw)
    EditText etChangepaypwNewpw;
    @Bind(R.id.et_changepaypw_confirmpw)
    EditText etChangepaypwConfirmpw;
    @Bind(R.id.et_changepaypw_code)
    EditText etChangepaypwCode;
    @Bind(R.id.btn_changepaypw_getcode)
    Button btnChangepaypwGetcode;
    @Bind(R.id.ib_changepaypw_next)
    ImageButton ibChangepaypwNext;

    private static final String TAG = "ChangePayPwActivity";
    @Bind(R.id.tv_bosschangepaypw)
    TextView tvBosschangepaypw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pay_pw);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        CustomApplication.addAct(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        ibChangepaypwNext.setOnClickListener(this);
        btnChangepaypwGetcode.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("修改支付密码");
        tvBosschangepaypw.setText(SpUtil.getString(BossChangePayPwActivity.this,"phone",""));
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ib_changepaypw_next:

                changePaypw();
                break;


            case R.id.btn_changepaypw_getcode:

                Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.GET_CODE, RequestMethod.POST);
                jsonObjectRequest.add("phone", SpUtil.getString(BossChangePayPwActivity.this, "phone", ""));
                CallServer.getInstance().add(BossChangePayPwActivity.this, jsonObjectRequest, callBack, UrlTag.GET_CODE, true, false, true);


                break;


        }
    }

    private void changePaypw() {

        if (!TextUtils.isEmpty(etChangepaypwName.getText().toString())
                && !TextUtils.isEmpty(etChangepaypwNewpw.getText().toString())
                && !TextUtils.isEmpty(etChangepaypwConfirmpw.getText().toString())
                && !TextUtils.isEmpty(etChangepaypwCode.getText().toString())) {

            if (etChangepaypwNewpw.getText().toString().equals(etChangepaypwConfirmpw.getText().toString())
                    && etChangepaypwNewpw.length() == 6) {

                Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_CHANGE_PAY_PW, RequestMethod.POST);
                jsonObjectRequest.add("sta", "2");
                jsonObjectRequest.add("password", etChangepaypwConfirmpw.getText().toString());
                jsonObjectRequest.add("id", SpUtil.getString(BossChangePayPwActivity.this, "id", ""));
                jsonObjectRequest.add("code", etChangepaypwCode.getText().toString());

                CallServer.getInstance().add(BossChangePayPwActivity.this, jsonObjectRequest, callBack, UrlTag.BOSS_CHANGE_PAY_PW, true, false, true);
            } else {

                Toast.makeText(BossChangePayPwActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();

            }


        } else {

            if (TextUtils.isEmpty(etChangepaypwName.getText().toString())) {
                Toast.makeText(BossChangePayPwActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etChangepaypwNewpw.getText().toString())) {
                Toast.makeText(BossChangePayPwActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(etChangepaypwCode.getText().toString())) {
                Toast.makeText(BossChangePayPwActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            }

        }
    }


    private HttpCallBack<JSONObject> callBack = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.BOSS_CHANGE_PAY_PW) {

                Log.d(TAG, "onSucceed: " + response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")) {

                        SetPayPwDialog setPayPwDialog = new SetPayPwDialog(BossChangePayPwActivity.this, R.layout.dialog_changepaypwsucess);
                        setPayPwDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        setPayPwDialog.show();

                        setPayPwDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                BossChangePayPwActivity.this.finish();
                            }
                        });

                    } else {
                        Toast.makeText(BossChangePayPwActivity.this, "修改失败，请重新修改", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == UrlTag.GET_CODE) {

                Log.d(TAG, "onSucceed: " + response.toString());
                Toast.makeText(BossChangePayPwActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                Timer timer = new Timer(60000, 1000);
                timer.start();

            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {

        }
    };


    class Timer extends CountDownTimer {


        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnChangepaypwGetcode.setEnabled(false);
            btnChangepaypwGetcode.setBackgroundResource(R.drawable.btn_gray);
            btnChangepaypwGetcode.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            btnChangepaypwGetcode.setEnabled(true);
            btnChangepaypwGetcode.setBackgroundResource(R.drawable.select_getcode);
            btnChangepaypwGetcode.setText("免费获取");
        }
    }
}
