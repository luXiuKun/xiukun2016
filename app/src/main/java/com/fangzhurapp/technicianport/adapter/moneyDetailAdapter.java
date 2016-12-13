package com.fangzhurapp.technicianport.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.moneyDetailBean;
import com.fangzhurapp.technicianport.utils.NumberUtils;
import com.yolanda.nohttp.Logger;

import java.util.List;

/**
 * Created by android on 2016/7/25.
 */
public class moneyDetailAdapter extends BaseAdapter{
    private List<moneyDetailBean> dataList;
    private Context mContext;
    private final  int TYPE_COUNT = 2;

    public moneyDetailAdapter(List<moneyDetailBean> list, Context context){

        this.dataList = list;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //提现
        if (dataList.get(position).getType().equals("1")){
            return  0;
            //工资
        }else if (dataList.get(position).getType().equals("2")){
            return 1;
        }
        return -1;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        WageHolder wageHolder = null;
        TxHolder txHolder = null;

        int itemViewType = getItemViewType(position);




            if (convertView == null){

                switch (itemViewType){
                    //收入
                    case 1:
                        txHolder = new TxHolder();


                        convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_moneydetail_out,null);

                        txHolder.tv_moneydetail_time = (TextView) convertView.findViewById(R.id.tv_moneydetail_time);
                        txHolder.tv_moneydetail_money = (TextView) convertView.findViewById(R.id.tv_moneydetail_money);
                        txHolder.tv_moneydetail_type = (TextView) convertView.findViewById(R.id.tv_moneydetail_type);
                        txHolder.tv_moneydetail_zmoney = (TextView) convertView.findViewById(R.id.tv_moneydetail_zmoney);
                        txHolder.tv_moneydetail_poundage = (TextView) convertView.findViewById(R.id.tv_moneydetail_poundage);


                        txHolder.tv_moneydetail_time.setText(dataList.get(position).getTime());
                        //String zmoney = NumberUtils.floatFormat(Float.valueOf(dataList.get(position).getMoney()) + Float.valueOf(dataList.get(position).getSmoney()));
                        txHolder.tv_moneydetail_money.setText("+"+dataList.get(position).getMoney()
                        );
                        txHolder.tv_moneydetail_type.setText(dataList.get(position).getCname());
                        txHolder.tv_moneydetail_zmoney.setText(Html.fromHtml("总金额"+"<font color= '#34C083'>"+dataList.get(position).getMoney()+"</font>"+"元"));
                        txHolder.tv_moneydetail_poundage.setText(Html.fromHtml("手续费"+"<font color= '#34C083'>"+dataList.get(position).getSmoney()+"</font>"+"元"));

                        convertView.setTag(txHolder);

                        break;
                    //支出
                    case 0:

                        wageHolder = new WageHolder();


                        convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_moneydetail_wage,null);

                        wageHolder.tv_wage_time = (TextView) convertView.findViewById(R.id.tv_wage_time);
                        wageHolder.tv_wage_money = (TextView) convertView.findViewById(R.id.tv_wage_money);
                        wageHolder.tv_wage = (TextView) convertView.findViewById(R.id.tv_wage);


                        wageHolder.tv_wage_time.setText(dataList.get(position).getTime());
                        wageHolder.tv_wage_money.setText("-"+dataList.get(position).getMoney());
                        wageHolder.tv_wage.setText(dataList.get(position).getCname());
                        convertView.setTag(wageHolder);

                        break;
                }
            }else{


                switch (itemViewType){
                    //收入
                    case 1:

                        txHolder = (TxHolder) convertView.getTag();

                        txHolder.tv_moneydetail_time.setText(dataList.get(position).getTime());
                       // String zmoney = NumberUtils.floatFormat(Float.valueOf(dataList.get(position).getMoney()) + Float.valueOf(dataList.get(position).getCounter_fee()));
                        txHolder.tv_moneydetail_money.setText("+"+dataList.get(position).getMoney() );
                        txHolder.tv_moneydetail_type.setText(dataList.get(position).getCname());
                        txHolder.tv_moneydetail_zmoney.setText(Html.fromHtml("总金额"+"<font color= '#34C083'>"+dataList.get(position).getMoney()+"</font>"+"元"));
                        txHolder.tv_moneydetail_poundage.setText(Html.fromHtml("手续费"+"<font color= '#34C083'>"+dataList.get(position).getSmoney()+"</font>"+"元"));
                        break;
                    //支出
                    case 0:


                        wageHolder = (WageHolder) convertView.getTag();

                        wageHolder.tv_wage_time.setText(dataList.get(position).getTime());
                        wageHolder.tv_wage_money.setText("-"+dataList.get(position).getMoney());
                        wageHolder.tv_wage.setText(dataList.get(position).getCname());

                        break;
                }
            }





        return convertView;
    }


    class WageHolder{

        TextView tv_wage_time;
        TextView tv_wage_money;
        TextView tv_wage;



    }

    class TxHolder{
        TextView tv_moneydetail_type;
        TextView tv_moneydetail_time;
        TextView tv_moneydetail_money;
        TextView tv_moneydetail_zmoney;
        TextView tv_moneydetail_poundage;
    }
}
