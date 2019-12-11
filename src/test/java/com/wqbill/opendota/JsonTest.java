package com.wqbill.opendota;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class JsonTest {

    // generate pojo by depth
    // bfs
    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/IDOTA2Match_570/GetMatchDetails.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(classPathResource.getInputStream());
        bfs(root, 0);
    }

    private static void bfs(JsonNode cur, int depth) {
        if (cur instanceof ObjectNode) {
            ObjectNode on = (ObjectNode) cur;
            on.fields().forEachRemaining(x -> {
                System.out.println(x.getKey());
                if (x.getValue().isValueNode()) {
                    System.out.println(x.getValue());
                } else
                    bfs(x.getValue(), depth + 1);
            });
        } else if (cur instanceof ArrayNode) {
            ArrayNode an = (ArrayNode) cur;
            an.forEach(x -> bfs(x, depth + 1));
        }
    }
}
