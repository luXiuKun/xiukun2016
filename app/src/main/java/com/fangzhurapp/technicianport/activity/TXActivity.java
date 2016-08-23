package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BankCardData;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.NumberUtils;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.view.PayPwDialog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TXActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.img_tx_banklogo)
    SimpleDraweeView imgTxBanklogo;
    @Bind(R.id.tv_tx_bankname)
    TextView tvTxBankname;
    @Bind(R.id.tv_tx_num)
    TextView tvTxNum;
    @Bind(R.id.rl_tx_checkcard)
    RelativeLayout rlTxCheckcard;
    @Bind(R.id.et_tx_price)
    EditText etTxPrice;
    @Bind(R.id.ib_tx_next)
    ImageButton ibTxNext;
    private static final String TAG = "TXActivity";
    private List<BankCardData> cardList;
    private String id = "";
    private PayPwDialog payPwDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(TXActivity.this);
        setContentView(R.layout.activity_tx);
        CustomApplication.addAct(this);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        rlTxCheckcard.setOnClickListener(this);
        ibTxNext.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("提现");
        imgTitleRight.setVisibility(View.INVISIBLE);
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        etTxPrice.setHint("可提现金额"+SpUtil.getString(TXActivity.this,"ktxprice","")+"元");

        getBindList();
    }

    private void getBindList() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.GET_BINDCARD_LIST, RequestMethod.POST);
        jsonObjectRequest.add("staff_id", SpUtil.getString(TXActivity.this,"id",""));
        CallServer.getInstance().add(TXActivity.this,jsonObjectRequest,callback, UrlTag.GET_BINDCARD_LIST,true,false,true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.rl_tx_checkcard:
                if (cardList != null){

                    Intent intent = new Intent(TXActivity.this, SelectBankActivity.class);
                    intent.putExtra("carddata", (Serializable) cardList);
                    startActivityForResult(intent,0x10);
                }
                break;

            case R.id.ib_tx_next:

                if (cardList != null && !TextUtils.isEmpty(id)){


                    if (!TextUtils.isEmpty(etTxPrice.getText().toString())){
                        Float ktxprice = Float.valueOf(SpUtil.getString(TXActivity.this, "ktxprice", ""));
                        Float aFloat = Float.valueOf(etTxPrice.getText().toString());

                        if (aFloat > ktxprice){
                            Toast.makeText(TXActivity.this, "输入金额不能超过可提现金额", Toast.LENGTH_SHORT).show();
                        }else{

                            if (aFloat <100){
                                Toast.makeText(TXActivity.this, "每笔不能低于100元", Toast.LENGTH_SHORT).show();
                            }else{

                                String s = NumberUtils.floatFormat(aFloat);
                                showPayDialog(s);
                            }

                        }
                    }else{
                        Toast.makeText(TXActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.img_logo:
                TXActivity.this.finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 001){
            BankCardData resultcarddata = (BankCardData) data.getSerializableExtra("resultcarddata");
            if (resultcarddata != null){

                tvTxBankname.setText(resultcarddata.getBankname());
                String cardnumber = resultcarddata.getCardnumber();
                cardnumber = cardnumber.substring(cardnumber.length()-4,cardnumber.length());
                tvTxNum.setText("尾号"+cardnumber);
                imgTxBanklogo.setImageURI(Uri.parse(resultcarddata.getLogourl()));
                id = resultcarddata.getId();
            }
        }
    }

    private void showPayDialog(String price) {

        payPwDialog = new PayPwDialog(TXActivity.this, R.layout.dialog_paypw,price);
        payPwDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        payPwDialog.show();

        payPwDialog.setPayPwDialogListener(new PayPwDialog.payListener() {
            @Override
            public void txListener(String price, String pw) {
                applyTX(price,pw);
            }

            @Override
            public void forgetPw() {
                Intent intent = new Intent(TXActivity.this, ChangePayPwActivity.class);
                startActivity(intent);
                payPwDialog.dismiss();
            }
        });
    }

    private void applyTX(String price, String pw) {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.TX, RequestMethod.POST);
        jsonObjectRequest.add("staff_id",SpUtil.getString(TXActivity.this,"id",""));
        jsonObjectRequest.add("bid",id);
        jsonObjectRequest.add("money",price);
        jsonObjectRequest.add("payment",pw);

        CallServer.getInstance().add(TXActivity.this,jsonObjectRequest,callback,UrlTag.TX,true,false,true);
    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.GET_BINDCARD_LIST){
                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONArray data = jsonObject.getJSONArray("data");
                        cardList = new ArrayList<>();

                        for (int i =0;i<data.length();i++){

                            BankCardData bankCardData = new BankCardData();
                            bankCardData.setAddtime(data.getJSONObject(i).getString("addtime"));
                            bankCardData.setBankname(data.getJSONObject(i).getString("bankname"));
                            bankCardData.setCardnumber(data.getJSONObject(i).getString("cardnumber"));
                            bankCardData.setLogourl(data.getJSONObject(i).getString("logourl"));
                            bankCardData.setSname(data.getJSONObject(i).getString("sname"));
                            bankCardData.setStaff_id(data.getJSONObject(i).getString("staff_id"));
                            bankCardData.setId(data.getJSONObject(i).getString("id"));
                            cardList.add(bankCardData);
                        }

                        tvTxBankname.setText(cardList.get(0).getBankname());
                        String cardnumber = cardList.get(0).getCardnumber();
                        cardnumber = cardnumber.substring(cardnumber.length()-4,cardnumber.length());
                        tvTxNum.setText("尾号"+cardnumber);
                        imgTxBanklogo.setImageURI(Uri.parse(cardList.get(0).getLogourl()));
                        id = cardList.get(0).getId();
                    }else{
                        Toast.makeText(TXActivity.this, "获取银行卡信息失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (what == UrlTag.TX){

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){

                        JSONObject data = jsonObject.getJSONObject("data");
                        String isok = data.getString("isok");
                        if (isok.equals("1")){
                            payPwDialog.dismiss();
                            Intent intent = new Intent(TXActivity.this, TxSucessActivity.class);
                            startActivity(intent);
                            TXActivity.this.finish();

                        }else{
                            Toast.makeText(TXActivity.this, "提现失败", Toast.LENGTH_SHORT).show();
                        }
                    }else{

                        Toast.makeText(TXActivity.this, "提现失败", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        getBindList();
    }
}
