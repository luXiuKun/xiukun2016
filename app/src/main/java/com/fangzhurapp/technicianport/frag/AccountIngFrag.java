package com.fangzhurapp.technicianport.frag;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.activity.InComeActivity;
import com.fangzhurapp.technicianport.adapter.BossSmsrAccountAdapter;
import com.fangzhurapp.technicianport.bean.moneyDetailBean;
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

/**
 * Created by android on 2016/10/25.
 */
public class AccountIngFrag extends Fragment {

    private View view;
    private ListView lv_account;
    private Context mContext;
    private static final String TAG = "AccountIngFrag";
    private List<moneyDetailBean> list;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (InComeActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_account, null);
        init();
        return view;
    }

    private void init() {

        RelativeLayout rl_account_nodata = (RelativeLayout) view.findViewById(R.id.rl_account_nodata);
        lv_account = (ListView) view.findViewById(R.id.lv_account);

        lv_account.setEmptyView(rl_account_nodata);

        getAccountData();
    }

    private void getAccountData() {

        if (SpUtil.getString(mContext,"ident","").equals("boss")){

            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_SMSR, RequestMethod.POST);
            jsonObjectRequest.add("sid", SpUtil.getString(mContext, "sid", ""));
            jsonObjectRequest.add("type", "2");
            CallServer.getInstance().add(mContext, jsonObjectRequest, callback, UrlTag.BOSS_SMSR, true, false, true);
        }


    }



    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.BOSS_SMSR) {
                LogUtil.d(TAG, response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray income = data.getJSONArray("income");
                        if (income.length() > 0) {
                            list = new ArrayList<>();
                            for (int i = 0; i < income.length(); i++) {
                                moneyDetailBean moneyDetailBean = new moneyDetailBean();
                                moneyDetailBean.setCname(income.getJSONObject(i).getString("cname"));
                                moneyDetailBean.setMoney(income.getJSONObject(i).getString("money"));
                                moneyDetailBean.setTime(income.getJSONObject(i).getString("time"));
                                moneyDetailBean.setType(income.getJSONObject(i).getString("type"));
                                moneyDetailBean.setPayment_onumber(income.getJSONObject(i).getString("payment_onumber"));
                                moneyDetailBean.setSmoney(income.getJSONObject(i).getString("smoney"));
                                moneyDetailBean.setPay_channel(income.getJSONObject(i).getString("pay_channel"));
                                list.add(moneyDetailBean);

                            }

                            BossSmsrAccountAdapter adapter = new BossSmsrAccountAdapter(list, mContext);
                            lv_account.setAdapter(adapter);


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


}
