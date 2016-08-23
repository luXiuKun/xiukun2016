package com.fangzhurapp.technicianport.http;

/**
 * Created by android on 2016/7/5.
 */
public class UrlConstant {

    public  final static String BASE_URL = "http://www.fangzhur.com/jizhongbao/";
    /**
     * 登陆
     */
    public final static String LOGIN = BASE_URL+"app_alpha.php?c=Setup&a=isLogin2";

    /**
     * 修改密码
     */
    public final static String CHANGE_PW= BASE_URL+"app_alpha.php?c=Setup&a=updatePwd3";

    /**
     * 找回密码
     */
    public final static String RETRIEVE_PW = BASE_URL+"app_alpha.php?c=Setup&a=updatePwd";

    /**
     * 订单  0--工作台;1--已分账
     */
    public final static String ORDER = BASE_URL+"app_alpha.php?c=Order&a=StagetList";

    /**
     * 确认分账
     */
    public final static String CONFIRM_FZ =BASE_URL+"app_alpha.php?c=Order&a=conFash";

    /**
     * 钱包首页
     */
    public final static String WALLET_HOME = BASE_URL+"app_alpha.php?c=Wallet&a=StaIndex";

    /**
     * 进行中的订单
     */
    public final static String ORDER_ING = BASE_URL+"app_alpha.php?c=Order&a=onOrderList";

    /**
     * 获取银行卡号信息
     */
    public final static String GET_BANKCARDDATA = BASE_URL+"app_alpha.php?c=Wallet&a=getBankInfo";

    /**
     * 提现接口
     */
    public final static String TX =BASE_URL+"app_alpha.php?c=Wallet&a=Withdrawal";

    /**
     * 判断是否绑定银行卡和设置支付密码了
     */
    public final static String BIND_STATE =BASE_URL+"app_alpha.php?c=Wallet&a=checkWith";

    /**
     * 绑定银行卡
     */
    public final static String BIND_BANKCARD = BASE_URL+"app_alpha.php?c=Wallet&a=binBank";

    /**
     * 添加/修改支付密码
     */
    public final static String SET_PAYPW =BASE_URL+"app_alpha.php?c=Wallet&a=updatePass";

    /**
     * 已绑定银行卡
     */
    public final static String GET_BINDCARD_LIST = BASE_URL+"app_alpha.php?c=Wallet&a=getBankList";


    /**
     * 解除绑定
     */
    public final static String UNBIND_CARD = BASE_URL+"app_alpha.php?c=Wallet&a=unsetBank";

    /**
     * 我的工资
     */
    public final static String MY_WALLET =BASE_URL+  "app_alpha.php?c=Wallet&a=WageDetail";

    /**
     * 现金明细
     */
    public final static String MONEY_DETAIL = BASE_URL+"app_alpha.php?c=Wallet&a=StaDetail";

    /**
     * 获取验证码
     */
    public final static String GET_CODE = BASE_URL+"app_alpha.php?c=Setup&a=sms";


    /**
     * 消息--取消订单列表
     */
    public final static String MSG_ORDER = BASE_URL+"app_alpha.php?c=Order&a=getNew";

    /**
     * 未读变已读
     */
    public final static String MSG_STATE = BASE_URL+"app_alpha.php?c=Order&a=updateNew";

    /**
     * 公告
     */
    public final static String GONGGAO =BASE_URL+"app_alpha.php?c=Setup&a=Announcement";

    /**
     * 商铺切换
     */
    public final static String CHANGE_SHOP = BASE_URL+"app_alpha.php?c=Setup&a=SwitStore";

    /**
     * 意见反馈
     */
    public final static String FEED_BACK = BASE_URL+"app_alpha.php?c=Feedback&a=index";


    /**
     * boss登录
     */
    public final static String BOSS_LOGIN = BASE_URL+"app_alpha.php?c=Setup&a=isLogin3";

    /**
     * boss业绩首页
     */
    public final static String BOSS_PERFORMANCE_HOME =BASE_URL+"app_alpha.php?c=Results&a=index";

    /**
     * boss钱包首页
     */
    public final static String BOSS_WALLET_HOME = BASE_URL+"app_alpha.php?c=Wallet&a=BossIndex";

    /**
     * boss店铺切换
     */
    public final static String BOSS_SHOP_CHANGE = BASE_URL+"app_alpha.php?c=Setup&a=SwitStore1";

    /**
     * 实际营收
     */
    public final static String BOSS_PERFORMANCE_SJYS =BASE_URL+ "app_alpha.php?c=Results&a=getTur";

    /**
     * boss员工提成
     */
    public final static String BOSS_PERFORMANCE_STAFFTC = BASE_URL+"app_alpha.php?c=Results&a=getEmpCom";

    /**
     * boss项目排行
     */
    public final static String BOSS_PROJECT_RANK = BASE_URL+"app_alpha.php?c=Results&a=proRank";

    /**
     * boss员工工资
     */
    public final static String BOSS_STAFF_WAGE = BASE_URL+"app_alpha.php?c=Wallet&a=checkList";

    /**
     * boss添加店长
     */
    public final static String BOSS_ADD_SHOPMG = BASE_URL+"app_alpha.php?c=Setup&a=addAdmin";


    /**
     * boss会员管理
     */
    public final static String BOSS_MY_VIP = BASE_URL+"app_alpha.php?c=User&a=getList";

    /**
     * boss店长，股东调取
     */
    public final static String BOSS_SHOPMG_GD =BASE_URL+"app_alpha.php?c=Setup&a=AdminList";

    /**
     * boss店长股东删除
     */
    public final static String BOSS_DEL_SHOPMG_GD = BASE_URL+"app_alpha.php?c=Setup&a=unsetAdmin";


    /**
     * boss现金明细
     */
    public final static String BOSS_MONEY_DETAIL = BASE_URL+"app_alpha.php?c=Wallet&a=StaDetail2";

    /**
     * boss提现前判断是否绑定银行卡和设置支付密码
     */
    public final static String BOSS_TX_STATE =BASE_URL+"app_alpha.php?c=Wallet&a=checkWith2";

    /**
     * boss绑定银行卡
     */
    public final static String BOSS_BIND_BANKCARD = BASE_URL+"app_alpha.php?c=Wallet&a=binBank2";

    /**
     * boss获取绑定银行卡信息
     */
    public final static String BOSS_BIND_CARD_LIST = BASE_URL+"app_alpha.php?c=Wallet&a=getBankList2";

    /**
     * boss添加/找回支付密码
     */
    public final static String BOSS_CHANGE_PAY_PW = BASE_URL+"app_alpha.php?c=Wallet&a=updatePass1";

    /**
     * boss提现接口
     */
    public final static String BOSS_TX = BASE_URL+"app_alpha.php?c=Wallet&a=Withdrawal2";


    /**
     * boss提交员工工资
     */
    public final static String BOSS_SUBMIT_WAGE =BASE_URL+"app_alpha.php?c=Wallet&a=save";

}
