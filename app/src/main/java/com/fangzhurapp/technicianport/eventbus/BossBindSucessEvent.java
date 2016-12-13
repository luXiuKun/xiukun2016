package com.fangzhurapp.technicianport.eventbus;

/**
 * Created by android on 2016/8/17.
 */
public class BossBindSucessEvent {
    private Boolean msg;

    public BossBindSucessEvent(Boolean msg){
        this.msg = msg;
    };



    public Boolean getBooleanMsg(){

        return msg;
    }

}
