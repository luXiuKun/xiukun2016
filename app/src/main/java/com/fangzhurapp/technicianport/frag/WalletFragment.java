package com.fangzhurapp.technicianport.frag;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fangzhurapp.technicianport.MainActivity;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.activity.InComeActivity;
import com.fangzhurapp.technicianport.activity.MyBindBankActivity;
import com.fangzhurapp.technicianport.activity.MyWalletActivity;
import com.fangzhurapp.technicianport.activity.SettingActivity;
import com.fangzhurapp.technicianport.activity.PartnerIncomeAct;
import com.fangzhurapp.technicianport.activity.StaffInComeActivity;
import com.fangzhurapp.technicianport.activity.TXPriceActivity;
import com.fangzhurapp.technicianport.adapter.CommAdapter;
import com.fangzhurapp.technicianport.adapter.ViewHolder;
import com.fangzhurapp.technicianport.bean.ShopdataBean;
import com.fangzhurapp.technicianport.eventbus.MsgEvent;
import com.fangzhurapp.technicianport.eventbus.MsgEvent1;
import com.fangzhurapp.technicianport.eventbus.MsgEventAils;
import com.fangzhurapp.technicianport.eventbus.TxEvent;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.AnimationUtils;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.view.SharePopWindow;
import com.umeng.socialize.UMShareAPI;
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

public class WalletFragment extends Fragment implements OnRefreshListener,View.OnClickListener{

    private static final String TAG = "WalletFragment";
    private View view;
    private ScrollView swipe_target;
    private ImageView mTitleRight;
    private Context mContext;
    private SwipeToLoadLayout swipe_wallet;
    private ImageView img_title_indicator;
    private TextView tv_shopname;

    private List<ShopdataBean> shopList;
    private String CHANGE_SHOP_STATE = "1";
    private PopupWindow popupWindow;
    private ListView pop_listview;
    private PopupWindow sharePop;
    private TextView tv_jswallet_xjzh;
    private TextView tv_jswallet_wage;
    private TextView tv_jswallet_cardcount;
    private TextView tv_jswallet_income;
    private String jsz;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext =(MainActivity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.fragment_wallet1, container, false);

        initView();
        initEvent();
        return view;
    }





    private void initEvent() {
        tv_shopname.setOnClickListener(this);
        mTitleRight.setOnClickListener(this);
        /*swipe_target.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        Intent intent = new Intent(mContext, TXPriceActivity.class);

                        startActivity(intent);
                        break;

                    case 2:
                        Intent intent1 = new Intent(mContext, MyWalletActivity.class);
                        startActivity(intent1);
                        break;

                    case 3:
                        //分享
                        share();
                        break;
                }
            }
        });*/
    }

    private void share() {




        SharePopWindow instance = SharePopWindow.getInstance(getActivity(),mContext, R.layout.layout_share_popupwindow);
        instance.showPopupWindow(view);


    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mContext).onActivityResult( requestCode, resultCode, data);
    }

    private void initView() {

        swipe_target = (ScrollView) view.findViewById(R.id.swipe_target);
        mTitleRight = (ImageView) view.findViewById(R.id.img_title_right);
        mTitleRight.setBackgroundResource(R.drawable.img_setting);
        swipe_wallet = (SwipeToLoadLayout) view.findViewById(R.id.swipe_wallet);

        img_title_indicator = (ImageView) view.findViewById(R.id.img_title_indicator);
        tv_shopname = (TextView) view.findViewById(R.id.tv_shopname);
        tv_shopname.setText(SpUtil.getString(mContext,"shopname",""));

        RelativeLayout rl_jswallet_xjzh = (RelativeLayout) view.findViewById(R.id.rl_jswallet_xjzh);
        RelativeLayout rl_jswallet_wage = (RelativeLayout) view.findViewById(R.id.rl_jswallet_wage);
        RelativeLayout rl_jswallet_bindcard = (RelativeLayout) view.findViewById(R.id.rl_jswallet_bindcard);
        RelativeLayout rl_jswallet_share = (RelativeLayout) view.findViewById(R.id.rl_jswallet_share);
        RelativeLayout rl_jswallet_income= (RelativeLayout) view.findViewById(R.id.rl_jswallet_income);

        rl_jswallet_xjzh.setOnClickListener(this);
        rl_jswallet_wage.setOnClickListener(this);
        rl_jswallet_bindcard.setOnClickListener(this);
        rl_jswallet_share.setOnClickListener(this);
        rl_jswallet_income.setOnClickListener(this);
        tv_jswallet_xjzh = (TextView) view.findViewById(R.id.tv_jswallet_xjzh);
        tv_jswallet_wage = (TextView) view.findViewById(R.id.tv_jswallet_wage);
        tv_jswallet_cardcount = (TextView) view.findViewById(R.id.tv_jswallet_cardcount);
        tv_jswallet_income = (TextView) view.findViewById(R.id.tv_jswallet_income);

        swipe_wallet.setOnRefreshListener(this);
        swipe_wallet.setRefreshing(true);





        changeShop();

    }

    private void changeShop() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.CHANGE_SHOP, RequestMethod.POST);
        jsonObjectRequest.add("phone",SpUtil.getString(mContext,"phone",""));
        jsonObjectRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback,UrlTag.CHANGE_SHOP,false,false,true);
    }


    @Subscribe
    public void onEventMainThread(MsgEvent msg) {

        tv_shopname.setText(msg.getStringMsg());
    }

    @Subscribe
    public void onEventMainThread(MsgEvent1 msg) {
        swipe_wallet.setRefreshing(msg.getBooleanMsg());

    }

    @Subscribe
    public void onEventMainThread(TxEvent msg) {
        swipe_wallet.setRefreshing(msg.getBooleanMsg());

    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.WALLET_HOME){
                swipe_wallet.setRefreshing(false);
                LogUtil.d(TAG,response.toString());



                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        jsz = data.getString("jsz");
                        String ktxye = data.getString("ktxye");
                        SpUtil.putString(mContext,"ktxprice",ktxye);
                        String gz = data.getString("gz");
                        String count = data.getString("count");

                        tv_jswallet_xjzh.setText(ktxye+"元");
                        tv_jswallet_wage.setText(gz+"元");
                        tv_jswallet_cardcount.setText(count+"张");
                        tv_jswallet_income.setText(jsz);
                    }else{


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }else if (what == UrlTag.CHANGE_SHOP){

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
                                shopdataBean.setName(data.getJSONObject(i).getString("name"));
                                shopdataBean.setId_number(data.getJSONObject(i).getString("id_number"));
                                shopList.add(shopdataBean);
                            }
                            //进入界面
                            if (CHANGE_SHOP_STATE.equals("1")){

                                if (shopList.size() == 1){
                                   // tv_shopname.setText(shopList.get(0).getSname());
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
                                            tv_shopname.setText(shopList.get(position).getSname());

                                            SpUtil.putString(mContext,"id",shopList.get(position).getId());
                                            SpUtil.putString(mContext,"sid",shopList.get(position).getSid());
                                            SpUtil.putString(mContext,"shopname",shopList.get(position).getSname());
                                            SpUtil.putString(mContext,"name",shopList.get(position).getName());
                                            SpUtil.putString(mContext, "id_number", shopList.get(position).getId_number());


                                            EventBus.getDefault().post(new MsgEvent(shopList.get(position).getSname()));
                                            //发送消息刷新
                                            EventBus.getDefault().post(new MsgEvent1(true));
                                            //切换商店重新设置别名
                                            EventBus.getDefault().post(new MsgEventAils(true));

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
            swipe_wallet.setRefreshing(false);
        }
    };


    @Override
    public void onRefresh() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.WALLET_HOME, RequestMethod.POST);
        jsonObjectRequest.add("id", SpUtil.getString(mContext,"id",""));
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback, UrlTag.WALLET_HOME,false,false,true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_title_right:

                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);

                break;

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
                changeShop();

                break;

            case R.id.rl_jswallet_xjzh:
                Intent tx = new Intent(mContext, TXPriceActivity.class);

                startActivity(tx);
                break;

            case R.id.rl_jswallet_wage:
                Intent intent1 = new Intent(mContext, MyWalletActivity.class);
                startActivity(intent1);
                break;

            case R.id.rl_jswallet_bindcard:
                Intent bindCard = new Intent(mContext, MyBindBankActivity.class);
                startActivity(bindCard);
                break;

            case R.id.rl_jswallet_share:
                share();
                break;

            case R.id.rl_jswallet_income:

                Intent staffIncome = new Intent(mContext, StaffInComeActivity.class);
                staffIncome.putExtra("jsincome",jsz);
                startActivity(staffIncome);
                break;

        }
    }





}
