package com.fangzhurapp.technicianport.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BankCardData;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.view.UnBindSucessDialog;
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

public class PartnerBankCardActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.btn_partner_bindcard)
    Button btnPartnerBindcard;
    @Bind(R.id.partnerbankcard_nodata)
    RelativeLayout partnerbankcardNodata;
    @Bind(R.id.lv_partner_mycard)
    ListView lvPartnerMycard;
    @Bind(R.id.ib_partner_unbind)
    ImageButton ibPartnerUnbind;
    private List<BankCardData> bindCardList;
    private SelectCardAdapter selectCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_partner_bank_card);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        btnPartnerBindcard.setOnClickListener(this);
        ibPartnerUnbind.setOnClickListener(this);
    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("我的银行卡");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        ibPartnerUnbind.setBackgroundResource(R.drawable.img_mybindcard_unbind);
        getCardList();
    }

    private void getCardList() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.PARTNER_CARDLIST, RequestMethod.POST);
        jsonObjectRequest.add("staff_id", SpUtil.getString(PartnerBankCardActivity.this,"partnerid",""));
        jsonObjectRequest.add("identity","4");
        CallServer.getInstance().add(PartnerBankCardActivity.this,jsonObjectRequest,callback, UrlTag.PARTNER_CARDLIST,true,false,true);



    }

    private void unBind() {
        if (bindCardList != null){

            Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.UNBIND_CARD, RequestMethod.POST);
            jsonObjectRequest.add("id",bindCardList.get(0).getId());
            CallServer.getInstance().add(PartnerBankCardActivity.this,jsonObjectRequest,callback,UrlTag.UNBIND_CARD,true,false,true);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_logo:
                PartnerBankCardActivity.this.finish();
                break;

            case R.id.btn_partner_bindcard:
                Intent intent = new Intent(PartnerBankCardActivity.this, BindBankCardActivity.class);
                startActivity(intent);
                break;

            case R.id.ib_partner_unbind:
                unBind();
                break;
        }
    }


    private static final String TAG = "PartnerBankCardActivity";
    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {

            if (what == UrlTag.PARTNER_CARDLIST){

                LogUtil.d(TAG,response.toString());


                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")) {
                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() > 0){
                            bindCardList = new ArrayList<>();
                            ibPartnerUnbind.setVisibility(View.VISIBLE);

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

                            selectCardAdapter = new SelectCardAdapter(PartnerBankCardActivity.this, bindCardList);
                            lvPartnerMycard.setAdapter(selectCardAdapter);
                        }else{
                            lvPartnerMycard.setEmptyView(partnerbankcardNodata);
                            ibPartnerUnbind.setVisibility(View.GONE);
                        }




                    } else {
                        lvPartnerMycard.setEmptyView(partnerbankcardNodata);
                        ibPartnerUnbind.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    lvPartnerMycard.setEmptyView(partnerbankcardNodata);
                    ibPartnerUnbind.setVisibility(View.GONE);
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

                            Toast.makeText(PartnerBankCardActivity.this, "解绑成功", Toast.LENGTH_SHORT).show();
                            bindCardList.remove(0);
                            selectCardAdapter.notifyDataSetChanged();
                            ibPartnerUnbind.setVisibility(View.GONE);

                        }else{

                            Toast.makeText(PartnerBankCardActivity.this, "解绑失败", Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        Toast.makeText(PartnerBankCardActivity.this, "解绑失败", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        getCardList();
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
            hold.cb_mybindcard_selcard.setVisibility(View.INVISIBLE);

            hold.img_mybindcard_banklogo.setImageURI(Uri.parse(cardList.get(position).getLogourl()));
            hold.tv_mybindcard_bankname.setText(cardList.get(position).getBankname());

            String cardnumber = cardList.get(position).getCardnumber();
            cardnumber = "**********"+cardnumber.substring(cardnumber.length() - 4, cardnumber.length());
            hold.tv_mybindcard_num.setText(cardnumber);



            return convertView;
        }

        class ViewHolder {
            SimpleDraweeView img_mybindcard_banklogo;
            TextView tv_mybindcard_bankname;
            TextView tv_mybindcard_num;
            CheckBox cb_mybindcard_selcard;

        }

    }
}
