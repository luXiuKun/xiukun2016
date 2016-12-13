package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.fangzhurapp.technicianport.bean.BossSJYSBean;
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

/**
 * 改为订单收入的界面
 */
public class BossSjysActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener {

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
    @Bind(R.id.img_sjys_nodata)
    ImageView imgSjysNodata;
    @Bind(R.id.swipe_sjys)
    SwipeToLoadLayout swipeSjys;
    private static final String TAG = "BossSjysActivity";
    @Bind(R.id.rl_sjysnodata)
    RelativeLayout rlSjysnodata;
    private String strTime;
    private String endTime;
    private ArrayList<BossSJYSBean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_sjys);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        swipeSjys.setOnRefreshListener(this);
        swipeSjys.setRefreshing(true);

        swipeTarget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dataList.get(position).getItems_type().equals("2")
                        || dataList.get(position).getItems_type().equals("3")){

                    Intent intent = new Intent(BossSjysActivity.this, BossGoodsDetailActivity.class);
                    intent.putExtra("goodsdetail", dataList.get(position));
                    intent.putExtra("goodstype", "1");
                    startActivity(intent);
                }else{

                    Intent intent = new Intent(BossSjysActivity.this, BossSjysOrderDetailActivity.class);
                    intent.putExtra("sjysdetail", dataList.get(position));
                    intent.putExtra("sjystype","1");
                    startActivity(intent);
                }

            }
        });
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("订单收入");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        swipeTarget.setEmptyView(rlSjysnodata);

        if (getIntent() != null) {
            strTime = getIntent().getStringExtra("strtime");
            endTime = getIntent().getStringExtra("endtime");

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_logo:
                BossSjysActivity.this.finish();
                break;


        }
    }

    @Override
    public void onRefresh() {


        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_PERFORMANCE_SJYS, RequestMethod.POST);
        jsonObjectRequest.add("sid", SpUtil.getString(BossSjysActivity.this, "sid", ""));
        jsonObjectRequest.add("strtime", strTime);
        jsonObjectRequest.add("endtime", endTime);
        jsonObjectRequest.add("sta", "1");
        CallServer.getInstance().add(BossSjysActivity.this, jsonObjectRequest, callback, UrlTag.BOSS_PERFORMANCE_SJYS, false, false, true);

    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.BOSS_PERFORMANCE_SJYS) {
                swipeSjys.setRefreshing(false);
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
                                BossSJYSBean bossSJYSBean = new BossSJYSBean();

                                bossSJYSBean.setStaff_name(list.getJSONObject(i).getString("staff_name"));
                                bossSJYSBean.setXm_name(list.getJSONObject(i).getString("xm_name"));
                                bossSJYSBean.setMoney(list.getJSONObject(i).getString("money"));
                                bossSJYSBean.setOnumber(list.getJSONObject(i).getString("onumber"));
                                bossSJYSBean.setStime(list.getJSONObject(i).getString("stime"));
                                bossSJYSBean.setType(list.getJSONObject(i).getString("type"));
                                bossSJYSBean.setFwtime(list.getJSONObject(i).getString("fwtime"));
                                bossSJYSBean.setSet_type(list.getJSONObject(i).getString("set_type"));
                                bossSJYSBean.setYgtc(list.getJSONObject(i).getString("ygtc"));
                                bossSJYSBean.setStaff(list.getJSONObject(i).getString("staff"));
                                bossSJYSBean.setRoom_number(list.getJSONObject(i).getString("room_number"));
                                bossSJYSBean.setItems_type(list.getJSONObject(i).getString("items_type"));
                                dataList.add(bossSJYSBean);
                            }


                            swipeTarget.setAdapter(new CommAdapter<BossSJYSBean>(BossSjysActivity.this, R.layout.item_bossperformance_sjys, dataList) {
                                @Override
                                public void convert(ViewHolder holder, BossSJYSBean bossSJYSBean, int position) {

                                    TextView tv_bosssjys_proname = holder.getView(R.id.tv_bosssjys_proname);
                                    TextView tv_bosssjys_js = holder.getView(R.id.tv_bosssjys_js);
                                    TextView tv_bosssjys_jsprice = holder.getView(R.id.tv_bosssjys_jsprice);
                                    TextView tv_bosssjys_ident = holder.getView(R.id.tv_bosssjys_ident);

                                    tv_bosssjys_ident.setText(dataList.get(position).getSet_type());
                                    tv_bosssjys_proname.setText(dataList.get(position).getXm_name());
                                    if (!TextUtils.isEmpty(dataList.get(position).getStaff_name())){

                                        tv_bosssjys_js.setText(dataList.get(position).getStaff_name());
                                    }else{
                                        tv_bosssjys_js.setText("无");

                                    }
                                    tv_bosssjys_jsprice.setText(dataList.get(position).getMoney());
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
        public void onFailed(int what,  Response<JSONObject> response) {
            if (what == UrlTag.BOSS_PERFORMANCE_SJYS) {
                swipeSjys.setRefreshing(false);
            }
        }
    };
}
