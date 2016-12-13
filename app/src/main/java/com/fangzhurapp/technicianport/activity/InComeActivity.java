package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.frag.AccountFrag;
import com.fangzhurapp.technicianport.frag.AccountIngFrag;


import butterknife.Bind;
import butterknife.ButterKnife;

public class InComeActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.view_account)
    View viewAccount;
    @Bind(R.id.ll_account)
    LinearLayout llAccount;
    @Bind(R.id.tv_accounting)
    TextView tvAccounting;
    @Bind(R.id.view_accounting)
    View viewAccounting;
    @Bind(R.id.ll_accounting)
    LinearLayout llAccounting;
    @Bind(R.id.fl_income)
    FrameLayout flIncome;
    private FragmentManager manager;
    private AccountFrag accountFrag;
    private AccountIngFrag accountIngFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_come);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        imgLogo.setOnClickListener(this);
        tvShopname.setText("扫码收入");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
        llAccount.setOnClickListener(this);
        llAccounting.setOnClickListener(this);

        manager = getSupportFragmentManager();


        selectFrag(0);
    }

    private void selectFrag(int i) {
        FragmentTransaction transaction = manager.beginTransaction();
        hideAllFrag(transaction);

        switch (i){

            case 0:
                tvAccount.setTextColor(getResources().getColor(R.color.tab_bg));
                tvAccounting.setTextColor(getResources().getColor(R.color.wordcolor));
                viewAccount.setVisibility(View.VISIBLE);
                viewAccounting.setVisibility(View.INVISIBLE);

                if (accountFrag == null){

                    accountFrag = new AccountFrag();
                    transaction.add(R.id.fl_income,accountFrag);

                }else{
                    transaction.show(accountFrag) ;
                }

                break;
            case 1:
                tvAccount.setTextColor(getResources().getColor(R.color.wordcolor));
                tvAccounting.setTextColor(getResources().getColor(R.color.tab_bg));
                viewAccount.setVisibility(View.INVISIBLE);
                viewAccounting.setVisibility(View.VISIBLE);

                if (accountIngFrag == null){

                    accountIngFrag = new AccountIngFrag();
                    transaction.add(R.id.fl_income,accountIngFrag);

                }else{
                    transaction.show(accountIngFrag) ;
                }

                break;
        }

        transaction.commit();
    }

    private void hideAllFrag(FragmentTransaction transaction) {

        if (accountFrag != null){

            transaction.hide(accountFrag);
        }

        if (accountIngFrag != null){

            transaction.hide(accountIngFrag);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ll_account:

                selectFrag(0);
                break;

            case R.id.ll_accounting:

                selectFrag(1);
                break;

            case R.id.img_logo:
                InComeActivity.this.finish();
                break;
        }
    }
}
