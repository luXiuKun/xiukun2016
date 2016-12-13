package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.utils.SpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BindSucessActivty extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.ib_bindsucess_setpw)
    ImageButton ibBindsucessSetpw;
    private String STATE ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_sucess_activty);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        ibBindsucessSetpw.setOnClickListener(this);
        imgLogo.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("绑定银行卡");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
        String pwstate = SpUtil.getString(BindSucessActivty.this, "pwstate", "");
        if (TextUtils.isEmpty(pwstate)){
            STATE = "1";
            ibBindsucessSetpw.setBackgroundResource(R.drawable.img_bindsucess_setpw);
        }else if (pwstate.equals("1")){
            STATE = "2";
            ibBindsucessSetpw.setBackgroundResource(R.drawable.img_txsucessbtn_bg);

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.ib_bindsucess_setpw:
                //已经设置过密码


                    if (STATE.equals("1")){

                        Intent intent = new Intent(BindSucessActivty.this, SetPayPWActivity.class);
                        startActivity(intent);
                        BindSucessActivty.this.finish();
                    }else if (STATE.equals("2")){

                        BindSucessActivty.this.finish();
                    }

                break;

            case R.id.img_logo:
                BindSucessActivty.this.finish();
                break;
        }
    }
}
