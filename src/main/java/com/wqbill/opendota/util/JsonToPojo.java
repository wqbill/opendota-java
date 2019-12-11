package com.wqbill.opendota.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * json解析
 * 模版类
 * jpa注解/mybatis注解
 */
public class JsonToPojo {
    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/IDOTA2Match_570/GetMatchDetails.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(classPathResource.getInputStream());
        dfs(root);
        MatchDetail matchDetail = mapper.readValue(classPathResource.getInputStream(), MatchDetail.class);
        System.out.println(matchDetail);
    }

    private static void dfs(JsonNode cur) {
        if (cur instanceof ObjectNode) {
            ObjectNode on = (ObjectNode) cur;
            on.fields().forEachRemaining(x -> {
                System.out.println(x.getKey());
                if (x.getValue().isValueNode()) {
                    System.out.println(x.getValue());
                } else
                    dfs(x.getValue());
            });
        } else if (cur instanceof ArrayNode) {
            ArrayNode an = (ArrayNode) cur;
            an.forEach(JsonToPojo::dfs);
        }
    }
}
