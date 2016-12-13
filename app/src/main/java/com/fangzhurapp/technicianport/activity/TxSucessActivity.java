package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TxSucessActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.ib_txsucess_confirm)
    ImageButton ibTxsucessConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_sucess);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        ibTxsucessConfirm.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setVisibility(View.INVISIBLE);
        tvShopname.setText("提现");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_txsucess_confirm:

                TxSucessActivity.this.finish();
                break;
        }
    }
}
