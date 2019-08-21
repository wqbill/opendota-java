package com.wqbill.opendota.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

//@Configuration
public class TestBeanPostProcessor implements BeanPostProcessor {
    @Autowired
    private Environment environment;

    @Override

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        String property = environment.getProperty("spring.datasource.url");
        System.out.println("test3" + property);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
