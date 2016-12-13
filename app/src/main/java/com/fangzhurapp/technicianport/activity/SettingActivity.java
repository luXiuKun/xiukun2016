package com.fangzhurapp.technicianport.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.utils.SpUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.img_setting_back)
    ImageView imgSettingBack;
    @Bind(R.id.tv_setting_name)
    TextView tvSettingName;
    @Bind(R.id.tv_setting_num)
    TextView tvSettingNum;
    @Bind(R.id.rl_setting_forgetpw)
    RelativeLayout rlSettingForgetpw;
    @Bind(R.id.rl_setting_hotline)
    RelativeLayout rlSettingHotline;
    @Bind(R.id.rl_setting_feedback)
    RelativeLayout rlSettingFeedback;
    @Bind(R.id.rl_setting_about)
    RelativeLayout rlSettingAbout;
    @Bind(R.id.ib_setting_outlogin)
    ImageButton ibSettingOutlogin;
    @Bind(R.id.ib_setting_head)
    ImageButton ibSetttingHead;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);

        initView();
        initEvent();
    }

    private void initEvent() {
        imgSettingBack.setOnClickListener(this);
        rlSettingForgetpw.setOnClickListener(this);
        rlSettingHotline.setOnClickListener(this);
        rlSettingFeedback.setOnClickListener(this);
        rlSettingAbout.setOnClickListener(this);
        ibSettingOutlogin.setOnClickListener(this);
    }

    private void initView() {

        if (!TextUtils.isEmpty(SpUtil.getString(SettingActivity.this, "sex", ""))) {

            if (SpUtil.getString(SettingActivity.this, "sex", "").equals("1")) {
                ibSetttingHead.setBackgroundResource(R.drawable.img_setting_man);
            }else{
                ibSetttingHead.setBackgroundResource(R.drawable.img_setting_woman);
            }

        }

        if (!TextUtils.isEmpty(SpUtil.getString(SettingActivity.this, "id_number", ""))
                &&!TextUtils.isEmpty(SpUtil.getString(SettingActivity.this, "name", ""))){

            tvSettingName.setText(SpUtil.getString(SettingActivity.this, "name", ""));
            tvSettingNum.setText(SpUtil.getString(SettingActivity.this, "id_number", "")+"号");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_setting_forgetpw:
                Intent intent = new Intent(SettingActivity.this, ChangePwActivity.class);
                startActivity(intent);
                break;

            case R.id.img_setting_back:
                SettingActivity.this.finish();
                break;

            case R.id.ib_setting_outlogin:

                outLogin();

                break;
            case R.id.rl_setting_about:
                Intent about = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(about);
                break;

            case R.id.rl_setting_feedback:
                Intent feedback = new Intent(SettingActivity.this, FeedBackActivity.class);
                startActivity(feedback);
                break;


            case R.id.rl_setting_hotline:
                Intent hotline = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01053396609"));
                hotline.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(hotline);
                break;
        }

    }

    private void outLogin() {

        AlertDialog.Builder outLogin = new AlertDialog.Builder(SettingActivity.this);

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
                SpUtil.remove(SettingActivity.this,"password");
                SpUtil.remove(SettingActivity.this,"selectident");
                Intent outlogin = new Intent(SettingActivity.this, SelectIdent.class);
                startActivity(outlogin);
            }
        });
        alertDialog = outLogin.show();


    }
}
