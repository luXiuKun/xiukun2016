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
import com.fangzhurapp.technicianport.bean.BossStaffTcBean;
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

public class BossStaffTcActivity extends AppCompatActivity implements View.OnClickListener,OnRefreshListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.rl_stafftc_nodata)
    RelativeLayout rlStafftcNodata;
    @Bind(R.id.swipe_target)
    ListView swipeTarget;
    @Bind(R.id.swipe_stafftc)
    SwipeToLoadLayout swipeStafftc;

    private ArrayList<BossStaffTcBean> dataList;

    private String strTime ;
    private String endTime ;

    private static final String TAG = "BossStaffTcActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_staff_tc);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        swipeStafftc.setOnRefreshListener(this);
        swipeTarget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BossStaffTcActivity.this, BossStaffTcDetailActivity.class);
                intent.putExtra("bossstafftc",dataList.get(position));
                startActivity(intent);
            }
        });

    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("员工提成");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
        swipeTarget.setEmptyView(rlStafftcNodata);

        swipeStafftc.setRefreshing(true);

        if (getIntent() != null){
            strTime = getIntent().getStringExtra("strtime");
            endTime = getIntent().getStringExtra("endtime");

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_logo:
                BossStaffTcActivity.this.finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_PERFORMANCE_STAFFTC, RequestMethod.POST);
        jsonObjectRequest.add("sid", SpUtil.getString(BossStaffTcActivity.this,"sid",""));
        jsonObjectRequest.add("strtime",strTime);
        jsonObjectRequest.add("endtime",endTime);
        CallServer.getInstance().add(BossStaffTcActivity.this,jsonObjectRequest,callback, UrlTag.BOSS_PERFORMANCE_STAFFTC,true,false,true);

    }



    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.BOSS_PERFORMANCE_STAFFTC){
                swipeStafftc.setRefreshing(false);
                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){

                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray list = data.getJSONArray("list");

                        if (list.length() >0){
                            dataList = new ArrayList<>();

                            for (int i =0; i<list.length(); i++){
                                BossStaffTcBean bossStaffTc = new BossStaffTcBean();
                                bossStaffTc.setName(list.getJSONObject(i).getString("name"));
                                bossStaffTc.setId_number(list.getJSONObject(i).getString("id_number"));
                                bossStaffTc.setPztc(list.getJSONObject(i).getString("pztc"));
                                bossStaffTc.setDztc(list.getJSONObject(i).getString("dztc"));
                                bossStaffTc.setKktc(list.getJSONObject(i).getString("kktc"));

                                dataList.add(bossStaffTc);
                            }


                            swipeTarget.setAdapter(new CommAdapter<BossStaffTcBean>(BossStaffTcActivity.this,R.layout.item_bossperformance_stafftc,dataList) {
                                @Override
                                public void convert(ViewHolder holder, BossStaffTcBean bossStaffTc, int position) {

                                    TextView tv_bossstafftc_name = holder.getView(R.id.tv_bossstafftc_name);
                                    TextView tv_bossstafftc_num =holder.getView(R.id.tv_bossstafftc_num);
                                    TextView tv_bossstafftc_tc =holder.getView(R.id.tv_bossstafftc_tc);


                                    tv_bossstafftc_name.setText(dataList.get(position).getName());
                                    tv_bossstafftc_num.setText(dataList.get(position).getId_number());
                                    tv_bossstafftc_tc.setText(dataList.get(position).getPztc());
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
            if (what == UrlTag.BOSS_PERFORMANCE_SJYS){
                swipeStafftc.setRefreshing(false);
            }
        }
    };
}
