package com.wqbill.opendota.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "opendota")
//@PropertySource(value = "application-opendota.properties")

public class Config {
    public static String STEAM_API_KEY;
}
