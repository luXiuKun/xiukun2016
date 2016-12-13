package com.fangzhurapp.technicianport.eventbus;

/**
 * Created by android on 2016/8/17.
 */
public class PartnerPayMsgEvent {
    private Boolean msg;

    public PartnerPayMsgEvent(Boolean msg){
        this.msg = msg;
    };



    public Boolean getBooleanMsg(){

        return msg;
    }

}
