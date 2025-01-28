package com.kanyuServer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.kanyuServer.mapper")
@SpringBootApplication
public class KanyuServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KanyuServerApplication.class, args);
    }

}
