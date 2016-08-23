package com.fangzhurapp.technicianport.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fangzhurapp.technicianport.R;

/**
 * Created by android on 2016/7/20.
 */
public class CancelBindDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private int mLayout;
    private CancelBindListener mListener;

    public CancelBindDialog(Context context) {
        super(context);
    }

    public CancelBindDialog(Context context,int layout){
        super(context);
        this.mContext = context;
        this.mLayout = layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, mLayout, null);
        RelativeLayout rl_cancelbind_yes = (RelativeLayout) view.findViewById(R.id.rl_cancelbind_yes);
        RelativeLayout rl_cancelbind_no = (RelativeLayout) view.findViewById(R.id.rl_cancelbind_no);
        rl_cancelbind_yes.setOnClickListener(this);
        rl_cancelbind_no.setOnClickListener(this);
        setContentView(view);

        setCanceledOnTouchOutside(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_cancelbind_no:
                CancelBindDialog.this.dismiss();
                break;

            case R.id.rl_cancelbind_yes:
                if (mListener != null){
                    mListener.setConfirmListener();
                }
                break;
        }
    }

    public void setCancelBindListener(CancelBindListener listener){

        this.mListener = listener;
    }

    public interface CancelBindListener{

        /**
         * 确定
         */
        void setConfirmListener();
    }
}
