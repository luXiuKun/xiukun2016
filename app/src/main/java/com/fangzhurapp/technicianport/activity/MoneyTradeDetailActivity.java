package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.moneyDetailBean;
import com.fangzhurapp.technicianport.utils.NumberUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoneyTradeDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_tradedetail_trademoney)
    TextView tvTradedetailTrademoney;
    @Bind(R.id.tv_tradedetail_poundage)
    TextView tvTradedetailPoundage;
    @Bind(R.id.tv_tradedetail_type)
    TextView tvTradedetailType;
    @Bind(R.id.tv_tradedetail_time)
    TextView tvTradedetailTime;
    @Bind(R.id.tv_tradedetail_num)
    TextView tvTradedetailNum;
    @Bind(R.id.tv_tradedetail_tradetype)
    TextView tvTradedetailTradetype;
    @Bind(R.id.tv_tradedetail_money)
    TextView tvTradedetailMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneytradedetail);
        ButterKnife.bind(this);
        CustomApplication.addAct(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);

        tvShopname.setText("交易详情");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        if (getIntent() != null) {

            moneyDetailBean bean = (moneyDetailBean) getIntent().getSerializableExtra("tradedetail");


            if (bean.getType().equals("1")) {

                tvTradedetailTradetype.setText("出账金额");
                tvTradedetailType.setText(bean.getCname());
                tvTradedetailMoney.setText("-"+ bean.getMoney());
                tvTradedetailTime.setText(bean.getTime());
                tvTradedetailTrademoney.setText(bean.getMoney()+"元");
                tvTradedetailPoundage.setText(bean.getSmoney()+"元");
                tvTradedetailNum.setText(bean.getPayment_onumber());

            } else {
                tvTradedetailTradetype.setText("入账金额");
                tvTradedetailType.setText(bean.getCname());
                //String zmoney = NumberUtils.floatFormat(Float.valueOf(bean.getMoney()) + Float.valueOf(bean.getCounter_fee()));

                tvTradedetailMoney.setText("+"+ bean.getMoney());
                tvTradedetailTime.setText(bean.getTime());
                tvTradedetailTrademoney.setText(bean.getMoney()+"元");
                tvTradedetailPoundage.setText(bean.getSmoney()+"元");
                tvTradedetailNum.setText(bean.getPayment_onumber());
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_logo:
                MoneyTradeDetailActivity.this.finish();
                break;
        }
    }
}
