//package com.wqbill.opendota.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.node.JsonNodeFactory;
//import com.google.common.collect.ImmutableMap;
//import com.wqbill.opendota.commons.Callback;
//import com.wqbill.opendota.config.Config;
//import com.wqbill.opendota.store.Queries;
//import com.wqbill.opendota.store.Redis;
//import com.wqbill.opendota.util.Utility;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class Scanner {
//    final int PAGE_SIZE = 100;
//    final int delay;
//
//
//    @Autowired
//    Redis redis;
//    @Autowired
//    Queries queries;
//    @Autowired
//    Utility utility;
//    @Autowired
//    Config config;
//
//    Long seqNum;
//    Long nextSeqNum;
//    Boolean delayNextRequest;
//    JsonNode match;
//
//    public Scanner() {
//        delay = Integer.parseInt(config.SCANNER_DELAY);
//    }
//
//    public void scanApi(Long seqNum) {
//        nextSeqNum = seqNum;
//        delayNextRequest = false;
//        processPage(seqNum, args -> finishPageSet(null));
//    }
//
//    public void processPage(long matchSeqNum, Callback cb) {
//        Utility.ApiJob container = utility.generateJob("api_sequence", ImmutableMap.of("start_at_match_seq_num", matchSeqNum));
//        utility.getData(ImmutableMap.of("url", container.getUrl(), "delay", delay), args -> {
//            JsonNode data = (JsonNode) args[1];
//            final JsonNode resp = data.get("result") != null && data.get("result").get("matches") != null ? data.get("result").get("matches") : new ArrayNode(JsonNodeFactory.instance);
//            if (resp.size() > 0) {
//                nextSeqNum = resp.get(resp.size() - 1).get("match_seq_num").asLong() + 1;
//            }
//            if (resp.size() < PAGE_SIZE) {
//                delayNextRequest = true;
//            }
//            log.info("[API] match_seq_num:{}, matches:{}", matchSeqNum, resp.size());
//            resp.forEach(x -> processMatch(x, cb));
//        });
//    }
//
//    private void processMatch(JsonNode match, Callback cb) {
//        // Optionally throttle inserts to prevent overload
//        if ((match.get("match_id").asLong() % 100) >= Long.parseLong(config.SCANNER_PERCENT)) {
//            finishMatch(null);
//        }
//        // check if match was previously processed
//        Object result = redis.get("scanner_insert:" + match.get("match_id"));
//        // don't insert this match if we already processed it recently
//        if (result == null) {
//            queries.insertMatch(
//                    match,
//                    ImmutableMap.of("type", "api", "origin", "scanner"),
//                    args -> {
//                        // Save match_id in Redis to avoid duplicate inserts (persist even if process restarts)
//                        redis.put("scanner_insert:" + match.get("match_id"), 1, 3600 * 4);
//                        finishMatch(null);
//                    }
//            );
//        }
//        finishMatch(null);
//    }
//
//    private void finishPageSet(Exception err) {
//        if (err != null) {
//            // something bad happened, retry this page
//            log.error(err.getMessage(), err);
//            scanApi(seqNum);
//        }
//        log.info("next_seq_num: {}", nextSeqNum);
//        redis.set("match_seq_num", nextSeqNum);
//        // Completed inserting matches on this page
//        // If not a full page, delay the next iteration
//        //返回值 setTimeout的唯一标示符 用于取消定时器 javascript
//        try {
//            Thread.sleep(delayNextRequest ? 3000 : 0);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        scanApi(nextSeqNum);
//    }
//
//    private void finishMatch(Exception err) {
//        if (err != null) {
//            log.info("failed to insert match from scanApi {}", match.get("match_id"));
//        }
//    }
//}
