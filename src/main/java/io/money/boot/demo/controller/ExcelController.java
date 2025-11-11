package io.money.boot.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.money.boot.demo.entity.UserReadEntity;
import io.money.boot.demo.entity.UserWriteEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lishijie-me
 * {@code @date} 2025/11/10 星期一 21:29:53
 * {@code @description} EasyExcel导入导出
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        exportExcel();
        Thread.sleep(1000L);
        importExcel();
    }

    /**
     * 导出Excel
     * */
    public static void exportExcel() throws FileNotFoundException {
        List<UserWriteEntity> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserWriteEntity userEntity = new UserWriteEntity();
            userEntity.setName("张三" + i);
            userEntity.setAge(20 + i);
            userEntity.setTime(new Date(System.currentTimeMillis() + i));
            dataList.add(userEntity);
        }
        //定义文件输出位置
        FileOutputStream outputStream = new FileOutputStream(new File("./easyexcel-export.xlsx"));
        EasyExcel.write(outputStream, UserWriteEntity.class).sheet("用户信息").doWrite(dataList);
    }

    /**
     * 导入Excel
     * */
    public static void importExcel() throws FileNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        //同步读取文件内容
        FileInputStream inputStream = new FileInputStream(new File("./easyexcel-export.xlsx"));
        List<UserReadEntity> list = EasyExcel.read(inputStream).head(UserReadEntity.class).sheet().doReadSync();
        String s = null;
        try {
            s = mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(s);
    }

}

