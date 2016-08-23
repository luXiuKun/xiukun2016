package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BossVipkkBean;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossVipkkDetailActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_vipdetail_vipnum)
    TextView tvVipdetailVipnum;
    @Bind(R.id.tv_vipdetail_kktime)
    TextView tvVipdetailKktime;
    @Bind(R.id.tv_vipdetail_viptype)
    TextView tvVipdetailViptype;
    @Bind(R.id.tv_vipdetail_vipname)
    TextView tvVipdetailVipname;
    @Bind(R.id.tv_vipdetail_xsstaff)
    TextView tvVipdetailXsstaff;
    @Bind(R.id.tv_vipdetail_jstype)
    TextView tvVipdetailJstype;
    @Bind(R.id.tv_vipdetail_staff)
    TextView tvVipdetailStaff;
    @Bind(R.id.tv_vipdetail_kkprice)
    TextView tvVipdetailKkprice;
    @Bind(R.id.tv_vipdetail_jstc)
    TextView tvVipdetailJstc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_vipkk_detail);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("订单详情");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);


        if (getIntent() != null){

            BossVipkkBean bean = (BossVipkkBean) getIntent().getSerializableExtra("bossvipdetail");

            tvVipdetailVipnum.setText(bean.getMnumber());
            tvVipdetailKktime.setText(bean.getAddtime());
            tvVipdetailViptype.setText(bean.getK_name());
            tvVipdetailVipname.setText(bean.getName());
            tvVipdetailXsstaff.setText(bean.getStaff());
            tvVipdetailJstype.setText(bean.getType());
            tvVipdetailStaff.setText(bean.getStaff());
            tvVipdetailKkprice.setText(bean.getJ_money());
            tvVipdetailJstc.setText(bean.getYgtc());


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.img_logo:
                BossVipkkDetailActivity.this.finish();
                break;
        }
    }
}
