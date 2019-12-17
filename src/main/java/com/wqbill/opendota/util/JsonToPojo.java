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
        MatchDetail matchDetail = mapper.readValue(classPathResource.getInputStream(), MatchDetail.class);
        System.out.println(matchDetail);
    }
}
