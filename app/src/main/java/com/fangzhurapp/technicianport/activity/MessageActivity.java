package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.adapter.CommAdapter;
import com.fangzhurapp.technicianport.adapter.ViewHolder;
import com.fangzhurapp.technicianport.bean.GonggaoBean;
import com.fangzhurapp.technicianport.bean.MessageBean;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_msg_back)
    ImageView imgMsgBack;
    @Bind(R.id.cb_msg)
    CheckBox cbMsg;
    @Bind(R.id.lv_msg)
    ListView lvMsg;
    private static final String TAG = "MessageActivity";
    @Bind(R.id.rl_nomsg)
    RelativeLayout rlNomsg;
    private List<MessageBean> msgList;
    private List<GonggaoBean> ggList;
    private String CHECK_STATE = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Fresco.initialize(MessageActivity.this);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        CustomApplication.addAct(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgMsgBack.setOnClickListener(this);
        cbMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CHECK_STATE = "1";
                    getMsg();
                } else {
                    CHECK_STATE = "2";
                    getGongGao();
                }
            }
        });


            lvMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (CHECK_STATE.equals("1")){

                        Intent intent = new Intent(MessageActivity.this, OrderCancelDetailActivity.class);
                        intent.putExtra("msgdetail",msgList.get(position));
                        startActivity(intent);
                    }
                }
            });




    }

    private void getGongGao() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.GONGGAO, RequestMethod.POST);
        jsonObjectRequest.add("sta","1");
        jsonObjectRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        CallServer.getInstance().add(MessageActivity.this,jsonObjectRequest,callback,UrlTag.GONGGAO,true,false,true);


    }

    private void initView() {
        lvMsg.setEmptyView(rlNomsg);
        cbMsg.setChecked(true);
        getMsg();
    }

    private void getMsg() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.MSG_ORDER, RequestMethod.POST);

        jsonObjectRequest.add("staff_id","187");
        jsonObjectRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        CallServer.getInstance().add(MessageActivity.this, jsonObjectRequest, callback, UrlTag.MSG_ORDER, true, false, true);
    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.MSG_ORDER) {

                LogUtil.d(TAG, response.toString());
                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray arr = data.getJSONArray("arr");

                        if (arr.length() > 0) {
                            msgList = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                MessageBean messageBean = new MessageBean();

                                messageBean.setId(arr.getJSONObject(i).getString("id"));
                                messageBean.setOnumber_id(arr.getJSONObject(i).getString("onumber_id"));
                                messageBean.setOnumber(arr.getJSONObject(i).getString("onumber"));
                                messageBean.setOnclick(arr.getJSONObject(i).getString("onclick"));
                                messageBean.setProject(arr.getJSONObject(i).getString("project"));
                                messageBean.setState(arr.getJSONObject(i).getString("state"));
                                messageBean.setTime(arr.getJSONObject(i).getString("time"));
                                messageBean.setStaff_id(arr.getJSONObject(i).getString("staff_id"));
                                messageBean.setAddtime(arr.getJSONObject(i).getString("addtime"));
                                msgList.add(messageBean);
                            }

                            CommAdapter<MessageBean> commAdapter = new CommAdapter<MessageBean>(MessageActivity.this, R.layout.item_msg_order, msgList) {
                                @Override
                                public void convert(ViewHolder holder, MessageBean messageBean, int position) {

                                    ImageView img_msg_order = holder.getView(R.id.img_msg_order);
                                    TextView tv_msg_orderstate = holder.getView(R.id.tv_msg_orderstate);
                                    TextView tv_msg_time = holder.getView(R.id.tv_msg_time);


                                    if (messageBean.getOnclick().equals("1")) {
                                        img_msg_order.setBackgroundResource(R.drawable.img_msg_order_pre);
                                    } else if (messageBean.getOnclick().equals("2")) {
                                        img_msg_order.setBackgroundResource(R.drawable.img_msg_order_nor);
                                    }
                                    tv_msg_time.setText(messageBean.getTime());
                                    tv_msg_orderstate.setText(messageBean.getState());
                                }
                            };

                            lvMsg.setAdapter(commAdapter);
                        }

                    } else {
                        Toast.makeText(MessageActivity.this, "暂无消息", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (what == UrlTag.GONGGAO){
                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() > 0){
                            ggList = new ArrayList<>();

                            for (int i =0;i<data.length();i++){
                                GonggaoBean gonggaoBean = new GonggaoBean();

                                gonggaoBean.setTime(data.getJSONObject(i).getString("time"));
                                gonggaoBean.setTitle(data.getJSONObject(i).getString("title"));
                                ggList.add(gonggaoBean);
                            }
                            CommAdapter<GonggaoBean> commAdapter = new CommAdapter<GonggaoBean>(MessageActivity.this, R.layout.item_gonggao, ggList) {
                                @Override
                                public void convert(ViewHolder holder, GonggaoBean gonggaoBean, int position) {

                                    TextView tv_gongao_title = holder.getView(R.id.tv_gongao_title);
                                    TextView tv_gonggao_time = holder.getView(R.id.tv_gonggao_time);

                                    tv_gongao_title.setText(gonggaoBean.getTitle());
                                    tv_gonggao_time.setText(gonggaoBean.getTime());
                                }
                            };
                            lvMsg.setAdapter(commAdapter);


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


    @Override
    protected void onRestart() {
        super.onRestart();
        cbMsg.setChecked(true);
        CHECK_STATE = "1";
        getMsg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_msg_back:
                MessageActivity.this.finish();
                break;
        }
    }
}
