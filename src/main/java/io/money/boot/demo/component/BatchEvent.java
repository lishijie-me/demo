package io.money.boot.demo.component;

import io.money.boot.demo.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author lishijie-me
 * {@code @date} 2025/11/9 星期日 19:14:10
 * {@code @description} BatchController
 */
@Component
public class BatchEvent {
    private static final Logger logger = LoggerFactory.getLogger(BatchEvent.class);

    @Scheduled(cron = "0 * * * * ?")
    public void logsPrintTest(){
        logger.info("Hello，I'm tank, welcome to my demo show system: money");
        logger.info("Now is: {}", DateTimeUtils.getNow());
    }

}
