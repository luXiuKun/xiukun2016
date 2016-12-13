package com.fangzhurapp.technicianport.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BankCardData;
import com.fangzhurapp.technicianport.eventbus.BossBindSucessEvent;
import com.fangzhurapp.technicianport.eventbus.MsgEvent;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.view.UnBindSucessDialog;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossBindBankActivity extends AppCompatActivity implements View.OnClickListener, OnRefreshListener {


    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.swipe_target)
    ListView swipeTarget;
    @Bind(R.id.swipe_bossbindbank)
    SwipeToLoadLayout swipeBindbank;
    @Bind(R.id.rl_bossbindbank)
    RelativeLayout rlBossbindbank;
    @Bind(R.id.btn_bossbindbank)
    Button btnBossbindbank;
    @Bind(R.id.ib_bossbindbank_unbind)
    ImageButton ibBossbindbankUnbind;
    private List<BankCardData> bindCardList;
    private static final String TAG = "BossBindBankActivity";

    private Map<Integer, Boolean> selectState;
    private SelectCardAdapter selectCardAdapter;
    private List<BankCardData> selectCard = new ArrayList<>();
    private UnBindSucessDialog unBindSucessDialog;

    private int selectPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_boss_bind_bank);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        swipeBindbank.setOnRefreshListener(this);
        btnBossbindbank.setOnClickListener(this);
        ibBossbindbankUnbind.setOnClickListener(this);
        imgTitleRight.setOnClickListener(this);
        swipeTarget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Boolean aBoolean = !selectState.get(position);

                for (Integer i: selectState.keySet()) {

                    selectState.put(i,false);
                }
                selectState.put(position,aBoolean);
                selectCardAdapter.notifyDataSetChanged();

                selectCard.clear();

                if (aBoolean)selectCard.add(bindCardList.get(position));

                if (selectCard.size() >0){
                    ibBossbindbankUnbind.setBackgroundResource(R.drawable.img_mybindcard_unbind);
                }else{
                    ibBossbindbankUnbind.setBackgroundResource(R.drawable.img_mybindcard_unbind_gray);
                }
                selectPosition = position;

            }
        });
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("我的银行卡");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        //imgTitleRight.setVisibility(View.INVISIBLE);
        imgTitleRight.setBackgroundResource(R.drawable.img_addbankcard);
        swipeBindbank.setRefreshing(true);


    }

    @Subscribe
    public void onEventMainThread(BossBindSucessEvent msg) {
        swipeBindbank.setRefreshing(true);
    }


    private void unBind() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.UNBIND_CARD, RequestMethod.POST);
        jsonObjectRequest.add("id",selectCard.get(selectPosition).getId());
        CallServer.getInstance().add(BossBindBankActivity.this,jsonObjectRequest,callback,UrlTag.UNBIND_CARD,true,false,true);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_logo:
                BossBindBankActivity.this.finish();
                break;

            case R.id.btn_bossbindbank:
                if (SpUtil.getString(BossBindBankActivity.this,"shenfen","").equals("1")){

                    Intent intent = new Intent(BossBindBankActivity.this, BossBindCardActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(BossBindBankActivity.this, "无操作权限", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ib_bossbindbank_unbind:

                if (selectCard.size() > 0){
                    unBind();
                }else{
                    Toast.makeText(BossBindBankActivity.this, "请选择一张银行卡", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.img_title_right:
                if (SpUtil.getString(BossBindBankActivity.this,"shenfen","").equals("1")){

                    Intent intent = new Intent(BossBindBankActivity.this, BossBindCardActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(BossBindBankActivity.this, "无操作权限", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }


    @Override
    public void onRefresh() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_BIND_CARD_LIST, RequestMethod.POST);
        jsonObjectRequest.setCacheMode(CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE);
        jsonObjectRequest.add("staff_id", SpUtil.getString(BossBindBankActivity.this, "id", ""));
        CallServer.getInstance().add(BossBindBankActivity.this, jsonObjectRequest, callback, UrlTag.BOSS_BIND_CARD_LIST, true, false, true);

    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {

            if (what == UrlTag.BOSS_BIND_CARD_LIST) {
                swipeBindbank.setRefreshing(false);
                LogUtil.d(TAG, response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data.length() > 0){
                            bindCardList = new ArrayList<>();
                            ibBossbindbankUnbind.setVisibility(View.VISIBLE);

                            for (int i = 0; i < data.length(); i++) {

                                BankCardData bankCardData = new BankCardData();
                                bankCardData.setAddtime(data.getJSONObject(i).getString("addtime"));
                                bankCardData.setBankname(data.getJSONObject(i).getString("bankname"));
                                bankCardData.setCardnumber(data.getJSONObject(i).getString("cardnumber"));
                                bankCardData.setLogourl(data.getJSONObject(i).getString("logourl"));
                                bankCardData.setSname(data.getJSONObject(i).getString("sname"));
                                bankCardData.setStaff_id(data.getJSONObject(i).getString("staff_id"));
                                bankCardData.setId(data.getJSONObject(i).getString("id"));
                                bindCardList.add(bankCardData);
                            }

                            initCheck();
                        }else{
                            swipeTarget.setEmptyView(rlBossbindbank);
                            ibBossbindbankUnbind.setVisibility(View.GONE);
                        }



                    } else {
                        swipeTarget.setEmptyView(rlBossbindbank);
                        ibBossbindbankUnbind.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    swipeTarget.setEmptyView(rlBossbindbank);
                    ibBossbindbankUnbind.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }else if (what == UrlTag.UNBIND_CARD){

                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        String isok = data.getString("isok");

                        if (isok.equals("1")){
                            EventBus.getDefault().post(new BossBindSucessEvent(true));
                            unBindSucessDialog = new UnBindSucessDialog(BossBindBankActivity.this, R.layout.dialog_unbindsucess);
                            unBindSucessDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            unBindSucessDialog.show();

                            Timer timer = new Timer(1000, 1000);
                            timer.start();

                            bindCardList.remove(selectPosition);
                            selectCardAdapter.notifyDataSetChanged();

                            //swipeBindbank.setRefreshing(true);

                        }else{

                            Toast.makeText(BossBindBankActivity.this, "解绑失败", Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        Toast.makeText(BossBindBankActivity.this, "解绑失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {
            if (what == UrlTag.BOSS_BIND_CARD_LIST) {
                swipeBindbank.setRefreshing(false);

                swipeTarget.setEmptyView(rlBossbindbank);
            }
        }
    };

    private void initCheck() {

        if (selectState == null) selectState = new HashMap<>();

        for (int i = 0; i < bindCardList.size(); i++) {

            selectState.put(i, false);
        }


        selectCardAdapter = new SelectCardAdapter(BossBindBankActivity.this, bindCardList);
        swipeTarget.setAdapter(selectCardAdapter);

    }


    private class SelectCardAdapter extends BaseAdapter {

        private List<BankCardData> cardList;
        private Context mContext;

        private SelectCardAdapter(Context context, List<BankCardData> list) {
            this.mContext = context;
            this.cardList = list;

        }

        @Override
        public int getCount() {
            return cardList.size();
        }

        @Override
        public Object getItem(int position) {
            return cardList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder hold = null;
            if (convertView == null) {
                hold = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_mybindcard_listview, null);
                hold.img_mybindcard_banklogo = (SimpleDraweeView) convertView.findViewById(R.id.img_mybindcard_banklogo);
                hold.tv_mybindcard_bankname = (TextView) convertView.findViewById(R.id.tv_mybindcard_bankname);
                hold.tv_mybindcard_num = (TextView) convertView.findViewById(R.id.tv_mybindcard_num);
                hold.cb_mybindcard_selcard = (CheckBox) convertView.findViewById(R.id.cb_mybindcard_selcard);

                convertView.setTag(hold);
            } else {

                hold = (ViewHolder) convertView.getTag();

            }
            hold.img_mybindcard_banklogo.setImageURI(Uri.parse(cardList.get(position).getLogourl()));
            hold.tv_mybindcard_bankname.setText(cardList.get(position).getBankname());

            String cardnumber = cardList.get(position).getCardnumber();
            cardnumber = cardnumber.substring(cardnumber.length() - 4, cardnumber.length());
            hold.tv_mybindcard_num.setText("尾号" + cardnumber);
            hold.cb_mybindcard_selcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Boolean aBoolean = !selectState.get(position);

                    for (Integer i : selectState.keySet()
                            ) {
                        selectState.put(i, false);

                    }
                    selectState.put(position, aBoolean);
                    selectCardAdapter.notifyDataSetChanged();
                    selectCard.clear();
                    if (aBoolean) selectCard.add(cardList.get(position));
                    if (selectCard.size() > 0) {
                        ibBossbindbankUnbind.setBackgroundResource(R.drawable.img_mybindcard_unbind);
                    } else {
                        ibBossbindbankUnbind.setBackgroundResource(R.drawable.img_mybindcard_unbind_gray);
                    }
                    selectPosition = position;


                }
            });

            hold.cb_mybindcard_selcard.setChecked(selectState.get(position));


            return convertView;
        }

        class ViewHolder {
            SimpleDraweeView img_mybindcard_banklogo;
            TextView tv_mybindcard_bankname;
            TextView tv_mybindcard_num;
            CheckBox cb_mybindcard_selcard;

        }

    }


    class Timer extends CountDownTimer {



        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            unBindSucessDialog.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
