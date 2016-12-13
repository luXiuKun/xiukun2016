package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.eventbus.BossBindSucessEvent;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.view.CancelBindDialog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * boss  我的银行卡
 */
public class BossBindCardActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.et_bindbank_name)
    EditText etBindbankName;
    @Bind(R.id.et_bindbank_card)
    EditText etBindbankCard;
    @Bind(R.id.img_bindbank_banklogo)
    SimpleDraweeView imgBindbankBanklogo;
    @Bind(R.id.tv_bindbank_bankname)
    TextView tvBindbankBankname;
    @Bind(R.id.ib_bindbank_confirm)
    ImageButton ibBindbankConfirm;

    private static final String TAG = "BindBankCardActivity";
    private Boolean REQUEST_STATE = true;//防止多次请求的状态
    private Boolean GET_CARDSUCESS = false;//获取到银行卡信息成功的状态
    private String bankName = "";//银行名称
    private String bankLogo = "";//银行logo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomApplication.addAct(this);
        Fresco.initialize(BossBindCardActivity.this);
        setContentView(R.layout.activity_bind_bank_card);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        ibBindbankConfirm.setOnClickListener(this);
        imgLogo.setOnClickListener(this);
        etBindbankCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.d(TAG,etBindbankCard.getText().toString().length()+"");
                    if (etBindbankCard.getText().toString().length() >= 10 && REQUEST_STATE){

                        /**
                         * 获取银行卡信息
                         */
                        getCardData();
                    }else if (etBindbankCard.getText().toString().length() <= 10){

                        REQUEST_STATE = true;
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    private void getCardData() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.GET_BANKCARDDATA, RequestMethod.POST);
        jsonObjectRequest.add("account",etBindbankCard.getText().toString());
        CallServer.getInstance().add(BossBindCardActivity.this,jsonObjectRequest,callback, UrlTag.GET_BANKCARD_DATA,true,false,true);


    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {

            if (what == UrlTag.GET_BANKCARD_DATA){
                LogUtil.d(TAG,response.toString());
                REQUEST_STATE = false;
                JSONObject jsonObject = response.get();
                try {
                    String status = jsonObject.getString("status");
                    if (status.equals("1")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray bankinfo = data.getJSONArray("bankinfo");
                        GET_CARDSUCESS = true;
                        for (int i =0 ; i< bankinfo.length(); i++){
                            bankName =bankinfo.getJSONObject(0).getString("bankname");
                            bankLogo = bankinfo.getJSONObject(0).getString("logourl");
                            tvBindbankBankname.setText(bankinfo.getJSONObject(0).getString("bankname"));

                            Uri imgUrl = Uri.parse(bankinfo.getJSONObject(0).getString("logourl"));
                            imgBindbankBanklogo.setImageURI(imgUrl);

                        }
                    }else if (status.equals("-1")){
                        GET_CARDSUCESS = false;
                        bankName = "";
                        bankLogo = "";
                        Toast.makeText(BossBindCardActivity.this, "未找到此银行卡信息", Toast.LENGTH_SHORT).show();
                        tvBindbankBankname.setText("");
                        imgBindbankBanklogo.setImageURI(null);
                    }
                } catch (JSONException e) {
                    GET_CARDSUCESS = false;
                    bankName = "";
                    bankLogo = "";
                    tvBindbankBankname.setText("");
                    imgBindbankBanklogo.setImageURI(null);
                    Toast.makeText(BossBindCardActivity.this, "未找到此银行卡信息", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }else if (what == UrlTag.BOSS_BIND_BANKCARD){

                LogUtil.d(TAG,response.toString());
                try {
                    JSONObject jsonObject = response.get();
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){

                        EventBus.getDefault().post(new BossBindSucessEvent(true));
                        Intent intent = new Intent(BossBindCardActivity.this, BindSucessActivty.class);
                        startActivity(intent);
                        BossBindCardActivity.this.finish();


                    }else{
                        JSONObject msg = jsonObject.getJSONObject("msg");
                        String fanhui = msg.getString("fanhui");
                        Toast.makeText(BossBindCardActivity.this, fanhui, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int what,  Response<JSONObject> response) {
            REQUEST_STATE = true;
        }
    };

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("绑定银行卡");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);
        etBindbankName.setText(SpUtil.getString(BossBindCardActivity.this,"name",""));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ib_bindbank_confirm:

                if ( !TextUtils.isEmpty(etBindbankName.getText().toString())
                        &&!TextUtils.isEmpty(etBindbankCard.getText().toString()) && GET_CARDSUCESS ){

                    bindBank();
                }else{

                    if (TextUtils.isEmpty(etBindbankName.getText().toString()) ){
                        Toast.makeText(BossBindCardActivity.this, "请输入姓名!", Toast.LENGTH_SHORT).show();
                    }else if (TextUtils.isEmpty(etBindbankCard.getText().toString())){
                        Toast.makeText(BossBindCardActivity.this, "请输入银行卡号!", Toast.LENGTH_SHORT).show();
                    }else if (!GET_CARDSUCESS){
                        Toast.makeText(BossBindCardActivity.this, "没有银行卡信息!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.img_logo:
                CancelBindDialog cancelBindDialog = new CancelBindDialog(BossBindCardActivity.this, R.layout.dialog_cancelbind);
                cancelBindDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                cancelBindDialog.show();

                cancelBindDialog.setCancelBindListener(new CancelBindDialog.CancelBindListener() {
                    @Override
                    public void setConfirmListener() {
                            BossBindCardActivity.this.finish();
                    }
                });
                break;
        }
    }

    private void bindBank() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_BIND_BANKCARD, RequestMethod.POST);
        jsonObjectRequest.add("account",etBindbankCard.getText().toString());
        jsonObjectRequest.add("staff_id", SpUtil.getString(BossBindCardActivity.this,"id",""));
        jsonObjectRequest.add("sname",etBindbankName.getText().toString());
        jsonObjectRequest.add("bankname",bankName);
        jsonObjectRequest.add("logourl",bankLogo);
        CallServer.getInstance().add(BossBindCardActivity.this,jsonObjectRequest,callback,UrlTag.BOSS_BIND_BANKCARD,true,false,true);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode ==KeyEvent.KEYCODE_BACK ){

            CancelBindDialog cancelBindDialog = new CancelBindDialog(BossBindCardActivity.this, R.layout.dialog_cancelbind);
            cancelBindDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            cancelBindDialog.show();

            cancelBindDialog.setCancelBindListener(new CancelBindDialog.CancelBindListener() {
                @Override
                public void setConfirmListener() {
                    BossBindCardActivity.this.finish();
                }
            });
            return true;
        }else{

            return super.onKeyDown(keyCode, event);
        }
    }


}
