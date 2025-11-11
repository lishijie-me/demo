package io.money.boot.demo.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author lishijie-me
 * {@code @date} 2025/11/10 星期一 22:19:29
 * {@code @description} UserReadEntity
 */
@Data
public class UserReadEntity {

    @ExcelProperty(value = "姓名")
    private String name;
    /**
     * 强制读取第三个 这里不建议 index 和 name 同时用，要么一个对象只用index，要么一个对象只用name去匹配
     */
    @ExcelProperty(index = 1)
    private int age;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "操作时间")
    private Date time;

    //set、get...
}
