package com.fangzhurapp.technicianport.frag;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.MainActivity;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.adapter.WorkOrderAdapter;
import com.fangzhurapp.technicianport.bean.WorkorderBean;
import com.fangzhurapp.technicianport.eventbus.MsgEvent;
import com.fangzhurapp.technicianport.eventbus.MsgEvent1;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.receiver.MessageReceiver;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.view.ConfirmFzSucessDialog;
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
 * 工作台
 */
public class WorkFragment extends Fragment implements OnRefreshListener{


    private View view;
    private SwipeToLoadLayout mSwipeload;
    private ListView swipe_target;
    private ImageView imageView;
    private final String STATUS = "0";
    private static final String TAG = "WorkFragment";
    private List<WorkorderBean> orderList ;
    private ViewStub vs_nodata;
    private WorkOrderAdapter workOrderAdapter;
    private int selPosition;
    private ConfirmFzSucessDialog confirmFzSucessDialog;
    private Context mContext;
    public static final String MESSAGE_RECEIVED_ACTION = "ORDER_MESSAGE_RECEIVED_ACTION";
    private MessageReceiver messageReceiver;
    private List<WorkorderBean> orderIngList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_work, container, false);
        initView();
        EventBus.getDefault().register(this);
        registerMessageReceiver();
        return view;
    }

    private void initView() {
        mSwipeload = (SwipeToLoadLayout) view.findViewById(R.id.swipe_work);
        swipe_target = (ListView) view.findViewById(R.id.swipe_target);
        vs_nodata = (ViewStub) view.findViewById(R.id.vs_nodata);
        mSwipeload.setRefreshing(true);
        mSwipeload.setOnRefreshListener(this);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity)context;
    }

    @Override
    public void onRefresh() {

        workOrder();
    }

    @Subscribe
    public void onEventMainThread(MsgEvent1 msg){

        mSwipeload.setRefreshing(true);
    }

    /**
     * 工作台订单
     */
    private void workOrder() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.ORDER, RequestMethod.POST);
        jsonObjectRequest.add("id", SpUtil.getString(CustomApplication.getInstance(),"id",""));
        jsonObjectRequest.add("status",STATUS );
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback, UrlTag.ORDER,false,false,true);
    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.ORDER){
                mSwipeload.setRefreshing(false);
                orderList = new ArrayList<>();
                newOrder();
                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() >0){
                            vs_nodata.setVisibility(View.GONE);
                            for (int i = 0; i< data.length(); i++){

                                if (data.getJSONObject(i).getString("o_type").equals("2")){
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

                                    orderList.add(workorderBean);
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

                            workOrderAdapter = new WorkOrderAdapter(mContext, orderList);

                            swipe_target.setAdapter(workOrderAdapter);

                            workOrderAdapter.setConfirmFzListener(new WorkOrderAdapter.ConfirmFzImpl() {
                                @Override
                                public void confirmFzListener(String id,int position) {
                                    selPosition = position;
                                    Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.CONFIRM_FZ, RequestMethod.POST);
                                    jsonObjectRequest.add("id",id);
                                    CallServer.getInstance().add(mContext,jsonObjectRequest,callback,UrlTag.CONFIRM_FZ,true,false,true);


                                }
                            });
                        }else{

                            Toast.makeText(mContext, "没有数据", Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        swipe_target.setAdapter(null);
                        vs_nodata.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    Toast.makeText(mContext, "返回参数有误", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }else if (what == UrlTag.CONFIRM_FZ){

                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    JSONObject data = jsonObject.getJSONObject("data");
                    String isok = data.getString("isok");
                    if (isok.equals("1")){
                        orderList.remove(selPosition);
                        workOrderAdapter.notifyDataSetChanged();


                        confirmFzSucessDialog = new ConfirmFzSucessDialog(mContext, R.layout.dialog_fzsucess);
                        confirmFzSucessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        confirmFzSucessDialog.show();
                        mSwipeload.setRefreshing(true);
                        TimeTimer timeTimer = new TimeTimer(1000, 1000);
                        timeTimer.start();

                       /* FragmentManager fm = getActivity().getSupportFragmentManager();

                        WalletFragment walletfragment = (WalletFragment)fm.findFragmentByTag("walletfragment");

                        if (walletfragment != null){
                            walletfragment.refresh();

                        }*/

                        EventBus.getDefault().post(new MsgEvent1(true));


                    }else{
                        Toast.makeText(mContext, "分账失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (what == UrlTag.ORDER_ING){
                LogUtil.d(TAG,response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){

                        JSONArray data = jsonObject.getJSONArray("data");
                        orderIngList = new ArrayList<>();

                        if (data.length() > 0){

                        for (int i =0;i<data.length();i++){

                            WorkorderBean workorderBean = new WorkorderBean();
                            workorderBean.setOnumber(data.getJSONObject(i).getString("onumber"));
                            workorderBean.setO_type(data.getJSONObject(i).getString("o_type"));
                            workorderBean.setProject_name(data.getJSONObject(i).getString("project_name"));
                            workorderBean.setPtime(data.getJSONObject(i).getString("ptime"));
                            workorderBean.setSmoney(data.getJSONObject(i).getString("smoney"));
                            workorderBean.setType(data.getJSONObject(i).getString("type"));

                            orderIngList.add(workorderBean);
                        }

                         if (orderList != null ){


                             if (workOrderAdapter != null){
                                 orderList.addAll(0,orderIngList);

                                // workOrderAdapter = new WorkOrderAdapter(mContext, orderList);
                                // workOrderAdapter.notifyDataSetChanged();
                                 swipe_target.setAdapter(workOrderAdapter);
                                 vs_nodata.setVisibility(View.GONE);
                             }else{
                                 workOrderAdapter = new WorkOrderAdapter(mContext, orderIngList);
                                 swipe_target.setAdapter(workOrderAdapter);
                                 vs_nodata.setVisibility(View.GONE);

                             }

                         }  else{

                             if (workOrderAdapter != null){
                                 orderList.addAll(0,orderIngList);
                                // workOrderAdapter = new WorkOrderAdapter(mContext, orderList);
                                // workOrderAdapter.notifyDataSetChanged();
                                 swipe_target.setAdapter(workOrderAdapter);
                                 vs_nodata.setVisibility(View.GONE);
                             }else{

                                 workOrderAdapter = new WorkOrderAdapter(mContext, orderIngList);
                                 swipe_target.setAdapter(workOrderAdapter);
                                 vs_nodata.setVisibility(View.GONE);

                             }

                         }



                        }else{

                            if (orderList.size() > 0 || orderIngList.size() >0){
                                vs_nodata.setVisibility(View.GONE);
                            }else{

                                vs_nodata.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(mContext, "没有进行中的订单", Toast.LENGTH_SHORT).show();

                            if (workOrderAdapter != null){
                                workOrderAdapter.notifyDataSetChanged();
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
            mSwipeload.setRefreshing(false);
        }
    };

    private void newOrder() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.ORDER_ING, RequestMethod.POST);
        jsonObjectRequest.add("sid", SpUtil.getString(mContext,"sid",""));
        jsonObjectRequest.add("id", SpUtil.getString(mContext,"id",""));
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback, UrlTag.ORDER_ING,true,false,true);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContext.unregisterReceiver(messageReceiver);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 倒计时,dialog显示1秒
     */

    class TimeTimer extends CountDownTimer {



        public TimeTimer(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {//计时

        }

        @Override
        public void onFinish() {//计时完毕
            confirmFzSucessDialog.dismiss();
        }
    }



    public void registerMessageReceiver() {

        messageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        mContext.registerReceiver(messageReceiver, filter);

        /**
         * 进行中订单
         */
        messageReceiver.setNewOrderListener(new MessageReceiver.newOrderListener() {
            @Override
            public void sendNewOrderMessage() {
                newOrder();
            }
        });

    }
}
