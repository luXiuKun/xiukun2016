package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.fangzhurapp.technicianport.bean.BossProjectRankBean;
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

public class BossRankActivity extends AppCompatActivity implements View.OnClickListener,OnRefreshListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.rl_rank_nodata)
    RelativeLayout rlRankNodata;
    @Bind(R.id.swipe_target)
    ListView swipeTarget;
    @Bind(R.id.swipe_rank)
    SwipeToLoadLayout swipeRank;

    private String strTime ;
    private String endTime ;
    private static final String TAG = "BossRankActivity";

    private ArrayList<BossProjectRankBean> dataList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_rank);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        swipeRank.setOnRefreshListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("项目排行");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
        swipeTarget.setEmptyView(rlRankNodata);
        swipeRank.setRefreshing(true);

        if (getIntent() != null){
            strTime = getIntent().getStringExtra("strtime");
            endTime = getIntent().getStringExtra("endtime");

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.img_logo:
                BossRankActivity.this.finish();
                break;

        }
    }

    @Override
    public void onRefresh() {


        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_PROJECT_RANK, RequestMethod.POST);
        jsonObjectRequest.add("sid", SpUtil.getString(BossRankActivity.this,"sid",""));
        jsonObjectRequest.add("strtime",strTime);
        jsonObjectRequest.add("endtime",endTime);
        CallServer.getInstance().add(BossRankActivity.this,jsonObjectRequest,callback, UrlTag.BOSS_PROJECT_RANK,true,false,true);

    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {

            if (what == UrlTag.BOSS_PROJECT_RANK){
                swipeRank.setRefreshing(false);

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONObject data = jsonObject.getJSONObject("data");

                        JSONArray list = data.getJSONArray("list");

                        if (list.length() > 0){
                            dataList = new ArrayList<>();

                            for (int i =0; i< list.length();i++){
                                BossProjectRankBean bossProjectRankBean = new BossProjectRankBean();
                                bossProjectRankBean.setRank(i+1+"");
                                bossProjectRankBean.setCount(list.getJSONObject(i).getString("count"));
                                bossProjectRankBean.setPname(list.getJSONObject(i).getString("pname"));
                                dataList.add(bossProjectRankBean);
                            }

                            swipeTarget.setAdapter(new CommAdapter<BossProjectRankBean>(BossRankActivity.this,R.layout.item_bossperformance_rank,dataList) {
                                @Override
                                public void convert(ViewHolder holder, BossProjectRankBean bossProjectRankBean, int position) {

                                    TextView tv_bossrank_rank = holder.getView(R.id.tv_bossrank_rank);
                                    TextView tv_bossrank_proname = holder.getView(R.id.tv_bossrank_proname);
                                    TextView tv_bossrank_ordercount = holder.getView(R.id.tv_bossrank_ordercount);


                                    tv_bossrank_rank.setText(bossProjectRankBean.getRank());
                                    tv_bossrank_proname.setText(bossProjectRankBean.getPname());
                                    tv_bossrank_ordercount.setText(bossProjectRankBean.getCount());

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
                if (what == UrlTag.BOSS_PROJECT_RANK){
                    swipeRank.setRefreshing(false);

                }
        }
    };
}
