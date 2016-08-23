package com.fangzhurapp.technicianport.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fangzhurapp.technicianport.R;

/**
 * Created by android on 2016/8/10.
 */
public class DelShopmgDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private int mLayout;
    private delShopmg mListener;

    public DelShopmgDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public DelShopmgDialog(Context context, int layout) {
        super(context);
        this.mContext = context;
        this.mLayout = layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, mLayout, null);
        RelativeLayout rl_delshopmg_yes = (RelativeLayout) view.findViewById(R.id.rl_delshopmg_yes);
        RelativeLayout rl_delshopmg_no = (RelativeLayout) view.findViewById(R.id.rl_delshopmg_no);
        rl_delshopmg_yes.setOnClickListener(this);
        rl_delshopmg_no.setOnClickListener(this);
        setContentView(view);

        setCanceledOnTouchOutside(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_delshopmg_no:
                DelShopmgDialog.this.dismiss();
                break;

            case R.id.rl_delshopmg_yes:
                if (mListener != null){
                    mListener.setConfirmListener();
                }
                break;
        }
    }

    public void setDelShopMgListener(delShopmg listener){

        this.mListener = listener;
    }

    public interface delShopmg{

        /**
         * 确定
         */
        void setConfirmListener();
    }
}
