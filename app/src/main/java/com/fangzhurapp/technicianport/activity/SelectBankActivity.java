package com.fangzhurapp.technicianport.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.BankCardData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectBankActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.lv_selectcard_carddata)
    ListView lvSelectcardCarddata;
    @Bind(R.id.ll_selectcard_addcard)
    LinearLayout llSelectcardAddcard;

    private Map<Integer,Boolean> stateMap;
    private List<BankCardData> carddata;
    private SelectCardAdapter selectCardAdapter;
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_select_bank);
        CustomApplication.addAct(this);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        llSelectcardAddcard.setOnClickListener(this);
        lvSelectcardCarddata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Boolean aBoolean = !stateMap.get(position);

                for (Integer i:stateMap.keySet()
                     ) {
                    stateMap.put(i,false);

                }
                stateMap.put(position,aBoolean);
                selectCardAdapter.notifyDataSetChanged();

                CheckBox cb_selcard = (CheckBox) parent.getChildAt(position).findViewById(R.id.cb_selcard);

                cb_selcard.setChecked(stateMap.get(position));

                if (stateMap.get(position)){

                    if (mIntent == null)mIntent = new Intent();
                    mIntent.putExtra("resultcarddata",carddata.get(position));
                    SelectBankActivity.this.setResult(001,mIntent);
                    SelectBankActivity.this.finish();
                }
            }
        });

    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("选择银行卡");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setVisibility(View.INVISIBLE);

        if (getIntent() != null){
            carddata = (List<BankCardData>) getIntent().getSerializableExtra("carddata");


            if (stateMap == null)stateMap = new HashMap<>();

            for (int i = 0; i < carddata.size(); i++){

                stateMap.put(i,false);
            }


            selectCardAdapter = new SelectCardAdapter(SelectBankActivity.this, carddata);
            lvSelectcardCarddata.setAdapter(selectCardAdapter);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ll_selectcard_addcard:

                Intent intent = new Intent(SelectBankActivity.this, BindBankCardActivity.class);
                startActivity(intent);
                SelectBankActivity.this.finish();

                break;

            case R.id.img_logo:
                SelectBankActivity.this.finish();
                break;

        }
    }


    private class SelectCardAdapter extends BaseAdapter{

        private List<BankCardData> cardList;
        private Context mContext;
        private SelectCardAdapter(Context context,List<BankCardData> list){
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
            ViewHolder hold =  null;
            if (convertView == null){
                hold = new ViewHolder();
                convertView = View.inflate(mContext,R.layout.item_selectcard_listview,null);
                hold.img_selcard_banklogo = (SimpleDraweeView) convertView.findViewById(R.id.img_selcard_banklogo);
                hold.tv_selcard_bankname = (TextView) convertView.findViewById(R.id.tv_selcard_bankname);
                hold.tv_selcard_num = (TextView) convertView.findViewById(R.id.tv_selcard_num);
                hold.cb_selcard = (CheckBox) convertView.findViewById(R.id.cb_selcard);

                convertView.setTag(hold);
            }else{

                hold = (ViewHolder)convertView.getTag();

            }
            hold.img_selcard_banklogo.setImageURI(Uri.parse(cardList.get(position).getLogourl()));
            hold.tv_selcard_bankname.setText(cardList.get(position).getBankname());

            String cardnumber = cardList.get(position).getCardnumber();
            cardnumber = cardnumber.substring(cardnumber.length() -4,cardnumber.length());
            hold.tv_selcard_num.setText("尾号"+cardnumber);
            hold.cb_selcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Boolean aBoolean = !stateMap.get(position);

                    for (Integer i:stateMap.keySet()
                            ) {
                        stateMap.put(i,false);

                    }
                    stateMap.put(position,aBoolean);
                    selectCardAdapter.notifyDataSetChanged();
                    if (aBoolean){
                        if (mIntent == null)mIntent = new Intent();
                        mIntent.putExtra("resultcarddata",cardList.get(position));
                        SelectBankActivity.this.setResult(001,mIntent);
                        SelectBankActivity.this.finish();
                    }



                }
            });

            hold.cb_selcard.setChecked(stateMap.get(position));




            return convertView;
        }

        class ViewHolder{
            SimpleDraweeView img_selcard_banklogo;
            TextView tv_selcard_bankname;
            TextView tv_selcard_num;
            CheckBox cb_selcard;

        }

    }
}
