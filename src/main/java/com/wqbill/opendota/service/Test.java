package com.wqbill.opendota.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Test {
    @Scheduled(fixedRate = 10 * 1000)
    public void testRate() {
        System.out.println(new Date());
    }

    public static String match_seq_num = "5130564964";

    public static String match_id = "5130564964";
}
