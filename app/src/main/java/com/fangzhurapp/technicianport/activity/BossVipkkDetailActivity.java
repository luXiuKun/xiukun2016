package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BossVipkkBean;
import com.fangzhurapp.technicianport.bean.BossWxIncomeBean;

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

            if (getIntent().getStringExtra("type").equals("1")){
                BossVipkkBean bean = (BossVipkkBean) getIntent().getSerializableExtra("bossvipdetail");

                tvVipdetailVipnum.setText(bean.getMnumber());
                tvVipdetailKktime.setText(bean.getAddtime());
                tvVipdetailViptype.setText(bean.getK_name());
                tvVipdetailVipname.setText(bean.getName());
                if (!TextUtils.isEmpty(bean.getStaff_name())){

                    tvVipdetailXsstaff.setText(bean.getStaff_name());
                }else {
                    tvVipdetailXsstaff.setText("无");
                }
                tvVipdetailJstype.setText(bean.getType());
                tvVipdetailStaff.setText(bean.getStaff());
                tvVipdetailKkprice.setText(bean.getJ_money());
                tvVipdetailJstc.setText(bean.getYgtc());
            }else if (getIntent().getStringExtra("type").equals("2")){

                BossWxIncomeBean wxkk = (BossWxIncomeBean)getIntent().getParcelableExtra("wxkk");

                tvVipdetailVipnum.setText(wxkk.getMnumber());
                tvVipdetailKktime.setText(wxkk.getTime());
                tvVipdetailViptype.setText(wxkk.getXm_name());
                tvVipdetailVipname.setText(wxkk.getName());
                if (!TextUtils.isEmpty(wxkk.getStaff_name())){

                    tvVipdetailXsstaff.setText(wxkk.getStaff_name());
                }else {
                    tvVipdetailXsstaff.setText("无");
                }
                tvVipdetailJstype.setText(wxkk.getType());
                tvVipdetailStaff.setText(wxkk.getStaff());
                tvVipdetailKkprice.setText(wxkk.getMoney());
                tvVipdetailJstc.setText(wxkk.getYgtc());

            }




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
