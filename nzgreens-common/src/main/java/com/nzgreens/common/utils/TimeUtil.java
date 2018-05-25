package com.nzgreens.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    static public final String FORMAT_MM_DD_ONLY = "MM-dd";
    static public final String FORMAT_DATE_ONLY = "yyyy-MM-dd";
    static public final String FORMAT_DATE_ONLY_1 = "yyyyMMdd";
    static public final String FORMAT_TIME_ONLY = "HH:mm:ss";
    static public final String FORMAT_COMPACT = "yyyyMMddHHmmss";
    static public final String FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";
    static public final String FORMAT_DETAIL = "yyyy-MM-dd HH:mm:ss.SSS";
    static public final String FORMAT_NO_SECOND = "yyyy-MM-dd HH:mm";
    static public final String FORMAT_NORMAL_ZH = "yyyy年MM月dd日 HH:mm:ss";
    static public final String FORMAT_YYYYMMDDHHMMSS_SLASH = "yyyy/MM/dd HH:mm:ss";
    static public final String FORMAT_TIME_HHMM = "HH:mm";
    static public final String FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    static public final String FORMAT_MM_DD_HHMM = "MM-dd HH:mm";
    static public final String FORMAT_MM_DD_HHMMSS = "MM-dd HH:mm:ss";

    static public final long DATE_SECOND = 86400;// 一天有86400秒
    static public final long DATE_MINUTE = 1440;// 一天有1440分
    static public final long MINUTE_SECOND = 60;// 一天有60分
    static public final long DATE_MILLISECOND = 86400000; //一天的毫秒數
    static public final long TWO_HOUR_MILLISECOND = 7200000; //兩小時的毫秒數

    public static Date parse(String str, String format) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(format);
            //sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return sf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String format(Date date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        //sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sf.format(date);
    }

    public static String format(String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        //sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sf.format(getDate());
    }

    public static boolean isExpire(String strTime, String strExpiredTime) {
        Date time = parse(strTime, FORMAT_NORMAL);
        Date expiredTime = parse(strExpiredTime, FORMAT_NORMAL);

        return (time.compareTo(expiredTime) >= 0);
    }

    /**
     * 生成制定日期的Date对象，从0点开始
     *
     * @param year
     * @param month
     * @param days
     * @return
     */
    public static Date createDate(int year, int month, int days) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, days, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getCurrentOnlyDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 计算时间差
     *
     * @param beginTime 开始时间，格式：yyyy-MM-dd HH:mm:ss
     * @param endTime   结束时间，格式：yyyy-MM-dd HH:mm:ss
     * @return 从开始时间到结束时间之间的时间差（秒）
     */
    public static long getTimeDifference(String beginTime, String endTime) {
        long between = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date end = null;
        Date begin = null;
        try {
            // 将截取到的时间字符串转化为时间格式的字符串
            end = sdf.parse(endTime);
            begin = sdf.parse(beginTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

        return between;
    }

    public static Date getDate() {
        return new Date();
    }

    public static String getTimeDifference(Date beginTime, Date endTime) {

        long between = endTime.getTime() - beginTime.getTime();
        if (between <= 0) {
            return "已过期";
        }

        between = between / 1000;// 除以1000是为了转换成秒
        long date = between / DATE_SECOND;
        long hour = (between - date * DATE_SECOND) / 3600;
        long minute = (between - date * DATE_SECOND - hour * 3600) / 60;
        long sec = (between - date * DATE_SECOND - hour * 3600 - minute * 60);
        if (date == 0) {
            return hour + "小时" + minute + "分钟" + sec + "秒";
        } else {
            return date + "天" + hour + "小时" + minute + "分钟" + sec + "秒";
        }
    }

    // 得到X天X小时时间
    public static String getDateExplain(long second) {
        String time = "";
        long hourCount_ = second / 3600;
        long dayCount_ = hourCount_ / 24;
        long remnantHour = hourCount_ % 24;
        if (dayCount_ != 0) {
            time = dayCount_ + "天";
        }
        if (remnantHour != 0) {
            time += remnantHour + "小时";
        }
        return time;
    }

    // 得到当前时间后x天的日期
    public static Date getFutrueDate(Date oldDate, int addDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(oldDate);
        c.add(Calendar.DATE, addDay);

        return c.getTime();
    }

    // 得到当前时间后x天的日期
    public static Date getFutrueDate(String oldDate, int addDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(parse(oldDate, FORMAT_DATE_ONLY));
        c.add(Calendar.DATE, addDay);

        return c.getTime();
    }

    // 比较两日期大小.
    public static int compare(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);

        if (c1.after(c2)) {
            return 1;
        } else if (c1.before(c2)) {
            return -1;
        } else {
            return 0;
        }
    }

    // 比较两日期大小
    public static int compare(String s1, String s2) {
        Date d1 = parse(s1, FORMAT_DATE_ONLY);
        Date d2 = parse(s2, FORMAT_DATE_ONLY);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if (c1.after(c2)) {
            return 1;
        } else if (c1.before(c2)) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * 解析时间格式为 yyyy-MM-dd HH:mm:ss 或者 yyyy/MM/dd HH:mm:ss 的字符串
     *
     * @param timeStr
     * @return
     */
    public static Date parseYYYYMMDDHHMMSSTimeStr(String timeStr) {

        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        String format = null;
        if (timeStr.contains("-")) {
            format = FORMAT_NORMAL;
        } else {
            format = FORMAT_YYYYMMDDHHMMSS_SLASH;
        }
        return parse(timeStr, format);
    }

    /**
     * 转换Long 时间类型 为 String
     *
     * @return
     */
    public static String format(long time, String format) {
        String timeStr = String.valueOf(time);
        int n = String.valueOf(System.currentTimeMillis()).length();
        int j = timeStr.length();
        if (j < n) {
            for (int i = 0; i < (n - j); i++) {
                timeStr += "0";
            }
            time = Long.valueOf(timeStr);
        }
        return format(new Date(time), format);
    }

    /**
     * 转换Long 时间类型 为 String
     *
     * @return
     */
    public static Date format(long time) {
        String timeStr = String.valueOf(time);
        int n = String.valueOf(System.currentTimeMillis()).length();
        int j = timeStr.length();
        if (j < n) {
            for (int i = 0; i < (n - j); i++) {
                timeStr += "0";
            }
            time = Long.valueOf(timeStr);
        }
        return new Date(time);
    }


    public static int getWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }


    public static TimeInterval getTimeInterval() {
        //必须是今天的单子
        Date d = new Date();
        //判断现在的时间是不是过了 凌晨6点
        boolean isOverFiveOclock = checkHour(d);
        Date startTime = null;
        Date endTime = null;
        if (!isOverFiveOclock) {
            startTime = getFiveOclock(d);
            d = TimeUtil.getFutrueDate(d, 1);
            endTime = getFiveOclock(d);
        } else {
            endTime = getFiveOclock(d);
            d = TimeUtil.getFutrueDate(d, -1);
            startTime = getFiveOclock(d);
        }
        TimeInterval userShareOrderTimeLimit = new TimeInterval();
        userShareOrderTimeLimit.setStartTime(startTime);
        userShareOrderTimeLimit.setEndTime(endTime);
        return userShareOrderTimeLimit;
    }

    public static class TimeInterval {
        private Date startTime;
        private Date endTime;

        public TimeInterval() {
        }

        public TimeInterval(Date startTime, Date endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }
    }

    /**
     * 验证时间是不是 6点了
     * 小于6点以前 是昨天的数据,大于6点,就是今天的数据
     *
     * @return
     */
    private static boolean checkHour(Date date) {
        String hour = TimeUtil.format(date, "HH");
        Integer h = Integer.valueOf(hour);
        return h < 6;
    }

    private static Date getFiveOclock(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }


    /**
     * 获取两个日期的日差，安装毫秒数来算
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Integer getDatInterval(Date startTime, Date endTime) {
        long intervalMilli = endTime.getTime() - startTime.getTime();
        int day = (int) (intervalMilli / DATE_MILLISECOND);
        day = intervalMilli % DATE_MILLISECOND == 0 ? day : day + 1;
        return day;
    }

    public static void main(String[] args) throws ParseException {
//		Date startTime = parse("2016-10-28 00:00:00", FORMAT_NORMAL);
//		Date endTime = new Date();
//		Date activityTime = parse("2016-10-28 00:00:00", FORMAT_NORMAL);
//		Long activityCount=getHgTradeSecond(startTime, activityTime);
//		System.out.println(differentDays(startTime, endTime));

        System.out.println(getMondayOfWeek(getFutrueDate(new Date(),5)));
    }


    /**
     * 获取广贵哈贵有效的交易时间秒数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Long getHgTradeSecond(Date startTime, Date endTime) {
        long allCount = 0L;
        if (startTime.getTime() > endTime.getTime())
            return 0L;
        Calendar start = Calendar.getInstance();
        start.setTime(startTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        while (start.getTimeInMillis() < end.getTimeInMillis()) {
            int startDayOfYear = start.get(Calendar.DAY_OF_YEAR);
            int endDayOfYear = end.get(Calendar.DAY_OF_YEAR);
            int startHour = start.get(Calendar.HOUR);
            int startMinute = start.get(Calendar.MINUTE);
            int startSecond = start.get(Calendar.SECOND);
            int dayOfweek = start.get(Calendar.DAY_OF_WEEK);
            if (startDayOfYear == endDayOfYear) {
                startHour = end.get(Calendar.HOUR);
                startMinute = end.get(Calendar.MINUTE);
                startSecond = end.get(Calendar.SECOND);
                if (dayOfweek == 1) {
                    //周日没有交易时间
                } else if (dayOfweek == 2) {
                    //周一 18个小时的交易时间 从6点开始
                    if (startHour > 6) {
                        int dayMillisecond = (((startHour - 6) * 60 + startMinute) * 60 + startSecond) * 1000;
                        allCount += dayMillisecond;
                    } else {

                    }
                } else if (dayOfweek == 7) {
                    if (startHour < 4) {
                        int dayMillisecond = ((startHour * 60 + startMinute) * 60 + startSecond) * 1000;
                        allCount += dayMillisecond;
                    } else {
                        allCount += TWO_HOUR_MILLISECOND * 2;
                    }
                } else {
                    if (startHour < 4) {
                        int dayMillisecond = ((startHour * 60 + startMinute) * 60 + startSecond) * 1000;
                        allCount += dayMillisecond;
                    } else if (startHour >= 4 || startHour <= 5) {
                        allCount += TWO_HOUR_MILLISECOND * 2;
                    } else {
                        allCount += TWO_HOUR_MILLISECOND * 2;
                        int dayMillisecond = (((startHour - 6) * 60 + startMinute) * 60 + startSecond) * 1000;
                        allCount += dayMillisecond;
                    }
                }
                break;
            }
            if (startHour == 0 && startMinute == 0 && startSecond == 0) {
                //计算完整的一天的交易时间
                if (dayOfweek == 1) {
                    //星期天沒有交易
                } else if (dayOfweek == 2) {
                    //星期一 只有18个小时的交易时间
                    allCount += (DATE_MILLISECOND - TWO_HOUR_MILLISECOND * 3);
                } else if (dayOfweek == 7) {
                    //星期六只有四个小时的有效交易时间
                    allCount += TWO_HOUR_MILLISECOND * 2;
                } else {
                    //周二到周五有22个小时的交易时间
                    allCount += DATE_MILLISECOND - TWO_HOUR_MILLISECOND;
                }
                start.add(Calendar.DAY_OF_YEAR, 1);
            } else {
                if (dayOfweek == 1) {
                    //周日没有交易时间
                } else if (dayOfweek == 2) {
                    //周一 18个小时的交易时间 从6点开始
                    if (startHour < 6) {
                        //计算一天的
                        allCount += DATE_MILLISECOND - TWO_HOUR_MILLISECOND * 3;
                    } else {
                        //大于等于六则是一天的完整时间-过了的有效交易时间
                        long mondayMillisecond = DATE_MILLISECOND - TWO_HOUR_MILLISECOND * 3;
                        long lostMillilisecond = (((startHour - 6) * 60 + startMinute) * 60 + startSecond) * 1000;
                        allCount += mondayMillisecond - lostMillilisecond;
                    }
                } else if (dayOfweek == 7) {
                    //星期六只有四个小时的有效交易时间  是0点到4点
                    if (startHour < 4) {
                        //小于4的才有有效交易时间
                        int dayMillisecond = (((4 - startHour) * 60 - startMinute) * 60 - startSecond) * 1000;
                        allCount += dayMillisecond;
                    }
                } else {
                    if (startHour < 4) {
                        //获取0到4小时之间的有效交易时间
                        int dayMillisecond = (((4 - startHour) * 60 - startMinute) * 60 - startSecond) * 1000;
                        allCount += dayMillisecond;
                        //再加上另外的18个小时的有效交易时间
                        allCount += DATE_MILLISECOND - TWO_HOUR_MILLISECOND * 3;
                    } else if (startHour >= 4 || startHour <= 5) {
                        //在4点与6点之间 有18个有效交易时间
                        allCount += DATE_MILLISECOND - TWO_HOUR_MILLISECOND * 3;
                    } else {
                        int dayMillisecond = (((24 - startHour) * 60 - startMinute) * 60 - startSecond) * 1000;
                        allCount += dayMillisecond;
                    }
                }
                start.set(Calendar.HOUR, 0);
                start.set(Calendar.MINUTE, 0);
                start.set(Calendar.SECOND, 0);
                start.add(Calendar.DAY_OF_YEAR, 1);
            }
        }
        return allCount / 1000;
    }


    /**
     * endTime比startTime多的天数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int differentDays(Date startTime, Date endTime) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startTime);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endTime);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) //同一年
        {
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) //闰年
                {
                    timeDistance += 366;
                } else //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2 - day1);
        } else //不同年
        {
            return day2 - day1;
        }
    }

    /**
     * 得到本周周一
     *
     * @return yyyy-MM-dd
     */
    public static Date getMondayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return parse(format(c.getTime(), FORMAT_DATE_ONLY),FORMAT_DATE_ONLY);
    }

    /**
     * 得到本周周日
     *
     * @return yyyy-MM-dd
     */
    public static Date getSundayOfThisWeek() {
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return parse(format(c.getTime(), FORMAT_DATE_ONLY), FORMAT_DATE_ONLY);
    }

    /**
     * 得到指定日期周一
     *
     * @return yyyy-MM-dd
     */
    public static Date getMondayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return parse(format(c.getTime(), FORMAT_DATE_ONLY), FORMAT_DATE_ONLY);
    }

    /**
     * 得到指定日期周日
     *
     * @return yyyy-MM-dd
     */
    public static Date getSundayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 7);
        return parse(format(c.getTime(), FORMAT_DATE_ONLY), FORMAT_DATE_ONLY);
    }

    public static boolean isToday(Date date) {
        String dateStr = format(date, FORMAT_DATE_ONLY);
        return StringUtils.equals(dateStr, format(new Date(), FORMAT_DATE_ONLY));
    }
    
    /**
     * 是否是周末
     * @param date
     * @return
     */
    public static boolean isWeekend(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
        {
           return true;
        }
        return false;
    }
    
    public static boolean isWeekend()
    {
        return isWeekend(new Date());
    }
    
    
}
