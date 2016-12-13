package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.adapter.CommAdapter;
import com.fangzhurapp.technicianport.adapter.ViewHolder;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
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

public class PartnerShopActivity extends AppCompatActivity {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.lv_shop)
    ListView lvShop;
    private List<String> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_shop);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartnerShopActivity.this
                        .finish();
            }
        });
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        if (getIntent()!=null){

            String account = getIntent().getStringExtra("account");
            String name = getIntent().getStringExtra("name");
            tvShopname.setText(name+"的店铺");
            getList(account);
        }
    }

    private void getList(String account) {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.PARTNER_SHOP, RequestMethod.POST);
        jsonObjectRequest.add("account",account);
        CallServer.getInstance().add(PartnerShopActivity.this,jsonObjectRequest,callback, UrlTag.PARTNER_SHOP,true,false,true);
        

    }

    private static final String TAG = "PartnerShopActivity";
    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
           
            if (what == UrlTag.PARTNER_SHOP){

                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();

                try {
                    JSONArray data = jsonObject.getJSONArray("data");

                    if (data.length() > 0){

                        mDataList = new ArrayList<>();

                        for (int i =0;i < data.length();i++){

                            mDataList.add(data.getJSONObject(i).getString("sname"));


                        }

                        lvShop.setAdapter(new CommAdapter<String>(PartnerShopActivity.this,R.layout.item_partner_shop,mDataList) {
                            @Override
                            public void convert(ViewHolder holder, String s, int position) {
                                TextView tv_partner_shopName = holder.getView(R.id.tv_partner_shopName);

                                tv_partner_shopName.setText(s);
                            }
                        });



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
