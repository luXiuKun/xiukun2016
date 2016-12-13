package com.fangzhurapp.technicianport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.VipConsumneDetailBean;
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

public class BossVipConsumeDetailAct extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.vip_consumedetail)
    ListView vipConsumedetail;
    @Bind(R.id.rl_vipconsumedetail_nodata)
    RelativeLayout rlVipconsumedetailNodata;

    private List<VipConsumneDetailBean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_vip_consume_detail);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        imgLogo.setOnClickListener(this);
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        vipConsumedetail.setEmptyView(rlVipconsumedetailNodata);
        if (getIntent() != null) {

            String uid = getIntent().getStringExtra("uid");
            String vipname = getIntent().getStringExtra("vipname");
            String endtime = getIntent().getStringExtra("endtime");
            String statime = getIntent().getStringExtra("statime");

            tvShopname.setText(vipname + "消费记录");

            getConsumeDetail(uid, statime, endtime);
        }
    }

    private void getConsumeDetail(String uid, String statime, String endtime) {


        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_VIPCONSUME_DETAIL, RequestMethod.POST);

        jsonObjectRequest.add("uid", uid);
        jsonObjectRequest.add("strtime", statime);
        jsonObjectRequest.add("endtime", endtime);
        jsonObjectRequest.add("sid", SpUtil.getString(BossVipConsumeDetailAct.this, "sid", ""));
        CallServer.getInstance().add(BossVipConsumeDetailAct.this, jsonObjectRequest, callback, UrlTag.BOSS_VIPCONSUME_DETAIL, true, false, true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_logo:
                BossVipConsumeDetailAct.this.finish();
                break;
        }
    }

    private static final String TAG = "BossVipConsumeDetailAct";

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {


            if (what == UrlTag.BOSS_VIPCONSUME_DETAIL) {
                LogUtil.d(TAG, response.toString());
                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")) {

                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() > 0) {

                            dataList = new ArrayList<>();

                            for (int i = 0; i < data.length(); i++) {

                                VipConsumneDetailBean vipConsumneDetailBean = new VipConsumneDetailBean();

                                vipConsumneDetailBean.setFname(data.getJSONObject(i).getString("fname"));
                                vipConsumneDetailBean.setJmoney(data.getJSONObject(i).getString("jmoney"));
                                vipConsumneDetailBean.setOnumber(data.getJSONObject(i).getString("onumber"));
                                vipConsumneDetailBean.setTime(data.getJSONObject(i).getString("time"));
                                vipConsumneDetailBean.setTmoney(data.getJSONObject(i).getString("tmoney"));
                                vipConsumneDetailBean.setXname(data.getJSONObject(i).getString("xname"));
                                vipConsumneDetailBean.setType(data.getJSONObject(i).getString("type"));
                                dataList.add(vipConsumneDetailBean);


                            }

                            ConsumeDetailAdapter consumeDetailAdapter = new ConsumeDetailAdapter();

                            vipConsumedetail.setAdapter(consumeDetailAdapter);

                        }

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


    private class ConsumeDetailAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public int getItemViewType(int position) {

            if (dataList.get(position).getType().equals("项目消费")) {

                return 0;
            } else if (dataList.get(position).getType().equals("商品消费")) {
                return 1;
            }
            return -1;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            GoodsHolder goodsHold = null;
            ProjectHolder proHold = null;

            if (convertView == null) {

                int itemViewType = getItemViewType(position);

                if (itemViewType != -1) {

                    switch (itemViewType) {

                        case 0:

                            proHold = new ProjectHolder();
                            convertView = LayoutInflater.from(BossVipConsumeDetailAct.this).inflate(R.layout.item_vipconsume_project, null);

                            proHold.tv_vipconsume_projectname = (TextView) convertView.findViewById(R.id.tv_vipconsume_projectname);
                            proHold.tv_vipconsume_projectprice = (TextView) convertView.findViewById(R.id.tv_vipconsume_projectprice);
                            proHold.tv_vipconsume_jstc = (TextView) convertView.findViewById(R.id.tv_vipconsume_jstc);
                            proHold.tv_vipconsume_time = (TextView) convertView.findViewById(R.id.tv_vipconsume_time);
                            proHold.tv_vipconsume_jsname = (TextView) convertView.findViewById(R.id.tv_vipconsume_jsname);
                            proHold.tv_vipconsume_ordernum = (TextView) convertView.findViewById(R.id.tv_vipconsume_ordernum);

                            proHold.tv_vipconsume_projectname.setText(dataList.get(position).getXname());
                            proHold.tv_vipconsume_projectprice.setText(dataList.get(position).getJmoney());
                            proHold.tv_vipconsume_jstc.setText(dataList.get(position).getTmoney());
                            proHold.tv_vipconsume_time.setText(dataList.get(position).getTime());
                            proHold.tv_vipconsume_jsname.setText(dataList.get(position).getFname());
                            proHold.tv_vipconsume_ordernum.setText(dataList.get(position).getOnumber());


                            convertView.setTag(proHold);
                            break;

                        case 1:
                            goodsHold = new GoodsHolder();
                            convertView = LayoutInflater.from(BossVipConsumeDetailAct.this).inflate(R.layout.item_vipconsume_goods, null);

                            goodsHold.tv_vipconsume_goodsname = (TextView) convertView.findViewById(R.id.tv_vipconsume_goodsname);
                            goodsHold.tv_vipconsume_goodsprice = (TextView) convertView.findViewById(R.id.tv_vipconsume_goodsprice);
                            goodsHold.tv_vipconsume_goodsjstc = (TextView) convertView.findViewById(R.id.tv_vipconsume_goodsjstc);
                            goodsHold.tv_vipconsume_goodstime = (TextView) convertView.findViewById(R.id.tv_vipconsume_goodstime);
                            goodsHold.tv_vipconsume_xsstaffname = (TextView) convertView.findViewById(R.id.tv_vipconsume_xsstaffname);
                            goodsHold.tv_vipconsume_goodsordernum = (TextView) convertView.findViewById(R.id.tv_vipconsume_goodsordernum);

                            goodsHold.tv_vipconsume_goodsname.setText(dataList.get(position).getXname());
                            goodsHold.tv_vipconsume_goodsprice.setText(dataList.get(position).getJmoney());
                            goodsHold.tv_vipconsume_goodsjstc.setText(dataList.get(position).getTmoney());
                            goodsHold.tv_vipconsume_goodstime.setText(dataList.get(position).getTime());
                            goodsHold.tv_vipconsume_xsstaffname.setText(dataList.get(position).getFname());
                            goodsHold.tv_vipconsume_goodsordernum.setText(dataList.get(position).getOnumber());

                            convertView.setTag(goodsHold);
                            break;
                    }

                }

            } else {

                int itemViewType = getItemViewType(position);

                if (itemViewType != -1) {

                    switch (itemViewType) {

                        case 0:
                            proHold = (ProjectHolder) convertView.getTag();

                            proHold.tv_vipconsume_projectname.setText(dataList.get(position).getXname());
                            proHold.tv_vipconsume_projectprice.setText(dataList.get(position).getJmoney());
                            proHold.tv_vipconsume_jstc.setText(dataList.get(position).getTmoney());
                            proHold.tv_vipconsume_time.setText(dataList.get(position).getTime());
                            proHold.tv_vipconsume_jsname.setText(dataList.get(position).getFname());
                            proHold.tv_vipconsume_ordernum.setText(dataList.get(position).getOnumber());

                            break;

                        case 1:

                            goodsHold = (GoodsHolder) convertView.getTag();


                            goodsHold.tv_vipconsume_goodsname.setText(dataList.get(position).getXname());
                            goodsHold.tv_vipconsume_goodsprice.setText(dataList.get(position).getJmoney());
                            goodsHold.tv_vipconsume_goodsjstc.setText(dataList.get(position).getTmoney());
                            goodsHold.tv_vipconsume_goodstime.setText(dataList.get(position).getTime());
                            goodsHold.tv_vipconsume_xsstaffname.setText(dataList.get(position).getFname());
                            goodsHold.tv_vipconsume_goodsordernum.setText(dataList.get(position).getOnumber());

                            break;
                    }

                }


            }
            return convertView;
        }


        class GoodsHolder {
            TextView tv_vipconsume_goodsname;
            TextView tv_vipconsume_goodsprice;
            TextView tv_vipconsume_goodsjstc;
            TextView tv_vipconsume_goodstime;
            TextView tv_vipconsume_xsstaffname;
            TextView tv_vipconsume_goodsordernum;

        }

        class ProjectHolder {
            TextView tv_vipconsume_projectname;
            TextView tv_vipconsume_projectprice;
            TextView tv_vipconsume_jstc;
            TextView tv_vipconsume_time;
            TextView tv_vipconsume_jsname;
            TextView tv_vipconsume_ordernum;

        }
    }
}
