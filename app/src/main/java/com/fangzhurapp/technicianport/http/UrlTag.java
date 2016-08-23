package com.fangzhurapp.technicianport.http;

/**
 * Created by android on 2016/7/13.
 */
public class UrlTag {
    /**
     * 登陆
     */
    public static final int LOGIN = 0x11;


    /**
     * 修改密码
     */
    public static final int CHANGE_PW =0x12;

    /**
     * 找回密码
     */
    public static final int RETRIEVE_PW = 0x13;

    /**
     * 订单
     */
    public static final int ORDER =0x14;

    /**
     * 确认分账
     */
    public static final int CONFIRM_FZ = 0x15;


    /**
     * 已分账
     */
    public static  final int FZ_SUCESS = 0x16;

    /**
     * 钱包首页
     */
    public static final int WALLET_HOME = 0x17;

    /**
     * 进行中订单
     */
    public static final int ORDER_ING= 0x18;


    /**
     * 获取银行卡信息
     */
    public static final int GET_BANKCARD_DATA =0x19;

    /**
     * 提现
     */
    public static final int TX = 0x20;



    /**
     * 绑定银行卡
     *
     */
    public static final int BIND_BANKCARD =0x21;

    /**
     * 设置支付密码/修改
     */
    public static final int SET_PAYPW =0x22;

    /**
     * 获取绑定银行卡的信息
     */
    public static final int GET_BINDCARD_LIST = 0x23;

    /**
     * 解除绑定
     */
    public static final int UNBIND_CARD = 0x24;

    /**
     * 我的工资
     */
    public static final int MY_WALLET = 0x25;

    /**
     * 现金明细
     */
    public static final int MONEY_DETAIL = 0x26;

    /**
     * 获取验证码
     */
    public static final int GET_CODE = 0x27;


    /**
     * 消息--取消订单
     */
    public static final int MSG_ORDER =0x28;

    /**
     * 未读变已读
     */
    public static final int MSG_STATE = 0x29;

    /**
     * 公告
     */
    public static final int GONGGAO =0x30;

    /**
     * 切换店铺
     */
    public static final int CHANGE_SHOP = 0x31;

    /**
     * 意见反馈
     */
    public static final int FEED_BACK = 0x32;


    /**
     * boss登录
     */
    public static final int BOSS_LOGIN = 0x33;

    /**
     * boss业绩首页
     */
    public static final int BOSS_PERFORMANCE_HOME = 0x34;

    /**
     * boss钱包首页
     */
    public static final int BOSS_WALLET_HOME = 0x35;

    /**
     * boss店铺切换
     */
    public static final int BOSS_SHOP_CHANGE = 0x36;

    /**
     * boss实际营收
     */
    public static final int BOSS_PERFORMANCE_SJYS = 0x37;

    /**
     * boss员工提成
     */
    public static final int BOSS_PERFORMANCE_STAFFTC = 0x38;

    /**
     * boss项目排行
     */
    public static final int BOSS_PROJECT_RANK = 0x39;

    /**
     * boss员工工资
     */
    public static final int BOSS_STAFF_WAGE = 0x40;

    /**
     * boss添加店长
     */
    public static final int BOSS_ADD_SHOPMG = 0x41;

    /**
     * boss我的会员
     */
    public static final int BOSS_MY_VIP = 0x42;

    /**
     * boss店长、股东调取
     */
    public static final int BOSS_SHOPMG_GD = 0x43;

    /**
     * boss店长、股东删除
     */
    public static final int BOSS_DEL_SHOP_GD =0x44;

    /**
     * boss现金明细
     */
    public static final int BOSS_MONEY_DETAIL =0x45;


    /**
     * boss提现的状态
     */
    public static final int BOSS_TX_STATE = 0x46;

    /**
     * boss绑定银行卡
     */
    public static final int BOSS_BIND_BANKCARD = 0x47;

    /**
     * boss已绑定卡信息
     */
    public static final int BOSS_BIND_CARD_LIST =0x48;

    /**
     * boss添加/修改支付密码
     */
    public static final int BOSS_CHANGE_PAY_PW =0x49;

    /**
     * boss提现接口
     */
    public static final int BOSS_TX =0x50;

    /**
     * boss提交员工工资
     */
    public static final int BOSS_SUBMIT_WAGE = 0x51;

}
