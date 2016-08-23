package com.fangzhurapp.technicianport.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.fangzhurapp.technicianport.R;

/**
 * Created by android on 2016/7/20.
 */
public class BindBankDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private int mLayout;
    private CancelBindListener mListener;

    public BindBankDialog(Context context) {
        super(context);
    }

    public BindBankDialog(Context context, int layout){
        super(context);
        this.mContext = context;
        this.mLayout = layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, mLayout, null);
        ImageButton ib_bindcarddialog = (ImageButton) view.findViewById(R.id.ib_bindcarddialog);
        ib_bindcarddialog.setOnClickListener(this);
        setContentView(view);

        setCanceledOnTouchOutside(true);

    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){

        case R.id.ib_bindcarddialog:
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
