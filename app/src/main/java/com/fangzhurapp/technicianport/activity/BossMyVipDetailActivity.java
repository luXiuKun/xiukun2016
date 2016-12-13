package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BossVIPBean;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossMyVipDetailActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.img_bossmyvipdetail)
    ImageView imgBossmyvipdetail;
    @Bind(R.id.tv_myvipdetail_vipnum)
    TextView tvMyvipdetailVipnum;
    @Bind(R.id.tv_myvipdetail_kktime)
    TextView tvMyvipdetailKktime;
    @Bind(R.id.tv_myvipdetail_name)
    TextView tvMyvipdetailName;
    @Bind(R.id.tv_myvipdetail_viptype)
    TextView tvMyvipdetailViptype;
    @Bind(R.id.tv_myvipdetail_kkprice)
    TextView tvMyvipdetailKkprice;
    @Bind(R.id.tv_myvipdetail_syprice)
    TextView tvMyvipdetailSyprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_my_vip_detail);
        ButterKnife.bind(this);

        initView();

        initEvent();
    }

    private void initEvent() {

        imgLogo.setOnClickListener(this);

    }

    private void initView() {

        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("我的会员");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        if (getIntent() != null){

            BossVIPBean bean = (BossVIPBean) getIntent().getSerializableExtra("bossmyvipdetail");

            tvMyvipdetailVipnum.setText(bean.getMnumber());
            tvMyvipdetailKktime.setText(bean.getAddtime());
            tvMyvipdetailName.setText(bean.getName());
            tvMyvipdetailViptype.setText(bean.getK_name());
            tvMyvipdetailKkprice.setText(bean.getJ_money());
            tvMyvipdetailSyprice.setText(bean.getMoney());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.img_logo:

                BossMyVipDetailActivity.this.finish();
                break;
        }
    }
}
