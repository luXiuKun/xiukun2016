package com.fangzhurapp.technicianport.bean;

import java.io.Serializable;

/**
 * Created by android on 2016/7/27.
 */
public class MessageBean implements Serializable{

    private String id;
    private String onumber_id;
    private String onumber;
    private String project;
    private String state;
    private String onclick;
    private String remarks;
    private String time;
    private String staff_id;
    private String addtime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOnumber_id() {
        return onumber_id;
    }

    public void setOnumber_id(String onumber_id) {
        this.onumber_id = onumber_id;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
