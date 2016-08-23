package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.utils.TimeUtils;
import com.fangzhurapp.technicianport.view.DataPickPopWindow;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的工资
 */
public class MyWalletActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_mywallet_time)
    TextView tvMywalletTime;
    @Bind(R.id.tv_mywallet_state)
    TextView tvMywalletState;
    @Bind(R.id.tv_mywallet_zmoney)
    TextView tvMywalletZmoney;
    @Bind(R.id.tv_mywallet_dixin)
    TextView tvMywalletDixin;
    @Bind(R.id.tv_mywallet_pz)
    TextView tvMywalletPz;
    @Bind(R.id.tv_mywallet_dz)
    TextView tvMywalletDz;
    @Bind(R.id.tv_mywallet_viptc)
    TextView tvMywalletViptc;
    @Bind(R.id.tv_mywallet_bonus)
    TextView tvMywalletBonus;
    @Bind(R.id.tv_mywallet_fine)
    TextView tvMywalletFine;

    private Calendar mCalendar = Calendar.getInstance();
    private String mYear = "";
    private String mMonth = "";

    private static final String TAG = "MyWalletActivity";
    private DataPickPopWindow dataPickPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        tvMywalletTime.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("我的工资");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);


        String time = TimeUtils.getBeforeMonth1();

        mYear = time.substring(0, 4);
        mMonth = time.substring(5, time.length());
        tvMywalletTime.setText(time);


        getMyWage();

    }

    private void getMyWage() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.MY_WALLET, RequestMethod.POST);
        jsonObjectRequest.add("id", SpUtil.getString(MyWalletActivity.this, "id", ""));
        jsonObjectRequest.add("years", mYear);
        jsonObjectRequest.add("month", mMonth);
        CallServer.getInstance().add(MyWalletActivity.this, jsonObjectRequest, callback, UrlTag.MY_WALLET, true, false, true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_logo:
                MyWalletActivity.this.finish();
                break;

            case R.id.tv_mywallet_time:

                showCalendar();
                break;

        }
    }

    private void showCalendar() {


        dataPickPopWindow = DataPickPopWindow.getInstance(MyWalletActivity.this, R.layout.popupwindow_datapicker);
        dataPickPopWindow.showPopupWindow(tvMywalletTime);
        dataPickPopWindow.setDataPick(new DataPickPopWindow.DataPickListener() {
            @Override
            public void onFinish(String time) {
                tvMywalletTime.setText(time);
                mYear = time.substring(0, 4);
                mMonth = time.substring(5, time.length());
                dataPickPopWindow.dismiss();
                getMyWage();
            }
        });

        /*if (dataPickPopWindow == null){
            dataPickPopWindow = new DataPickPopWindow(MyWalletActivity.this, R.layout.popupwindow_datapicker);

            dataPickPopWindow.showPopupWindow(tvMywalletTime);
            dataPickPopWindow.setDataPick(new DataPickPopWindow.DataPickListener() {
                @Override
                public void onFinish(String time) {
                    tvMywalletTime.setText(time);
                    mYear = time.substring(0, 4);
                    mMonth = time.substring(5, time.length());
                    dataPickPopWindow.dismiss();
                    getMyWage();
                }
            });

            dataPickPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    dataPickPopWindow = null;
                }
            });*/

        }




    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.MY_WALLET) {
                LogUtil.d(TAG, response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        String dmoney = data.getString("dmoney");
                        String typea = data.getString("typea");
                        String typeb = data.getString("typeb");
                        String types = data.getString("types");
                        String fmoney = data.getString("fmoney");
                        String jmoney = data.getString("jmoney");
                        String money = data.getString("money");
                        String status = data.getString("status");
                        tvMywalletDixin.setText(dmoney);
                        tvMywalletZmoney.setText("￥" + money);
                        tvMywalletPz.setText(typea);
                        tvMywalletDz.setText(typeb);
                        tvMywalletViptc.setText(types);
                        tvMywalletBonus.setText(jmoney);
                        tvMywalletFine.setText(fmoney);

                        if (status.equals("1")) {
                            tvMywalletState.setText("工资已发放");
                        } else if (status.equals("2")) {
                            tvMywalletState.setText("工资未发放");
                        }

                    }
                } catch (JSONException e) {
                    tvMywalletState.setText("本月没有工资记录");
                    tvMywalletDixin.setText("0");
                    tvMywalletZmoney.setText("￥" + "0");
                    tvMywalletPz.setText("0");
                    tvMywalletDz.setText("0");
                    tvMywalletViptc.setText("0");
                    tvMywalletBonus.setText("0");
                    tvMywalletFine.setText("0");
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }
    };
}
