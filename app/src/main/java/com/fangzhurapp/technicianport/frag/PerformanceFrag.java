package com.fangzhurapp.technicianport.frag;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fangzhurapp.technicianport.BossMainActivity;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.activity.BossGroupActivity;
import com.fangzhurapp.technicianport.activity.BossOrderVipAct;
import com.fangzhurapp.technicianport.activity.BossRankActivity;
import com.fangzhurapp.technicianport.activity.BossSjysActivity;
import com.fangzhurapp.technicianport.activity.BossStaffTcActivity;
import com.fangzhurapp.technicianport.activity.BossVipActivity;
import com.fangzhurapp.technicianport.activity.BossVipConsumeActivity;
import com.fangzhurapp.technicianport.activity.BossWAXPActivity;
import com.fangzhurapp.technicianport.adapter.CommAdapter;
import com.fangzhurapp.technicianport.adapter.ViewHolder;
import com.fangzhurapp.technicianport.bean.ShopdataBean;
import com.fangzhurapp.technicianport.eventbus.BossBoolMsgEvent;
import com.fangzhurapp.technicianport.eventbus.BossMsgEvent;
import com.fangzhurapp.technicianport.eventbus.BossNameMsgEvent;
import com.fangzhurapp.technicianport.eventbus.BossPerformanceMsgEvent;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.AnimationUtils;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.NumberUtils;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.utils.TimeUtils;
import com.fangzhurapp.technicianport.view.DataPickPopWindow;
import com.fangzhurapp.technicianport.view.DataPickdayPopWindow;
import com.fangzhurapp.technicianport.view.LxkCircleView;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.listener.PieRadarChartTouchListener;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
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
public class PerformanceFrag extends Fragment implements View.OnClickListener,OnRefreshListener{


    private View view;
    private RelativeLayout rl_performance_sjys;
    private RelativeLayout rl_performance_vip;
    private RelativeLayout rl_performance_stafftc;
    private RelativeLayout rl_performance_proname;
    private Context mContext;
    private TextView tv_day;
    private TextView tv_week;
    private TextView tv_month;
    private TextView tv_year;
    private ImageView img_day;
    private ImageView img_week;
    private ImageView img_month;
    private ImageView img_year;
    private LinearLayout ll_performance_day;
    private LinearLayout ll_performance_week;
    private LinearLayout ll_performance_month;
    private LinearLayout ll_performance_year;

    private static final String TAG = "PerformanceFrag";
    private TextView tv_sjys;
    private TextView tv_vip;
    private TextView tv_stafftc;
    private TextView tv_proRank;
    private TextView tv_kdj;
    private TextView tv_orderSum;
    private TextView tv_kkCount;
    private LxkCircleView pie_performance;
    private TextView tv_shopname;
    private String CHANGE_SHOP_STATE = "1";
    private PopupWindow popupWindow;
    private ListView pop_listview;
    private List<ShopdataBean> shopList;
    private ImageView img_title_indicator;
    private String strTime;
    private String endTime;
    private ScrollView swipe_target;
    private SwipeToLoadLayout swipe_boss_pre;
    private LinearLayout ll_starttime,ll_endtime;
    private TextView startTime,endTime1;
    private DataPickdayPopWindow dataPickPopWindow;

    private String sTime,eTime;
    private TextView tv_performance_order;
    private RelativeLayout rl_performance_order;


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
        view = inflater.inflate(R.layout.frag_boss_performance, null);
        EventBus.getDefault().register(this);
        initView();
        initEvent();
        return view;
    }

    private void initEvent() {

        rl_performance_sjys.setOnClickListener(this);
        rl_performance_vip.setOnClickListener(this);
        rl_performance_stafftc.setOnClickListener(this);
        rl_performance_proname.setOnClickListener(this);

        ll_performance_day.setOnClickListener(this);
        ll_performance_week.setOnClickListener(this);
        ll_performance_month.setOnClickListener(this);
        ll_performance_year.setOnClickListener(this);
        tv_shopname.setOnClickListener(this);

        ll_starttime.setOnClickListener(this);
        ll_endtime.setOnClickListener(this);
        rl_performance_order.setOnClickListener(this);
    }

    private void initView() {
        tv_shopname = (TextView) view.findViewById(R.id.tv_shopname);
        rl_performance_sjys = (RelativeLayout) view.findViewById(R.id.rl_performance_sjys);
        rl_performance_vip = (RelativeLayout) view.findViewById(R.id.rl_performance_vip);
        rl_performance_stafftc = (RelativeLayout) view.findViewById(R.id.rl_performance_stafftc);
        rl_performance_proname = (RelativeLayout) view.findViewById(R.id.rl_performance_proname);

        ll_performance_day = (LinearLayout) view.findViewById(R.id.ll_performance_day);
        ll_performance_week = (LinearLayout) view.findViewById(R.id.ll_performance_week);
        ll_performance_month = (LinearLayout) view.findViewById(R.id.ll_performance_month);
        ll_performance_year = (LinearLayout) view.findViewById(R.id.ll_performance_year);

        tv_day = (TextView) view.findViewById(R.id.tv_day);
        tv_week = (TextView) view.findViewById(R.id.tv_week);
        tv_month = (TextView) view.findViewById(R.id.tv_month);
        tv_year = (TextView) view.findViewById(R.id.tv_year);

        img_day = (ImageView) view.findViewById(R.id.img_day);
        img_week = (ImageView) view.findViewById(R.id.img_week);
        img_month = (ImageView) view.findViewById(R.id.img_month);
        img_year = (ImageView) view.findViewById(R.id.img_year);

        tv_sjys = (TextView) view.findViewById(R.id.tv_performance_sjysmoney);
        tv_vip = (TextView) view.findViewById(R.id.tv_performance_vipmoney);
        tv_stafftc = (TextView) view.findViewById(R.id.tv_performance_stafftc);
        tv_proRank = (TextView) view.findViewById(R.id.tv_performance_prorank);
        tv_kdj = (TextView) view.findViewById(R.id.tv_performance_kdj);
        tv_orderSum = (TextView) view.findViewById(R.id.tv_performance_ordersum);
        tv_kkCount = (TextView) view.findViewById(R.id.tv_performance_kk);
        img_title_indicator = (ImageView) view.findViewById(R.id.img_title_indicator);

        ll_starttime = (LinearLayout) view.findViewById(R.id.ll_starttime);
        ll_endtime = (LinearLayout) view.findViewById(R.id.ll_endtime);
         endTime1 =   (TextView) view.findViewById(R.id.tv_performance_endtime);
        startTime = (TextView)view.findViewById(R.id.tv_performance_starttime);

        ImageView img_title_right = (ImageView) view.findViewById(R.id.img_title_right);
        img_title_right.setVisibility(View.INVISIBLE);

        pie_performance = (LxkCircleView) view.findViewById(R.id.pie_performance);
        swipe_target = (ScrollView) view.findViewById(R.id.swipe_target);
        swipe_boss_pre = (SwipeToLoadLayout) view.findViewById(R.id.swipe_boss_pre);

        swipe_boss_pre.setOnRefreshListener(this);

        tv_performance_order = (TextView) view.findViewById(R.id.tv_performance_order);
        rl_performance_order = (RelativeLayout) view.findViewById(R.id.rl_performance_order);
        getShopData();
        selectTime(0);

        //属性
        /*pie_performance.setHoleRadius(40);
        pie_performance.setTransparentCircleRadius(44);

        pie_performance.setDrawCenterText(true);
        pie_performance.setDrawHoleEnabled(true);
        pie_performance.setRotationAngle(240);
        pie_performance.setRotationEnabled(true);
        pie_performance.setUsePercentValues(true);
        pie_performance.setCenterTextSize(18);
        pie_performance.setDescription("");
        
        pie_performance.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {



            }

            @Override
            public void onNothingSelected() {

            }
        });

        Legend legend = pie_performance.getLegend();
        legend.setDrawInside(false);
        legend.setEnabled(false);*/

        pie_performance.setArcSelection(new LxkCircleView.onArcSelection() {
            @Override
            public void arcClick(int select) {

                switch (select){

                    case 0:
                        Intent alipay = new Intent(mContext, BossWAXPActivity.class);
                        alipay.putExtra("selectarc",0);
                        alipay.putExtra("strtime",sTime);
                        alipay.putExtra("endtime",eTime);
                        startActivity(alipay);
                        break;

                    case 1:
                        Intent wx = new Intent(mContext, BossWAXPActivity.class);
                        wx.putExtra("selectarc",1);
                        wx.putExtra("strtime",sTime);
                        wx.putExtra("endtime",eTime);
                        startActivity(wx);
                        break;

                    case 2:
                        Intent vipConsume = new Intent(mContext, BossVipConsumeActivity.class);
                        vipConsume.putExtra("strtime",sTime);
                        vipConsume.putExtra("endtime",eTime);
                        startActivity(vipConsume);
                        break;

                    case 3:
                        Intent money = new Intent(mContext, BossWAXPActivity.class);
                        money.putExtra("selectarc",3);
                        money.putExtra("strtime",sTime);
                        money.putExtra("endtime",eTime);
                        startActivity(money);
                        break;

                    case 4:
                        Intent group = new Intent(mContext, BossGroupActivity.class);
                        group.putExtra("strtime",sTime);
                        group.putExtra("endtime",eTime);
                        startActivity(group);
                        break;

                    case 5:
                        Intent pos = new Intent(mContext, BossWAXPActivity.class);
                        pos.putExtra("selectarc",5);
                        pos.putExtra("strtime",sTime);
                        pos.putExtra("endtime",eTime);
                        startActivity(pos);
                        break;

                }

            }
        });

        pie_performance.setOnHoleCilckListener(new LxkCircleView.onHoleClickListener() {
            @Override
            public void holeClick() {
            }
        });
    }

    @Subscribe
    public void onEventMainThread(BossMsgEvent msg){
        tv_shopname.setText(msg.getStringMsg());
    }

    @Subscribe
    public void onEventMainThread(BossBoolMsgEvent msg){

        //getShopData();
        swipe_boss_pre.setRefreshing(true);
    }




    private void getShopData() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_SHOP_CHANGE, RequestMethod.POST);
        jsonObjectRequest.add("phone",SpUtil.getString(mContext,"phone",""));
        jsonObjectRequest.setRetryCount(3);
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback,UrlTag.BOSS_SHOP_CHANGE,true,false,true);
    }

    private void selectTime(int i) {
        hideAll();
        switch (i){

            case 0:
                tv_day.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));
                img_day.setVisibility(View.VISIBLE);
                strTime = TimeUtils.getDayStartTime()+" 00:00:00";
                endTime = TimeUtils.getDayEndTime()+" 23:59:59";

                startTime.setText(TimeUtils.getDayStartTime());
                endTime1.setText(TimeUtils.getDayEndTime());
                sTime = TimeUtils.getDayStartTime()+" 00:00:00";
                eTime = TimeUtils.getDayEndTime()+" 23:59:59";
                getPerformanceHomeData(strTime, endTime);
                break;

            case 1:
                tv_week.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));
                img_week.setVisibility(View.VISIBLE);
                strTime = TimeUtils.getWeekStartTime()+" 00:00:00";
                endTime = TimeUtils.getDayStartTime()+" 23:59:59";

                startTime.setText(TimeUtils.getWeekStartTime());
                endTime1.setText(TimeUtils.getDayStartTime());

                sTime = TimeUtils.getWeekStartTime()+" 00:00:00";
                eTime =  TimeUtils.getDayStartTime()+" 23:59:59";

                getPerformanceHomeData(strTime,endTime);
                break;

            case 2:
                tv_month.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));
                img_month.setVisibility(View.VISIBLE);
                strTime = TimeUtils.getBeforeMonth()+" 00:00:00";
                endTime = TimeUtils.getDayEndTime()+" 23:59:59";

                startTime.setText(TimeUtils.getBeforeMonth());
                endTime1.setText(TimeUtils.getDayEndTime());

                sTime = TimeUtils.getBeforeMonth()+" 00:00:00";
                eTime = TimeUtils.getDayEndTime()+" 23:59:59";
                getPerformanceHomeData(strTime,endTime);
                break;

            case 3:
                tv_year.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.tab_bg));
                img_year.setVisibility(View.VISIBLE);
                strTime = TimeUtils.getYearTime()+" 00:00:00";
                endTime = TimeUtils.getDayEndTime()+" 23:59:59";
                startTime.setText(TimeUtils.getYearTime());
                endTime1.setText(TimeUtils.getDayEndTime());

                sTime =  TimeUtils.getYearTime()+" 00:00:00";
                eTime = TimeUtils.getDayEndTime()+" 23:59:59";
                getPerformanceHomeData(strTime,endTime);
                break;
        }
    }

    private void hideAll() {

        tv_day.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.wordcolor));
        tv_week.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.wordcolor));
        tv_month.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.wordcolor));
        tv_year.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.wordcolor));

        img_day.setVisibility(View.INVISIBLE);
        img_week.setVisibility(View.INVISIBLE);
        img_month.setVisibility(View.INVISIBLE);
        img_year.setVisibility(View.INVISIBLE);

    }

    private void showPeiChart(PieData pieData,String yye) {

        /*Spanned spanned = Html.fromHtml(yye+"<br/>"+"<font color= '#34C083'>营业额</font>");
        pie_performance.setCenterText(spanned);
        pie_performance.setData(pieData);
        pie_performance.invalidate();*/

        

    }


    /*private PieData setPieData(ArrayList<PieEntry> data){


        ArrayList<Integer> colors = new ArrayList<>();





        PieDataSet pieDataSet = new PieDataSet(data,"");
        pieDataSet.setValueFormatter(new PercentFormatter());
        pieDataSet.setDrawValues(false);
        pieDataSet.setValueTextSize(15);
        pieDataSet.setValueTextColor(getResources().getColor(R.color.white));
        pieDataSet.setSliceSpace(1f);
        colors.add(new Color().rgb(157,204,251));
        colors.add(new Color().rgb(190,187,255));
        colors.add(new Color().rgb(156,205,205));
        colors.add(new Color().rgb(254,197,75));
        colors.add(new Color().rgb(255,163,163));
        colors.add(new Color().rgb(117,237,223));

        pieDataSet.setColors(colors);
        pieDataSet.setSelectionShift(0);//设置不延伸
        PieData pieData = new PieData(pieDataSet);
        return pieData;
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rl_performance_sjys:
                Intent intent = new Intent(mContext, BossOrderVipAct.class);
                intent.putExtra("strtime",strTime);
                intent.putExtra("endtime",endTime);
                mContext.startActivity(intent);
                break;

            case R.id.rl_performance_vip:
                Intent vip = new Intent(mContext, BossVipActivity.class);
                vip.putExtra("strtime",strTime);
                vip.putExtra("endtime",endTime);
                mContext.startActivity(vip);
                break;
            case R.id.rl_performance_stafftc:
                Intent stafftc = new Intent(mContext, BossStaffTcActivity.class);
                stafftc.putExtra("strtime",strTime);
                stafftc.putExtra("endtime",endTime);
                mContext.startActivity(stafftc);
                break;
            case R.id.rl_performance_proname:
                Intent rank = new Intent(mContext, BossRankActivity.class);
                rank.putExtra("strtime",strTime);
                rank.putExtra("endtime",endTime);
                mContext.startActivity(rank);
                break;

            case R.id.ll_performance_day:
                selectTime(0);
                break;

            case R.id.ll_performance_week:
                selectTime(1);
                break;

            case R.id.ll_performance_month:
                selectTime(2);
                break;

            case R.id.ll_performance_year:
                selectTime(3);
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
                getShopData();
                break;

            case R.id.ll_starttime:
                dataPickPopWindow = DataPickdayPopWindow.getInstance(mContext, R.layout.popupwindow_datapickerday);

                dataPickPopWindow.showPopupWindow(ll_starttime);



                dataPickPopWindow.setDataPick(new DataPickdayPopWindow.DataPickListener() {
                    @Override
                    public void onFinish(String time) {
                        startTime.setText(time);
                        sTime = time + " 00:00:00";
                        dataPickPopWindow.dismiss();
                    }
                });
                dataPickPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {

                        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_PERFORMANCE_HOME, RequestMethod.POST);
                        jsonObjectRequest.add("sid", SpUtil.getString(mContext,"sid",""));
                        jsonObjectRequest.add("strtime",sTime);
                        jsonObjectRequest.add("endtime",eTime);
                        CallServer.getInstance().add(mContext,jsonObjectRequest,callback, UrlTag.BOSS_PERFORMANCE_HOME,true,false,true);
                    }
                });

                break;

            case R.id.ll_endtime:

                 dataPickPopWindow = DataPickdayPopWindow.getInstance(mContext, R.layout.popupwindow_datapickerday);

                dataPickPopWindow.showPopupWindow(ll_starttime);



                dataPickPopWindow.setDataPick(new DataPickdayPopWindow.DataPickListener() {
                    @Override
                    public void onFinish(String time) {
                        endTime1.setText(time);
                        eTime = time+" 23:59:59";
                        dataPickPopWindow.dismiss();
                    }
                });

                dataPickPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {

                        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_PERFORMANCE_HOME, RequestMethod.POST);

                        jsonObjectRequest.add("sid", SpUtil.getString(mContext,"sid",""));
                        jsonObjectRequest.add("strtime",sTime);
                        jsonObjectRequest.add("endtime",eTime);

                        CallServer.getInstance().add(mContext,jsonObjectRequest,callback, UrlTag.BOSS_PERFORMANCE_HOME,true,false,true);
                    }
                });


                break;

            case R.id.rl_performance_order:

                Intent sjys = new Intent(mContext, BossSjysActivity.class);
                sjys.putExtra("strtime",strTime);
                sjys.putExtra("endtime",endTime);
                mContext.startActivity(sjys);
                break;



        }
    }


    private void getPerformanceHomeData(String strtime,String endtime){


        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_PERFORMANCE_HOME, RequestMethod.POST);

        jsonObjectRequest.add("sid", SpUtil.getString(mContext,"sid",""));
        jsonObjectRequest.add("strtime",strtime);
        jsonObjectRequest.add("endtime",endtime);

        CallServer.getInstance().add(mContext,jsonObjectRequest,callback, UrlTag.BOSS_PERFORMANCE_HOME,true,false,true);


    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            
            if (what == UrlTag.BOSS_PERFORMANCE_HOME){
                swipe_boss_pre.setRefreshing(false);

                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")){

                        JSONObject data = jsonObject.getJSONObject("data");

                        parserData(data);
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
                                            SpUtil.putString(mContext,"shenfen",shopList.get(position).getShenfen());


                                            //更换店名
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
            swipe_boss_pre.setRefreshing(false);

            if (what == UrlTag.BOSS_SHOP_CHANGE){

            }
        }
    };

    private void parserData(JSONObject data) {

        if (data != null){

            try {
                tv_performance_order.setText(data.getString("sjys")+"元");
                tv_stafftc.setText(data.getString("ygtc")+"元");
                tv_sjys.setText(data.getString("income")+"元");
                if (!TextUtils.isEmpty(data.getString("no_project"))) {

                    tv_proRank.setText("No.1"+data.getString("no_project"));
                }else{
                    tv_proRank.setText("暂无订单");
                }
                if (data.getString("kdj").equals("0")){

                    tv_kdj.setText(data.getString("kdj"));

                }else{

                    tv_kdj.setText(NumberUtils.floatFormat(Float.valueOf(data.getString("kdj"))));
                }
                tv_kkCount.setText(data.getString("kaika_num"));
                tv_orderSum.setText(data.getString("dingdan_sum"));
                tv_vip.setText(data.getString("bkje")+"元");

                String zhifubao = data.getString("zhifubao");
                String weixin = data.getString("weixin");
                String xianjin = data.getString("xianjin");
                String pos = data.getString("pos");
                String hyxf = data.getString("hyxf");
                String tgxf = data.getString("tgxf");
                String yye = data.getString("yye");
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add(zhifubao);
                dataList.add(weixin);
                dataList.add(hyxf);
                dataList.add(xianjin);
                dataList.add(tgxf);
                dataList.add(pos);

                pie_performance.setHoleText(yye);
                pie_performance.setData(dataList);

               /* ArrayList<PieEntry> pieEntries = new ArrayList<>();
                pieEntries.add(new PieEntry(1,"支付宝"+"\n"+Float.valueOf(zhifubao)));
                pieEntries.add(new PieEntry(1,"微信"+"\n"+Float.valueOf(weixin)));
                pieEntries.add(new PieEntry(1,"现金收入"+"\n"+Float.valueOf(xianjin)));
                pieEntries.add(new PieEntry(1,"pos"+"\n"+Float.valueOf(pos)));
                pieEntries.add(new PieEntry(1,"团购收入"+"\n"+Float.valueOf(tgxf)));
                pieEntries.add(new PieEntry(1,"会员消费"+"\n"+Float.valueOf(hyxf)));
                PieData pieData = setPieData(pieEntries);
                showPeiChart(pieData,yye);*/



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{

            Toast.makeText(mContext, "返回参数有误", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onRefresh() {
        getPerformanceHomeData(strTime, endTime);
    }
}
