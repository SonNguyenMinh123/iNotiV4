package com.notification.inotythemehhhhh.mycenterutils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DateUtil {

    /*------------------ Time Ago --------------------*/
    private static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    private static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");

    public static String toTimeAgo(long duration) {
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < times.size(); i++) {
            Long current = times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                res.append(temp).append(" ").append(timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
                break;
            }
        }
        if ("".equals(res.toString()))
            return "0 second ago";
        else
            return res.toString();
    }
/*------------------ /Time Ago --------------------*/

    public static String converLongTimeToStr(long time) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;

        long hour = (time) / hh;
        long minute = (time - hour * hh) / mi;
        long second = (time - hour * hh - minute * mi) / ss;

        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
//        if (hour > 0) {
            return strHour + ":" + strMinute + ":" + strSecond;
      /*  } else {
            return strMinute + ":" + strSecond;
        }*/
    }

    /**
     * input: hh:mi:ss
     * ouput timemilisecond
     *
     * @param str
     * @return
     */
    public static double convertStringtoLongTime(String str) {
        String split[] = str.split(":");
        float hh = Float.parseFloat(split[0]);
        float mi = Float.parseFloat(split[1]);
        float ss = Float.parseFloat(split[2]);
        double timemili = 0;
        if (hh > 0) {
            timemili += hh * 60 * 60 * 1000;
        }
        if (mi > 0) {
            timemili += mi * 60 * 1000;
        }
        if (ss > 0) {
            timemili += ss * 1000;
        }
        return timemili;
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
