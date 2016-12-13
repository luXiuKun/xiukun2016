package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BossStaffTcBean;
import com.fangzhurapp.technicianport.utils.NumberUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossStaffTcDetailActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.img_bossstaff)
    ImageView imgBossstaff;
    @Bind(R.id.tv_bossstaff_num)
    TextView tvBossstaffNum;
    @Bind(R.id.tv_bossstaff_staffname)
    TextView tvBossstaffStaffname;
    @Bind(R.id.tv_bossstaff_pztc)
    TextView tvBossstaffPztc;
    @Bind(R.id.tv_bossstaff_dztc)
    TextView tvBossstaffDztc;
    @Bind(R.id.tv_bossstaff_xstc)
    TextView tvBossstaffXstc;
    @Bind(R.id.tv_bossstaff_ztc)
    TextView tvBossstaffZtc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_staff_tc_detail);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("员工提成");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);


        if (getIntent() != null) {

            BossStaffTcBean bean = (BossStaffTcBean) getIntent().getSerializableExtra("bossstafftc");

            tvBossstaffNum.setText(bean.getId_number());
            tvBossstaffStaffname.setText(bean.getName());
            tvBossstaffPztc.setText(bean.getPztc());
            tvBossstaffDztc.setText(bean.getDztc());
            tvBossstaffXstc.setText(bean.getKktc());
           // String tc = NumberUtils.floatFormat(Float.valueOf(bean.getKktc()) + Float.valueOf(bean.getDztc()) + Float.valueOf(bean.getPztc()));
            tvBossstaffZtc.setText(bean.getTc_count());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.img_logo:
                BossStaffTcDetailActivity.this.finish();
                break;
        }
    }
}
