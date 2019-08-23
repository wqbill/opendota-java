package com.wqbill.opendota.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import com.wqbill.opendota.commons.Callback;
import com.wqbill.opendota.store.Queries;
import com.wqbill.opendota.store.Redis;
import com.wqbill.opendota.util.Utility;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log
public class Scanner {
    final int PAGE_SIZE = 100;
    final int delay = config.SCANNER_DELAY;


    @Autowired
    private Redis redis;
    @Autowired
    Queries queries;
    @Autowired
            Utility utility;
    Long nextSeqNum;
    Boolean delayNextRequest;

    public void scanApi(Long seqNum, Callback cb) {
        nextSeqNum = seqNum;
        delayNextRequest = false;
        processPage(seqNum, cb);
    }

    public void processPage(long matchSeqNum, Callback cb) {
        Utility.ApiJob container = utility.generateJob("api_sequence", ImmutableMap.of("start_at_match_seq_num", matchSeqNum));
        utility.getData(ImmutableMap.of("url", container.getUrl(), "delay", delay), args -> {
            Object err = args[0];
            JsonNode data = (JsonNode) args[1];
            if (err != null) {
                return cb.execute(err);
            }
            final JsonNode resp = data.get("result") != null && data.get("result").get("matches") != null ? data.get("result").get("matches") : new ArrayNode();
            if (resp.size() > 0) {
                nextSeqNum = resp.get(resp.size() - 1).get("match_seq_num").asLong() + 1;
            }
            if (resp.size() < PAGE_SIZE) {
                delayNextRequest = true;
            }
            System.out.printf("[API] match_seq_num:%s, matches:%s", matchSeqNum, resp.size());
            return resp.forEach(x -> processMatch(x, cb));
        });
    }

    private void processMatch(JsonNode match, Callback cb) {
        // Optionally throttle inserts to prevent overload
        if ((match.get("match_id").asLong() % 100) >= config.SCANNER_PERCENT) {
//            return finishMatch(null, cb);
        }
        // check if match was previously processed
        Object result = redis.get("scanner_insert:" + match.get("match_id"));
        Object err = null;
//        (err, result) =>{
//            if (err) {
//                return finishMatch(err, cb);
//            }
        // don't insert this match if we already processed it recently
        if (result == null) {
            return queries.insertMatch(match, ImmutableMap.of("type", "api", "origin", "scanner"), () -> {
                if (err == null) {
                    // Save match_id in Redis to avoid duplicate inserts (persist even if process restarts)
                    redis.put("scanner_insert:" + match.get("match_id"), 1, 3600 * 4);
                }
                finishMatch(err, cb);
            });
        }
//            return finishMatch(err, cb);
//        });
    }

    private void finishMatch(Object err, Callback cb) {
        if (err != null) {
            log.info("failed to insert match from scanApi %s");//error('failed to insert match from scanApi %s', match.match_id);
        }
        cb.<Void>execute(err);
    }
}
