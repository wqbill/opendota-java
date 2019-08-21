package com.wqbill.opendota;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import redis.embedded.RedisServer;

@SpringBootApplication
@EnableScheduling
public class OpendotaApplication {

    public static void main(String[] args) {
        try {
            RedisServer redisServer = new RedisServer(6379);
            redisServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpringApplication.run(OpendotaApplication.class, args);
    }

}
