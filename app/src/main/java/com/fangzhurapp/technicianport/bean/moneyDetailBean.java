package com.fangzhurapp.technicianport.bean;

import java.io.Serializable;

/**
 * Created by android on 2016/7/25.
 */
public class moneyDetailBean implements Serializable{


    private String money;
    private String time;
    private String type;
    private String counter_fee;
    private String payment_onumber;
    private String cname;
    private String smoney;
    private String pay_channel;//支付渠道

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public String getSmoney() {
        return smoney;
    }

    public void setSmoney(String smoney) {
        this.smoney = smoney;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPayment_onumber() {
        return payment_onumber;
    }

    public void setPayment_onumber(String payment_onumber) {
        this.payment_onumber = payment_onumber;
    }

    public String getCounter_fee() {
        return counter_fee;
    }

    public void setCounter_fee(String counter_fee) {
        this.counter_fee = counter_fee;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
