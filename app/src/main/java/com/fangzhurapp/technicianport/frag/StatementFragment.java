package com.fangzhurapp.technicianport.frag;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.MainActivity;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.adapter.StatementOrderAdapter;
import com.fangzhurapp.technicianport.bean.WorkorderBean;
import com.fangzhurapp.technicianport.eventbus.MsgEvent1;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 已分账
 */
public class StatementFragment extends Fragment implements OnRefreshListener{

    private View view;
    private SwipeToLoadLayout mSwipeload;
    private ListView swipe_target;
    private ViewStub vs_nodata;
    private String STATUS ="1";
    private List<WorkorderBean> orderList;
    private static final String TAG = "StatementFragment";
    private Context mContext;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_statement, container, false);
        EventBus.getDefault().register(this);
        initView();
        return view;
    }

    private void initView() {

        mSwipeload = (SwipeToLoadLayout) view.findViewById(R.id.swipe_work);
        swipe_target = (ListView) view.findViewById(R.id.swipe_target);
        vs_nodata = (ViewStub) view.findViewById(R.id.vs_fz_nodata);
        mSwipeload.setRefreshing(true);
        mSwipeload.setOnRefreshListener(this);

    }





    @Override
    public void onRefresh() {
        fzOrder();
    }

    @Subscribe
    public void onEventMainThread(MsgEvent1 msg){

        mSwipeload.setRefreshing(true);
    }

    private void fzOrder() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.ORDER, RequestMethod.POST);
        jsonObjectRequest.add("id", SpUtil.getString(CustomApplication.getInstance(),"id",""));
        jsonObjectRequest.add("status",STATUS );
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback, UrlTag.FZ_SUCESS,false,false,true);
    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.FZ_SUCESS){
                mSwipeload.setRefreshing(false);
                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){

                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() > 0){

                            vs_nodata.setVisibility(View.GONE);
                        orderList = new ArrayList<>();

                        for (int i =0;i < data.length();i++){

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


                        StatementOrderAdapter statementOrderAdapter = new StatementOrderAdapter(mContext, orderList);
                        swipe_target.setAdapter(statementOrderAdapter);

                        }
                    }else{
                        swipe_target.setAdapter(null);
                        vs_nodata.setVisibility(View.VISIBLE);
                    }




                } catch (JSONException e) {
                    swipe_target.setAdapter(null);
                    Toast.makeText(mContext, "返回参数有误", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }
    };

    public void resresh(){
        mSwipeload.setRefreshing(true);
    }
}
