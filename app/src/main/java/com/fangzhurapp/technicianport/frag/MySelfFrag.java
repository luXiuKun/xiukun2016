package com.fangzhurapp.technicianport.frag;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzhurapp.technicianport.BossMainActivity;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.activity.AboutActivity;
import com.fangzhurapp.technicianport.activity.BossGdActivity;
import com.fangzhurapp.technicianport.activity.BossMyselfVipActivity;
import com.fangzhurapp.technicianport.activity.BossShopManagerActivity;
import com.fangzhurapp.technicianport.activity.ChangePwActivity;
import com.fangzhurapp.technicianport.activity.FeedBackActivity;
import com.fangzhurapp.technicianport.activity.MessageActivity;
import com.fangzhurapp.technicianport.activity.SelectIdent;
import com.fangzhurapp.technicianport.eventbus.BossMsgEvent;
import com.fangzhurapp.technicianport.eventbus.BossNameMsgEvent;
import com.fangzhurapp.technicianport.utils.SpUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by android on 2016/8/1.
 */
public class MySelfFrag extends Fragment implements View.OnClickListener{


    private View view;
    private LinearLayout mShopManager;
    private LinearLayout mGd;
    private LinearLayout mVip;
    private Context mContext;
    private RelativeLayout mFeedBack;
    private RelativeLayout mAbout;
    private RelativeLayout mForgetPw;
    private RelativeLayout mHotLine;
    private RelativeLayout moutLogin;
    private TextView tv_myself_name;
    private AlertDialog alertDialog;
    private ImageView img_boss_message;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (BossMainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_myself, null);
        EventBus.getDefault().register(this);
        initView();
        initEvent();
        return view;

    }

    @Subscribe
    public void onEventMainThread(BossNameMsgEvent msg){
        tv_myself_name.setText(msg.getStringMsg());
    }

    private void initEvent() {
        mShopManager.setOnClickListener(this);
        mGd.setOnClickListener(this);
        mVip.setOnClickListener(this);
        mFeedBack.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mForgetPw.setOnClickListener(this);
        mHotLine.setOnClickListener(this);
        moutLogin.setOnClickListener(this);
        img_boss_message.setOnClickListener(this);
    }

    private void initView() {

        mShopManager = (LinearLayout) view.findViewById(R.id.ll_myself_shopmanager);
        mGd = (LinearLayout) view.findViewById(R.id.ll_myself_gd);
        mVip = (LinearLayout) view.findViewById(R.id.ll_myself_vip);

        mFeedBack = (RelativeLayout) view.findViewById(R.id.rl_myself_feedback);
        mAbout = (RelativeLayout) view.findViewById(R.id.rl_myself_about);
        mForgetPw = (RelativeLayout) view.findViewById(R.id.rl_myself_forgetpw);
        mHotLine = (RelativeLayout) view.findViewById(R.id.rl_myself_hotline);
        moutLogin = (RelativeLayout) view.findViewById(R.id.rl_myself_outlogin);
        tv_myself_name = (TextView) view.findViewById(R.id.tv_myself_name);
        img_boss_message = (ImageView) view.findViewById(R.id.img_boss_message);

        tv_myself_name.setText(SpUtil.getString(mContext,"name",""));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.img_boss_message:
                Intent bossMsg = new Intent(mContext, MessageActivity.class);
                startActivity(bossMsg);
                break;

            case R.id.ll_myself_shopmanager:
                Intent shopmg = new Intent(mContext, BossShopManagerActivity.class);
                startActivity(shopmg);
                break;

            case R.id.ll_myself_gd:
                Intent gd = new Intent(mContext, BossGdActivity.class);
                startActivity(gd);

                break;
            case R.id.ll_myself_vip:
                Intent vip = new Intent(mContext, BossMyselfVipActivity.class);
                startActivity(vip);

                break;

            case R.id.rl_myself_feedback:
                Intent feedBack = new Intent(mContext, FeedBackActivity.class);
                startActivity(feedBack);
                break;

            case R.id.rl_myself_about:
                Intent about = new Intent(mContext, AboutActivity.class);
                startActivity(about);
                break;

            case R.id.rl_myself_forgetpw:
                Intent pw = new Intent(mContext, ChangePwActivity.class);
                startActivity(pw);
                break;

            case R.id.rl_myself_hotline:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01053396609"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case R.id.rl_myself_outlogin:
                outLogin();

                break;
        }

    }

    private void outLogin() {

        AlertDialog.Builder outLogin = new AlertDialog.Builder(mContext);

        outLogin.setTitle("退出登录")
                .setMessage("确定要退出登录吗?")
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (alertDialog != null){

                            alertDialog.dismiss();
                        }
                    }
                });
        outLogin.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CustomApplication.removeAllAct();
                SpUtil.remove(mContext,"password");
                SpUtil.remove(mContext,"selectident");

                Intent ident = new Intent(mContext, SelectIdent.class);
                startActivity(ident);
            }
        });
        alertDialog = outLogin.show();


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }



}
