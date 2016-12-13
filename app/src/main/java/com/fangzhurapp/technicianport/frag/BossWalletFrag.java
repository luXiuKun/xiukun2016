package com.fangzhurapp.technicianport.frag;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fangzhurapp.technicianport.BossMainActivity;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.activity.BossBindBankActivity;
import com.fangzhurapp.technicianport.activity.BossStaffWageActivity;
import com.fangzhurapp.technicianport.activity.BossTXPriceActivity;
import com.fangzhurapp.technicianport.activity.InComeActivity;
import com.fangzhurapp.technicianport.adapter.CommAdapter;
import com.fangzhurapp.technicianport.adapter.ViewHolder;
import com.fangzhurapp.technicianport.bean.ShopdataBean;
import com.fangzhurapp.technicianport.eventbus.BossBindSucessEvent;
import com.fangzhurapp.technicianport.eventbus.BossBoolMsgEvent;
import com.fangzhurapp.technicianport.eventbus.BossMsgEvent;
import com.fangzhurapp.technicianport.eventbus.BossNameMsgEvent;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.AnimationUtils;
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
    private ScrollView swipe_target;
    private TextView tv_shopname;
    private SwipeToLoadLayout swipe_bosswallet;
    private static final String TAG = "BossWalletFrag";

    private String CHANGE_SHOP_STATE = "1";
    private PopupWindow popupWindow;
    private ListView pop_listview;
    private List<ShopdataBean> shopList;
    private ImageView img_title_indicator;
    private TextView tv_bosswallet_smsr;
    private TextView tv_bosswallet_daymoney;
    private TextView tv_bosswallet_js;
    private TextView tv_bosswallet_txprice;
    private TextView tv_bosswallet_cardcount;
    private TextView tv_bosswallet_staffwage;

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

        view = inflater.inflate(R.layout.frag_bosswallet1, null);
        EventBus.getDefault().register(this);
        initView();
        initEvent();

        return view;
    }

    private void initEvent() {
        tv_shopname.setOnClickListener(this);
        swipe_bosswallet.setOnRefreshListener(this);



    }

    private void initView() {

        ImageView img_title_right = (ImageView) view.findViewById(R.id.img_title_right);
        img_title_right.setVisibility(View.INVISIBLE);
        swipe_target = (ScrollView) view.findViewById(R.id.swipe_target);
        tv_shopname = (TextView) view.findViewById(R.id.tv_shopname);
        swipe_bosswallet = (SwipeToLoadLayout) view.findViewById(R.id.swipe_bosswallet);

        img_title_indicator = (ImageView) view.findViewById(R.id.img_title_indicator);


        LinearLayout ll_bosswallet_smsr  = (LinearLayout) view.findViewById(R.id.ll_bosswallet_smsr);
        LinearLayout ll_bosswallet_ktx  = (LinearLayout) view.findViewById(R.id.ll_bosswallet_ktx);
        LinearLayout ll_bosswallet_bindcard  = (LinearLayout) view.findViewById(R.id.ll_bosswallet_bindcard);
        RelativeLayout rl_bosswallet_staffwage  = (RelativeLayout) view.findViewById(R.id.rl_bosswallet_staffwage);

        ll_bosswallet_smsr.setOnClickListener(this);
        ll_bosswallet_ktx.setOnClickListener(this);
        ll_bosswallet_bindcard.setOnClickListener(this);
        rl_bosswallet_staffwage.setOnClickListener(this);
        tv_bosswallet_smsr = (TextView) view.findViewById(R.id.tv_bosswallet_smsr);
        tv_bosswallet_daymoney = (TextView) view.findViewById(R.id.tv_bosswallet_daymoney);
        tv_bosswallet_js = (TextView) view.findViewById(R.id.tv_bosswallet_js);
        tv_bosswallet_txprice = (TextView) view.findViewById(R.id.tv_bosswallet_txprice);
        tv_bosswallet_cardcount = (TextView) view.findViewById(R.id.tv_bosswallet_cardcount);
        tv_bosswallet_staffwage = (TextView) view.findViewById(R.id.tv_bosswallet_staffwage);


        tv_shopname.setText(SpUtil.getString(mContext,"shopname","这是店铺名"));
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

    @Subscribe
    public void onEventMainThread(BossBindSucessEvent msg){
        swipe_bosswallet.setRefreshing(true);
    }

    @Override
    public void onRefresh() {


        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_WALLET_HOME, RequestMethod.POST);
        jsonObjectRequest.add("sid",SpUtil.getString(mContext,"sid",""));
        jsonObjectRequest.add("id",SpUtil.getString(mContext,"id",""));
        jsonObjectRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback, UrlTag.BOSS_WALLET_HOME,false,false,true);

    }

    private void getShopData() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_SHOP_CHANGE, RequestMethod.POST);
        jsonObjectRequest.add("phone",SpUtil.getString(mContext,"phone",""));
        jsonObjectRequest.setRetryCount(3);
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

                        JSONObject data = jsonObject.getJSONObject("data");
                        SpUtil.putString(mContext,"bossktxprice",data.getString("tx_money"));


                        tv_bosswallet_txprice.setText(data.getString("tx_money")+"元");
                        tv_bosswallet_staffwage.setText(data.getString("yggz")+"元");
                        tv_bosswallet_cardcount.setText(data.getString("binbank_count")+"张");
                        tv_bosswallet_daymoney.setText(data.getString("todayToAccount"));
                        tv_bosswallet_js.setText(data.getString("inSettlement"));
                        tv_bosswallet_smsr.setText(data.getString("onLineIncome")+"元");

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
                                shopdataBean.setShenfen(data.getJSONObject(i).getString("shenfen"));
                                shopList.add(shopdataBean);
                            }
                            //进入界面
                            if (CHANGE_SHOP_STATE.equals("1")){

                                if (shopList.size() == 1){
                                    //tv_shopname.setText(shopList.get(0).getSname());
                                    tv_shopname.setEnabled(false);
                                    img_title_indicator.setVisibility(View.INVISIBLE);
                                    SpUtil.putString(mContext,"shopname",shopList.get(0).getSname());

                                }else{

                                    //tv_shopname.setText(shopList.get(0).getSname());

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
                                            SpUtil.putString(mContext,"shenfen",shopList.get(position).getShenfen());

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
        public void onFailed(int what,  Response<JSONObject> response) {
            if (what == UrlTag.BOSS_WALLET_HOME){
                swipe_bosswallet.setRefreshing(false);
            }else if (what == UrlTag.BOSS_SHOP_CHANGE){
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_shopname:
                CHANGE_SHOP_STATE = "2";

                RotateAnimation rotateAnimation = AnimationUtils.setRotateAnimation(0f, 90f, 0.5f, 0.5f, 300, true);
                img_title_indicator.startAnimation(rotateAnimation);
                popupWindow = new PopupWindow();
                View view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_changeshop, null);
                pop_listview = (ListView) view.findViewById(R.id.pop_listview);
                popupWindow.setContentView(view);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setBackgroundDrawable(new PaintDrawable());
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(tv_shopname);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        RotateAnimation rotateAnimation = AnimationUtils.setRotateAnimation(90f, 0f, 0.5f, 0.5f, 300, true);
                        img_title_indicator.startAnimation(rotateAnimation);
                    }
                });
                getShopData();
                break;

            case R.id.ll_bosswallet_smsr:
                Intent inCome = new Intent(getActivity(), InComeActivity.class);
                startActivity(inCome);
                break;

            case R.id.ll_bosswallet_ktx:

                Intent intent = new Intent(mContext, BossTXPriceActivity.class);
                startActivity(intent);
                break;

            case R.id.ll_bosswallet_bindcard:
                Intent bossbind = new Intent(mContext, BossBindBankActivity.class);
                startActivity(bossbind);
                break;
            case R.id.rl_bosswallet_staffwage:
                Intent staffwage = new Intent(mContext, BossStaffWageActivity.class);
                startActivity(staffwage);
                break;
        }
    }



}
