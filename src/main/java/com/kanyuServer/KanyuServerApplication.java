package com.kanyuServer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.kanyuServer.mapper")
@SpringBootApplication
@EnableScheduling // 启用定时任务支持
public class KanyuServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KanyuServerApplication.class, args);
    }

}
