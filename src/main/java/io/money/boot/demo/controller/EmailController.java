package io.money.boot.demo.controller;

import io.money.boot.demo.sevice.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author lishijie-me
 * {@code @date} 2025/11/9 星期日 16:39:06
 * {@code @description} EmailController
 */
@RestController
public class EmailController {
    private static final Logger logger = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;

    @GetMapping("/sendEmail")
    public String sendEmail() {
        logger.info("调用方法{}", "sendEmail");
        emailService.sendEmail("user@example.com", "Hello, sendEmail!");
        emailService.sendEmailTaskExecutor("user@example.com", "Hello, sendEmailTaskExecutor!");
        return LocalDateTime.now()+"Email is being sent!";
    }

    @GetMapping("/sendEmailFuture")
    public String sendEmailFuture() throws InterruptedException, ExecutionException {
        logger.info("调用方法{}", "sendEmailFuture");
        Future<String> result = emailService.sendEmailFuture("user@example.com", "Hello, Async!");
        return result.get(); // 等待异步任务完成并获取结果
    }

    @GetMapping("/sendEmailCompletableFuture")
    public CompletableFuture<String> sendEmailCompletableFuture() {
        logger.info("调用方法{}", "sendEmailCompletableFuture");
        return emailService.sendEmailCompletableFuture("user@example.com", "Hello, CompletableFuture!");
    }
}
