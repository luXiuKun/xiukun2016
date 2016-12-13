package com.fangzhurapp.technicianport;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import com.fangzhurapp.technicianport.eventbus.BossPerformanceMsgEvent;
import com.fangzhurapp.technicianport.frag.BossWalletFrag;
import com.fangzhurapp.technicianport.frag.MySelfFrag;
import com.fangzhurapp.technicianport.frag.PerformanceFrag;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

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

    private AlertDialog upData;
    private String value;
    private int version;
    private AlertDialog.Builder exitDialog;
    private AlertDialog exit;
    private static final String TAG = "BossMainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_main);
        CustomApplication.addAct(this);
        //getSupportActionBar().hide();
        ButterKnife.bind(this);
        version = VersionUtil.getVersion(BossMainActivity.this);
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
        checkVersion();
        setAils();
    }

    private void setAils() {

        JPushInterface.setAlias(BossMainActivity.this, "boss"+ SpUtil.getString(BossMainActivity.this,"id",""),mAliasCallback);
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

    private void checkVersion() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.VERSION, RequestMethod.POST);
        jsonObjectRequest.setRetryCount(3);
        jsonObjectRequest.add("conf_name","androidphone");
        CallServer.getInstance().add(BossMainActivity.this,jsonObjectRequest,callback, UrlTag.VERSION,false,false,true);
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

                                AlertDialog.Builder upDataDialog = new AlertDialog.Builder(BossMainActivity.this);
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
                                        BossMainActivity.this.finish();


                                    }
                                });

                                upDataDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (upData != null){

                                            upData.dismiss();
                                        }
                                        BossMainActivity.this.finish();
                                    }
                                }) ;

                                upData = upDataDialog.show();


                            }else{

                                Toast.makeText(BossMainActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
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
            exitDialog = new AlertDialog.Builder(BossMainActivity.this);
            exitDialog.setTitle("退出应用");
            exitDialog.setMessage("确定要退出吗?");
            exitDialog.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                   // BossMainActivity.this.finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
