package com.liu.Account.commonUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.text.TextUtils;

public class DateUtil {
    /** 时间日期格式化到年月日时分秒. */
    public static final String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String dateFormatYMDHMSw = "yyyy.MM.dd HH:mm";

    /** 时间日期格式化到年月日. */
    public static final String dateFormatYMD = "yyyy-MM-dd";

    /** 时间日期格式化到年月. */
    public static final String dateFormatYM = "yyyy-MM";

    /** 时间日期格式化到年月日时分. */
    public static final String dateFormatYMDHM = "yyyy-MM-dd HH:mm";

    /** 时间日期格式化到月日. */
    public static final String dateFormatMD = "MM/dd";

    /** 时分秒. */
    public static final String dateFormatHMS = "HH:mm:ss";

    /** 时分. */
    public static final String dateFormatHM = "HH:mm";

    /** 上午. */
    public static final String AM = "AM";

    /** 下午. */
    public static final String PM = "PM";
    /**
     * 描述：判断是否是闰年()
     * <p>(year能被4整除 并且 不能被100整除) 或者 year能被400整除,则该年为闰年.
     *
     * @param year 年代（如2012）
     * @return boolean 是否为闰年
     */
    public static boolean isLeapYear(int year) {
        if(year<0)
            return false;
        if ((year % 4 == 0 && year % 400 != 0) || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 描述：根据时间返回格式化后的时间的描述.
     * 与当前日期相差小于1小时显示多少分钟前  大于1小时显示今天＋实际日期，大于一天   显示实际时间
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param outFormat 需要返回的时间格式 如yyyy-MM-dd HH:mm
     * @return  对传入时间的描述
     */
    public static String formatDateStr2Desc(String strDate,String outFormat) {
        if(strDate==null||outFormat==null)
            return null;
        DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c2.setTime(df.parse(strDate));
            c1.setTime(new Date());
            //计算传入时间与当前日期所差天数
            int d = getOffectDay(c1.getTimeInMillis(), c2.getTimeInMillis());
            if(d==0){
                //传入日期与当前日期是同一天
                //计算传入日期与当前日期所差的小时数
                int h = getOffectHour(c1.getTimeInMillis(), c2.getTimeInMillis());
                if(h>0){
                    //传入时间与当前日期相差一小时及以上
                    //返回           今天 12：03  格式 的字符串
                    return "今天"+getStringByFormat(strDate,dateFormatHM);
                    //return h + "小时前";
                }else if(h==0){
                    //传入日期与当前日期是同一小时
                    //计算二者相差的分钟数
                    int m = getOffectMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
                    if(m>0){
                        return m + "分钟前";
                    }else if(m==0){
                        return "刚刚";
                    }
                }
            }
            //如果与当前日期相差大于一天 ，则返回传入的格式型时间
            String out = getStringByFormat(strDate,outFormat);
            if(!TextUtils.isEmpty(out)){
                return out;
            }
        } catch (Exception e) {
        }

        return strDate;
    }

    /**
     * 描述：计算两个日期所差的分钟数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的分钟数
     */
    public static int getOffectMinutes(long date1, long date2) {
        if(date1==0||date2==0)
            return 0;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int m1 = calendar1.get(Calendar.MINUTE);
        int m2 = calendar2.get(Calendar.MINUTE);
        int h = getOffectHour(date1, date2);
        int m = 0;
        m = m1-m2+h*60;
        return m;
    }

    /**
     * 描述：计算两个日期所差的天数.
     *
     * @param milliseconds1 第一个日期的毫秒数
     * @param milliseconds2 第二个日期的毫秒数
     * @return int 所差的天数
     */
    public static int getOffectDay(long milliseconds1, long milliseconds2) {
        if(milliseconds1==0||milliseconds2==0)
            return 0;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(milliseconds1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(milliseconds2);
        //先判断是否同年
        int y1 = calendar1.get(Calendar.YEAR);
        int y2 = calendar2.get(Calendar.YEAR);
        int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int maxDays = 0;
        int day = 0;
        if (y1 - y2 > 0) {
            maxDays = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 + maxDays;
        } else if (y1 - y2 < 0) {
            maxDays = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
            day = d1 - d2 - maxDays;
        } else {
            day = d1 - d2;
        }
        return day;
    }
    /**
     * 描述：计算两个日期所差的小时数.
     *
     * @param date1 第一个时间的毫秒表示
     * @param date2 第二个时间的毫秒表示
     * @return int 所差的小时数
     */
    public static int getOffectHour(long date1, long date2) {
        if(date1==0||date2==0)
            return 0;
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(date2);
        int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
        int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
        int h = 0;
        int day = getOffectDay(date1, date2);
        h = h1-h2+day*24;
        return h;
    }

    /**
     * 描述：获取表示当前日期时间的字符串.
     *
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型的当前日期时间
     */
    public static String getCurrentDate(String format) {
        if(format==null)
            return null;
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;

    }
    /**
     * 描述：获取表示当前日期时间的字符串(可偏移).
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset 偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String getCurrentDate(String format,int calendarField,int offset) {
        if(format==null)
            return null;
        String mDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;

    }

    /**
     * 描述：获取本月第一天.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型日期时间
     */
    public static String getFirstDayOfMonth(String format) {
        if(format==null)
            return null;
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            //当前月的第一天
            c.set(GregorianCalendar.DAY_OF_MONTH, 1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    public static long getFirstDayOfMonth(){
        Calendar c=new GregorianCalendar();
        c.set(GregorianCalendar.DAY_OF_MONTH, 1);
        c.set(GregorianCalendar.HOUR_OF_DAY,0);
        c.set(GregorianCalendar.MINUTE,0);
        c.set(GregorianCalendar.SECOND,0);
        return c.getTimeInMillis();
    }
    public static long getLastDayOfMonth(){
        Calendar c=new GregorianCalendar();
        c.set(Calendar.DATE, 1);
        c.roll(Calendar.DATE, -1);
        c.set(GregorianCalendar.HOUR_OF_DAY,0);
        c.set(GregorianCalendar.MINUTE,0);
        c.set(GregorianCalendar.SECOND, 0);
        return c.getTimeInMillis();
    }
    /**
     * 描述：获取本月最后一天.
     *
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型日期时间
     */
    public static String getLastDayOfMonth(String format) {
        if(format==null)
            return null;
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            // 当前月的最后一天
            c.set(Calendar.DATE, 1);
            c.roll(Calendar.DATE, -1);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }


    /**
     * 描述：Date类型转化为String类型.
     *
     * @param date Date类型时间
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String String类型日期时间
     */
    public static String getStringByFormat(Date date, String format) {
        if(format==null||date==null)
            return null;
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        String strDate = null;
        try {
            strDate = mSimpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    /**
     * 描述：获取指定日期时间的字符串,用于导出想要的格式.
     *
     * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
     * @param format 输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 转换后的String类型的日期时间
     */
    public static String getStringByFormat(String strDate, String format) {
        if(format==null||strDate==null)
            return null;
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDHMS);
            c.setTime(mSimpleDateFormat.parse(strDate));
            SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
            mDateTime = mSimpleDateFormat2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * 描述：获取milliseconds表示的日期时间的字符串.
     *
     * @param milliseconds 时间的毫秒数
     * @param format  格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return String 日期时间字符串
     */
    public static String getStringByFormat(long milliseconds,String format) {
        if(format==null||milliseconds==0)
            return null;
        String thisDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            thisDateTime = mSimpleDateFormat.format(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thisDateTime;
    }

    /**
     * 描述：获取指定日期时间的字符串(可偏移).
     *
     * @param strDate String形式的日期时间  必须与format格式相同
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset 偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String getStringByOffset(String strDate, String format,int calendarField,int offset) {
        if(format==null||strDate==null)
            return null;
        String mDateTime = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(mSimpleDateFormat.parse(strDate));
            c.add(calendarField, offset);
            mDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDateTime;
    }

    /**
     * 描述：Date类型转化为String类型(可偏移).
     *
     * @param date Date类型时间
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset 偏移(值大于0,表示+,值小于0,表示－)
     * @return String String类型的日期时间
     */
    public static String getStringByOffset(Date date, String format,int calendarField,int offset) {
        if(format==null||date==null)
            return null;
        String strDate = null;
        try {
            Calendar c = new GregorianCalendar();
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            c.setTime(date);
            c.add(calendarField, offset);
            strDate = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }
    /**
     * 描述：获取偏移之后的Date.
     * @param date 日期时间
     * @param calendarField Calendar属性，对应offset的值， 如(Calendar.DATE,表示+offset天,Calendar.HOUR_OF_DAY,表示＋offset小时)
     * @param offset 偏移(值大于0,表示+,值小于0,表示－)
     * @return Date 偏移之后的日期时间
     */
    public static Date getDateByOffset(Date date,int calendarField,int offset) {
        if(date==null)
            return null;
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(date);
            c.add(calendarField, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }
    /**
     * 得到传入字符串的毫秒数
     * @param strDate 字符串形式的时间 必须和format格式相同
     * @param format 格式化字符串，如："yyyy-MM-dd HH:mm:ss"
     * @return long 传入日期的毫秒数
     * */
    public static long getMilliseconds(String strDate,String format){
        if(format==null||strDate==null)
            return 0;
        long mill=0;
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
        try {
            Date d=mSimpleDateFormat.parse(strDate);
            mill=d.getTime();
            return d.getTime();
        } catch (ParseException e) {
            //  自动生成的 catch 块
            e.printStackTrace();
        }
        return mill;
    }
}