package com.fangzhurapp.technicianport.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.activity.SetPayPWActivity;
import com.jungly.gridpasswordview.GridPasswordView;

/**
 * Created by android on 2016/7/20.
 */
public class PayPwDialog extends Dialog {
    private Context mContext;
    private int mLayout;
    private String mPrice;
    private payListener mListener;

    public PayPwDialog(Context context) {
        super(context);
    }

    public PayPwDialog(Context context, int layout,String data){
        super(context);
        this.mContext = context;
        this.mLayout = layout;
        this.mPrice = data;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, mLayout, null);

        TextView tv_paypwdialog_price = (TextView) view.findViewById(R.id.tv_paypwdialog_price);

        TextView tv_paypwdialog_forgetpw = (TextView) view.findViewById(R.id.tv_paypwdialog_forgetpw);

        GridPasswordView gpv_paypwdialog_pw = (GridPasswordView) view.findViewById(R.id.gpv_paypwdialog_pw);

        ImageButton ib_paypwdialog_close = (ImageButton) view.findViewById(R.id.ib_paypwdialog_close);
        tv_paypwdialog_price.setText("￥"+mPrice);
        ib_paypwdialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPwDialog.this.dismiss();
            }
        });

        gpv_paypwdialog_pw.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (psw.length() == 6){

                    if (mListener != null){
                        mListener.txListener(mPrice,psw);
                    }
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });

        tv_paypwdialog_forgetpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (mListener != null){
                   mListener.forgetPw();
               }
            }
        });



        setContentView(view);

        setCanceledOnTouchOutside(false);

    }

   public void setPayPwDialogListener(payListener listener){
       this.mListener = listener;
   }

    public interface payListener{
        /**
         * 提现
         */
        void txListener(String price,String pw);

        /**
         * 忘记密码
         */
        void forgetPw();
    }
}
