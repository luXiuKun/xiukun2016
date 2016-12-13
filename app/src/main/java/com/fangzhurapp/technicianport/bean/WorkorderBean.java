package com.fangzhurapp.technicianport.bean;

/**
 * Created by android on 2016/7/15.
 */
public class WorkorderBean {

    private String id;
    private String mnumber;//订单号
    private String money;//结算金额
    private String o_type;//订单类型
    private String project_name;//项目类型
    private String ptime;//下单时间
    private String tc_money;//提成
    private String type;//排钟类型
    private String set_type;//身份


    private String addtime;//开卡时间
    private String k_type;//会员卡种类
    private String k_money;//支付金额



    private String onumber;//进行中订单编号
    private String smoney;//标准价

    private String add_order;//是否加钟；大于1都是加钟

    private String room_id;//房间号
    private String room_number;

    private String items_type;


    public String getItems_type() {
        return items_type;
    }

    public void setItems_type(String items_type) {
        this.items_type = items_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMnumber() {
        return mnumber;
    }

    public void setMnumber(String mnumber) {
        this.mnumber = mnumber;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getO_type() {
        return o_type;
    }

    public void setO_type(String o_type) {
        this.o_type = o_type;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getTc_money() {
        return tc_money;
    }

    public void setTc_money(String tc_money) {
        this.tc_money = tc_money;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSet_type() {
        return set_type;
    }

    public void setSet_type(String set_type) {
        this.set_type = set_type;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getK_type() {
        return k_type;
    }

    public void setK_type(String k_type) {
        this.k_type = k_type;
    }

    public String getK_money() {
        return k_money;
    }

    public void setK_money(String k_money) {
        this.k_money = k_money;
    }

    public String getOnumber() {
        return onumber;
    }

    public void setOnumber(String onumber) {
        this.onumber = onumber;
    }

    public String getSmoney() {
        return smoney;
    }

    public void setSmoney(String smoney) {
        this.smoney = smoney;
    }


    public String getAdd_order() {
        return add_order;
    }

    public void setAdd_order(String add_order) {
        this.add_order = add_order;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }
}
