package com.wqbill.opendota.service;

import com.wqbill.opendota.util.Utility;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Proplayers {
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void doProPlayers() {
        Utility.ApiJob apiJob = Utility.generateJob("api_notable", new HashMap<>());

    }
}
