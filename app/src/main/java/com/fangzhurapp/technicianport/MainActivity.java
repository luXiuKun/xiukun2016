package com.fangzhurapp.technicianport;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.eventbus.MsgEvent;
import com.fangzhurapp.technicianport.eventbus.MsgEvent1;
import com.fangzhurapp.technicianport.eventbus.MsgEventAils;
import com.fangzhurapp.technicianport.frag.OrderFragment;
import com.fangzhurapp.technicianport.frag.WalletFragment;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.utils.VersionUtil;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
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
    private AlertDialog upData;
    private String value;
    private int version;

    private FragmentManager fragmentManager;
    private OrderFragment orderFragment;
    private WalletFragment walletFragment;
    private AlertDialog.Builder exitDialog;
    private AlertDialog exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);
        CustomApplication.addAct(this);


        //getSupportActionBar().hide();
        version = VersionUtil.getVersion(MainActivity.this);
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

        checkVersion();
    }

    private void checkVersion() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.VERSION, RequestMethod.POST);
        jsonObjectRequest.setRetryCount(3);
        jsonObjectRequest.add("conf_name","androidphone");
        CallServer.getInstance().add(MainActivity.this,jsonObjectRequest,callback, UrlTag.VERSION,false,false,true);
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

        JPushInterface.setAlias(MainActivity.this, "js"+SpUtil.getString(MainActivity.this,"id",""),mAliasCallback);
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

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.VERSION){

                Logger.d(response.toString());

                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")){

                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() > 0){

                            for (int i =0;i<data.length();i++){

                                value = data.getJSONObject(i).getString("value");

                            }

                            int resultVersion = Integer.valueOf(value);

                            if (version < resultVersion){

                                AlertDialog.Builder upDataDialog = new AlertDialog.Builder(MainActivity.this);
                                upDataDialog.setTitle("版本更新");
                                upDataDialog.setMessage("有新版本了,是否更新?");
                                upDataDialog.setCancelable(false);
                                upDataDialog.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Uri uri = Uri.parse("http://a.app.qq.com/o/simple.jsp?pkgname=com.fangzhurapp.technicianport");

                                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        if (upData != null){

                                            upData.dismiss();
                                        }
                                        MainActivity.this.finish();


                                    }
                                });

                                upDataDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (upData != null){

                                            upData.dismiss();
                                        }
                                        MainActivity.this.finish();
                                    }
                                }) ;

                                upData = upDataDialog.show();


                            }else{

                                Toast.makeText(MainActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode ==KeyEvent.KEYCODE_BACK ){
            exitDialog = new AlertDialog.Builder(MainActivity.this);
            exitDialog.setTitle("退出应用");
            exitDialog.setMessage("确定要退出吗?");
            exitDialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                   //MainActivity.this.finish();
                    CustomApplication.removeAllAct();

                }
            });

            exitDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (exit != null){

                        exit.dismiss();
                    }
                }
            }) ;
            exit = exitDialog.show();

        }
        return super.onKeyDown(keyCode, event);
    }



}
