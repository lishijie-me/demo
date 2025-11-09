package io.money.boot.demo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lishijie-me
 * {@code @date} 2025/11/9 星期日 19:41:14
 * {@code @description} DateTimeUtils
 */
public class DateTimeUtils {

    /**
     * 获取当前日期和时间
     * */
    public static String getNow(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return localDateTime.format(formatter);
    }

}
