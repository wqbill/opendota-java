package com.wqbill.opendota.test;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class TestInitializingBean implements InitializingBean {
    @Autowired
    private Environment environment;
    @Override
    public void afterPropertiesSet() throws Exception {
        String property = environment.getProperty("spring.datasource.url");
        System.out.println("test2"+property);
    }
}
