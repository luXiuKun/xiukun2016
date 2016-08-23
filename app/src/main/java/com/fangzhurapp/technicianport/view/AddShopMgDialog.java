package com.fangzhurapp.technicianport.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fangzhurapp.technicianport.R;
import com.fangzhurapp.technicianport.utils.SpUtil;

/**
 * Created by android on 2016/8/9.
 */
public class AddShopMgDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private int layout;
    private EditText et_addshopmg_name;
    private EditText et_addshopmg_phone;
    private ImageButton ib_addshopmg_submit;
    private AddShopMg mListener;
    private String mState;


    public AddShopMgDialog(Context context) {
        super(context);
    }

    public AddShopMgDialog(Context context,int layout,String state) {
        super(context);
        this.mContext = context;
        this.layout = layout;
        this.mState = state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = View.inflate(mContext, layout, null);
        TextView tv_addshopmg_title = (TextView) view.findViewById(R.id.tv_addshopmg_title);

        if (mState.equals("2")){
            tv_addshopmg_title.setText("添加股东");
        }else{
            tv_addshopmg_title.setText("添加店长");
        }
        et_addshopmg_name = (EditText) view.findViewById(R.id.et_addshopmg_name);
        et_addshopmg_phone = (EditText) view.findViewById(R.id.et_addshopmg_phone);
        EditText et_addshopmg_shopname = (EditText) view.findViewById(R.id.et_addshopmg_shopname);
        ib_addshopmg_submit = (ImageButton) view.findViewById(R.id.ib_addshopmg_submit);
        ib_addshopmg_submit.setOnClickListener(this);
        et_addshopmg_shopname.setText(SpUtil.getString(mContext,"shopname",""));

        setContentView(view);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.ib_addshopmg_submit:

                if (!TextUtils.isEmpty(et_addshopmg_name.getText().toString())
                        && !TextUtils.isEmpty(et_addshopmg_phone.getText().toString())
                        && et_addshopmg_phone.getText().toString().length() == 11){
                    if (mListener != null){

                        mListener.subMItData(et_addshopmg_name.getText().toString(),et_addshopmg_phone.getText().toString(),SpUtil.getString(mContext,"shopname",""));
                    }
                }else{

                   if (TextUtils.isEmpty(et_addshopmg_name.getText().toString())){
                       Toast.makeText(mContext, "姓名请输入", Toast.LENGTH_SHORT).show();
                   }else if (TextUtils.isEmpty(et_addshopmg_phone.getText().toString())){
                       Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();
                   }else if (et_addshopmg_phone.getText().toString().length() < 11){
                       Toast.makeText(mContext, "手机号不合法", Toast.LENGTH_SHORT).show();
                   }
                }
                break;

        }
    }

    public void setAddShopMg(AddShopMg listener){

        this.mListener = listener;
    }



  public interface AddShopMg{


        void  subMItData(String name,String phone,String shopname);

    }


}
