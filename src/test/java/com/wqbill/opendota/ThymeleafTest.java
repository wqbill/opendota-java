package com.wqbill.opendota;

import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;

public class ThymeleafTest {
    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("templates/JsonData.java");
        System.out.println(classPathResource.exists());

        Context context = new Context();
        context.setVariable("a", 10086);
        context.setVariable("b", "1&2");
        context.setVariable("array", new String[]{"曹操", "刘备", "孙权", "汉献帝"});

        ClassLoaderTemplateResolver resolver1 = new ClassLoaderTemplateResolver();
        resolver1.setPrefix("templates/");
        resolver1.setSuffix(".java");
        resolver1.setTemplateMode(TemplateMode.TEXT);
        TemplateEngine engine1 = new TemplateEngine();
        engine1.setTemplateResolver(resolver1);
        FileWriter writer1 = new FileWriter("result.java");
        engine1.process("JsonData", context, writer1);

    }
}
