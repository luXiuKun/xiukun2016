package com.fangzhurapp.technicianport;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangzhurapp.technicianport.eventbus.MsgEvent;
import com.fangzhurapp.technicianport.eventbus.MsgEvent1;
import com.fangzhurapp.technicianport.eventbus.MsgEventAils;
import com.fangzhurapp.technicianport.frag.OrderFragment;
import com.fangzhurapp.technicianport.frag.WalletFragment;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;

//import org.greenrobot.eventbus.Subscribe;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.fg_main)
    FrameLayout fgMain;
    @Bind(R.id.iv_order)
    ImageView ivOrder;
    @Bind(R.id.tv_order)
    TextView tvOrder;
    @Bind(R.id.ll_main_order)
    LinearLayout llMainOrder;
    @Bind(R.id.iv_wallet)
    ImageView ivWallet;
    @Bind(R.id.tv_wallet)
    TextView tvWallet;
    @Bind(R.id.ll_main_wallet)
    LinearLayout llMainWallet;

    private FragmentManager fragmentManager;
    private OrderFragment orderFragment;
    private WalletFragment walletFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        CustomApplication.addAct(this);
        getSupportActionBar().hide();
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null){

            orderFragment = (OrderFragment) fragmentManager.findFragmentByTag("orderfragment");
             walletFragment = (WalletFragment) fragmentManager.findFragmentByTag("walletfragment");

            fragmentManager.beginTransaction().show(orderFragment)
                    .hide(walletFragment)
                    .commit();

        }

        ButterKnife.bind(this);
        initView();
        initEvent();

        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.CALL_PHONE, android.Manifest.permission.READ_LOGS, android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.SET_DEBUG_APP, android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                    android.Manifest.permission.GET_ACCOUNTS, android.Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this,mPermissionList,100);
        }
    }

    @Subscribe
   public void onEventMainThread(MsgEventAils msg){
        setAils();
   }


    private void initView() {

        setSelect(0);

        /**
         * 设置别名，id为别名
         */
        setAils();
    }

    private static final String TAG = "MainActivity";

    private void setAils() {

        JPushInterface.setAlias(MainActivity.this, SpUtil.getString(MainActivity.this,"id",""),mAliasCallback);
    }



    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {

            switch (code){

                case 0:
                    LogUtil.d(TAG,"别名设置成功");
                    break;


            }
        }

    };

    private void initEvent() {
        llMainOrder.setOnClickListener(this);
        llMainWallet.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_main_order:
                setSelect(0);
                break;
            case R.id.ll_main_wallet:
                setSelect(1);
                break;
        }
    }

    private void setSelect(int i) {
        setTab(i);

    }

    private void setTab(int i) {

        resBackGround();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (i){

            case 0:
                ivOrder.setImageResource(R.drawable.tab_order_nor);
                tvOrder.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));
                if (orderFragment == null){
                    orderFragment = new OrderFragment();
                    transaction.add(R.id.fg_main,orderFragment,"orderfragment");
                }else{

                    transaction.show(orderFragment);

                }

                break;


            case 1:
                ivWallet.setBackgroundResource(R.drawable.tab_wallet_nor);
                tvWallet.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));

                if (walletFragment == null){
                    walletFragment = new WalletFragment();
                    transaction.add(R.id.fg_main,walletFragment,"walletfragment");
                }else{
                    transaction.show(walletFragment) ;
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 先隐藏掉所有的fragment
     * @param transaction
     */
    private void hideAllFragment(FragmentTransaction transaction) {

        if (orderFragment != null){
            transaction.hide(orderFragment);
        }
        if (walletFragment != null){
            transaction.hide(walletFragment);
        }

    }

    private void resBackGround() {
        ivOrder.setImageResource(R.drawable.tab_order_pre);
        ivWallet.setBackgroundResource(R.drawable.tab_wallet_pre);
        tvOrder.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.btn_gray));
        tvWallet.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.btn_gray));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
