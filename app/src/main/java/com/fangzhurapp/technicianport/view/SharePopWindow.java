package com.fangzhurapp.technicianport.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.CustomApplication;
import com.fangzhurapp.technicianport.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by android on 2016/7/28.
 */
public class SharePopWindow extends PopupWindow implements View.OnClickListener{
    private  Activity mAct;
    private View view;
    private LinearLayout ll_wx,ll_wx_friends;
    private Context mContext;
    private static SharePopWindow instance;

    public synchronized static SharePopWindow getInstance(Activity mAct,Context context, int layout){

        if (instance == null){

            instance = new SharePopWindow(mAct,context,layout);
        }

        return instance;
    };

    private SharePopWindow(Activity mAct,Context context, int layout){
        this.mContext = context;
        this.mAct = mAct;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(layout,null);

        initView();
    }

    private void initView() {

        ll_wx = (LinearLayout) view.findViewById(R.id.ll_wx);
        ll_wx_friends = (LinearLayout) view.findViewById(R.id.ll_wx_friends);
        ll_wx.setOnClickListener(this);
        ll_wx_friends.setOnClickListener(this);
        setContentView(view);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        //this.setAnimationStyle(R.style.share_popupwindow_anim);

        ColorDrawable dw = new ColorDrawable(0x90000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dismiss();
                return true;
            }
        });
    }


    public void showPopupWindow(View v){

        if (!this.isShowing()){
            this.showAtLocation(v, Gravity.BOTTOM,0,0);
        }else{
            this.dismiss();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ll_wx:

               /*  UMImage umImage = new UMImage(mContext, BitmapFactory.decodeResource(getResources(), R.drawable.share_logo));

        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                };*/

                UMImage umImage = new UMImage(mContext, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_logo));
                new ShareAction(mAct)
                 .setCallback(umShareListener)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .withText("足浴养生行业领先的移动智能服务平台，一天只要1块钱，一年省下10万元！")
                .withTitle("记钟宝——足浴人的生财神器")
                .withMedia(umImage)
                .withTargetUrl("http://www.fangzhur.com/public/download.php")
                .share();
                break;

            case R.id.ll_wx_friends:

                UMImage umImage1 = new UMImage(mContext, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.app_logo));
                new ShareAction(mAct)
                        .setCallback(umShareListener)
                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withText("足浴养生行业领先的移动智能服务平台，一天只要1块钱，一年省下10万元！")
                        .withTitle("记钟宝——足浴人的生财神器")
                        .withMedia(umImage1)
                        .withTargetUrl("http://www.fangzhur.com/public/download.php")
                        .share();
                break;

        }
    }

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
}
