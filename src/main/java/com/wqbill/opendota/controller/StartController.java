package com.wqbill.opendota.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wqbill.opendota.service.Test;
import com.wqbill.opendota.util.Utility;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StartController {
    @RequestMapping("start")
    public void start() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("start_at_match_seq_num", Test.match_seq_num);
        Utility.ApiJob apiJob = Utility.generateJob("api_sequence", map);
        String url = apiJob.getUrl();
        System.out.println(url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = objectMapper.readTree(response.body().byteStream());
        JsonNode matches = json.get("result").get("matches");
        for (JsonNode match : matches) {
//            System.out.println(match);
        }
        System.out.println(matches.size());
        System.out.println(matches.get(matches.size() - 1).get("match_seq_num").asLong() + 1);
    }

    public static void main(String[] args) throws Exception {
        new StartController().start();
    }
}
