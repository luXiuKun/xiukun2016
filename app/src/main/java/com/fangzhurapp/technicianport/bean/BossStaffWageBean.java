package com.fangzhurapp.technicianport.bean;

import java.io.Serializable;

/**
 * Created by android on 2016/6/27.
 */
public class BossStaffWageBean implements Serializable{

    private String id;
    private String name;
    private String id_number;
    private String dmoney;
    private String jmoney;
    private String fmoney;
    private String typea;
    private String typeb;
    private String types;
    private String money;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getDmoney() {
        return dmoney;
    }

    public void setDmoney(String dmoney) {
        this.dmoney = dmoney;
    }

    public String getJmoney() {
        return jmoney;
    }

    public void setJmoney(String jmoney) {
        this.jmoney = jmoney;
    }

    public String getFmoney() {
        return fmoney;
    }

    public void setFmoney(String fmoney) {
        this.fmoney = fmoney;
    }

    public String getTypea() {
        return typea;
    }

    public void setTypea(String typea) {
        this.typea = typea;
    }

    public String getTypeb() {
        return typeb;
    }

    public void setTypeb(String typeb) {
        this.typeb = typeb;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
