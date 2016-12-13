package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.utils.TimeUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PartnerIncomeAct extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "PartnerIncomeAct";
    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_staff_income)
    TextView tvStaffIncome;
    @Bind(R.id.img_zongincome)
    ImageView imgZongincome;
    @Bind(R.id.tv_zongincome)
    TextView tvZongincome;
    @Bind(R.id.ll_partner_zongincome)
    LinearLayout llPartnerZongincome;
    @Bind(R.id.img_monthincome)
    ImageView imgMonthincome;
    @Bind(R.id.tv_monthincome)
    TextView tvMonthincome;
    @Bind(R.id.ll_partner_monthincome)
    LinearLayout llPartnerMonthincome;
    @Bind(R.id.tv_partner_cityincome)
    TextView tvPartnerCityincome;
    @Bind(R.id.ll_partnerincome_city)
    LinearLayout llPartnerincomeCity;
    @Bind(R.id.line_cityincome)
    View lineCityincome;
    @Bind(R.id.tv_partner_oneincome)
    TextView tvPartnerOneincome;
    @Bind(R.id.tv_partner_twoincome)
    TextView tvPartnerTwoincome;
    private int mYear,mMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_income);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartnerIncomeAct.this.finish();
            }
        });
        llPartnerZongincome.setOnClickListener(this);
        llPartnerMonthincome.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("我的收入");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
        if (SpUtil.getString(PartnerIncomeAct.this, "gold", "").equals("1") &&
                SpUtil.getString(PartnerIncomeAct.this, "city", "").equals("0")) {

            llPartnerincomeCity.setVisibility(View.GONE);
            lineCityincome.setVisibility(View.GONE);
        }

        getTime();

        selectTab(0);

    }

    private void getTime() {

        Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH) + 1;
    }

    private void selectTab(int i) {

        switch (i) {
            case 0:
                imgZongincome.setBackgroundResource(R.drawable.img_partner_zong_pre);
                tvZongincome.setTextColor(getResources().getColor(R.color.tab_bg));
                imgMonthincome.setBackgroundResource(R.drawable.img_partner_month_nor);
                tvMonthincome.setTextColor(getResources().getColor(R.color.wordcolor));
                getIncome(i);
                break;

            case 1:
                imgZongincome.setBackgroundResource(R.drawable.img_partner_zong_nor);
                tvZongincome.setTextColor(getResources().getColor(R.color.wordcolor));
                imgMonthincome.setBackgroundResource(R.drawable.img_partner_month_pre);
                tvMonthincome.setTextColor(getResources().getColor(R.color.tab_bg));
                getIncome(i);
                break;
        }
    }

    private void getIncome(int i) {

        if (i == 0){

            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.PARTNER_INCOME, RequestMethod.POST);

            jsonObjectRequest.add("account",SpUtil.getString(PartnerIncomeAct.this,"uname",""));
            CallServer.getInstance().add(PartnerIncomeAct.this,jsonObjectRequest,callback,UrlTag.PARTNER_INCOME,true,false,true);
        }else{

            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.PARTNER_INCOME, RequestMethod.POST);

            jsonObjectRequest.add("account",SpUtil.getString(PartnerIncomeAct.this,"uname",""));
            jsonObjectRequest.add("strtime",mYear+"-"+mMonth+"-"+"01 00:00:00");
            jsonObjectRequest.add("endtime ", TimeUtils.getMonthLastDay1(mMonth)+" 23:59:59");
            CallServer.getInstance().add(PartnerIncomeAct.this,jsonObjectRequest,callback,UrlTag.PARTNER_INCOME,true,false,true);
        }



    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
                if (what == UrlTag.PARTNER_INCOME){
                    LogUtil.d(TAG,response.toString());

                    JSONObject jsonObject = response.get();
                    try {
                        String sucess = jsonObject.getString("sucess");
                        if (sucess.equals("1")){
                            JSONObject data = jsonObject.getJSONObject("data");

                            String city = data.getString("city");
                            String one = data.getString("one");
                            String sum = data.getString("sum");
                            String two = data.getString("two");
                            tvStaffIncome.setText("￥"+sum);
                            tvPartnerCityincome.setText(city);
                            tvPartnerOneincome.setText(one);
                            tvPartnerTwoincome.setText(two);
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
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.ll_partner_zongincome:
                selectTab(0);
                break;

            case R.id.ll_partner_monthincome:
                selectTab(1);
                break;
        }
    }
}
