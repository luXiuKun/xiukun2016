package com.fangzhurapp.technicianport.frag;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fangzhurapp.technicianport.BossMainActivity;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.activity.BossBindBankActivity;
import com.fangzhurapp.technicianport.activity.BossStaffWageActivity;
import com.fangzhurapp.technicianport.activity.BossTXPriceActivity;
import com.fangzhurapp.technicianport.adapter.CommAdapter;
import com.fangzhurapp.technicianport.adapter.ViewHolder;
import com.fangzhurapp.technicianport.bean.ShopdataBean;
import com.fangzhurapp.technicianport.eventbus.BossBoolMsgEvent;
import com.fangzhurapp.technicianport.eventbus.BossMsgEvent;
import com.fangzhurapp.technicianport.eventbus.BossNameMsgEvent;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2016/8/1.
 */
public class BossWalletFrag extends Fragment implements OnRefreshListener,View.OnClickListener{

    private Context mContext;
    private View view;
    private GridView swipe_target;
    private TextView tv_shopname;
    private SwipeToLoadLayout swipe_bosswallet;
    private String[] text = new String[]{"可提现金额","结算中的金额","员工工资","我的银行卡"};
    private int[] img = new int[]{R.drawable.wallet_ktxmoney,R.drawable.wallet_jsmoney,R.drawable.wallet_wage,R.drawable.img_bosswallet_bankcard};
    private static final String TAG = "BossWalletFrag";
    private List<String> dataList;

    private String CHANGE_SHOP_STATE = "1";
    private PopupWindow popupWindow;
    private ListView pop_listview;
    private List<ShopdataBean> shopList;
    private ImageView img_title_indicator;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (BossMainActivity)context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_bosswallet, null);
        EventBus.getDefault().register(this);
        initView();
        initEvent();

        return view;
    }

    private void initEvent() {
        tv_shopname.setOnClickListener(this);
        swipe_bosswallet.setOnRefreshListener(this);

        swipe_target.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 0:

                        TextView tv_gvwallet_price = (TextView) parent.getChildAt(position).findViewById(R.id.tv_gvwallet_price);
                        SpUtil.putString(mContext,"bossktxprice",tv_gvwallet_price.getText().toString());
                        Intent intent = new Intent(mContext, BossTXPriceActivity.class);
                        startActivity(intent);

                        break;

                    case 2:
                        Intent staffwage = new Intent(mContext, BossStaffWageActivity.class);
                        startActivity(staffwage);
                        break;

                    case 3:
                        Intent bossbind = new Intent(mContext, BossBindBankActivity.class);
                        startActivity(bossbind);
                        break;
                }
            }
        });

    }

    private void initView() {

        ImageView img_title_right = (ImageView) view.findViewById(R.id.img_title_right);
        img_title_right.setVisibility(View.INVISIBLE);
        swipe_target = (GridView) view.findViewById(R.id.swipe_target);
        tv_shopname = (TextView) view.findViewById(R.id.tv_shopname);
        swipe_bosswallet = (SwipeToLoadLayout) view.findViewById(R.id.swipe_bosswallet);

        img_title_indicator = (ImageView) view.findViewById(R.id.img_title_indicator);
  /*      swipe_target.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return text.length;
            }

            @Override
            public Object getItem(int position) {
                return text[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                convertView = View.inflate(mContext,R.layout.item_walletgv,null);
                TextView tv_gvwallet_type = (TextView) convertView.findViewById(R.id.tv_gvwallet_type);
                TextView tv_gvwallet_price = (TextView) convertView.findViewById(R.id.tv_gvwallet_price);
                ImageView img_gvwallet = (ImageView) convertView.findViewById(R.id.img_gvwallet);


                    if (position ==text.length -1 ){

                        img_gvwallet.setBackgroundResource(img[position]);
                        tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.gvwallet_textcolor));
                        tv_gvwallet_type.setText(text[position]);
                        tv_gvwallet_price.setText("0张");
                        tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.cardtext));
                    }else if(position == 1){

                        img_gvwallet.setBackgroundResource(img[position]);
                        tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.gvwallet_textcolor));
                        tv_gvwallet_type.setText(text[position]);
                        tv_gvwallet_type.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.viewline));
                        tv_gvwallet_price.setText("￥"+"0.0");
                        tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.viewline));
                    }else{
                        img_gvwallet.setBackgroundResource(img[position]);
                        tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.gvwallet_textcolor));
                        tv_gvwallet_type.setText(text[position]);
                        tv_gvwallet_price.setText("￥"+"0.0");
                    }


                return convertView;
            }
        });*/
        getShopData();
        swipe_bosswallet.setRefreshing(true);
    }


    @Subscribe
    public void onEventMainThread(BossMsgEvent msg){
        tv_shopname.setText(msg.getStringMsg());
    }

    @Subscribe
    public void onEventMainThread(BossBoolMsgEvent msg){
        swipe_bosswallet.setRefreshing(true);
    }

    @Override
    public void onRefresh() {


        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_WALLET_HOME, RequestMethod.POST);
        jsonObjectRequest.add("sid",SpUtil.getString(mContext,"sid",""));
        jsonObjectRequest.add("id",SpUtil.getString(mContext,"id",""));
        jsonObjectRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback, UrlTag.BOSS_WALLET_HOME,true,false,true);

    }

    private void getShopData() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_SHOP_CHANGE, RequestMethod.POST);
        jsonObjectRequest.add("phone",SpUtil.getString(mContext,"phone",""));
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback,UrlTag.BOSS_SHOP_CHANGE,true,false,true);
    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {

            if (what == UrlTag.BOSS_WALLET_HOME){
                swipe_bosswallet.setRefreshing(false);
                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        dataList = new ArrayList<>();
                        JSONObject data = jsonObject.getJSONObject("data");
                        dataList.add(data.getString("tx_money"));
                        dataList.add(data.getString("js_money"));
                        dataList.add(data.getString("yggz"));
                        dataList.add( data.getString("binbank_count"));
                        swipe_target.setAdapter(new CommAdapter<String>(mContext,R.layout.item_walletgv,dataList) {
                            @Override
                            public void convert(ViewHolder holder, String s, int position) {

                                TextView tv_gvwallet_type = holder.getView(R.id.tv_gvwallet_type);
                                TextView tv_gvwallet_price = holder.getView(R.id.tv_gvwallet_price);
                                ImageView img_gvwallet = holder.getView(R.id.img_gvwallet);

                                if (position ==text.length -1 ){

                                    img_gvwallet.setBackgroundResource(img[position]);
                                    tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.gvwallet_textcolor));
                                    tv_gvwallet_type.setText(text[position]);
                                    tv_gvwallet_price.setText(dataList.get(position)+"张");
                                    tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.cardtext));

                                }else if(position == 1){

                                    img_gvwallet.setBackgroundResource(img[position]);
                                    tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.gvwallet_textcolor));
                                    tv_gvwallet_type.setText(text[position]);
                                    tv_gvwallet_type.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.viewline));
                                    tv_gvwallet_price.setText("￥"+dataList.get(position));
                                    tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.viewline));
                                }else{
                                    img_gvwallet.setBackgroundResource(img[position]);
                                    tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.gvwallet_textcolor));
                                    tv_gvwallet_type.setText(text[position]);
                                    tv_gvwallet_price.setText("￥"+dataList.get(position));
                                }

                            }
                        });


                    }else{


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (what == UrlTag.BOSS_SHOP_CHANGE){

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() > 0){


                            shopList = new ArrayList<>();

                            for (int i =0 ;i<data.length();i++){
                                ShopdataBean shopdataBean = new ShopdataBean();
                                shopdataBean.setId(data.getJSONObject(i).getString("id"));
                                shopdataBean.setSid(data.getJSONObject(i).getString("sid"));
                                shopdataBean.setSname(data.getJSONObject(i).getString("sname"));
                                shopdataBean.setFname(data.getJSONObject(i).getString("fname"));

                                shopList.add(shopdataBean);
                            }
                            //进入界面
                            if (CHANGE_SHOP_STATE.equals("1")){

                                if (shopList.size() == 1){
                                    tv_shopname.setText(shopList.get(0).getSname());
                                    tv_shopname.setEnabled(false);
                                    img_title_indicator.setVisibility(View.INVISIBLE);
                                    SpUtil.putString(mContext,"shopname",shopList.get(0).getSname());

                                }else{

                                    tv_shopname.setText(shopList.get(0).getSname());

                                    SpUtil.putString(mContext,"shopname",shopList.get(0).getSname());
                                }

                                //如果多家商店
                            }else{

                                if (shopList.size() == 1){

                                    tv_shopname.setEnabled(false);
                                    img_title_indicator.setVisibility(View.INVISIBLE);
                                    CommAdapter<ShopdataBean> commAdapter = new CommAdapter<ShopdataBean>(mContext, R.layout.item_changeshop, shopList) {
                                        @Override
                                        public void convert(ViewHolder holder, ShopdataBean shopdataBean, int position) {

                                            TextView tv_shopname = holder.getView(R.id.tv_shopname);
                                            tv_shopname.setText(shopdataBean.getSname());
                                            SpUtil.putString(mContext,"shopname",shopdataBean.getSname());
                                        }
                                    };
                                    pop_listview.setAdapter(commAdapter);
                                }else{

                                    CommAdapter<ShopdataBean> commAdapter = new CommAdapter<ShopdataBean>(mContext, R.layout.item_changeshop, shopList) {
                                        @Override
                                        public void convert(ViewHolder holder, ShopdataBean shopdataBean, int position) {

                                            TextView tv_shopname = holder.getView(R.id.tv_shopname);
                                            tv_shopname.setText(shopdataBean.getSname());
                                        }
                                    };
                                    pop_listview.setAdapter(commAdapter);

                                    pop_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            TextView item_tv = (TextView) parent.getChildAt(position).findViewById(R.id.tv_shopname);
                                            tv_shopname.setText(item_tv.getText().toString());

                                            SpUtil.putString(mContext,"id",shopList.get(position).getId());
                                            SpUtil.putString(mContext,"sid",shopList.get(position).getSid());
                                            SpUtil.putString(mContext,"shopname",item_tv.getText().toString());
                                            SpUtil.putString(mContext,"name",shopList.get(position).getFname());

                                            /*FragmentManager fragmentManager = getFragmentManager();

                                            PerformanceFrag perFrag = (PerformanceFrag) fragmentManager.findFragmentByTag("PerformanceFrag");

                                            if (perFrag != null){

                                                perFrag.changeShopName(item_tv.getText().toString());
                                            }
*/
                                            EventBus.getDefault().post(new BossMsgEvent(shopList.get(position).getSname()));
                                            //更换名字
                                            EventBus.getDefault().post(new BossNameMsgEvent(shopList.get(position).getFname()));
                                            //更换店铺刷新数据
                                            EventBus.getDefault().post(new BossBoolMsgEvent(true));


                                            popupWindow.dismiss();

                                        }
                                    });

                                }

                            }



                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            if (what == UrlTag.BOSS_WALLET_HOME){
                swipe_bosswallet.setRefreshing(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_shopname:
                CHANGE_SHOP_STATE = "2";
                popupWindow = new PopupWindow();
                View view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_changeshop, null);
                pop_listview = (ListView) view.findViewById(R.id.pop_listview);
                popupWindow.setContentView(view);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setBackgroundDrawable(new PaintDrawable());
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(tv_shopname);
                getShopData();
                break;
        }
    }


    public void changeShopName(String name){
        tv_shopname.setText(name);
    }
}
