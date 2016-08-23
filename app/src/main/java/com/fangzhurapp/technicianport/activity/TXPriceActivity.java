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

public class TXPriceActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_txprice_moneydetail)
    TextView tvTxpriceMoneydetail;
    @Bind(R.id.tv_txprice_money)
    TextView tvTxpriceMoney;
    @Bind(R.id.ib_txprice_tx)
    ImageButton ibTxpriceTx;
    @Bind(R.id.ib_txprice_addbank)
    ImageButton ibTxpriceAddbank;
    private static final String TAG = "TXPriceActivity";
    private BindBankDialog bindBankDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txprice);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

        initView();
        initEvent();

    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        ibTxpriceTx.setOnClickListener(this);
        ibTxpriceAddbank.setOnClickListener(this);
        tvTxpriceMoneydetail.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("可提现金额");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        tvTxpriceMoneydetail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvTxpriceMoneydetail.getPaint().setAntiAlias(true);


            if (!TextUtils.isEmpty(SpUtil.getString(TXPriceActivity.this,"ktxprice",""))){

                tvTxpriceMoney.setText("￥"+SpUtil.getString(TXPriceActivity.this,"ktxprice",""));
            }else{
                tvTxpriceMoney.setText("￥"+0);
            }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.img_logo:
                TXPriceActivity.this.finish();
                break;
            case R.id.ib_txprice_tx:
                tiXian();

                break;
            case R.id.ib_txprice_addbank:
                Intent intent = new Intent(TXPriceActivity.this, MyBindBankActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_txprice_moneydetail:
                Intent intent1 = new Intent(TXPriceActivity.this, MoneyDetailActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void tiXian() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BIND_STATE, RequestMethod.POST);
        jsonObjectRequest.add("staff_id", SpUtil.getString(TXPriceActivity.this,"id",""));
        CallServer.getInstance().add(TXPriceActivity.this,jsonObjectRequest,callback, UrlTag.TX,true,false,true);
    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.TX){
                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")){

                        JSONObject data = jsonObject.getJSONObject("data");
                        String bangka = data.getString("bangka");
                        String mima = data.getString("mima");

                        if (bangka.equals("-1")){
                            bindBankDialog = new BindBankDialog(TXPriceActivity.this, R.layout.dialog_bindbankcard);
                            bindBankDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            bindBankDialog.show();
                            bindBankDialog.setCancelBindListener(new BindBankDialog.CancelBindListener() {
                                @Override
                                public void setConfirmListener() {
                                    Intent intent = new Intent(TXPriceActivity.this, BindBankCardActivity.class);
                                    startActivity(intent);
                                    bindBankDialog.dismiss();
                                }
                            });

                        }else if (mima.equals("-1")){
                            Intent intent = new Intent(TXPriceActivity.this, SetPayPWActivity.class);
                            startActivity(intent);
                        }else if(mima.equals("1") && bangka.equals("1")){
                            SpUtil.putString(TXPriceActivity.this,"pwstate",mima);
                            Intent intent = new Intent(TXPriceActivity.this, TXActivity.class);
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
