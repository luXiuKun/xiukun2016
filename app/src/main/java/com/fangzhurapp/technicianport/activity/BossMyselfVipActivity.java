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
import com.fangzhurapp.technicianport.bean.BossVIPBean;
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

public class BossMyselfVipActivity extends AppCompatActivity implements View.OnClickListener ,OnRefreshListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.rl_bossmyvip_nodata)
    RelativeLayout rlBossmyvipNodata;
    @Bind(R.id.swipe_target)
    ListView swipeTarget;
    @Bind(R.id.swipe_myvip)
    SwipeToLoadLayout swipeMyvip;

    private static final String TAG = "BossMyselfVipActivity";

    private ArrayList<BossVIPBean> dataList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_myself_vip);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        swipeMyvip.setOnRefreshListener(this);
        swipeTarget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BossMyselfVipActivity.this, BossMyVipDetailActivity.class);
                intent.putExtra("bossmyvipdetail",dataList.get(position));
                startActivity(intent);

            }
        });


    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("我的会员");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        swipeTarget.setEmptyView(rlBossmyvipNodata);
        swipeMyvip.setRefreshing(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_logo:
                BossMyselfVipActivity.this.finish();
                break;
        }
    }

    @Override
    public void onRefresh() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_MY_VIP, RequestMethod.POST);

        jsonObjectRequest.add("sid", SpUtil.getString(BossMyselfVipActivity.this,"sid",""));

        CallServer.getInstance().add(BossMyselfVipActivity.this,jsonObjectRequest,callback, UrlTag.BOSS_MY_VIP,false,false,true);


    }



    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {


            if (what == UrlTag.BOSS_MY_VIP){
                swipeMyvip.setRefreshing(false);
                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")){

                        JSONObject data = jsonObject.getJSONObject("data");
                        String count = data.getString("count");
                        String sum = data.getString("sum");

                        JSONArray list = data.getJSONArray("list");

                        if (list.length() > 0){
                            dataList = new ArrayList<>();

                            for (int i =0;i < list.length();i++){
                                BossVIPBean bossVIPBean = new BossVIPBean();

                                bossVIPBean.setName(list.getJSONObject(i).getString("name"));
                                bossVIPBean.setK_name(list.getJSONObject(i).getString("k_name"));
                                bossVIPBean.setMoney(list.getJSONObject(i).getString("money"));
                                bossVIPBean.setId(list.getJSONObject(i).getString("id"));
                                bossVIPBean.setMnumber(list.getJSONObject(i).getString("mnumber"));
                                bossVIPBean.setPhone(list.getJSONObject(i).getString("phone"));
                                bossVIPBean.setAddtime(list.getJSONObject(i).getString("addtime"));
                                bossVIPBean.setJ_money(list.getJSONObject(i).getString("j_money"));

                                dataList.add(bossVIPBean);


                            }


                            swipeTarget.setAdapter(new CommAdapter<BossVIPBean>(BossMyselfVipActivity.this,R.layout.item_bossmyvip,dataList) {
                                @Override
                                public void convert(ViewHolder holder, BossVIPBean bossVIPBean, int position) {

                                    TextView tv_bossmyvip_name = holder.getView(R.id.tv_bossmyvip_name);
                                    TextView tv_bossmyvip_viptype = holder.getView(R.id.tv_bossmyvip_viptype);
                                    TextView tv_bosssmyvip_money = holder.getView(R.id.tv_bosssmyvip_money);


                                    tv_bossmyvip_name.setText(bossVIPBean.getName());
                                    tv_bossmyvip_viptype.setText(bossVIPBean.getK_name());
                                    tv_bosssmyvip_money.setText(bossVIPBean.getMoney());

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
            if (what == UrlTag.BOSS_MY_VIP){
                swipeMyvip.setRefreshing(false);
            }
        }
    };


}
