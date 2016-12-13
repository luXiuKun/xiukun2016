package com.fangzhurapp.technicianport.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.PartnerBean;
import com.fangzhurapp.technicianport.bean.moneyDetailBean;

import java.util.List;

/**
 * Created by android on 2016/7/25.
 */
public class PartnerDetailAdapter extends BaseAdapter{
    private List<PartnerBean> dataList;
    private Context mContext;
    private final  int TYPE_COUNT = 2;
    private String lv;
    public RemindListener mListener;

    public PartnerDetailAdapter(List<PartnerBean> list, Context context,String lv){

        this.dataList = list;
        this.mContext = context;
        this.lv = lv;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {

        //未开通
        if (dataList.get(position).getIs_pay().equals("0")){
            return 0;
        }else{
            //开通
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        int itemViewType = getItemViewType(position);
        NoPayHolder noPayHolder = null;
        PayHolder payHolder = null;

        if (convertView == null){


            switch (itemViewType){

                case 0:
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_partner_2,null);
                    noPayHolder = new NoPayHolder();
                    noPayHolder.btn_remind = (ImageButton) convertView.findViewById(R.id.btn_remind);
                    noPayHolder.tv_partner_name = (TextView) convertView.findViewById(R.id.tv_partner_name);
                    noPayHolder.tv_partner_pay= (TextView) convertView.findViewById(R.id.tv_partner_pay);
                    noPayHolder.tv_partner_shopName= (TextView) convertView.findViewById(R.id.tv_partner_shopName);
                    noPayHolder.img_partner_nopay = (ImageView) convertView.findViewById(R.id.img_partner_nopay);

                    noPayHolder.tv_partner_name.setText("匿名用户("+dataList.get(position).getAccount()+")");
                    noPayHolder.tv_partner_pay.setText(dataList.get(position).getReward());
                    noPayHolder.tv_partner_shopName.setText(dataList.get(position).getStore_count());
                    if (lv.equals("2")){
                        noPayHolder.img_partner_nopay.setVisibility(View.VISIBLE);
                        noPayHolder.btn_remind.setVisibility(View.INVISIBLE);
                    }
                    noPayHolder.btn_remind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null){

                                mListener.remind(position);
                            }
                        }
                    });

                    convertView.setTag(noPayHolder);

                    break;

                case 1:

                    convertView = LayoutInflater.from(mContext).inflate(R.layout.item_partner_1,null);
                    payHolder = new PayHolder();
                    payHolder.tv_partner_name = (TextView) convertView.findViewById(R.id.tv_partner_name);
                    payHolder.tv_partner_pay= (TextView) convertView.findViewById(R.id.tv_partner_pay);
                    payHolder.tv_partner_shopName= (TextView) convertView.findViewById(R.id.tv_partner_shopName);
                    payHolder.tv_partner_companyName = (TextView) convertView.findViewById(R.id.tv_partner_companyName);
                    payHolder.img_partnerDet_tag = (ImageView) convertView.findViewById(R.id.img_partnerDet_tag);

                    if (!TextUtils.isEmpty(dataList.get(position).getName())
                            && !TextUtils.isEmpty(dataList.get(position).getSname())){


                        payHolder.tv_partner_name.setText(dataList.get(position).getName()+"("+dataList.get(position).getAccount()+")");

                        payHolder.tv_partner_companyName.setText(dataList.get(position).getSname());

                    }else{
                        if (dataList.get(position).getIs_pay().equals("0")){

                            payHolder.img_partnerDet_tag.setBackgroundResource(R.drawable.img_partner_noname);
                            payHolder.tv_partner_name.setText("匿名用户"+"("+dataList.get(position).getAccount()+")");
                            payHolder.tv_partner_name.setTextColor(mContext.getResources().getColor(R.color.blacktext));
                            payHolder.tv_partner_companyName.setVisibility(View.INVISIBLE);
                        }else{
                            payHolder.img_partnerDet_tag.setBackgroundResource(R.drawable.img_partnerdet_bg);
                            payHolder.tv_partner_name.setText("匿名用户"+"("+dataList.get(position).getAccount()+")");
                            payHolder.tv_partner_name.setTextColor(mContext.getResources().getColor(R.color.tab_bg));
                            payHolder.tv_partner_companyName.setVisibility(View.INVISIBLE);
                        }
                    }
                    payHolder.tv_partner_pay.setText(dataList.get(position).getReward());
                    payHolder.tv_partner_shopName.setText(dataList.get(position).getStore_count());
                    convertView.setTag(payHolder);

                    break;
            }
        }else{


            switch (itemViewType){

                case 0:

                    noPayHolder = (NoPayHolder) convertView.getTag();

                    noPayHolder.tv_partner_name.setText("匿名用户("+dataList.get(position).getAccount()+")");
                    noPayHolder.tv_partner_pay.setText(dataList.get(position).getReward());
                    noPayHolder.tv_partner_shopName.setText("0家");
                    if (lv.equals("2")){
                        noPayHolder.img_partner_nopay.setVisibility(View.VISIBLE);
                        noPayHolder.btn_remind.setVisibility(View.INVISIBLE);
                    }

                    noPayHolder.btn_remind.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null){

                                mListener.remind(position);
                            }
                        }
                    });

                    break;

                case 1:
                    payHolder = (PayHolder) convertView.getTag();

                    if (!TextUtils.isEmpty(dataList.get(position).getName())
                            && !TextUtils.isEmpty(dataList.get(position).getSname())){


                        payHolder.tv_partner_name.setText(dataList.get(position).getName()+"("+dataList.get(position).getAccount()+")");

                        payHolder.tv_partner_companyName.setText(dataList.get(position).getSname());

                    }else{
                        if (dataList.get(position).getIs_pay().equals("0")){

                            payHolder.img_partnerDet_tag.setBackgroundResource(R.drawable.img_partner_noname);
                            payHolder.tv_partner_name.setText("匿名用户"+"("+dataList.get(position).getAccount()+")");
                            payHolder.tv_partner_name.setTextColor(mContext.getResources().getColor(R.color.blacktext));
                            payHolder.tv_partner_companyName.setVisibility(View.INVISIBLE);
                        }else{
                            payHolder.img_partnerDet_tag.setBackgroundResource(R.drawable.img_partnerdet_bg);
                            payHolder.tv_partner_name.setText("匿名用户"+"("+dataList.get(position).getAccount()+")");
                            payHolder.tv_partner_name.setTextColor(mContext.getResources().getColor(R.color.tab_bg));
                            payHolder.tv_partner_companyName.setVisibility(View.INVISIBLE);
                        }
                    }
                    payHolder.tv_partner_pay.setText(dataList.get(position).getReward());
                    payHolder.tv_partner_shopName.setText(dataList.get(position).getStore_count());
                    break;



            }

        }




        return convertView;
    }

    class NoPayHolder{
        TextView tv_partner_name;
        TextView tv_partner_shopName;
        TextView tv_partner_pay;
        ImageButton btn_remind;
        ImageView img_partnerDet_tag;
        ImageView img_partner_nopay;

    }


    class PayHolder{

        TextView tv_partner_name;
        TextView tv_partner_shopName;
        TextView tv_partner_pay;
        TextView tv_partner_companyName;
        ImageView img_partnerDet_tag;
    }

    public interface RemindListener{

        void remind(int position);
    }

    public void setRemindListener(RemindListener mListener){

        this.mListener = mListener;

    }


}
