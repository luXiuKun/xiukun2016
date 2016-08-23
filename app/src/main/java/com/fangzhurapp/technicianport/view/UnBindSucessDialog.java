package com.fangzhurapp.technicianport.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by android on 2016/7/18.
 */
public class UnBindSucessDialog extends Dialog {
    private Context mContext;
    private int mLayout;

    public UnBindSucessDialog(Context context) {
        super(context);
    }

    public UnBindSucessDialog(Context context, int layout){
        super(context);
        this.mContext = context;
        this.mLayout = layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(mContext, mLayout, null);


        setContentView(view);
    }
}
