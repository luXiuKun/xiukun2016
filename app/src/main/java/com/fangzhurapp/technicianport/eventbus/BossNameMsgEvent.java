package com.fangzhurapp.technicianport.eventbus;

/**
 * Created by android on 2016/8/17.
 */
public class BossNameMsgEvent {
    private String msg;

    public BossNameMsgEvent(String msg){
        this.msg = msg;
    };




    public String getStringMsg(){

        return msg;
    }



}
