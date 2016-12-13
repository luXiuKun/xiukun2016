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

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossGoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_goodsdetail_ordernum)
    TextView tvGoodsdetailOrdernum;
    @Bind(R.id.tv_sjysdetail_ordertime)
    TextView tvSjysdetailOrdertime;
    @Bind(R.id.tv_goodsdetail_goodsname)
    TextView tvGoodsdetailGoodsname;
    @Bind(R.id.tv_goodsdetail_staffname)
    TextView tvGoodsdetailStaffname;
    @Bind(R.id.tv_goodsdetail_jstype)
    TextView tvGoodsdetailJstype;
    @Bind(R.id.tv_goodsdetail_staff)
    TextView tvGoodsdetailStaff;
    @Bind(R.id.tv_goodsdetail_goodsprice)
    TextView tvGoodsdetailGoodsprice;
    @Bind(R.id.tv_goodsdetail_stafftc)
    TextView tvGoodsdetailStafftc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_wxincome_goods_detail);
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

        if (getIntent() != null){

            if (getIntent().getStringExtra("goodstype").equals("1")){
                BossSJYSBean bean = (BossSJYSBean) getIntent().getSerializableExtra("goodsdetail");
                tvGoodsdetailOrdernum.setText(bean.getOnumber());
                tvSjysdetailOrdertime.setText(bean.getStime());

                tvGoodsdetailGoodsname.setText(bean.getXm_name());

                if (!TextUtils.isEmpty(bean.getStaff_name())){

                    tvGoodsdetailStaffname.setText(bean.getStaff_name());
                }else{
                    tvGoodsdetailStaffname.setText("无");

                }
                tvGoodsdetailJstype.setText(bean.getSet_type());

                tvGoodsdetailStaff.setText(bean.getStaff());

                tvGoodsdetailGoodsprice.setText(bean.getMoney());
                tvGoodsdetailStafftc.setText(bean.getYgtc());
            }else{

                BossWxIncomeBean bean = (BossWxIncomeBean) getIntent().getParcelableExtra("goodsdetail1");
                tvGoodsdetailOrdernum.setText(bean.getOnumber());
                tvSjysdetailOrdertime.setText(bean.getTime());

                tvGoodsdetailGoodsname.setText(bean.getXm_name());

                if (!TextUtils.isEmpty(bean.getStaff_name())){

                    tvGoodsdetailStaffname.setText(bean.getStaff_name());
                }else{
                    tvGoodsdetailStaffname.setText("无");

                }
                tvGoodsdetailJstype.setText(bean.getType());

                tvGoodsdetailStaff.setText(bean.getSet_type());

                tvGoodsdetailGoodsprice.setText(bean.getMoney());
                tvGoodsdetailStafftc.setText(bean.getYgtc());
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_logo:
                BossGoodsDetailActivity.this.finish();
                break;

        }
    }
}
