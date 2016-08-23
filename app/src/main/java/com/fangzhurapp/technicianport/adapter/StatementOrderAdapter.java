package com.fangzhurapp.technicianport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.bean.WorkorderBean;

import java.util.List;

/**
 * Created by android on 2016/7/15.
 */
public class StatementOrderAdapter extends BaseAdapter {
    private Context mContext;
    private List<WorkorderBean> dataList;
    private int ITEM_TYPE_COUNT = 2;
    private static final String TAG = "StatementOrderAdapter";

    public StatementOrderAdapter(Context context, List<WorkorderBean> data){
        this.dataList = data;
        this.mContext = context;
    };
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public int getViewTypeCount() {
        return ITEM_TYPE_COUNT;
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
    public int getItemViewType(int position) {
        //会员卡
        if (dataList.get(position).getO_type().equals("1")){

            return 0;
            //已完成订单
        }else if (dataList.get(position).getO_type().equals("2")){

            return 1;

        }

        return -1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int itemType = getItemViewType(position);
        OrderFinishViewHolder finishHolder = null;
        OrderVIPViewHolder vipHolder = null;

        if (convertView == null) {


                switch (itemType) {
                    //会员开卡
                    case 0:

                        vipHolder = new OrderVIPViewHolder();
                        convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_statementfrag_ordervip,null);

                        vipHolder.tv_ordervip_num = (TextView) convertView.findViewById(R.id.tv_sf_ordervip_num);
                        vipHolder.tv_ordervip_kktime = (TextView) convertView.findViewById(R.id.tv_sf_ordervip_kktime);
                        vipHolder.tv_ordervip_viptype = (TextView) convertView.findViewById(R.id.tv_sf_ordervip_viptype);
                        vipHolder.tv_ordervip_payprice = (TextView) convertView.findViewById(R.id.tv_sf_ordervip_payprice);
                        vipHolder.tv_ordervip_tc = (TextView) convertView.findViewById(R.id.tv_sf_ordervip_tc);

                        vipHolder.tv_ordervip_num.setText(dataList.get(position).getMnumber());
                        vipHolder.tv_ordervip_kktime.setText(dataList.get(position).getAddtime());
                        vipHolder.tv_ordervip_viptype.setText(dataList.get(position).getK_type());
                        vipHolder.tv_ordervip_payprice.setText(dataList.get(position).getK_money());
                        vipHolder.tv_ordervip_tc.setText(dataList.get(position).getTc_money());



                        convertView.setTag(vipHolder);
                        break;
                    //已完成订单
                    case 1:

                        finishHolder = new OrderFinishViewHolder();

                        convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_statementfrag_order,null);
                        finishHolder.tv_orderfinish_num = (TextView) convertView.findViewById(R.id.tv_sf_order_num);
                        finishHolder.tv_orderfinish_ordertime = (TextView) convertView.findViewById(R.id.tv_sf_order_ordertime);
                        finishHolder.tv_orderfinish_proname = (TextView) convertView.findViewById(R.id.tv_sf_order_proname);
                        finishHolder.tv_orderfinish_patype = (TextView) convertView.findViewById(R.id.tv_sf_pztype);
                        finishHolder.tv_orderfinish_kkident = (TextView) convertView.findViewById(R.id.tv_sf_khident);
                        finishHolder.tv_orderfinish_jsprice = (TextView) convertView.findViewById(R.id.tv_sf_jsprice);
                        finishHolder.tv_orderfinish_tcprice = (TextView) convertView.findViewById(R.id.tv_sf_order_tcprice);

                        finishHolder.tv_orderfinish_num.setText(dataList.get(position).getMnumber());
                        finishHolder.tv_orderfinish_ordertime.setText(dataList.get(position).getPtime());
                        finishHolder.tv_orderfinish_proname.setText(dataList.get(position).getProject_name());
                        finishHolder.tv_orderfinish_patype.setText(dataList.get(position).getType());
                        finishHolder.tv_orderfinish_tcprice.setText(dataList.get(position).getTc_money());
                        finishHolder.tv_orderfinish_jsprice.setText(dataList.get(position).getMoney());
                        finishHolder.tv_orderfinish_kkident.setText(dataList.get(position).getSet_type());


                        convertView.setTag(finishHolder);



                        break;


                }


        }else {

                switch (itemType){

                    case 0:
                        vipHolder = (OrderVIPViewHolder)convertView.getTag();
                        vipHolder.tv_ordervip_num.setText(dataList.get(position).getMnumber());
                        vipHolder.tv_ordervip_kktime.setText(dataList.get(position).getAddtime());
                        vipHolder.tv_ordervip_viptype.setText(dataList.get(position).getK_type());
                        vipHolder.tv_ordervip_payprice.setText(dataList.get(position).getK_money());
                        vipHolder.tv_ordervip_tc.setText(dataList.get(position).getTc_money());


                        break;

                    case 1:

                        finishHolder = (OrderFinishViewHolder) convertView.getTag();
                        finishHolder.tv_orderfinish_num.setText(dataList.get(position).getMnumber());
                        finishHolder.tv_orderfinish_ordertime.setText(dataList.get(position).getPtime());
                        finishHolder.tv_orderfinish_proname.setText(dataList.get(position).getProject_name());
                        finishHolder.tv_orderfinish_patype.setText(dataList.get(position).getType());
                        finishHolder.tv_orderfinish_tcprice.setText(dataList.get(position).getTc_money());
                        finishHolder.tv_orderfinish_jsprice.setText(dataList.get(position).getMoney());
                        finishHolder.tv_orderfinish_kkident.setText(dataList.get(position).getSet_type());


                        break;


                }


        }

        return convertView;
    }





    class OrderFinishViewHolder{

        TextView tv_orderfinish_num;
        TextView tv_orderfinish_ordertime;
        TextView tv_orderfinish_proname;
        TextView tv_orderfinish_patype;
        TextView tv_orderfinish_kkident;
        TextView tv_orderfinish_jsprice;
        TextView tv_orderfinish_tcprice;
    }

    class OrderVIPViewHolder{

        TextView tv_ordervip_num;
        TextView tv_ordervip_kktime;
        TextView tv_ordervip_viptype;
        TextView tv_ordervip_payprice;
        TextView tv_ordervip_tc;
    }


}
