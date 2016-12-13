package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BossSJYSBean;
import com.fangzhurapp.technicianport.bean.BossWxIncomeBean;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossSjysOrderDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.img_sjysdetail)
    ImageView imgSjysdetail;
    @Bind(R.id.tv_sjysdetail_ordernum)
    TextView tvSjysdetailOrdernum;
    @Bind(R.id.tv_sjysdetail_jstime)
    TextView tvSjysdetailJstime;
    @Bind(R.id.tv_sjysdetail_proname)
    TextView tvSjysdetailProname;
    @Bind(R.id.tv_sjysdetail_fwtime)
    TextView tvSjysdetailFwtime;
    @Bind(R.id.tv_sjysdetail_ident)
    TextView tvSjysdetailIdent;
    @Bind(R.id.tv_sjysdetail_fwjs)
    TextView tvSjysdetailFwjs;
    @Bind(R.id.tv_sjysdetail_pztype)
    TextView tvSjysdetailPztype;
    @Bind(R.id.tv_sjysdetail_jsprice)
    TextView tvSjysdetailJsprice;
    @Bind(R.id.tv_sjysdetail_jstc)
    TextView tvSjysdetailJstc;
    @Bind(R.id.tv_sjysdetail_housenum)
    TextView tvSjysdetailHousenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_sjys_order_detail);
        ButterKnife.bind(this);
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
        if (getIntent() != null) {

            if (getIntent().getStringExtra("sjystype").equals("1")){
                BossSJYSBean bean = (BossSJYSBean) getIntent().getSerializableExtra("sjysdetail");

                tvSjysdetailOrdernum.setText(bean.getOnumber());
                tvSjysdetailJstime.setText(bean.getStime());
                tvSjysdetailProname.setText(bean.getXm_name());
                tvSjysdetailFwtime.setText(bean.getFwtime()+"分钟");
                tvSjysdetailIdent.setText(bean.getSet_type());
                if (!TextUtils.isEmpty(bean.getStaff_name())){

                    tvSjysdetailFwjs.setText(bean.getStaff_name());
                }else{
                    tvSjysdetailFwjs.setText("无");
                }
                tvSjysdetailPztype.setText(bean.getType());
                tvSjysdetailJsprice.setText(bean.getMoney());
                tvSjysdetailJstc.setText(bean.getYgtc());
                tvSjysdetailHousenum.setText(bean.getRoom_number()+"房");

            }else if (getIntent().getStringExtra("sjystype").equals("2")){

                BossWxIncomeBean bean = (BossWxIncomeBean) getIntent().getParcelableExtra("wxorder");

                tvSjysdetailOrdernum.setText(bean.getOnumber());
                tvSjysdetailJstime.setText(bean.getTime());
                tvSjysdetailProname.setText(bean.getXm_name());
                tvSjysdetailFwtime.setText(bean.getFwtime()+"分钟");
                tvSjysdetailIdent.setText(bean.getSet_type());
                if (!TextUtils.isEmpty(bean.getStaff_name())){

                    tvSjysdetailFwjs.setText(bean.getStaff_name());
                }else{
                    tvSjysdetailFwjs.setText("无");

                }
                tvSjysdetailPztype.setText(bean.getType());
                tvSjysdetailJsprice.setText(bean.getMoney());
                tvSjysdetailJstc.setText(bean.getYgtc());
                tvSjysdetailHousenum.setText(bean.getRoom()+"房");
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_logo:
                BossSjysOrderDetailActivity.this.finish();
                break;

        }
    }
}
