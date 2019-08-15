package com.wqbill.opendota.service;

import org.springframework.scheduling.annotation.Scheduled;

public class Teams {
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void doTeams() {

    }
}
