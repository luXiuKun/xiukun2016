package com.fangzhurapp.technicianport;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by android on 2016/7/5.
 */
public class CustomApplication extends MultiDexApplication {
    private static Application instance;

    private static List<Activity>activitys = new LinkedList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        NoHttp.initialize(this);

        /**
         * 打开调试模式
         */
        Logger.setDebug(true);
        Logger.setTag("---NoHttp---");
        /**
         * 微信分享
         */
        PlatformConfig.setWeixin("wxe64eb893824cb31f", "d736d0fa4f2dda9eeb685066bbca4021");

        /**
         * 腾讯bug追踪
         */
        CrashReport.initCrashReport(getApplicationContext(), "900057118", false);

    }
    /**
     * 得到应用程序的application
     *
     * @return {@link Application}
     */
    public static Application getInstance() {
        return instance;
    }

    /**
     * 将activity添加到list中
     */
    public static void addAct(Activity act){
        activitys.add(act);
    }


    /**
     * 退出所有的activity
     */
    public static void removeAllAct(){

        //finish所有的Activity
        for (Activity act : activitys) {
            
            if(null != act && !act.isFinishing()){
                act.finish();
            }
        }

        if (activitys.size() == 0)activitys.clear();
    }
}
