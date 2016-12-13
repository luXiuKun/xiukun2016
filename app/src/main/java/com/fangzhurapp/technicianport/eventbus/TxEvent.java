package com.fangzhurapp.technicianport.eventbus;

/**
 * Created by android on 2016/8/17.
 */
public class TxEvent {
    private Boolean msg;

    public TxEvent(Boolean msg){
        this.msg = msg;
    };



    public Boolean getBooleanMsg(){

        return msg;
    }

}
