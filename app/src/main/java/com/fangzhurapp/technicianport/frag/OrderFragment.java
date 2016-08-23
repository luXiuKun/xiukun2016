package com.fangzhurapp.technicianport.frag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.MainActivity;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.activity.MessageActivity;
import com.fangzhurapp.technicianport.adapter.CommAdapter;
import com.fangzhurapp.technicianport.adapter.ViewHolder;
import com.fangzhurapp.technicianport.bean.ShopdataBean;
import com.fangzhurapp.technicianport.eventbus.MsgEvent;
import com.fangzhurapp.technicianport.eventbus.MsgEvent1;
import com.fangzhurapp.technicianport.eventbus.MsgEventAils;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.receiver.MessageReceiver;
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


public class OrderFragment extends Fragment implements View.OnClickListener{


    private static final String TAG = "OrderFragment";
    private View view;
    private TextView mTvWord;
    private TextView mTvFz;
    private View mLineWord;
    private View mLineFz;
    private ViewPager mVpOrderChild;

    private List<Fragment> fragmentList ;
    private Context mContext;
    private ImageView img_title_right;
    private TextView tv_shopname;
    private ListView pop_listview;
    private List<ShopdataBean> shopList;
    private ImageView img_title_indicator;

    private String CHANGE_SHOP_STATE = "1";
    private PopupWindow popupWindow;
    private LinearLayout ll_work;
    private LinearLayout ll_fz;
    private StatementFragment statementFragment;
    private WorkFragment workFragment;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity)context;
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
        Log.d(TAG, "onCreateView: "+"OrderFragment");
        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.fragment_order, container, false);

        initView();
        initEvent();

        selectFragment(0);
        return view;
    }

    @Subscribe
    public void onEventMainThread(MsgEvent msg){

    }


    private void initEvent() {
        ll_work.setOnClickListener(this);
        ll_fz.setOnClickListener(this);
        img_title_right.setOnClickListener(this);
        tv_shopname.setOnClickListener(this);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        };
        mVpOrderChild.setAdapter(fragmentPagerAdapter);


        mVpOrderChild.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当前选中的fragment
                int currentItem = mVpOrderChild.getCurrentItem();
                selectFragment(currentItem);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }

    private void initView() {
        mTvWord = (TextView) view.findViewById(R.id.tv_order_work);
        mTvFz = (TextView) view.findViewById(R.id.tv_order_fz);
        mLineWord = view.findViewById(R.id.line_work);
        mLineFz = view.findViewById(R.id.line_fz);
        mVpOrderChild = (ViewPager) view.findViewById(R.id.vp_order_child);
        img_title_right = (ImageView) view.findViewById(R.id.img_title_right);
        tv_shopname = (TextView) view.findViewById(R.id.tv_shopname);
        img_title_indicator = (ImageView) view.findViewById(R.id.img_title_indicator);

        ll_work = (LinearLayout) view.findViewById(R.id.ll_work);
        ll_fz = (LinearLayout) view.findViewById(R.id.ll_fz);

        fragmentList = new ArrayList<>();

        workFragment = new WorkFragment();
        statementFragment = new StatementFragment();
        fragmentList.add(workFragment);
        fragmentList.add(statementFragment);


        getMsgState();
        changeShop();

    }

    private void getMsgState() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.MSG_ORDER, RequestMethod.POST);
        jsonObjectRequest.add("staff_id","187");
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback,UrlTag.MSG_ORDER,true,false,true);
    }


    private void selectFragment(int i) {

        mVpOrderChild.setCurrentItem(i);
        switch (i){
            case 0:
                mTvWord.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));
                mLineWord.setVisibility(View.VISIBLE);
                mLineFz.setVisibility(View.INVISIBLE);
                mTvFz.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.order_header_text));
                break;
            case 1:
                mTvFz.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));
                mLineWord.setVisibility(View.INVISIBLE);
                mLineFz.setVisibility(View.VISIBLE);
                mTvWord.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.order_header_text));
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_title_right:
                Intent intent = new Intent(mContext, MessageActivity.class);
                startActivity(intent);
                break;

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
                changeShop();
                break;


            case R.id.ll_work:
                selectFragment(0);
                break;

            case R.id.ll_fz:
                selectFragment(1);
                break;
        }
    }

    private void changeShop() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.CHANGE_SHOP, RequestMethod.POST);
        jsonObjectRequest.add("phone",SpUtil.getString(mContext,"phone",""));
        jsonObjectRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback,UrlTag.CHANGE_SHOP,false,false,true);
    }

    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {

            if (what == UrlTag.MSG_ORDER){

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONObject data = jsonObject.getJSONObject("data");

                        String aNew = data.getString("new");
                        if (aNew.equals("1")){
                            img_title_right.setBackgroundResource(R.drawable.title_msg_pre);
                        }

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
                                            tv_shopname.setText(shopList.get(position).getSname());

                                            SpUtil.putString(mContext,"id",shopList.get(position).getId());
                                            SpUtil.putString(mContext,"sid",shopList.get(position).getSid());
                                            SpUtil.putString(mContext,"shopname",shopList.get(position).getSname());
                                            SpUtil.putString(mContext,"name",shopList.get(position).getName());

                                           /* FragmentManager fm = getActivity().getSupportFragmentManager();

                                            WalletFragment walletfragment = (WalletFragment)fm.findFragmentByTag("walletfragment");
                                            if (walletfragment != null){


                                                walletfragment.changeShopName(item_tv.getText().toString());
                                            }*/

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
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }
    };
}
