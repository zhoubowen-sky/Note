package com.lingdongkuaichuan.note.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 周博文 on 2017/6/4.
 */

public class DateUtil {

    /**
     * 将时间戳转换为 string 格式的标准时间
     * @param dateline
     * @return
     */
    public static String dateLineToString(long dateline){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date_str = simpleDateFormat.format(new Date(dateline));
        return date_str;
    }
    public static String dateLineToString(String dateline){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date_str = simpleDateFormat.format(new Date(new Long(dateline)));
        return date_str;
    }

}
