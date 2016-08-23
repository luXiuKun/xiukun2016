package com.fangzhurapp.technicianport.bean;


import java.io.Serializable;

/**
 * Created by android on 2016/6/12.
 */
public class BossSJYSBean implements Serializable{

    private String id;
    private String money;//结算金额
    private String onumber;
    private String project;
    private String staff_id;
    private String staff_name;//技师姓名
    private String stime;
    private String type;
    private String xm_name;//项目名称
    private String pmethod;
    private String staff;
    private String fwtime;
    private String set_type;
    private String ygtc;

    public String getYgtc() {
        return ygtc;
    }

    public void setYgtc(String ygtc) {
        this.ygtc = ygtc;
    }

    public String getSet_type() {
        return set_type;
    }

    public void setSet_type(String set_type) {
        this.set_type = set_type;
    }

    public String getFwtime() {
        return fwtime;
    }

    public void setFwtime(String fwtime) {
        this.fwtime = fwtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOnumber() {
        return onumber;
    }

    public void setOnumber(String onumber) {
        this.onumber = onumber;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getXm_name() {
        return xm_name;
    }

    public void setXm_name(String xm_name) {
        this.xm_name = xm_name;
    }

    public String getPmethod() {
        return pmethod;
    }

    public void setPmethod(String pmethod) {
        this.pmethod = pmethod;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }
}
