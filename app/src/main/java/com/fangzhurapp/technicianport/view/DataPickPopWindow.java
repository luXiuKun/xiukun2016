package com.fangzhurapp.technicianport.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;

/**
 * Created by android on 2016/7/28.
 */
public class DataPickPopWindow extends PopupWindow implements View.OnClickListener{
    private View view;
    private DatePicker data_picker;
    private TextView btn_datapicker_cancel;
    private TextView btn_datapicker_finish;
    private DataPickListener mListener;
    private static DataPickPopWindow instance;
    private Context mContext;

    public synchronized static DataPickPopWindow getInstance(Context context,int layout){

        if (instance == null){

            instance = new DataPickPopWindow(context,layout);
        }

        return instance;
    }

    private DataPickPopWindow(Context context,int layout){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        view = inflater.inflate(layout,null);
        initView();

    }

    private void initView() {

        data_picker = (DatePicker) view.findViewById(R.id.data_picker);
        btn_datapicker_cancel = (TextView) view.findViewById(R.id.btn_datapicker_cancel);
        btn_datapicker_finish = (TextView) view.findViewById(R.id.btn_datapicker_finish);
        btn_datapicker_cancel.setOnClickListener(this);
        btn_datapicker_finish.setOnClickListener(this);
        setContentView(view);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        float height = mContext.getResources().getDimension(R.dimen.dd_dimen_330px);

        setHeight((int)height);
        setOutsideTouchable(false);
       setFocusable(true);
        this.setAnimationStyle(R.style.popupwindow_anim);

        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        ColorDrawable dw = new ColorDrawable(0x90000000);
        //this.setBackgroundDrawable(new PaintDrawable());
        this.setBackgroundDrawable(dw);

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK){
                    DataPickPopWindow.this.dismiss();

                    return  true;
                }
                return false;
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
            //取消
            case R.id.btn_datapicker_cancel:
                this.dismiss();
                break;

            //完成
            case R.id.btn_datapicker_finish:
                if (mListener != null){
                    String[] date = data_picker.getDate();
                    String join = TextUtils.join("-", date);
                    mListener.onFinish(join);
                }
                break;

        }
    }

    public void setDataPick(DataPickListener listener){
        this.mListener = listener;
    }


    public interface  DataPickListener{


        /**
         * 确认时间
         */
        void onFinish(String time);
    }
}
