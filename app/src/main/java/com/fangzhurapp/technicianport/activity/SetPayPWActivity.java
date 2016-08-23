package com.fangzhurapp.technicianport.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.jungly.gridpasswordview.GridPasswordView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SetPayPWActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.gpv_paypw)
    GridPasswordView gpvPaypw;

    public static Activity SETPAYPWACTIVITY = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pay_pw);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        SETPAYPWACTIVITY = this;
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        gpvPaypw.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (psw.length() == 6){

                    Intent intent = new Intent(SetPayPWActivity.this, ConfirmPayPWActivity.class);
                    intent.putExtra("psw",psw);
                    startActivity(intent);
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
    }

    private void initView() {

        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("设置支付密码");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_logo:

                SetPayPWActivity.this.finish();
                break;


        }
    }
}
