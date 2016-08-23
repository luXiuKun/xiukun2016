package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BossStaffWageBean;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossStaffWageDetailActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.img_bossstaffwage_icon)
    ImageView imgBossstaffwageIcon;
    @Bind(R.id.tv_bossstaffwage_zmoney)
    TextView tvBossstaffwageZmoney;
    @Bind(R.id.tv_staffwagedetail_dx)
    TextView tvStaffwagedetailDx;
    @Bind(R.id.tv_staffwagedetail_pztc)
    TextView tvStaffwagedetailPztc;
    @Bind(R.id.tv_staffwagedetail_dztc)
    TextView tvStaffwagedetailDztc;
    @Bind(R.id.tv_staffwagedetail_kktc)
    TextView tvStaffwagedetailKktc;
    @Bind(R.id.et_staffwagedetail_jj)
    EditText etStaffwagedetailJj;
    @Bind(R.id.btn_changejj)
    Button btnChangejj;
    @Bind(R.id.et_staffwagedetail_fk)
    EditText etStaffwagedetailFk;
    @Bind(R.id.btn_changefk)
    Button btnChangefk;

    private String BTN_CHANGE_JJ_STATE ="0";
    private String BTN_CHANGE_FK_STATE ="0";
    private BossStaffWageBean bean;
    private List<BossStaffWageBean> toJson ;
    private static final String TAG = "BossStaffWageDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_staff_wage_detail);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        btnChangejj.setOnClickListener(this);
        btnChangefk.setOnClickListener(this);

    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);


        if (getIntent() != null){

            bean = (BossStaffWageBean) getIntent().getSerializableExtra("bossstaffwagedetail");

            tvShopname.setText(bean.getName()+"工资");
            tvBossstaffwageZmoney.setText("￥"+ bean.getMoney());
            tvStaffwagedetailDx.setText(bean.getDmoney());
            tvStaffwagedetailPztc.setText(bean.getTypea());
            tvStaffwagedetailDztc.setText(bean.getTypeb());
            tvStaffwagedetailKktc.setText(bean.getTypes());

            etStaffwagedetailJj.setText(bean.getJmoney());
            etStaffwagedetailFk.setText(bean.getFmoney());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.btn_changejj:

                if (BTN_CHANGE_JJ_STATE.equals("0")){

                    etStaffwagedetailJj.setEnabled(true);
                    btnChangejj.setText("保存");
                    BTN_CHANGE_JJ_STATE = "1";

                }else{
                    BTN_CHANGE_JJ_STATE = "0";
                    btnChangejj.setText("修改");
                    etStaffwagedetailJj.setEnabled(false);

                    subMitJj();

                }

                break;


            case R.id.btn_changefk:

                if (BTN_CHANGE_FK_STATE.equals("0")){

                    etStaffwagedetailFk.setEnabled(true);
                    btnChangefk.setText("保存");
                    BTN_CHANGE_FK_STATE = "1";

                }else{
                    BTN_CHANGE_FK_STATE = "0";
                    btnChangefk.setText("修改");
                    etStaffwagedetailFk.setEnabled(false);

                    subMitFk();

                }

                break;

            case R.id.img_logo:

                BossStaffWageDetailActivity.this.finish();
                break;
        }
    }

    /**
     * 提交罚款
     */
    private void subMitFk() {
        if (!TextUtils.isEmpty(btnChangefk.getText().toString())){
            toJson = new ArrayList<>();
            BossStaffWageBean bossStaffWageBean = new BossStaffWageBean();
            bossStaffWageBean.setId(bean.getId());
            bossStaffWageBean.setDmoney(bean.getDmoney());
            bossStaffWageBean.setTypea(bean.getTypea());
            bossStaffWageBean.setTypeb(bean.getTypeb());
            bossStaffWageBean.setTypes(bean.getTypes());
            bossStaffWageBean.setFmoney(btnChangefk.getText().toString());
            bossStaffWageBean.setJmoney(btnChangejj.getText().toString());
            toJson.add(bossStaffWageBean);

            Gson gson = new Gson();
            String fk = gson.toJson(toJson);

            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_SUBMIT_WAGE, RequestMethod.POST);

            jsonObjectRequest.add("json",fk);

            CallServer.getInstance().add(BossStaffWageDetailActivity.this,jsonObjectRequest,callback,UrlTag.BOSS_SUBMIT_WAGE,true,false,true);


        }else{

            Toast.makeText(BossStaffWageDetailActivity.this, "请输入罚款", Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * 提交奖金
     */
    private void subMitJj() {

        if (!TextUtils.isEmpty(btnChangejj.getText().toString())){
            toJson = new ArrayList<>();
            BossStaffWageBean bossStaffWageBean = new BossStaffWageBean();

            bossStaffWageBean.setId(bean.getId());
            bossStaffWageBean.setDmoney(bean.getDmoney());
            bossStaffWageBean.setTypea(bean.getTypea());
            bossStaffWageBean.setTypeb(bean.getTypeb());
            bossStaffWageBean.setTypes(bean.getTypes());
            bossStaffWageBean.setJmoney(btnChangejj.getText().toString());
            bossStaffWageBean.setFmoney(btnChangefk.getText().toString());
            toJson.add(bossStaffWageBean);
            Gson gson = new Gson();
            String jj = gson.toJson(toJson);

            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_SUBMIT_WAGE, RequestMethod.POST);

            jsonObjectRequest.add("json",jj);

            CallServer.getInstance().add(BossStaffWageDetailActivity.this,jsonObjectRequest,callback,UrlTag.BOSS_SUBMIT_WAGE,true,false,true);
        }else{

            Toast.makeText(BossStaffWageDetailActivity.this, "请输入奖金", Toast.LENGTH_SHORT).show();

        }



    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {


            if (what == UrlTag.BOSS_SUBMIT_WAGE){
                LogUtil.d(TAG,response.toString());

            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }
    };
}
