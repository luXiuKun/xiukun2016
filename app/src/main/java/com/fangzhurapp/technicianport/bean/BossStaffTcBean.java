package com.fangzhurapp.technicianport.bean;

import java.io.Serializable;

/**
 * Created by android on 2016/6/12.
 */
public class BossStaffTcBean implements Serializable{

    private String pztc;
    private String dztc;
    private String tc_count;
    private String id_number;
    private String kktc;
    private String name;

    public String getPztc() {
        return pztc;
    }

    public void setPztc(String pztc) {
        this.pztc = pztc;
    }

    public String getDztc() {
        return dztc;
    }

    public void setDztc(String dztc) {
        this.dztc = dztc;
    }

    public String getTc_count() {
        return tc_count;
    }

    public void setTc_count(String tc_count) {
        this.tc_count = tc_count;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getKktc() {
        return kktc;
    }

    public void setKktc(String kktc) {
        this.kktc = kktc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
