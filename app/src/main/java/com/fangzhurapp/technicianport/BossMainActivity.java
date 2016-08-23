package com.fangzhurapp.technicianport;

import android.*;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangzhurapp.technicianport.frag.BossWalletFrag;
import com.fangzhurapp.technicianport.frag.MySelfFrag;
import com.fangzhurapp.technicianport.frag.PerformanceFrag;

import butterknife.Bind;
import butterknife.ButterKnife;
public class BossMainActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.fl_boss)
    FrameLayout flBoss;
    @Bind(R.id.iv_bossmain_performance)
    ImageView ivBossmainPerformance;
    @Bind(R.id.tv_bossmain_performance)
    TextView tvBossmainPerformance;
    @Bind(R.id.ll_bossmain_performance)
    LinearLayout llBossmainPerformance;
    @Bind(R.id.iv_bossmain_wallet)
    ImageView ivBossmainWallet;
    @Bind(R.id.tv_bossmain_wallet)
    TextView tvBossmainWallet;
    @Bind(R.id.ll_bossmain_wallet)
    LinearLayout llBossmainWallet;
    @Bind(R.id.iv_myself)
    ImageView ivMyself;
    @Bind(R.id.tv_myself)
    TextView tvMyself;
    @Bind(R.id.ll_bossmain_myself)
    LinearLayout llBossmainMyself;
    private FragmentManager fragmentManager;
    private PerformanceFrag perFrag;
    private BossWalletFrag bossWalletFrag;
    private MySelfFrag mySelfFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_main);
        CustomApplication.addAct(this);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null){

            perFrag = (PerformanceFrag) fragmentManager.findFragmentByTag("PerformanceFrag");
            bossWalletFrag = (BossWalletFrag) fragmentManager.findFragmentByTag("BossWalletFrag");
            mySelfFrag = (MySelfFrag) fragmentManager.findFragmentByTag("MySelfFrag");

            fragmentManager.beginTransaction().show(perFrag)
                    .hide(bossWalletFrag)
                    .hide(mySelfFrag)
                    .commit();

        }

        selectTab(0);
        initEvent();

    }

    private void initEvent() {
        llBossmainPerformance.setOnClickListener(this);
        llBossmainWallet.setOnClickListener(this);
        llBossmainMyself.setOnClickListener(this);
    }


    private void setTab(int i) {
        resBackground();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFrag(fragmentTransaction);
        switch (i) {

            case 0:
                ivBossmainPerformance.setBackgroundResource(R.drawable.img_boss_performance_pre);
                tvBossmainPerformance.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));

                if (perFrag == null){
                    perFrag = new PerformanceFrag();
                    fragmentTransaction.add(R.id.fl_boss,perFrag,"PerformanceFrag");

                }else{
                    fragmentTransaction.show(perFrag) ;
                }

                break;

            case 1:
                ivBossmainWallet.setBackgroundResource(R.drawable.img_boss_wallet_pre);
                tvBossmainWallet.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));
                if (bossWalletFrag == null){
                    bossWalletFrag = new BossWalletFrag();
                    fragmentTransaction.add(R.id.fl_boss,bossWalletFrag,"BossWalletFrag");

                }else{
                    fragmentTransaction.show(bossWalletFrag) ;
                }

                break;


            case 2:
                ivMyself.setBackgroundResource(R.drawable.img_boss_myself_pre);
                tvMyself.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));
                if (mySelfFrag == null){
                    mySelfFrag = new MySelfFrag();
                    fragmentTransaction.add(R.id.fl_boss,mySelfFrag,"MySelfFrag");

                }else{
                    fragmentTransaction.show(mySelfFrag) ;
                }

                break;

        }

        fragmentTransaction.commit();
    }

    private void hideAllFrag(FragmentTransaction fragmentTransaction) {
        if (perFrag != null){
            fragmentTransaction.hide(perFrag);
        }
        if (bossWalletFrag != null){
            fragmentTransaction.hide(bossWalletFrag);
        }

        if (mySelfFrag != null){
            fragmentTransaction.hide(mySelfFrag);
        }
    }


    private void resBackground() {
        ivBossmainPerformance.setBackgroundResource(R.drawable.img_boss_performance_nor);
        ivBossmainWallet.setBackgroundResource(R.drawable.img_boss_wallet_nor);
        ivMyself.setBackgroundResource(R.drawable.img_boss_myself_nor);

        tvBossmainPerformance.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.btn_gray));
        tvBossmainWallet.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.btn_gray));
        tvMyself.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.btn_gray));
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ll_bossmain_performance:
                selectTab(0);
                break;

            case R.id.ll_bossmain_wallet:
                selectTab(1);
                break;

            case R.id.ll_bossmain_myself:
                selectTab(2);
                break;

        }
    }

    private void selectTab(int i) {
        setTab(i);
    }


}
