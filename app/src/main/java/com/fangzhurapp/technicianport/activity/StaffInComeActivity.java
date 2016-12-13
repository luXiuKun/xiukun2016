package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.adapter.staffIncomeAdapter;
import com.fangzhurapp.technicianport.bean.WorkorderBean;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StaffInComeActivity extends AppCompatActivity {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.tv_staff_income)
    TextView tvStaffIncome;
    @Bind(R.id.lv_staff_income)
    ListView lvStaffIncome;
    private List<WorkorderBean> orderList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_in_come);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("我的收入");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaffInComeActivity.this.finish();
            }
        });

        if (getIntent() != null){

            String jsincome = getIntent().getStringExtra("jsincome");
            tvStaffIncome.setText("￥"+jsincome);

        }
        
        getInCome();
        
        
    }

    private void getInCome() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.ORDER, RequestMethod.POST);

        jsonObjectRequest.add("id", SpUtil.getString(StaffInComeActivity.this,"id",""));

        CallServer.getInstance().add(StaffInComeActivity.this,jsonObjectRequest,callback, UrlTag.ORDER,true,false,true);

    }

    private static final String TAG = "StaffInComeActivity";
    
    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            
            if (what == UrlTag.ORDER){

                LogUtil.d(TAG,response.toString());


                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){

                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() >0){

                            orderList = new ArrayList<>();

                            for (int i = 0; i< data.length(); i++){

                                if (data.getJSONObject(i).getString("o_type").equals("2")){

                                    if (data.getJSONObject(i).getString("items_type").equals("2")
                                            || data.getJSONObject(i).getString("items_type").equals("3")){

                                        WorkorderBean workorderBean = new WorkorderBean();
                                        workorderBean.setId(data.getJSONObject(i).getString("id"));
                                        workorderBean.setMnumber(data.getJSONObject(i).getString("mnumber"));
                                        workorderBean.setProject_name(data.getJSONObject(i).getString("project_name"));
                                        workorderBean.setPtime(data.getJSONObject(i).getString("ptime"));
                                        workorderBean.setTc_money(data.getJSONObject(i).getString("tc_money"));
                                        workorderBean.setO_type(data.getJSONObject(i).getString("o_type"));
                                        workorderBean.setMoney(data.getJSONObject(i).getString("money"));
                                        workorderBean.setSet_type(data.getJSONObject(i).getString("set_type"));
                                        workorderBean.setAdd_order(data.getJSONObject(i).getString("add_order"));
                                        workorderBean.setRoom_id(data.getJSONObject(i).getString("room_id"));
                                        workorderBean.setRoom_number(data.getJSONObject(i).getString("room_number"));
                                        workorderBean.setItems_type(data.getJSONObject(i).getString("items_type"));

                                        orderList.add(workorderBean);

                                    }else if (data.getJSONObject(i).getString("items_type").equals("1")){
                                        WorkorderBean workorderBean = new WorkorderBean();
                                        workorderBean.setId(data.getJSONObject(i).getString("id"));
                                        workorderBean.setMnumber(data.getJSONObject(i).getString("mnumber"));
                                        workorderBean.setProject_name(data.getJSONObject(i).getString("project_name"));
                                        workorderBean.setPtime(data.getJSONObject(i).getString("ptime"));
                                        workorderBean.setType(data.getJSONObject(i).getString("type"));
                                        workorderBean.setTc_money(data.getJSONObject(i).getString("tc_money"));
                                        workorderBean.setO_type(data.getJSONObject(i).getString("o_type"));
                                        workorderBean.setMoney(data.getJSONObject(i).getString("money"));
                                        workorderBean.setSet_type(data.getJSONObject(i).getString("set_type"));
                                        workorderBean.setAdd_order(data.getJSONObject(i).getString("add_order"));
                                        workorderBean.setRoom_id(data.getJSONObject(i).getString("room_id"));
                                        workorderBean.setRoom_number(data.getJSONObject(i).getString("room_number"));
                                        workorderBean.setItems_type(data.getJSONObject(i).getString("items_type"));
                                        orderList.add(workorderBean);
                                    }

                                }else if (data.getJSONObject(i).getString("o_type").equals("1")){

                                    WorkorderBean workorderBean = new WorkorderBean();
                                    workorderBean.setId(data.getJSONObject(i).getString("id"));
                                    workorderBean.setMnumber(data.getJSONObject(i).getString("mnumber"));
                                    workorderBean.setAddtime(data.getJSONObject(i).getString("addtime"));
                                    workorderBean.setK_type(data.getJSONObject(i).getString("k_type"));
                                    workorderBean.setK_money(data.getJSONObject(i).getString("k_money"));
                                    workorderBean.setTc_money(data.getJSONObject(i).getString("tc_money"));
                                    workorderBean.setO_type(data.getJSONObject(i).getString("o_type"));
                                    orderList.add(workorderBean);

                                }

                            }

                            staffIncomeAdapter staffIncomeAdapter = new staffIncomeAdapter(StaffInComeActivity.this, orderList);
                            lvStaffIncome.setAdapter(staffIncomeAdapter);

                        }else{

                        }


                    }else{

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
