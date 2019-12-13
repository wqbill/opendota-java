package com.wqbill.opendota;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.wqbill.opendota.json.Field;
import javafx.util.Pair;
import lombok.ToString;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.fasterxml.jackson.databind.node.JsonNodeType.*;

public class JsonTest {
    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/IDOTA2Match_570/GetMatchDetails.json");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(classPathResource.getInputStream());
        Stack<Pair<JsonNode, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(root, 0));
        bfs(stack);
    }

    private static void bfs(Stack<Pair<JsonNode, Integer>> stack) {
        if (!stack.isEmpty()) {
            Pair<JsonNode, Integer> pair = stack.pop();
            JsonNode node = pair.getKey();
            Integer depth = pair.getValue();
            if (node instanceof ObjectNode) {
                // 每个object生成一个class
                ObjectNode on = (ObjectNode) node;
                List<Field> fields = new ArrayList<>();
                on.fields().forEachRemaining(x -> {
                    Field field = new Field();
                    field.setOriginalName(x.getKey());
                    fields.add(field);
                    System.out.println(x.getKey() + "\t" + depth + "\t" + x.getValue().getNodeType());
                    if (x.getValue().getNodeType() == NUMBER) {
                        String type = "Double";
                        switch (x.getValue().numberType()) {
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
                    }else if(x.getValue().getNodeType()==ARRAY){
                        field.setType("List<subType>");
                    }else if(x.getValue().getNodeType()==BOOLEAN){
                        field.setType(Boolean.class.getSimpleName());
                    }else if(x.getValue().getNodeType()==OBJECT){
                        field.setType("subType");
                    } else if(x.getValue().getNodeType()==STRING){
                        field.setType(String.class.getSimpleName());
                    }
                    System.out.println(field.getType());
                    fields.add(field);
                    stack.push(new Pair<>(x.getValue(), depth + 1));
                });
            } else if (node instanceof ArrayNode) {
                ArrayNode an = (ArrayNode) node;
                if (an.size() > 0) {
                    JsonNode firstSub = an.get(0);
                    stack.push(new Pair<>(firstSub, depth));
                }
            } else if (node instanceof ValueNode) {
            } else {
                System.out.println("unknown situation");
            }
            bfs(stack);
        }
    }

    @Test
    public void test(){
        System.out.println(BigDecimal.class.getSimpleName());
        System.out.println(123);
    }
}
