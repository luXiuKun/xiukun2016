package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.adapter.CommAdapter;
import com.fangzhurapp.technicianport.adapter.ViewHolder;
import com.fangzhurapp.technicianport.bean.BossVipkkBean;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossVipActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.swipe_target)
    ListView swipeTarget;
    @Bind(R.id.swipe_vip)
    SwipeToLoadLayout swipeVip;
    @Bind(R.id.img_vip_nodata)
    ImageView imgVipNodata;
    @Bind(R.id.rl_vipnodata)
    RelativeLayout rlVipnodata;
    private String strTime;
    private String endTime;
    private ArrayList<BossVipkkBean> dataList;

    private static final String TAG = "BossVipActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_vip);
        getSupportActionBar().hide();
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        swipeVip.setOnRefreshListener(this);
        imgLogo.setOnClickListener(this);

        swipeTarget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BossVipActivity.this, BossVipkkDetailActivity.class);
                intent.putExtra("bossvipdetail", dataList.get(position));
                startActivity(intent);
            }
        });

    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("会员开卡");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
        swipeTarget.setEmptyView(rlVipnodata);
        swipeVip.setRefreshing(true);

        if (getIntent() != null) {
            strTime = getIntent().getStringExtra("strtime");
            endTime = getIntent().getStringExtra("endtime");

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_logo:
                BossVipActivity.this.finish();
                break;

        }
    }

    @Override
    public void onRefresh() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_PERFORMANCE_SJYS, RequestMethod.POST);
        jsonObjectRequest.add("sid", SpUtil.getString(BossVipActivity.this, "sid", ""));
        jsonObjectRequest.add("strtime", strTime);
        jsonObjectRequest.add("endtime", endTime);
        jsonObjectRequest.add("sta", "2");
        CallServer.getInstance().add(BossVipActivity.this, jsonObjectRequest, callback, UrlTag.BOSS_PERFORMANCE_SJYS, true, false, true);
    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.BOSS_PERFORMANCE_SJYS) {
                swipeVip.setRefreshing(false);
                LogUtil.d(TAG, response.toString());

                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray list = data.getJSONArray("list");

                        if (list.length() > 0) {
                            dataList = new ArrayList<>();

                            for (int i = 0; i < list.length(); i++) {
                                BossVipkkBean bossVipBean = new BossVipkkBean();
                                bossVipBean.setK_name(list.getJSONObject(i).getString("k_name"));
                                bossVipBean.setJ_money(list.getJSONObject(i).getString("j_money"));
                                bossVipBean.setMnumber(list.getJSONObject(i).getString("mnumber"));
                                bossVipBean.setAddtime(list.getJSONObject(i).getString("addtime"));
                                bossVipBean.setStaff_name(list.getJSONObject(i).getString("staff_name"));
                                bossVipBean.setYgtc(list.getJSONObject(i).getString("ygtc"));
                                bossVipBean.setStaff(list.getJSONObject(i).getString("staff"));
                                bossVipBean.setType(list.getJSONObject(i).getString("type"));
                                bossVipBean.setName(list.getJSONObject(i).getString("name"));
                                dataList.add(bossVipBean);
                            }


                            swipeTarget.setAdapter(new CommAdapter<BossVipkkBean>(BossVipActivity.this, R.layout.item_bossperformance_vip, dataList) {
                                @Override
                                public void convert(ViewHolder holder, BossVipkkBean bossSJYSBean, int position) {

                                    TextView tv_bossvip_cardtype = holder.getView(R.id.tv_bossvip_cardtype);
                                    TextView tv_bossvip_jsprice = holder.getView(R.id.tv_bossvip_jsprice);


                                    tv_bossvip_cardtype.setText(dataList.get(position).getK_name());
                                    tv_bossvip_jsprice.setText(dataList.get(position).getJ_money());
                                }
                            });

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            if (what == UrlTag.BOSS_PERFORMANCE_SJYS) {
                swipeVip.setRefreshing(false);
            }
        }
    };
}
