package com.fangzhurapp.technicianport.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.eventbus.BossBoolMsgEvent;
import com.fangzhurapp.technicianport.eventbus.PartnerPayMsgEvent;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.utils.WXPayUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_sucess)
    TextView tvSucess;

    private IWXAPI api;
    private Request<JSONObject> jsonObjectRequest;
    private Request<JSONObject> partnerQuery;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, WXPayUtil.APP_ID);
        api.handleIntent(getIntent(), this);
        tvShopname.setText("充值");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
        imgLogo.setBackgroundResource(R.drawable.img_title_back);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {


            if (resp.errCode == 0) {

                if (SpUtil.getString(WXPayEntryActivity.this, "selectident", "").equals("3")) {
                    tvSucess.setText("付费成功");
                    partnerQuery = NoHttp.createJsonObjectRequest(UrlConstant.PARTNER_QUERY, RequestMethod.POST);
                    partnerQuery.add("out_trade_no", SpUtil.getString(WXPayEntryActivity.this, "out_trade_no", ""));
                    partnerQuery.setRetryCount(3);
                    CallServer.getInstance().add(WXPayEntryActivity.this, partnerQuery, callback, UrlTag.PARTNER_QUERY, false, true, true);

                    imgLogo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WXPayEntryActivity.this.finish();
                        }
                    });
                } else {
                    jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_PAY_QUERY, RequestMethod.POST);
                    jsonObjectRequest.add("out_trade_no", SpUtil.getString(WXPayEntryActivity.this, "out_trade_no", ""));
                    jsonObjectRequest.setRetryCount(3);
                    CallServer.getInstance().add(WXPayEntryActivity.this, jsonObjectRequest, callback, UrlTag.BOSS_PAY_QUERY, false, true, true);

                    imgLogo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WXPayEntryActivity.this.finish();
                        }
                    });
                }


            }

            if (resp.errCode == -2) {
                WXPayEntryActivity.this.finish();

            }

            if (resp.errCode == -1) {
                Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                WXPayEntryActivity.this.finish();

            }


        }
    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {

            if (what == UrlTag.BOSS_PAY_QUERY) {

                Logger.d(response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String pay = jsonObject.getString("pay");

                    if (pay.equals("SUCCESS")) {
                        EventBus.getDefault().post(new BossBoolMsgEvent(true));
                    } else {
                        jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_PAY_QUERY, RequestMethod.POST);
                        jsonObjectRequest.add("out_trade_no", SpUtil.getString(WXPayEntryActivity.this, "out_trade_no", ""));
                        CallServer.getInstance().add(WXPayEntryActivity.this, jsonObjectRequest, callback, UrlTag.BOSS_PAY_QUERY, false, true, false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (what == UrlTag.PARTNER_QUERY) {

                JSONObject jsonObject = response.get();
                try {
                    JSONObject data = jsonObject.getJSONObject("data");
                    String pay = data.getString("pay");

                    if (pay.equals("SUCCESS")) {
                        SpUtil.putString(WXPayEntryActivity.this, "gold", "1");
                        SpUtil.putString(WXPayEntryActivity.this, "city", "0");
                        EventBus.getDefault().post(new PartnerPayMsgEvent(true));
                    } else {
                        partnerQuery = NoHttp.createJsonObjectRequest(UrlConstant.PARTNER_QUERY, RequestMethod.POST);
                        partnerQuery.add("out_trade_no", SpUtil.getString(WXPayEntryActivity.this, "out_trade_no", ""));
                        CallServer.getInstance().add(WXPayEntryActivity.this, partnerQuery, callback, UrlTag.PARTNER_QUERY, false, true, false);
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