package io.money.boot.demo.sevice;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author lishijie-me
 * {@code @date} 2025/11/9 星期日 16:32:27
 * {@code @description} EmailService
 * 当调用sendEmail方法时，它会被异步执行。调用者无需等待方法执行完毕，而可以继续执行其他操作
 * 你可以在其他类中调用这个异步方法，而不需要担心它是否完成
 * 当你访问/send-email时，控制器方法会立即返回响应，而sendEmail方法将在后台异步执行
 */
@Service
public class EmailService {

    @Async("taskExecutor")
    public void sendEmail(String emailAddress, String message){
        // 模拟长时间执行任务
        try {
            Thread.sleep(5000); // 假设发送邮件需要5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendEmail: " + LocalDateTime.now()+emailAddress);
    }
    /**
     * 通过在@Async注解中指定线程池的bean名（如taskExecutor），Spring会使用我们自定义的线程池来处理该异步方法
     * */
    @Async("taskExecutor")
    public void sendEmailTaskExecutor(String emailAddress, String message) {
        // 模拟长时间执行任务
        try {
            Thread.sleep(10000); // 假设发送邮件需要5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendEmailTaskExecutor: " + LocalDateTime.now()+ emailAddress);
    }

    @Async("taskExecutor")
    public Future<String> sendEmailFuture(String emailAddress, String message) {
        try {
            Thread.sleep(10000); // 模拟发送邮件
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>("sendEmailFuture: " + LocalDateTime.now() + emailAddress);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> sendEmailCompletableFuture(String emailAddress, String message) {
        try {
            Thread.sleep(20000); // 模拟发送邮件
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture("sendEmailCompletableFuture: " + LocalDateTime.now() + emailAddress);
    }
}
