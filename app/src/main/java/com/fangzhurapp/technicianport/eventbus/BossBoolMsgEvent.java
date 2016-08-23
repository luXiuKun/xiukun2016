package com.fangzhurapp.technicianport.eventbus;

/**
 * Created by android on 2016/8/17.
 */
public class BossBoolMsgEvent {
    private Boolean msg;

    public BossBoolMsgEvent(Boolean msg){
        this.msg = msg;
    };



    public Boolean getBooleanMsg(){

        return msg;
    }

}
