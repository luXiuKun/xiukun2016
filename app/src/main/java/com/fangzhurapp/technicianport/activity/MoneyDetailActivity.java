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

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.adapter.moneyDetailAdapter;
import com.fangzhurapp.technicianport.bean.moneyDetailBean;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoneyDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.lv_moneydetail)
    ListView lvMoneydetail;
    private static final String TAG = "MoneyDetailActivity";
    @Bind(R.id.rl_nojyjl)
    RelativeLayout rlNojyjl;
    private List<moneyDetailBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_detail);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);

        lvMoneydetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MoneyDetailActivity.this, MoneyTradeDetailActivity.class);
                intent.putExtra("tradedetail",list.get(position));
                startActivity(intent);
            }
        });

    }

    private void initView() {

        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("现金明细");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
        lvMoneydetail.setEmptyView(rlNojyjl);
        getMoneyDetail();

    }

    private void getMoneyDetail() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.MONEY_DETAIL, RequestMethod.POST);
        jsonObjectRequest.add("id", SpUtil.getString(MoneyDetailActivity.this, "id", ""));
        CallServer.getInstance().add(MoneyDetailActivity.this, jsonObjectRequest, callback, UrlTag.MONEY_DETAIL, true, false, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_logo:
                MoneyDetailActivity.this.finish();
                break;


        }
    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.MONEY_DETAIL) {
                LogUtil.d(TAG, response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")) {

                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() > 0) {
                            list = new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                moneyDetailBean moneyDetailBean = new moneyDetailBean();
                                moneyDetailBean.setCname(data.getJSONObject(i).getString("cname"));
                                moneyDetailBean.setMoney(data.getJSONObject(i).getString("money"));
                                moneyDetailBean.setTime(data.getJSONObject(i).getString("time"));
                                moneyDetailBean.setType(data.getJSONObject(i).getString("type"));
                                moneyDetailBean.setSmoney(data.getJSONObject(i).getString("smoney"));
                                moneyDetailBean.setPayment_onumber(data.getJSONObject(i).getString("payment_onumber"));


                                list.add(moneyDetailBean);

                            }

                            moneyDetailAdapter moneyDetailAdapter = new moneyDetailAdapter(list, MoneyDetailActivity.this);
                            lvMoneydetail.setAdapter(moneyDetailAdapter);


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
}
