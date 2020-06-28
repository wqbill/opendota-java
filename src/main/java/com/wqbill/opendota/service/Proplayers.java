package com.wqbill.opendota.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.wqbill.opendota.commons.Callback;
import com.wqbill.opendota.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class Proplayers {
    @Autowired
    Utility utility;

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void doProPlayers() {
        Utility.ApiJob apiJob = utility.generateJob("api_notable", new HashMap<>());
        utility.getData(apiJob.getUrl(), new Callback() {
            @Override
            public void execute(Object... args) {
                if (args[0] instanceof JsonNode) {
                    JsonNode playerInfos = ((JsonNode) args[0]).get("player_infos");
                    playerInfos.forEach(x -> {
//
//                        queries.upsert(db, 'notable_players', p, {
//                                account_id: p.account_id,});
                    });
                }
            }
        });
    }
}
