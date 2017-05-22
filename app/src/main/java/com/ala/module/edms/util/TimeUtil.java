package com.ala.module.edms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Date;

public class TimeUtil {


    public static String getCurrentTime() {
        SimpleDateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        timeFmt.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return timeFmt.format(new Date());
    }


    public static String getCurrentPhotoTime() {
        SimpleDateFormat photoFmt = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        photoFmt.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return photoFmt.format(new Date());
    }

    /**
     * 获取日期
     * 
     * @return
     */
    public static String getCurrentDay() {
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFmt.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String d = dateFmt.format(new Date());
        return d;
    }


    public static String getCurrentHour() {
        SimpleDateFormat hourFmt = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        hourFmt.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String d = hourFmt.format(new Date());
        return d;
    }


    public static String addCurrentDay() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        String currentTimeUp = sf.format(c.getTime()); // 增加后的时间
        return currentTimeUp;
    }

    public static String addDay(int days) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, days);
        String currentTimeUp = dateTimeFormat.format(c.getTime()); // 增加后的时间
        return currentTimeUp;
    }


    public static long getSecond(String time) {
        String[] oo = time.split(":");
        long hour = Integer.parseInt(oo[0]);
        long min = Integer.parseInt(oo[1]);
        long s = Integer.parseInt(oo[2]);
        return s + min * 60 + hour * 60 * 60;
    }


    public static int getHour(String time) {
        String[] oo = time.split(":");
        int hour = Integer.parseInt(oo[0]);
        return hour;
    }


    public static int getDeltaTimeDays(String startDate, String endDate) {
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        dateFmt.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        DateFormat df = dateFmt;
        try {
            Date d1 = df.parse(startDate);
            Date d2 = df.parse(endDate);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            int days = (int) (diff / (1000 * 60 * 60 * 24));
            return days;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDeltaTimeDaysPhoto(String startDate, String endDate) {
        SimpleDateFormat photoFmt = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        photoFmt.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        DateFormat df = photoFmt;
        try {
            Date d1 = df.parse(startDate);
            Date d2 = df.parse(endDate);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            int days = (int) (diff / (1000 * 60 * 60 * 24));
            return days;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDeltaTimeHours(String startDate, String endDate) {
        SimpleDateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        timeFmt.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        DateFormat df = timeFmt;
        try {
            Date d1 = df.parse(startDate);
            Date d2 = df.parse(endDate);
            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
            int hours = (int) (diff / (1000 * 60 * 60));
            return hours;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getDeltaTimeMins(String startDate, String endDate) {
        SimpleDateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        timeFmt.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        DateFormat df = timeFmt;
        try {
            Date d1 = df.parse(startDate);
            Date d2 = df.parse(endDate);
            long diff = d1.getTime() - d2.getTime();
            int hours = (int) (diff / (1000 * 60));
            return hours;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static String getDeltaTimeSeconds(String startDate, String endDate) throws IllegalArgumentException {
        SimpleDateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        timeFmt.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        DateFormat df = timeFmt;
        try {
            Date d1 = df.parse(startDate);
            Date d2 = df.parse(endDate);
            long diff = d2.getTime() - d1.getTime();
            if (diff < 0) {
                throw new IllegalArgumentException("时间差为�?");
            }
            SimpleDateFormat secondFmt = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            secondFmt.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            return secondFmt.format(diff);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static Boolean compareTime(String currentDate, String endDate) {
        boolean result = false;
        SimpleDateFormat timeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        DateFormat df = timeFmt;
        try {
            Date d1 = df.parse(currentDate);
            Date d2 = df.parse(endDate);
            long diff = d2.getTime() - d1.getTime();
            if (diff > 0) {
                result = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
