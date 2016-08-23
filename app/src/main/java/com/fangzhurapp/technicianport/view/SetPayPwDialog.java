package com.fangzhurapp.technicianport.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fangzhurapp.technicianport.R;

/**
 * Created by android on 2016/7/20.
 */
public class SetPayPwDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private int mLayout;

    public SetPayPwDialog(Context context) {
        super(context);
    }

    public SetPayPwDialog(Context context, int layout){
        super(context);
        this.mContext = context;
        this.mLayout = layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, mLayout, null);

        ImageView img_changepaypw_close = (ImageView) view.findViewById(R.id.img_changepaypw_close);
        img_changepaypw_close.setOnClickListener(this);
        setContentView(view);

        setCanceledOnTouchOutside(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_changepaypw_close:
                SetPayPwDialog.this.dismiss();
                break;


        }
    }


}
