package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BossMsgBean;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossMsgDetailActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.img_bossmsgdetail)
    ImageView imgBossmsgdetail;
    @Bind(R.id.tv_bossmsgdetail_time)
    TextView tvBossmsgdetailTime;
    @Bind(R.id.tv_bossmsgdetail_name)
    TextView tvBossmsgdetailName;
    @Bind(R.id.tv_bossmsgdetail_content)
    TextView tvBossmsgdetailContent;
    private BossMsgBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_msg_detail);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("消息详情");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        imgBossmsgdetail.setBackgroundResource(R.drawable.img_orderdetail);
        if (getIntent()!= null){

            bean = (BossMsgBean) getIntent().getSerializableExtra("bossmsgdetail");

            tvBossmsgdetailTime.setText(bean.getLtime());
            tvBossmsgdetailName.setText(bean.getSname());
            String auname = bean.getAuname();
            tvBossmsgdetailContent.setText(
                    Html.fromHtml("店长"+"<font color= '#34C083'>"+auname+"</font>"+"<font>已经核对</font>"+
                            bean.getMonth()+"<font>月份员工工资请到钱包-员工工资页面核实、并发放工资</font>"));


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.img_logo:
                BossMsgDetailActivity.this.finish();
                break;
        }
    }
}
