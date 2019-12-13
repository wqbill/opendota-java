package com.wqbill.opendota;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;

public class ThymeleafTest {
    public static void main(String[] args) throws IOException {

    }

    @Test
    public void test() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("templates/Test.java");
        System.out.println(classPathResource.exists());

        Context context = new Context();
        context.setVariable("a", 10086);
        context.setVariable("b", "1&2");
        context.setVariable("array", new String[]{"曹操", "刘备", "孙权", "汉献帝"});

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".java");
        resolver.setTemplateMode(TemplateMode.TEXT);
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        FileWriter writer = new FileWriter("Result.java");
        engine.process("JsonData", context, writer);
    }
}
