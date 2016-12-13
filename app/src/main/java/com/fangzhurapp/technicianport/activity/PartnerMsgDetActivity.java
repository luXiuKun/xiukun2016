package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PartnerMsgDetActivity extends AppCompatActivity {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_partnermsgdetail_time)
    TextView tvPartnermsgdetailTime;
    @Bind(R.id.tv_partnermsgdetail_title)
    TextView tvPartnermsgdetailTitle;
    @Bind(R.id.tv_partnermsgdetail_content)
    TextView tvPartnermsgdetailContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_msg_det);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("消息详情");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        if (getIntent() != null) {


            tvPartnermsgdetailTitle.setText(getIntent().getStringExtra("title"));
            tvPartnermsgdetailTime.setText(getIntent().getStringExtra("time"));
            tvPartnermsgdetailContent.setText(getIntent().getStringExtra("content"));
        }


        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartnerMsgDetActivity.this.finish();
            }
        });


    }
}
