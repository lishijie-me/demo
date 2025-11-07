package io.money.boot.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishijie-me
 * {@code @date} 2025/11/6 星期四 21:56:04
 * {@code @description} HelloController
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello(){
        return "Hello，shijie";
    }
}
