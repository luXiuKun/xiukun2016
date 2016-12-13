package com.fangzhurapp.technicianport.utils;

import java.text.DecimalFormat;

/**
 * Created by android on 2016/5/27.
 */
public class NumberUtils {
    /**
     * 保留两位小数
     * @return
     */
    public static String floatFormat(float num){
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        return decimalFormat.format(num);
    }

    /**
     * 保留一位
     * @param num
     * @return
     */
    public static String floatFormat1(float num){
        DecimalFormat decimalFormat = new DecimalFormat(".0");

        return decimalFormat.format(num);
    }


    /**
     * 不保留小数点
     */
    public static String floatFormat2(float num){
        DecimalFormat decimalFormat = new DecimalFormat("0");

        return decimalFormat.format(num);
    }
}
