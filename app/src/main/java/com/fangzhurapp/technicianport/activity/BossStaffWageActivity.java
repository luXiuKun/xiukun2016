package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.adapter.CommAdapter;
import com.fangzhurapp.technicianport.adapter.ViewHolder;
import com.fangzhurapp.technicianport.bean.BossStaffWageBean;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.NumberUtils;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.utils.TimeUtils;
import com.fangzhurapp.technicianport.view.DataPickPopWindow;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossStaffWageActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;

    @Bind(R.id.swipe_target)
    ListView swipeTarget;
    @Bind(R.id.swipe_staffwage)
    SwipeToLoadLayout swipeStaffwage;
    @Bind(R.id.rl_staffwage_nodata)
    RelativeLayout rlStaffwageNodata;
    @Bind(R.id.btn_bossstaffwage_state)
    Button btnBossstaffwageState;

    private String strTime = "";
    private String endTime = "";
    private static final String TAG = "BossStaffWageActivity";
    private ArrayList<BossStaffWageBean> dataList;
    private DataPickPopWindow dataPickPopWindow;
    private TextView tv_count;
    private TextView tv_wage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_staff_wage);
        ButterKnife.bind(this);
        CustomApplication.addAct(this);
        getSupportActionBar().hide();
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        swipeStaffwage.setOnRefreshListener(this);
        tvShopname.setOnClickListener(this);

        swipeTarget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(BossStaffWageActivity.this, BossStaffWageDetailActivity.class);
                intent.putExtra("bossstaffwagedetail", dataList.get(position));
                startActivity(intent);
            }
        });
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        imgTitleRight.setVisibility(View.INVISIBLE);
        swipeTarget.setEmptyView(rlStaffwageNodata);

        tvShopname.setText(TimeUtils.getBeforeMonth1());
        strTime = TimeUtils.getBeforeMonth1() + "-01 00:00:00";
        String beforeMonth1 = TimeUtils.getBeforeMonth1();
        String s = beforeMonth1.substring(5, 7);

        endTime = TimeUtils.getMonthLastDay1(Integer.valueOf(s)) + " 23:59:59";
        View footer = View.inflate(BossStaffWageActivity.this, R.layout.layout_boss_staffwage_footer, null);
        tv_count = (TextView) footer.findViewById(R.id.tv_boss_staffwage_staffcount);
        tv_wage = (TextView) footer.findViewById(R.id.tv_boss_staffwage_zwage);

        swipeTarget.addFooterView(footer);
        swipeStaffwage.setRefreshing(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_logo:
                BossStaffWageActivity.this.finish();
                break;

            case R.id.tv_shopname:
                dataPickPopWindow = DataPickPopWindow.getInstance(BossStaffWageActivity.this, R.layout.popupwindow_datapicker);

                dataPickPopWindow.showPopupWindow(btnBossstaffwageState);

                dataPickPopWindow.setDataPick(new DataPickPopWindow.DataPickListener() {
                    @Override
                    public void onFinish(String time) {
                        tvShopname.setText(time);
                        strTime = time + "-01 00:00:00";
                        String s = time.substring(5, time.length());
                        endTime = TimeUtils.getMonthLastDay1(Integer.valueOf(s)) + " 23:59:59";
                        dataPickPopWindow.dismiss();
                        swipeStaffwage.setRefreshing(true);
                    }
                });

                break;
        }
    }

    @Override
    public void onRefresh() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_STAFF_WAGE, RequestMethod.POST);

        jsonObjectRequest.add("sid", SpUtil.getString(BossStaffWageActivity.this, "sid", ""));
        jsonObjectRequest.add("strtime", strTime);
        jsonObjectRequest.add("endtime", endTime);

        CallServer.getInstance().add(BossStaffWageActivity.this, jsonObjectRequest, callback, UrlTag.BOSS_STAFF_WAGE, true, false, true);


    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.BOSS_STAFF_WAGE) {
                swipeStaffwage.setRefreshing(false);
                LogUtil.d(TAG, response.toString());

                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String is_f = data.getString("is_f");
                        String count = data.getString("count");
                        String sum = data.getString("sum");
                        tv_wage.setText(sum+"元");
                        tv_count.setText(count+"人");

                        if (is_f.equals("0")) {
                            btnBossstaffwageState.setText("发放工资");
                        } else {
                            btnBossstaffwageState.setText("工资已发放");

                        }
                        JSONArray list = data.getJSONArray("list");
                        if (list.length() > 0) {
                            btnBossstaffwageState.setVisibility(View.VISIBLE);
                            dataList = new ArrayList<>();

                            for (int i = 0; i < list.length(); i++) {

                                BossStaffWageBean bossStaffWageBean = new BossStaffWageBean();
                                bossStaffWageBean.setId(list.getJSONObject(i).getString("id"));
                                bossStaffWageBean.setName(list.getJSONObject(i).getString("name"));
                                bossStaffWageBean.setId_number(list.getJSONObject(i).getString("id_number"));
                                bossStaffWageBean.setDmoney(list.getJSONObject(i).getString("dmoney"));
                                bossStaffWageBean.setJmoney(list.getJSONObject(i).getString("jmoney"));
                                bossStaffWageBean.setFmoney(list.getJSONObject(i).getString("fmoney"));
                                bossStaffWageBean.setTypea(list.getJSONObject(i).getString("typea"));
                                bossStaffWageBean.setTypeb(list.getJSONObject(i).getString("typeb"));
                                bossStaffWageBean.setTypes(list.getJSONObject(i).getString("types"));
                                bossStaffWageBean.setMoney(list.getJSONObject(i).getString("money"));
                                dataList.add(bossStaffWageBean);
                            }


                            swipeTarget.setAdapter(new CommAdapter<BossStaffWageBean>(BossStaffWageActivity.this, R.layout.item_bossstaffwage, dataList) {
                                @Override
                                public void convert(ViewHolder holder, BossStaffWageBean bossStaffWageBean, int position) {
                                    TextView tv_bossstaffwage_name = holder.getView(R.id.tv_bossstaffwage_name);
                                    TextView tv_bossstaffwage_tc = holder.getView(R.id.tv_bossstaffwage_tc);
                                    TextView tv_bossstaffwage_wage = holder.getView(R.id.tv_bossstaffwage_wage);


                                    tv_bossstaffwage_name.setText(bossStaffWageBean.getName());

                                    tv_bossstaffwage_wage.setText(bossStaffWageBean.getMoney());
                                    Float a = Float.valueOf(bossStaffWageBean.getTypea()) + Float.valueOf(bossStaffWageBean.getTypeb()) + Float.valueOf(bossStaffWageBean.getTypes());
                                    if (a > 0) {

                                        String s = NumberUtils.floatFormat(a);
                                        tv_bossstaffwage_tc.setText(s);

                                    } else {

                                        tv_bossstaffwage_tc.setText("0.00");
                                    }


                                }
                            });

                        }

                    }
                } catch (JSONException e) {
                    swipeTarget.setAdapter(null);
                    btnBossstaffwageState.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            if (what == UrlTag.BOSS_STAFF_WAGE) {
                swipeStaffwage.setRefreshing(false);
            }
        }
    };
}
