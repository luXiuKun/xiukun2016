package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.NumberUtils;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.utils.ToastUtil;
import com.fangzhurapp.technicianport.utils.WXPayUtil;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossTopupActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.cb_weixin)
    CheckBox cbWeixin;
    @Bind(R.id.rl_bosstopup_weixin)
    RelativeLayout rlBosstopupWeixin;
    @Bind(R.id.et_bosstopup_price)
    EditText etBosstopupPrice;
    @Bind(R.id.btn_confirmtopup)
    Button btnConfirmtopup;
    private String topupType = "1";
    private static final String TAG = "BossTopupActivity";
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_topup);
        ButterKnife.bind(this);
        CustomApplication.addAct(this);
        api = WXAPIFactory.createWXAPI(this, WXPayUtil.APP_ID);
        initView();
        initEvent();
    }

    private void initEvent() {
        rlBosstopupWeixin.setOnClickListener(this);
        btnConfirmtopup.setOnClickListener(this);
        imgLogo.setOnClickListener(this);
    }

    private void initView() {

        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("充值");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);




    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.btn_confirmtopup:
                if (cbWeixin.isChecked()){
                    if (!TextUtils.isEmpty(etBosstopupPrice.getText().toString())){
                        Float valueOf = Float.valueOf(etBosstopupPrice.getText().toString());
                        if (valueOf >= 100){

                            wxPay();
                        }else{
                            Toast.makeText(BossTopupActivity.this, "每笔金额最低100元", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(BossTopupActivity.this, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(BossTopupActivity.this, "请选择支付方式", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.img_logo:
                BossTopupActivity.this.finish();
                break;
            case R.id.rl_bosstopup_weixin:

                cbWeixin.setChecked( !cbWeixin.isChecked());
                break;

        }
    }

    private void wxPay() {




        String price = etBosstopupPrice.getText().toString();
        String payPrice = NumberUtils.floatFormat2((Float.valueOf(price) * 0.006f+Float.valueOf(price) ) * 100);
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_WXPAY, RequestMethod.POST);

        jsonObjectRequest.add("total_fee",payPrice);
        jsonObjectRequest.add("order_name","充值");
        jsonObjectRequest.add("sid", SpUtil.getString(BossTopupActivity.this,"sid",""));
        jsonObjectRequest.add("type", "4");
        jsonObjectRequest.add("type_id", SpUtil.getString(BossTopupActivity.this,"id",""));
        jsonObjectRequest.add("trade_type", "APP");
        CallServer.getInstance().add(BossTopupActivity.this,jsonObjectRequest,callback,UrlTag.WX_PAY,false,true,true);


    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.WX_PAY){

                Logger.d(response.toString());
                JSONObject jsonObject = response.get();

                try {

                    String out_trade_no = jsonObject.getString("out_trade_no");
                    if (!TextUtils.isEmpty(out_trade_no)){

                        SpUtil.putString(BossTopupActivity.this,"out_trade_no",out_trade_no);
                        JSONObject unifiedOrderResult = jsonObject.getJSONObject("unifiedOrderResult");
                        String prepay_id = unifiedOrderResult.getString("prepay_id");
                        String nonce_str = unifiedOrderResult.getString("nonce_str");
                        req(prepay_id,nonce_str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        @Override
        public void onFailed(int what,  Response<JSONObject> response) {

        }
    };

    private void req(String prepay_id,String nonce_str) {

        try {


                PayReq req = new PayReq();
                String time = WXPayUtil.getTime();
                String noncestr = WXPayUtil.getRandomStringByLength(32);
                req.appId			= WXPayUtil.APP_ID;
                req.partnerId		= WXPayUtil.MCH_ID;
                req.prepayId		= prepay_id;

                req.nonceStr		= nonce_str;
                req.timeStamp		= time;
                req.packageValue	= "Sign=WXPay";

                SortedMap<Object,Object> signMap = new TreeMap<Object,Object>();
                signMap.put("appid", req.appId);
                signMap.put("noncestr", req.nonceStr);
                signMap.put("package", req.packageValue);
                signMap.put("partnerid", req.partnerId);
                signMap.put("prepayid", req.prepayId);
                signMap.put("timestamp", req.timeStamp);

                String createSign = WXPayUtil.createSign("UTF-8", signMap);
                req.sign	= createSign;
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                api.registerApp(WXPayUtil.APP_ID);
                api.sendReq(req);



        } catch (Exception e) {
            // TODO: handle exception

        }
    }
}
