package com.fangzhurapp.technicianport.adapter;

import android.content.Context;
import android.text.TextUtils;
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
    private int ITEM_TYPE_COUNT = 3;
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
            //订单
            if (dataList.get(position).getItems_type().equals("1")){

                return 1;
                //商品
            }else if (dataList.get(position).getItems_type().equals("2")
                    || dataList.get(position).getItems_type().equals("3")){
                return 2;
            }

        }

        return -1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int itemType = getItemViewType(position);
        OrderFinishViewHolder finishHolder = null;
        OrderVIPViewHolder vipHolder = null;
        goodsOrderFinishHolder goodsFinishHolder = null;

        if (convertView == null) {


                switch (itemType) {
                    //会员开卡
                    case 0:

                        vipHolder = new OrderVIPViewHolder();
                        convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_workfragment_ordervip,null);

                        vipHolder.tv_ordervip_num = (TextView) convertView.findViewById(R.id.tv_ordervip_num);
                        vipHolder.tv_ordervip_kktime = (TextView) convertView.findViewById(R.id.tv_ordervip_kktime);
                        vipHolder.tv_ordervip_viptype = (TextView) convertView.findViewById(R.id.tv_ordervip_viptype);
                        vipHolder.tv_ordervip_payprice = (TextView) convertView.findViewById(R.id.tv_ordervip_payprice);
                        vipHolder.tv_ordervip_tc = (TextView) convertView.findViewById(R.id.tv_ordervip_tc);
                        vipHolder.ib_ordervip_fz = (ImageButton) convertView.findViewById(R.id.ib_ordervip_fz);
                        vipHolder.ib_ordervip_fz.setVisibility(View.INVISIBLE);
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
                        convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_workfragment_orderfinish,null);
                        finishHolder.tv_orderfinish_num = (TextView) convertView.findViewById(R.id.tv_orderfinish_num);
                        finishHolder.tv_orderfinish_ordertime = (TextView) convertView.findViewById(R.id.tv_orderfinish_ordertime);
                        finishHolder.tv_orderfinish_proname = (TextView) convertView.findViewById(R.id.tv_orderfinish_proname);
                        finishHolder.tv_orderfinish_patype = (TextView) convertView.findViewById(R.id.tv_orderfinish_patype);
                        finishHolder.tv_orderfinish_kkident = (TextView) convertView.findViewById(R.id.tv_orderfinish_kkident);
                        finishHolder.tv_orderfinish_jsprice = (TextView) convertView.findViewById(R.id.tv_orderfinish_jsprice);
                        finishHolder.tv_orderfinish_tcprice = (TextView) convertView.findViewById(R.id.tv_orderfinish_tcprice);
                        finishHolder.ib_orderfinish_fz = (ImageButton) convertView.findViewById(R.id.ib_orderfinish_fz);
                        finishHolder.tv_orderfinish_housenum = (TextView) convertView.findViewById(R.id.tv_orderfinish_housenum);

                        finishHolder.ib_orderfinish_fz.setVisibility(View.INVISIBLE);

                        if (dataList.get(position).getAdd_order().equals("1")){
                            finishHolder.tv_orderfinish_proname.setText(dataList.get(position).getProject_name());

                        }else{
                            finishHolder.tv_orderfinish_proname.setText(dataList.get(position).getProject_name()+"(加钟)");
                        }
                        finishHolder.tv_orderfinish_ordertime.setText(dataList.get(position).getPtime());
                        finishHolder.tv_orderfinish_num.setText(dataList.get(position).getMnumber());
                        finishHolder.tv_orderfinish_patype.setText(dataList.get(position).getType());
                        finishHolder.tv_orderfinish_tcprice.setText(dataList.get(position).getTc_money());
                        finishHolder.tv_orderfinish_jsprice.setText(dataList.get(position).getMoney());
                        finishHolder.tv_orderfinish_kkident.setText(dataList.get(position).getSet_type());



                        if (!TextUtils.isEmpty(dataList.get(position).getRoom_number())){

                            finishHolder.tv_orderfinish_housenum.setText(dataList.get(position).getRoom_number()+"房");
                        }else{
                            finishHolder.tv_orderfinish_housenum.setText("无");
                        }

                        convertView.setTag(finishHolder);



                        break;


                    case 2:

                        goodsFinishHolder = new goodsOrderFinishHolder();
                        convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_workfragment_goodsfinish,null);

                        goodsFinishHolder.tv_goodsfinish_goodsname = (TextView) convertView.findViewById(R.id.tv_goodsfinish_goodsname);
                        goodsFinishHolder.tv_goodsfinish_num = (TextView) convertView.findViewById(R.id.tv_goodsfinish_num);
                        goodsFinishHolder.tv_goodsfinish_time = (TextView) convertView.findViewById(R.id.tv_goodsfinish_time);
                        goodsFinishHolder.tv_goodsfinish_goodsname1 = (TextView) convertView.findViewById(R.id.tv_goodsfinish_goodsname1);
                        goodsFinishHolder.tv_goodsfinish_price = (TextView) convertView.findViewById(R.id.tv_goodsfinish_price);
                        goodsFinishHolder.tv_goodsfinish_tc = (TextView) convertView.findViewById(R.id.tv_goodsfinish_tc);
                        goodsFinishHolder.ib_goodsfinish_fz = (ImageButton) convertView.findViewById(R.id.ib_goodsfinish_fz);
                        goodsFinishHolder.ib_goodsfinish_fz.setVisibility(View.INVISIBLE);
                        goodsFinishHolder.tv_goodsfinish_goodsname.setText(dataList.get(position).getProject_name());
                        goodsFinishHolder.tv_goodsfinish_num.setText(dataList.get(position).getMnumber());
                        goodsFinishHolder.tv_goodsfinish_time.setText(dataList.get(position).getPtime());
                        goodsFinishHolder.tv_goodsfinish_goodsname1.setText(dataList.get(position).getProject_name());
                        goodsFinishHolder.tv_goodsfinish_price.setText(dataList.get(position).getMoney());
                        goodsFinishHolder.tv_goodsfinish_tc.setText(dataList.get(position).getTc_money());





                        convertView.setTag(goodsFinishHolder);
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
                        vipHolder.ib_ordervip_fz.setVisibility(View.INVISIBLE);

                        break;

                    case 1:

                        finishHolder = (OrderFinishViewHolder) convertView.getTag();
                        finishHolder.ib_orderfinish_fz.setVisibility(View.INVISIBLE);
                        if (dataList.get(position).getAdd_order().equals("1")){

                            finishHolder.tv_orderfinish_proname.setText(dataList.get(position).getProject_name());
                        }else{
                            finishHolder.tv_orderfinish_proname.setText(dataList.get(position).getProject_name()+"(加钟)");

                        }
                        finishHolder.tv_orderfinish_num.setText(dataList.get(position).getMnumber());
                        finishHolder.tv_orderfinish_ordertime.setText(dataList.get(position).getPtime());

                        finishHolder.tv_orderfinish_patype.setText(dataList.get(position).getType());
                        finishHolder.tv_orderfinish_tcprice.setText(dataList.get(position).getTc_money());
                        finishHolder.tv_orderfinish_jsprice.setText(dataList.get(position).getMoney());
                        finishHolder.tv_orderfinish_kkident.setText(dataList.get(position).getSet_type());

                        if (!TextUtils.isEmpty(dataList.get(position).getRoom_number())){

                            finishHolder.tv_orderfinish_housenum.setText(dataList.get(position).getRoom_number()+"房");
                        }else{
                            finishHolder.tv_orderfinish_housenum.setText("无");
                        }

                        break;

                    case 2:
                        goodsFinishHolder = (goodsOrderFinishHolder) convertView.getTag();
                        goodsFinishHolder.ib_goodsfinish_fz.setVisibility(View.INVISIBLE);
                        goodsFinishHolder.tv_goodsfinish_goodsname.setText(dataList.get(position).getProject_name());
                        goodsFinishHolder.tv_goodsfinish_num.setText(dataList.get(position).getMnumber());
                        goodsFinishHolder.tv_goodsfinish_time.setText(dataList.get(position).getPtime());
                        goodsFinishHolder.tv_goodsfinish_goodsname1.setText(dataList.get(position).getProject_name());
                        goodsFinishHolder.tv_goodsfinish_price.setText(dataList.get(position).getMoney());
                        goodsFinishHolder.tv_goodsfinish_tc.setText(dataList.get(position).getTc_money());

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
        TextView tv_orderfinish_housenum;

        ImageButton ib_orderfinish_fz;
    }

    class OrderVIPViewHolder{

        TextView tv_ordervip_num;
        TextView tv_ordervip_kktime;
        TextView tv_ordervip_viptype;
        TextView tv_ordervip_payprice;
        TextView tv_ordervip_tc;
        ImageButton ib_ordervip_fz;
    }

    class goodsOrderFinishHolder{

        TextView tv_goodsfinish_goodsname;
        TextView tv_goodsfinish_num;
        TextView tv_goodsfinish_time;
        TextView tv_goodsfinish_goodsname1;
        TextView tv_goodsfinish_price;
        TextView tv_goodsfinish_tc;
        ImageButton ib_goodsfinish_fz;
    }


}
