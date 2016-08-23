package com.fangzhurapp.technicianport.utils;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by android on 2016/5/17.
 */
public class TimeUtils {

    /**
     * 将服务器返回的毫秒值转换为时间 年-月-日
     * @param time
     * @return
     */
    public static String formatTime(String time){
        return (String) DateFormat.format("yyyy-MM-dd",
                Long.parseLong(time) * 1000);

    };


    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    };

    /**
     *获取当日开始时间
     * @return
     */
    public static String getDayStartTime(){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date start = calendar.getTime();

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
         String time = formater.format(start);
        return time;
    };


    /**
     * 获取当日结束时间
     * @return
     */
    public static String getDayEndTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.SECOND, -1);

        Date end = calendar.getTime();

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String time = formater.format(end);
        return time;
    };

    /**
     * 获取前一个月的时间，凌晨开始
     * @return
     */
    public static String getBeforeMonth(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.YEAR,0);
        calendar.add(calendar.MONTH,-1);
        calendar.add(calendar.DAY_OF_MONTH,0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date time = calendar.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String month = formater.format(time);

        return month;
    }

    /**
     * 获取一个月前,不显示日
     * @return
     */
    public static String getBeforeMonth1(){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.YEAR,0);
        calendar.add(calendar.MONTH,-1);
        calendar.add(calendar.DAY_OF_MONTH,0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date time = calendar.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM");
        String month = formater.format(time);

        return month;
    }

    /**
     * 获取前三个月的时间
     */
    public static String getThreeBeforeMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.YEAR,0);
        calendar.add(calendar.MONTH,-3);
        calendar.add(calendar.DAY_OF_MONTH,0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date time = calendar.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String month = formater.format(time);

        return month;

    }


    /**
     * 获取昨日00:00:00
     * @return
     */
    public static String getYesterDayStartTime(){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        calendar.add(calendar.YEAR,0);
        calendar.add(calendar.MONTH,0);
        calendar.add(calendar.DAY_OF_MONTH,-1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);


        Date start = calendar.getTime();

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String time = formater.format(start);
        return time;
    };

    /**
     * 获取前一周时间
     * @return
     */
    public static String getWeekStartTime(){
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.add(calendar.YEAR,0);
        calendar.add(calendar.MONTH,0);
        calendar.add(calendar.DAY_OF_MONTH,-7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date start = calendar.getTime();

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String time = formater.format(start);
        return time;
    };

    /**
     * 获取一年前的时间
     * @return
     */
    public static String getYearTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(calendar.YEAR,-1);
        calendar.add(calendar.MONTH,0);
        calendar.add(calendar.DAY_OF_MONTH,0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date start = calendar.getTime();

        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String time = formater.format(start);
        return time;
    };


    /**
     * 获取指定月份的最后一天
     */
    public static String getMonthLastDay(String month,int a){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year-a,Integer.valueOf(month),day);
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(calendar.DAY_OF_MONTH));
        Date time = calendar.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        return formater.format(time);
    };


    public static String getMonthLastDay1(int month){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year,month,day);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        Date time = calendar.getTime();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        return formater.format(time);
    };



    /**
     * 时间比较大小
     */
    public  static int timeCompare(String time1, String time2){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        try {
            Date strTime = df.parse(time1);
            Date endTime = df.parse(time2);

            //开始时间比结束时间小
            if (strTime.getTime() > endTime.getTime()){

                return 1;
                //开始时间比结束时间大
            }else if (strTime.getTime() < endTime.getTime()){

                return -1;
            }else if(strTime.getTime() == endTime.getTime()) {

                return 0;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -2;
    }

    /**
     * 生成订单号 当前毫秒值
     * @return
     */
    public static String getOrderNumber(){

        String DATE_PATTERN="yyMMddHHmmssSSS";

        return ""+new SimpleDateFormat(DATE_PATTERN).format(new Date());

    }



}
