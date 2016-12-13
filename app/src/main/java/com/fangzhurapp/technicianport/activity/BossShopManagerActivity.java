package com.fangzhurapp.technicianport.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.adapter.MenuAdapter;
import com.fangzhurapp.technicianport.bean.ShopMgBean;
import com.fangzhurapp.technicianport.http.CallServer;
import com.fangzhurapp.technicianport.http.HttpCallBack;
import com.fangzhurapp.technicianport.http.UrlConstant;
import com.fangzhurapp.technicianport.http.UrlTag;
import com.fangzhurapp.technicianport.listener.OnItemClickListener;
import com.fangzhurapp.technicianport.utils.LogUtil;
import com.fangzhurapp.technicianport.utils.SpUtil;
import com.fangzhurapp.technicianport.view.AddShopMgDialog;
import com.fangzhurapp.technicianport.view.DelShopmgDialog;
import com.fangzhurapp.technicianport.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BossShopManagerActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.img_logo)
    ImageView imgLogo;
    @Bind(R.id.tv_shopname)
    TextView tvShopname;
    @Bind(R.id.img_title_indicator)
    ImageView imgTitleIndicator;
    @Bind(R.id.img_title_right)
    ImageView imgTitleRight;
    @Bind(R.id.smr_bossshopmg)
    SwipeMenuRecyclerView smrBossshopmg;
    private static final String TAG = "BossShopManagerActivity";
    @Bind(R.id.rl_bossshopmg_nodata)
    RelativeLayout rlBossshopmgNodata;
    private AddShopMgDialog addShopMgDialog;
    private ArrayList<ShopMgBean> dataList;
    private MenuAdapter menuAdapter;
    private DelShopmgDialog delShopmgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boss_shop_manager);
        ButterKnife.bind(this);
        CustomApplication.addAct(this);
        initView();
        initEvent();
    }

    private void initEvent() {
        imgLogo.setOnClickListener(this);
        imgTitleRight.setOnClickListener(this);

    }

    private void initView() {
        imgLogo.setBackgroundResource(R.drawable.img_title_back);
        tvShopname.setText("店长管理");
        imgTitleIndicator.setVisibility(View.INVISIBLE);
        imgTitleRight.setBackgroundResource(R.drawable.img_addbankcard);

        //smrBossshopmg.setItemViewSwipeEnabled(true);
        smrBossshopmg.setItemAnimator(new DefaultItemAnimator());
        smrBossshopmg.setHasFixedSize(true);
        smrBossshopmg.setLayoutManager(new LinearLayoutManager(this));
        //smrBossshopmg.addItemDecoration(new ListViewDecoration());
        if (SpUtil.getString(BossShopManagerActivity.this,"shenfen","").equals("1")){

            smrBossshopmg.setSwipeMenuCreator(swipeMenuCreator);
        }else{

        }
        //smrBossshopmg.openRightMenu(0);
        smrBossshopmg.setSwipeMenuItemClickListener(new OnSwipeMenuItemClickListener() {
            @Override
            public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {

                if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {


                    delShopMg(adapterPosition);

                }
            }
        });


        getShopmg();

    }

    /**
     * 删除店长
     *
     * @param adapterPosition
     */
    private void delShopMg(final int adapterPosition) {

        delShopmgDialog = new DelShopmgDialog(BossShopManagerActivity.this, R.layout.dialog_delshopmg);

        delShopmgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delShopmgDialog.show();

        delShopmgDialog.setDelShopMgListener(new DelShopmgDialog.delShopmg() {
            @Override
            public void setConfirmListener() {

                Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_DEL_SHOPMG_GD, RequestMethod.POST);
                jsonObjectRequest.add("sid", SpUtil.getString(BossShopManagerActivity.this, "sid", ""));
                jsonObjectRequest.add("bid", SpUtil.getString(BossShopManagerActivity.this, "id", ""));
                jsonObjectRequest.add("id", dataList.get(adapterPosition).getId());
                CallServer.getInstance().add(BossShopManagerActivity.this, jsonObjectRequest, callback, UrlTag.BOSS_DEL_SHOP_GD, true, false, true);

                dataList.remove(adapterPosition);
                menuAdapter.notifyDataSetChanged();
                delShopmgDialog.dismiss();
            }
        });


    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int size = getResources().getDimensionPixelSize(R.dimen.item_height);
            SwipeMenuItem delItem = new SwipeMenuItem(BossShopManagerActivity.this)
                    .setBackgroundDrawable(R.drawable.selector_red)

                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(size)
                    .setHeight(size);

            swipeRightMenu.addMenuItem(delItem);

        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
        }
    };

    private void getShopmg() {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_SHOPMG_GD, RequestMethod.POST);

        jsonObjectRequest.add("sid", SpUtil.getString(BossShopManagerActivity.this, "sid", ""));
        jsonObjectRequest.add("status", "3");

        CallServer.getInstance().add(BossShopManagerActivity.this, jsonObjectRequest, callback, UrlTag.BOSS_SHOPMG_GD, true, false, true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_logo:
                BossShopManagerActivity.this.finish();
                break;

            case R.id.img_title_right:
                if (SpUtil.getString(BossShopManagerActivity.this,"shenfen","").equals("1")){

                    showDialog();
                }else{
                    Toast.makeText(BossShopManagerActivity.this, "无操作权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showDialog() {

        addShopMgDialog = new AddShopMgDialog(BossShopManagerActivity.this, R.layout.dialog_addshopmg, "3");
        addShopMgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addShopMgDialog.show();

        addShopMgDialog.setAddShopMg(new AddShopMgDialog.AddShopMg() {
            @Override
            public void subMItData(String name, String phone, String shopname) {

                addShopmg(name, phone);

            }
        });
    }

    private void addShopmg(String name, String phone) {

        Request<JSONObject> jsonObjectRequest = NoHttp.createJsonObjectRequest(UrlConstant.BOSS_ADD_SHOPMG, RequestMethod.POST);

        jsonObjectRequest.add("name", name);
        jsonObjectRequest.add("account", phone);
        jsonObjectRequest.add("sid", SpUtil.getString(BossShopManagerActivity.this, "sid", ""));
        jsonObjectRequest.add("id", SpUtil.getString(BossShopManagerActivity.this, "id", ""));
        jsonObjectRequest.add("status", "3");

        CallServer.getInstance().add(BossShopManagerActivity.this, jsonObjectRequest, callback, UrlTag.BOSS_ADD_SHOPMG, true, false, true);
    }


    private HttpCallBack<JSONObject> callback = new HttpCallBack<JSONObject>() {
        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            if (what == UrlTag.BOSS_ADD_SHOPMG) {


                LogUtil.d(TAG, response.toString());
                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");
                    if (sucess.equals("1")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        String isok = data.getString("isok");
                        if (isok.equals("1")) {


                            addShopMgDialog.dismiss();
                            Toast.makeText(BossShopManagerActivity.this, "添加成功", Toast.LENGTH_SHORT).show();

                            getShopmg();
                        }


                    } else {

                        JSONObject msg = jsonObject.getJSONObject("msg");

                        String fanhui = msg.getString("fanhui");
                        Toast.makeText(BossShopManagerActivity.this, fanhui, Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (what == UrlTag.BOSS_SHOPMG_GD) {


                LogUtil.d(TAG, response.toString());
                JSONObject jsonObject = response.get();

                try {
                    String sucess = jsonObject.getString("sucess");

                    if (sucess.equals("1")) {
                        JSONArray data = jsonObject.getJSONArray("data");

                        if (data.length() > 0) {
                            dataList = new ArrayList<>();

                            for (int i = 0; i < data.length(); i++) {

                                ShopMgBean shopMgBean = new ShopMgBean();

                                shopMgBean.setId(data.getJSONObject(i).getString("id"));
                                shopMgBean.setName(data.getJSONObject(i).getString("name"));
                                shopMgBean.setAccount(data.getJSONObject(i).getString("account"));

                                dataList.add(shopMgBean);

                            }

                            menuAdapter = new MenuAdapter(dataList);
                            menuAdapter.setOnItemClickListener(onItemClickListener);
                            smrBossshopmg.setAdapter(menuAdapter);

                        }


                    } else {

                        rlBossshopmgNodata.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (what == UrlTag.BOSS_DEL_SHOP_GD) {


                LogUtil.d(TAG, response.toString());
            }
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {

        }
    };


}
