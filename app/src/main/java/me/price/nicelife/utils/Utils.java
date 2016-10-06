package me.price.nicelife.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jx-pc on 2016/10/5.
 */
public class Utils {

    public static final int INTERVAL = 1000 * 60 * 60 * 24;

    public static boolean isConnect = false;

    public  static final int IS_SYN = 0;
    public  static final  int NOT_SYN =1;
    public static  final int STATE_MODIFY = 0;
    public static final int STATE_ADD = 1;
    public  static  final int STATE_DELETE = 2;
    public static final int STATE_TODO = 0;
    public static final int STATE_FINISH = 1;

    public static int testNum = 100;

    public static Date string2Date(String dates) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dates);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return date;
    }

    public static String date2String(Date dates) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(dates);
    }
}
