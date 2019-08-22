package com.hwyt.xpwx.Utils.common;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: daocren
 * Date: 11-5-30
 * Time: 下午5:27
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    private static final DateFormat FORMATOR = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat FORMATOR2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final DateFormat FORMATOR_YMD = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat FORMATOR_YMD2 = new SimpleDateFormat("yyyy/MM/dd");
    private static final DateFormat FORMATOR_YMD3 = new SimpleDateFormat("yyyy年MM月dd日");
    private static final DateFormat FORMATOR_YM = new SimpleDateFormat("yyyy年MM月");
    private static final DateFormat FORMATOR_YMD_ = new SimpleDateFormat("yy-MM-dd");
    private static final DateFormat FORMATOR_SIMPLE = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final DateFormat FORMATOR_YMDHm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final DateFormat FORMATOR_MD = new SimpleDateFormat("MM-dd HH:mm");
    private static final DateFormat FORMATORMD = new SimpleDateFormat("MM.dd HH:mm");
    private static final DateFormat FORMATORMDS = new SimpleDateFormat("MM.dd HH:mm:ss");
    public static final DateFormat FORMATOR_MMSS = new SimpleDateFormat("HH:mm");
    public static final DateFormat FORMATOR_MM_DD = new SimpleDateFormat("MM月dd日");
    public static final DateFormat FORMATOR_MM_DD1 = new SimpleDateFormat("MM月\ndd日");
    public static final DateFormat FORMATORMMDD = new SimpleDateFormat("MM-dd");

    public static String formatFormSeconds(long sec) {
        Date date = new Date(sec * 1000);
        return getString(date);
    }

    /**
     * 根据 yyyy-MM-dd HH:mm:ss 格式获取日期字符串
     *
     * @param date
     * @return String
     */
    public static String getString(Date date) {
        return FORMATOR.format(date);
    }

    public static String getStringMS(long time) {
        return FORMATOR_MMSS.format(new Date(time));
    }

    public static String getStringMMdd(long time) {
        return FORMATOR_MM_DD.format(new Date(time));
    }
    public static String FORMATOR_MM_DD1(long time) {
        return FORMATOR_MM_DD1.format(new Date(time));
    }

    public static String getStringMM_dd(long time) {
        return FORMATORMMDD.format(new Date(time));
    }

    public static long getLong(String date) {
        try {
            return FORMATOR.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getStringYMD(Date date) {
        return FORMATOR_YMD.format(date);
    }

    public static String getStringYMD(Long date) {
        return FORMATOR_YMD.format(new Date(date));
    }

    public static String getStringYMD2(Long date) {
        return FORMATOR_YMD2.format(new Date(date));
    }

    public static String FORMATOR_YMD3(Long date) {
        return FORMATOR_YMD3.format(new Date(date));
    }

       public static String FORMATOR_YM(Date date) {
        return FORMATOR_YM.format(date);
    }

    /**
     * 根据 yyyy-MM-dd 格式获取日期字符串
     *
     * @param date
     * @return String
     */
    public static String getSimpleString(Date date) {
        return FORMATOR_SIMPLE.format(date);
    }

    /**
     * 根据long值获取时间字符串，long值中不包含毫秒信息
     *
     * @param time
     * @return
     */
    public static String getString(long time) {
        return FORMATOR.format(new Date(time));

    }

    /**
     * 根据long值获取时间字符串，long值中不包含毫秒信息
     *
     * @param time
     * @return
     */
    public static String getString_new(long time) {
        // return FORMATOR.format(new Date(time * 1000));
        return FORMATOR.format(new Date(time));
    }

    /**
     * 根据long值获取时间字符串，long值中不包含毫秒信息
     *
     * @param time
     * @return
     */
    public static String getStringByMD(long time) {
        return FORMATOR_MD.format(new Date(time));
    }

    public static String getStringByMDOTD(long time) {
        return FORMATORMD.format(new Date(time));
    }

    public static String getStringByMDOTDS(long time) {
        return FORMATORMDS.format(new Date(time));
    }


    public static String formYMDHmsToYMD(String string) {
        return getStringYMD(getDate(string));
    }

    /**
     * 根据字符串生成日期
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss 格式的字符串
     * @return Date
     */
    public static Date getDate(String dateStr) {
        try {
            return FORMATOR.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 根据字符串生成日期
     *
     * @param dateStr yyyy-MM-dd 格式的字符串
     * @return Date
     */
    public static Date getDateyyyyMMdd(String dateStr) {
        try {
            return FORMATOR_YMD.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 转换字符串格式
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss 格式的字符串转为 yyyy-MM-dd HH:mm
     * @return Date
     */
    public static String getDateStr(String dateStr) {
        try {
            Date date = FORMATOR.parse(dateStr);
            return FORMATOR_YMDHm.format(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 根据 yyyy-MM-dd HH:mm 格式获取日期Long
     *
     * @param str
     * @return String
     */
    public static long getLongForm(String str) {
        long date = 0;
        try {
            date = (FORMATOR_YMDHm.parse(str).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据 yyyy-MM-dd hh:mm 格式获取日期字符串
     *
     * @param time
     * @return String
     */
    public static String getSimpleStringFormLong(long time) {
        return FORMATOR_YMDHm.format(new Date(time));
    }

    /**
     * 获取当前时间的前X天时间
     *
     * @param cl
     * @return
     */
    public static Calendar getBeforeDay(Calendar cl, int x) {
        //使用roll方法进行向前回滚  
        //cl.roll(Calendar.DATE, -1);  
        //使用set方法直接进行设置  
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day - x);
        return cl;
    }

    /**
     * 获取当前时间的后x天时间
     *
     * @param cl
     * @return
     */
    public static Calendar getAfterDay(Calendar cl, int x) {
        //使用roll方法进行回滚到后一天的时间  
        //cl.roll(Calendar.DATE, 1);  
        //使用set方法直接设置时间值  
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day + x);
        return cl;
    }
}
