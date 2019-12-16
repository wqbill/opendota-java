package com.wqbill.opendota;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.wqbill.opendota.json.Field;
import com.wqbill.opendota.util.FieldUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class JsonTest {
    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/IDOTA2Match_570/GetMatchDetails.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(classPathResource.getInputStream());
        Queue<JsonNode> queue = new LinkedList<>();
        queue.offer(root);
        traversal(queue);
    }

    private static void traversal(Queue<JsonNode> queue) {
        if (!queue.isEmpty()) {
            JsonNode node = queue.poll();
            System.out.println("start###############");
            // 最顶层不是array
            if (node instanceof ObjectNode) {
                // 每个object生成一个class
                ObjectNode on = (ObjectNode) node;
                List<Field> fields = new ArrayList<>();
                on.fields().forEachRemaining(x -> {
                    Field field = new Field();
                    field.setOriginalName(x.getKey());
                    fields.add(field);
                    JsonNode fieldNode = x.getValue();
                    System.out.println(x.getKey() + "\t" + fieldNode.getNodeType());
                    switch (fieldNode.getNodeType()) {
                        case ARRAY:
                            field.setType("List<subType>");
                            System.out.println("@@@@@@@@@@@@@@@@@@");
                            ArrayNode an = (ArrayNode) fieldNode;
                            int count = 0;
                            while (an.size() > 0 && an.get(0) instanceof ArrayNode) {
                                an = (ArrayNode) an.get(0);
                                count++;
                            }
                            String subType;
                            if (an.size() > 0) {
                                JsonNode firstSub = an.get(0);
                                queue.offer(firstSub);
                                subType = FieldUtils.capitalFirst(FieldUtils.singular(x.getKey()));
                            } else {
                                subType = "Object";
                            }
                            StringBuilder sb1 = new StringBuilder();
                            StringBuilder sb2 = new StringBuilder();
                            for (int i = 0; i <= count; i++) {
                                sb1.append("List<");
                                sb2.append(">");
                            }
                            String type = sb1.append(subType).append(sb2).toString();
                            field.setType(type);
                            break;
                        case BINARY:
                            break;
                        case BOOLEAN:
                            field.setType(Boolean.class.getSimpleName());
                            break;
                        case MISSING:
                            break;
                        case NULL:
                            break;
                        case NUMBER:
                            type = null;
                            switch (fieldNode.numberType()) {
                                case INT:
                                    type = Integer.class.getSimpleName();
                                    break;
                                case LONG:
                                    type = Long.class.getSimpleName();
                                    break;
                                case BIG_INTEGER:
                                    type = BigInteger.class.getSimpleName();
                                    break;
                                case FLOAT:
                                    type = Float.class.getSimpleName();
                                    break;
                                case DOUBLE:
                                    type = Double.class.getSimpleName();
                                    break;
                                case BIG_DECIMAL:
                                    type = BigDecimal.class.getSimpleName();
                                    break;
                            }
                            field.setType(type);
                            break;
                        case OBJECT:
                            field.setType(FieldUtils.capitalFirst(FieldUtils.singular(x.getKey())));
                            queue.offer(x.getValue());
                            break;
                        case POJO:
                            break;
                        case STRING:
                            field.setType(String.class.getSimpleName());
                            break;
                    }
                    System.out.println(field.getType());
                    fields.add(field);
                });
            }
            System.out.println("end###############");
            traversal(queue);
        }
    }
}
