package com.fangzhurapp.technicianport.utils;

import android.util.Log;

/**
 * Created by Administrator on 2015/10/29.
 * LOG管理类
 */
public class LogUtil {
    private static boolean isDebug = true;//当app开发完成之后要置为false

    /**
     * 打印d级别的log
     * @param tag
     * @param msg
     */
    public static void d(String tag,String msg){
        if(isDebug){
            Log.d(tag, msg);
        }
    }

    /**
     * 打印d级别的log
     * @param //tag
     * @param msg
     */
    public static void d(Object object,String msg){
        if(isDebug){
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }


    /**
     * 打印e级别的log
     * @param tag
     * @param msg
     */
    public static void e(String tag,String msg){
        if(isDebug){
            Log.e(tag, msg);
        }
    }

    /**
     * 打印e级别的log
     * @param //tag
     * @param msg
     */
    public static void e(Object object,String msg){
        if(isDebug){
            Log.e(object.getClass().getSimpleName(), msg);
        }
    }
}
