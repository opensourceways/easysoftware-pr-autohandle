package com.softwaremarket.prhandle;

import com.softwaremarket.prhandle.task.RpmPrHandleTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        RpmPrHandleTask rpmVersionTask = context.getBean(RpmPrHandleTask.class);
        rpmVersionTask.closeCifailedPr();
        System.exit(0);
    }

}
