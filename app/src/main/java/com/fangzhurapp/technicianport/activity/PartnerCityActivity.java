package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PartnerCityActivity extends AppCompatActivity {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.lv_partner_city)
    ListView lvPartnerCity;
    @Bind(R.id.rl_partnercity_nodata)
    RelativeLayout rlPartnercityNodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_city);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartnerCityActivity.this.finish();
            }
        });

    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("我的城市");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
        lvPartnerCity.setEmptyView(rlPartnercityNodata);
    }
}
