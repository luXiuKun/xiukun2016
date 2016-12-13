package com.fangzhurapp.technicianport.eventbus;

/**
 * Created by android on 2016/8/17.
 */
public class BossPerformanceMsgEvent {
    private Boolean msg;

    public BossPerformanceMsgEvent(Boolean msg){
        this.msg = msg;
    };



    public Boolean getBooleanMsg(){

        return msg;
    }

}
