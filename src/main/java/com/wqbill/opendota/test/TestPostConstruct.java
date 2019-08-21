package com.wqbill.opendota.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
public class TestPostConstruct {
    @Autowired
    private Environment environment;

    @PostConstruct
    public void test() {
        String property = environment.getProperty("spring.datasource.url");
        System.out.println("test1" + property);
    }
}
