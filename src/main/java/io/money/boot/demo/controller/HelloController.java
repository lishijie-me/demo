package io.money.boot.demo.controller;

import io.money.boot.demo.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lishijie-me
 * {@code @date} 2025/11/6 星期四 21:56:04
 * {@code @description} HelloController
 */
@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/hello")
    public String hello(){
        logger.info("调用HelloController#hello方法{}", DateTimeUtils.getNow());
        return "Hello，money";
    }
}
