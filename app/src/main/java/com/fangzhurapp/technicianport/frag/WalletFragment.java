package com.fangzhurapp.technicianport.frag;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.MainActivity;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.activity.BossMoneyDetailActivity;
import com.fangzhurapp.technicianport.activity.MyWalletActivity;
import com.fangzhurapp.technicianport.activity.SettingActivity;
import com.fangzhurapp.technicianport.activity.TXPriceActivity;
import com.fangzhurapp.technicianport.eventbus.MsgEvent;
import com.fangzhurapp.technicianport.eventbus.MsgEvent1;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import android.Manifest;
import android.widget.Toast;

public class WalletFragment extends Fragment implements OnRefreshListener,View.OnClickListener{

    private static final String TAG = "WalletFragment";
    private View view;
    private String[] text = new String[]{"可提现金额","结算中的金额","我的工资","推荐店铺送百元红包"};
    private int[] img = new int[]{R.drawable.wallet_ktxmoney,R.drawable.wallet_jsmoney,R.drawable.wallet_wage,R.drawable.wallet_share};
    private GridView swipe_target;
    private String[] stringArr ;
    private ImageView mTitleRight;
    private Context mContext;
    private SwipeToLoadLayout swipe_wallet;
    private ImageView img_title_indicator;
    private TextView tv_shopname;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mContext =(MainActivity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: "+"WalletFragment");
        EventBus.getDefault().register(this);
        view = inflater.inflate(R.layout.fragment_wallet, container, false);


        initView();
        initEvent();
        return view;
    }





    private void initEvent() {
        mTitleRight.setOnClickListener(this);
        swipe_target.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        Intent intent = new Intent(mContext, TXPriceActivity.class);

                       TextView tv_gvwallet_price = (TextView) parent.getChildAt(position).findViewById(R.id.tv_gvwallet_price);
                        if (tv_gvwallet_price != null){

                            SpUtil.putString(mContext,"ktxprice",tv_gvwallet_price.getText().toString());
                        }
                        startActivity(intent);
                        break;

                    case 2:
                        Intent intent1 = new Intent(mContext, MyWalletActivity.class);
                        startActivity(intent1);
                        break;

                    case 3:
                        //分享
                        share();
                        break;
                }
            }
        });
    }

    private void share() {

        UMImage umImage = new UMImage(mContext, BitmapFactory.decodeResource(getResources(), R.drawable.share_logo));

        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                };


        new ShareAction(getActivity()).setDisplayList(displaylist)
                .setListenerList(umShareListener)
               // .setShareboardclickCallback(shareBoardlistener)
                .withMedia(umImage)
                .withText("足浴养生行业领先的移动智能服务平台，一天只要1块钱，一年省下10万元！")
                .withTitle("记钟宝——足浴人的生财神器")
                .withTargetUrl("http://www.fangzhur.com/public/download.php")
                .open();



    }
   /* private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {
        @Override
        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

            if (share_media.equals("WEIXIN")){
                Toast.makeText(mContext, "微信好友", Toast.LENGTH_SHORT).show();

            }else if (share_media.equals("WEIXIN_CIRCLE")){
                Toast.makeText(mContext, "微信朋友圈", Toast.LENGTH_SHORT).show();
            }
        }
    };*/

    private UMShareListener umShareListener =new UMShareListener(){

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            Toast.makeText(mContext, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            Toast.makeText(mContext, "分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            Toast.makeText(mContext, "取消分享", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mContext).onActivityResult( requestCode, resultCode, data);
    }

    private void initView() {

        swipe_target = (GridView) view.findViewById(R.id.swipe_target);
        mTitleRight = (ImageView) view.findViewById(R.id.img_title_right);
        mTitleRight.setBackgroundResource(R.drawable.img_setting);
        swipe_wallet = (SwipeToLoadLayout) view.findViewById(R.id.swipe_wallet);
        img_title_indicator = (ImageView) view.findViewById(R.id.img_title_indicator);
        tv_shopname = (TextView) view.findViewById(R.id.tv_shopname);
        tv_shopname.setText(SpUtil.getString(mContext,"shopname",""));
        img_title_indicator.setVisibility(View.INVISIBLE);

        swipe_wallet.setOnRefreshListener(this);
        swipe_wallet.setRefreshing(true);

        swipe_target.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return text.length;
            }

            @Override
            public Object getItem(int position) {
                return text[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                convertView = View.inflate(mContext,R.layout.item_walletgv,null);
                TextView tv_gvwallet_type = (TextView) convertView.findViewById(R.id.tv_gvwallet_type);
                TextView tv_gvwallet_price = (TextView) convertView.findViewById(R.id.tv_gvwallet_price);
                ImageView img_gvwallet = (ImageView) convertView.findViewById(R.id.img_gvwallet);

                if (position ==text.length -1 ){
                    img_gvwallet.setBackgroundResource(img[img.length-1]);
                    tv_gvwallet_price.setText("推荐有礼");
                    tv_gvwallet_type.setText("推荐店铺送百元红包");
                    tv_gvwallet_type.setTextSize(13F);
                    tv_gvwallet_type.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.gvwallet_textcolor1));
                }else{

                    img_gvwallet.setBackgroundResource(img[position]);
                    tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.gvwallet_textcolor));
                    tv_gvwallet_type.setText(text[position]);
                    tv_gvwallet_price.setText("0.0");
                }
                return convertView;
            }
        });





    }
    @Subscribe
    public void onEventMainThread(MsgEvent msg) {

        tv_shopname.setText(msg.getStringMsg());
    }

    @Subscribe
    public void onEventMainThread(MsgEvent1 msg) {
        swipe_wallet.setRefreshing(msg.getBooleanMsg());

    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.WALLET_HOME){
                swipe_wallet.setRefreshing(false);
                LogUtil.d(TAG,response.toString());

                JSONObject jsonObject = response.get();
                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")){
                        stringArr = new String[3];
                        JSONObject data = jsonObject.getJSONObject("data");
                        String jsz = data.getString("jsz");
                        String ktxye = data.getString("ktxye");
                        stringArr[1] = jsz;
                        stringArr[0] = ktxye;
                        String gz = data.getString("gz");
                        stringArr[2] = gz;


                        swipe_target.setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return text.length;
                            }

                            @Override
                            public Object getItem(int position) {
                                return text[position];
                            }

                            @Override
                            public long getItemId(int position) {
                                return position;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {

                                convertView = View.inflate(mContext,R.layout.item_walletgv,null);
                                TextView tv_gvwallet_type = (TextView) convertView.findViewById(R.id.tv_gvwallet_type);
                                TextView tv_gvwallet_price = (TextView) convertView.findViewById(R.id.tv_gvwallet_price);
                                ImageView img_gvwallet = (ImageView) convertView.findViewById(R.id.img_gvwallet);

                                if (position ==text.length -1 ){
                                    img_gvwallet.setBackgroundResource(img[img.length-1]);
                                    tv_gvwallet_price.setText("推荐有礼");
                                    tv_gvwallet_type.setText("推荐店铺送百元红包");
                                    tv_gvwallet_type.setTextSize(13F);
                                    tv_gvwallet_type.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.gvwallet_textcolor1));
                                }else{

                                    img_gvwallet.setBackgroundResource(img[position]);
                                    tv_gvwallet_price.setTextColor(CustomApplication.getInstance().getResources().getColor(R.color.gvwallet_textcolor));
                                    tv_gvwallet_type.setText(text[position]);
                                    tv_gvwallet_price.setText(stringArr[position]);
                                }
                                return convertView;
                            }
                        });

                    }else{


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            swipe_wallet.setRefreshing(false);
        }
    };


    @Override
    public void onRefresh() {
        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.WALLET_HOME, RequestMethod.POST);
        jsonObjectRequest.add("id", SpUtil.getString(mContext,"id",""));
        CallServer.getInstance().add(mContext,jsonObjectRequest,callback, UrlTag.WALLET_HOME,false,false,true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_title_right:

                Intent intent = new Intent(mContext, SettingActivity.class);
                startActivity(intent);

                break;
        }
    }


    public void changeShopName(String shopname){

        tv_shopname.setText(shopname);
    }

    public void refresh(){

        swipe_wallet.setRefreshing(true);
    }


}
