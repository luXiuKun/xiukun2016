package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.MessageBean;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONObject;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderCancelDetailActivity extends AppCompatActivity {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.img_orderdetail)
    ImageView imgOrderdetail;
    @Bind(R.id.tv_orderdetail_time)
    TextView tvOrderdetailTime;
    @Bind(R.id.tv_orderdetail_name)
    TextView tvOrderdetailName;
    @Bind(R.id.tv_orderdetail_num)
    TextView tvOrderdetailNum;
    @Bind(R.id.tv_orderdetail_proname)
    TextView tvOrderdetailProname;
    @Bind(R.id.tv_orderdetail_state)
    TextView tvOrderdetailState;
    private static final String TAG = "OrderCancelDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_cancel_detail);
        ButterKnife.bind(this);
        CustomApplication.addAct(this);
        getSupportActionBar().hide();
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderCancelDetailActivity.this.finish();
            }
        });

    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("订单取消详情");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);


        if (getIntent() != null){

            MessageBean msgdetail = (MessageBean) getIntent().getSerializableExtra("msgdetail");

            if (msgdetail != null){

                tvOrderdetailTime.setText(msgdetail.getAddtime());
                tvOrderdetailProname.setText(msgdetail.getProject());
                tvOrderdetailNum.setText(msgdetail.getOnumber());
                tvOrderdetailState.setText(msgdetail.getState());


                if (msgdetail.getOnclick().equals("1")){

                    Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.MSG_STATE, RequestMethod.POST);
                    jsonObjectRequest.add("id",msgdetail.getId());
                    CallServer.getInstance().add(OrderCancelDetailActivity.this,jsonObjectRequest,callback, UrlTag.MSG_STATE,false,false,true);

                }
            }
        }

    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            LogUtil.d(TAG,response.toString());
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }
    };
}
