package com.fangzhurapp.technicianport.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.NumberUtils;
import com.fangzhurapp.technicianport.utils.SpUtil;
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

import org.json.JSONObject;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发放工资确认
 */
public class BossIssueWagesActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_bossissue_staffcount)
    TextView tvBossissueStaffcount;
    @Bind(R.id.tv_bossissue_wages)
    TextView tvBossissueWages;
    @Bind(R.id.tv_bossissue_month)
    TextView tvBossissueMonth;
    @Bind(R.id.tv_bossissue_moneypay)
    TextView tvBossissueMoneypay;
    @Bind(R.id.cb_bossissue_money)
    CheckBox cbBossissueMoney;
    @Bind(R.id.tv_bossissue_wxpay)
    TextView tvBossissueWxpay;
    @Bind(R.id.cb_bossissue_wxpay)
    CheckBox cbBossissueWxpay;
    @Bind(R.id.btn_bossissue_confirm)
    Button btnBossissueConfirm;
    private String selectType = "";
    private String staffwages;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_issue_wages);
        api = WXAPIFactory.createWXAPI(this, WXPayUtil.APP_ID);
        ButterKnife.bind(this);
        CustomApplication.addAct(this);
        initView();
        initEvent();
    }

    private void initEvent() {


        imgLogo.setOnClickListener(this);
        btnBossissueConfirm.setOnClickListener(this);

        cbBossissueMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectType = "1";
                    cbBossissueMoney.setEnabled(false);

                    cbBossissueWxpay.setEnabled(true);
                    cbBossissueWxpay.setChecked(false);
                    tvBossissueMoneypay.setTextColor(getResources().getColor(R.color.tab_bg));
                    tvBossissueWxpay.setTextColor(getResources().getColor(R.color.blacktext));

                }else{

                }
            }
        });

        cbBossissueWxpay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectType = "2";
                    cbBossissueWxpay.setEnabled(false);
                    cbBossissueMoney.setChecked(false);
                    cbBossissueMoney.setEnabled(true);

                    tvBossissueMoneypay.setTextColor(getResources().getColor(R.color.blacktext));
                    tvBossissueWxpay.setTextColor(getResources().getColor(R.color.tab_bg));
                }else{

                }
            }
        });


    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("确认信息");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);


        if (getIntent() != null){
            String month = getIntent().getStringExtra("month");

            int staffcount = getIntent().getIntExtra("staffcount", 0);
            staffwages = getIntent().getStringExtra("staffwages");
            tvBossissueWages.setText(staffwages);
            tvBossissueStaffcount.setText(staffcount+"人");
            tvBossissueMonth.setText(month);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_logo:
                BossIssueWagesActivity.this.finish();
                break;
            case R.id.btn_bossissue_confirm:
                    if (selectType.equals("1")){

                        moneyPay();
                    }else if (selectType.equals("2")){
                        wxPay();
                    }else{
                        Toast.makeText(BossIssueWagesActivity.this, "请选择一种支付方式", Toast.LENGTH_SHORT).show();
                    }
                break;
        }
    }

    private void moneyPay() {
        String bossktxprice = SpUtil.getString(BossIssueWagesActivity.this, "bossktxprice", "");
        if (Float.valueOf(bossktxprice) >= Float.valueOf(staffwages)){
            //现金支付
            Toast.makeText(BossIssueWagesActivity.this, "现金支付", Toast.LENGTH_SHORT).show();

        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("温馨提示")
                    .setMessage("您的账户余额不足")
                    .setNegativeButton("充值", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(BossIssueWagesActivity.this, BossTopupActivity.class);
                            startActivity(intent);
                        }
                    });

            builder.setPositiveButton("微信支付", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    wxPay();
                }
            });

            builder.show();



        }


    }

    private void wxPay() {



        String nonce_str = WXPayUtil.getRandomStringByLength(32);
        String orderNumber = WXPayUtil.getOrderNumber();
        String payPrice = NumberUtils.floatFormat2(Float.valueOf(staffwages) * 100);
        SortedMap<Object,Object> parameters = new TreeMap<>();
        parameters.put("appid", WXPayUtil.APP_ID);
        parameters.put("mch_id", WXPayUtil.MCH_ID);
        parameters.put("body", "发放工资");
        parameters.put("nonce_str", nonce_str);
        parameters.put("total_fee", payPrice);
        parameters.put("trade_type", "APP");
        parameters.put("out_trade_no", orderNumber);
        parameters.put("spbill_create_ip", "172.23.214.1");
        parameters.put("notify_url", "http://weixin.qq.com");

        try {


            String entity = "<xml>"
                    +"<appid>"+WXPayUtil.APP_ID+"</appid>"
                    +"<body>"+"发放工资"+"</body>"
                    +"<mch_id>"+WXPayUtil.MCH_ID+"</mch_id>"
                    +"<nonce_str>"+nonce_str+"</nonce_str>"
                    +"<notify_url>"+"http://weixin.qq.com"+"</notify_url>"
                    +"<out_trade_no>"+orderNumber+"</out_trade_no>"
                    +"<spbill_create_ip>"+"172.23.214.1"+"</spbill_create_ip>"
                    +"<sign>"+WXPayUtil.createSign("UTF-8", parameters)+"</sign>"
                    +"<total_fee>"+payPrice+"</total_fee>"
                    +"<trade_type>"+"APP"+"</trade_type>"
                    +"</xml>";



            Request<String> stringRequest = NoHttp.createStringRequest(WXPayUtil.WXPAY_URL, RequestMethod.POST);

            stringRequest.setDefineRequestBodyForXML(entity);
            CallServer.getInstance().add(BossIssueWagesActivity.this,stringRequest,callback, UrlTag.WX_PAY,true,false,true);



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }




    }



    private HttpCallBack<String> callback = new HttpCallBack<String>() {
        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == UrlTag.WX_PAY){

                Logger.d(response.toString());
                Map<String, String> readXml = WXPayUtil.readXml(response.get());
                if (readXml != null){
                    Gson gson = new Gson();
                    String data = gson.toJson(readXml);

                    req(data);
                }else{
                    Toast.makeText(BossIssueWagesActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
                }


            }
        }

        @Override
        public void onFailed(int what,  Response<String> response) {

        }
    };


    private void req(String data) {

        try {
            JSONObject json = new JSONObject(data);
            if(null != json && !json.has("retcode") ){

                PayReq req = new PayReq();
                String time = WXPayUtil.getTime();
                String noncestr = WXPayUtil.getRandomStringByLength(32);
                req.appId			= WXPayUtil.APP_ID;
                req.partnerId		= WXPayUtil.MCH_ID;
                req.prepayId		= json.getString("prepay_id");
                req.nonceStr		= noncestr;
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


            }else{
                Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
                Toast.makeText(BossIssueWagesActivity.this, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO: handle exception

        }
    }
}
