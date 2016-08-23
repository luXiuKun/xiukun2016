package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.view.BindBankDialog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossTXPriceActivity extends AppCompatActivity implements View.OnClickListener {


    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_bosstxprice_moneydetail)
    TextView tvBosstxpriceMoneydetail;
    @Bind(R.id.tv_bosstxprice_money)
    TextView tvBosstxpriceMoney;
    @Bind(R.id.ib_bosstxprice_tx)
    ImageButton ibBosstxpriceTx;
    @Bind(R.id.ib_bosstxprice_pay)
    ImageButton ibBosstxpricePay;
    private BindBankDialog bindBankDialog;

    private static final String TAG = "BossTXPriceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_txprice);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        CustomApplication.addAct(this);

        initView();
        initEvent();

    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        ibBosstxpriceTx.setOnClickListener(this);
        ibBosstxpricePay.setOnClickListener(this);
        tvBosstxpriceMoneydetail.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("可提现金额");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        tvBosstxpriceMoneydetail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvBosstxpriceMoneydetail.getPaint().setAntiAlias(true);


        if (!TextUtils.isEmpty(SpUtil.getString(BossTXPriceActivity.this, "bossktxprice", ""))) {

            tvBosstxpriceMoney.setText(SpUtil.getString(BossTXPriceActivity.this, "bossktxprice", ""));
        } else {
            tvBosstxpriceMoney.setText("￥" + 0);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_logo:
                BossTXPriceActivity.this.finish();
                break;
            case R.id.ib_bosstxprice_tx:

                if (SpUtil.getString(BossTXPriceActivity.this,"shenfen","").equals("1")){

                    TxState();
                }else{
                    Toast.makeText(BossTXPriceActivity.this, "无操作权限", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.tv_bosstxprice_moneydetail:
                Intent intent2 = new Intent(BossTXPriceActivity.this, BossMoneyDetailActivity.class);
                startActivity(intent2);
                break;

            case R.id.ib_bosstxprice_pay:

                if (SpUtil.getString(BossTXPriceActivity.this,"shenfen","").equals("1")){
                    Toast.makeText(BossTXPriceActivity.this, "充值", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(BossTXPriceActivity.this, "无操作权限", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void TxState() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_TX_STATE, RequestMethod.POST);
        jsonObjectRequest.add("staff_id", SpUtil.getString(BossTXPriceActivity.this,"id",""));
        CallServer.getInstance().add(BossTXPriceActivity.this,jsonObjectRequest,callback, UrlTag.BOSS_TX_STATE,true,false,true);
    }
    
    
    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.BOSS_TX_STATE){

                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")){

                        JSONObject data = jsonObject.getJSONObject("data");
                        String bangka = data.getString("bangka");
                        String mima = data.getString("mima");

                        if (bangka.equals("-1")){
                            bindBankDialog = new BindBankDialog(BossTXPriceActivity.this, R.layout.dialog_bindbankcard);
                            bindBankDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            bindBankDialog.show();
                            bindBankDialog.setCancelBindListener(new BindBankDialog.CancelBindListener() {
                                @Override
                                public void setConfirmListener() {
                                    Intent intent = new Intent(BossTXPriceActivity.this, BindBankCardActivity.class);
                                    startActivity(intent);
                                    bindBankDialog.dismiss();
                                }
                            });

                        }else if (mima.equals("-1")){
                            Intent intent = new Intent(BossTXPriceActivity.this, SetPayPWActivity.class);
                            startActivity(intent);
                        }else if(mima.equals("1") && bangka.equals("1")){
                            SpUtil.putString(BossTXPriceActivity.this,"pwstate",mima);

                            Intent intent = new Intent(BossTXPriceActivity.this, BossTXActivity.class);
                            startActivity(intent);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }
    };


}
