package com.fangzhurapp.technicianport.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.et_feddback)
    EditText etFeddback;
    @Bind(R.id.tv_feedback_count)
    TextView tvFeedbackCount;
    @Bind(R.id.tv_feedback_zcount)
    TextView tvFeedbackZcount;
    private static final String TAG = "FeedBackActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        CustomApplication.addAct(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        imgTitleRight.setOnClickListener(this);
        etFeddback.addTextChangedListener(new TextWatcher() {
            int maxCount = 200;
            int temp =0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (temp < maxCount){
                    temp +=count;
                    temp -=before;
                    tvFeedbackCount.setText(temp+"");
                    tvFeedbackCount.setTextColor(Color.BLACK);
                    tvFeedbackZcount.setTextColor(Color.BLACK);
                    if (!TextUtils.isEmpty(etFeddback.getText())){

                        imgTitleRight.setBackgroundResource(R.drawable.img_feedback_pre);
                    }else{
                        imgTitleRight.setBackgroundResource(R.drawable.img_feedback_nor);
                    }
                }else{
                    temp +=count;
                    temp -=before;
                    tvFeedbackCount.setText(temp+"");
                    tvFeedbackCount.setTextColor(Color.BLACK);
                    tvFeedbackZcount.setTextColor(Color.BLACK);
                    if (!TextUtils.isEmpty(etFeddback.getText())){

                        imgTitleRight.setBackgroundResource(R.drawable.img_feedback_pre);
                    }else{
                        imgTitleRight.setBackgroundResource(R.drawable.img_feedback_nor);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (temp == maxCount){
                    tvFeedbackCount.setTextColor(Color.RED);
                    tvFeedbackZcount.setTextColor(Color.RED);
                }
            }
        });
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("意见反馈");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setBackgroundResource(R.drawable.img_feedback_nor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.img_title_right:

                if (!TextUtils.isEmpty(etFeddback.getText().toString())){

                    subMitData();
                }else{
                    Toast.makeText(FeedBackActivity.this, "请填写意见", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_logo:
                FeedBackActivity.this.finish();
                break;
        }
    }

    private void subMitData() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.FEED_BACK, RequestMethod.POST);
        jsonObjectRequest.add("uid", SpUtil.getString(FeedBackActivity.this,"id",""));
        jsonObjectRequest.add("ident",SpUtil.getString(FeedBackActivity.this,"ident",""));
        jsonObjectRequest.add("contnet",etFeddback.getText().toString());
        CallServer.getInstance().add(FeedBackActivity.this,jsonObjectRequest, callback,UrlTag.FEED_BACK,true,false,true);
    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.FEED_BACK){

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        Toast.makeText(FeedBackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        FeedBackActivity.this.finish();
                    }else{
                        Toast.makeText(FeedBackActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
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
