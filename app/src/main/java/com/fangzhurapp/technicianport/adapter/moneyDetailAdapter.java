package com.fangzhurapp.technicianport.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.moneyDetailBean;

import java.util.List;

/**
 * Created by android on 2016/7/25.
 */
public class moneyDetailAdapter extends BaseAdapter{
    private List<moneyDetailBean> dataList;
    private Context mContext;

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
            return  1;
            //工资
        }else if (dataList.get(position).getType().equals("2")){
            return 2;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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


        switch (itemViewType){
            //提现
            case 1:
                txHolder = new TxHolder();

                if (convertView == null){
                    convertView =  View.inflate(mContext, R.layout.item_moneydetail_tx,null);

                    txHolder.tv_tx_time = (TextView) convertView.findViewById(R.id.tv_tx_time);
                    txHolder.tv_tx_money = (TextView) convertView.findViewById(R.id.tv_tx_money);


                    convertView.setTag(txHolder);
                }else{

                    txHolder = (TxHolder) convertView.getTag();
                }
                txHolder.tv_tx_time.setText(dataList.get(position).getTime());
                txHolder.tv_tx_money.setText("-"+dataList.get(position).getMoney());


                break;
            //工资
            case 2:

                wageHolder = new WageHolder();

                if (convertView == null){
                    convertView =  View.inflate(mContext, R.layout.item_moneydetail_wage,null);

                    wageHolder.tv_wage_time = (TextView) convertView.findViewById(R.id.tv_wage_time);
                    wageHolder.tv_wage_money = (TextView) convertView.findViewById(R.id.tv_wage_money);


                    convertView.setTag(wageHolder);
                }else{

                    wageHolder = (WageHolder) convertView.getTag();
                }
                wageHolder.tv_wage_time.setText(dataList.get(position).getTime());
                wageHolder.tv_wage_money.setText("+"+dataList.get(position).getMoney());

                break;
        }


        return convertView;
    }


    class WageHolder{

        TextView tv_wage_time;
        TextView tv_wage_money;


    }

    class TxHolder{
        TextView tv_tx_time;
        TextView tv_tx_money;
    }
}
