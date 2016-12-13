package com.fangzhurapp.technicianport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.adapter.CommAdapter;
import com.fangzhurapp.technicianport.adapter.ViewHolder;
import com.fangzhurapp.technicianport.bean.PartnerMsgBean;
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

public class PartnerMsgAct extends AppCompatActivity {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.lv_partner_msg)
    ListView lvPartnerMsg;
    @Bind(R.id.rl_partner_nomsg)
    RelativeLayout rlPartnerNomsg;
    private List<PartnerMsgBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_msg);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        lvPartnerMsg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(PartnerMsgAct.this, PartnerMsgDetActivity.class);
                intent.putExtra("title",mList.get(position).getTitle());
                intent.putExtra("time",mList.get(position).getAdd_time());
                intent.putExtra("content",mList.get(position).getContent());
                startActivity(intent);

            }
        });
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("我的消息");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        lvPartnerMsg.setEmptyView(rlPartnerNomsg);

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PartnerMsgAct.this.finish();
            }
        });


        getMsgList();

    }

    private void getMsgList() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.PARTNER_MSG, RequestMethod.POST);
        jsonObjectRequest.add("account", SpUtil.getString(PartnerMsgAct.this,"uname",""));
        CallServer.getInstance().add(PartnerMsgAct.this,jsonObjectRequest,callback, UrlTag.PARTNER_MSG,true,false,true);

    }

    private static final String TAG = "PartnerMsgAct";
    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {

            if (what == UrlTag.PARTNER_MSG){

                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray list = data.getJSONArray("list");
                        if (list.length() >0 ){
                            mList = new ArrayList<>();
                            for (int i = 0; i< list.length();i++){

                                PartnerMsgBean partnerMsgBean = new PartnerMsgBean();

                                partnerMsgBean.setAdd_time(list.getJSONObject(i).getString("add_time"));
                                partnerMsgBean.setContent(list.getJSONObject(i).getString("content"));
                                partnerMsgBean.setTitle(list.getJSONObject(i).getString("title"));

                                mList.add(partnerMsgBean);
                            }

                            lvPartnerMsg.setAdapter(new CommAdapter<PartnerMsgBean>(PartnerMsgAct.this
                            ,R.layout.item_partnermsg,mList) {
                                @Override
                                public void convert(ViewHolder holder, PartnerMsgBean partnerMsgBean, int position) {

                                    TextView title = holder.getView(R.id.tv_partnermsg_title);
                                    TextView content = holder.getView(R.id.tv_partnermsg_content);
                                    TextView time = holder.getView(R.id.tv_partnermsg_time);

                                    title.setText(partnerMsgBean.getTitle());
                                    content.setText(partnerMsgBean.getContent());
                                    time.setText(partnerMsgBean.getAdd_time());
                                }
                            });

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
