package com.wqbill.opendota;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.wqbill.opendota.json.Field;
import com.wqbill.opendota.util.FieldUtils;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
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
        Queue<String> typeQueue = new LinkedList<>();
        queue.offer(root);
        // 主类名配置
        typeQueue.offer("MatchDetail");
        traversal(queue, typeQueue);
    }

    private static void traversal(Queue<JsonNode> queue, Queue<String> typeQueue) {
        if (!queue.isEmpty()) {
            JsonNode node = queue.poll();
            String className = typeQueue.poll();
            System.out.println("当前节点\t" + className);
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
                            ArrayNode an = (ArrayNode) fieldNode;
                            int count = 0;
                            while (an.size() > 0 && an.get(0) instanceof ArrayNode) {
                                an = (ArrayNode) an.get(0);
                                count++;
                            }
                            String subType;
                            if (an.size() > 0) {
                                JsonNode firstSub = an.get(0);
                                subType = FieldUtils.arrayName(x.getKey());
                                queue.offer(firstSub);
                                typeQueue.offer(subType);
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
                            subType = FieldUtils.objectName(x.getKey());
                            field.setType(subType);
                            queue.offer(x.getValue());
                            typeQueue.offer(subType);
                            break;
                        case POJO:
                            break;
                        case STRING:
                            field.setType(String.class.getSimpleName());
                            break;
                    }
                    System.out.println(field.getType());
                });
                // thymeleaf模版
                Context context = new Context();
                context.setVariable("fields", fields);
                context.setVariable("jpa", true);
//                context.setVariable("lombok", true);
                context.setVariable("lombok", false);
                context.setVariable("className", className);
                context.setVariable("packageName", "tv.cty");
                ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
                resolver.setPrefix("templates/");
                resolver.setSuffix(".java");
                resolver.setTemplateMode(TemplateMode.TEXT);
                TemplateEngine engine = new TemplateEngine();
                engine.setTemplateResolver(resolver);
                FileWriter writer = null;
                try {
                    // 路径配置
                    // 模版config
                    // e.g. ThymeleafConfig
                    writer = new FileWriter(className + ".java");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                engine.process("JsonData", context, writer);
            }
            System.out.println("end###############");
            traversal(queue, typeQueue);
        }
    }
}
