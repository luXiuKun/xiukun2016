package com.fangzhurapp.technicianport.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by android on 2016/7/19.
 */
public class MessageReceiver extends BroadcastReceiver {
    public static newOrderListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mListener != null){

            mListener.sendNewOrderMessage();
        }

    }

    public interface newOrderListener{

        void sendNewOrderMessage();
    }

    public void setNewOrderListener(newOrderListener listener){

        this.mListener = listener;
    }


}
