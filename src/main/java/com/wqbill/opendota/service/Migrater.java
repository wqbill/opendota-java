package com.wqbill.opendota.service;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class Migrater {
    private final JdbcTemplate jdbcTemplate;

    public Migrater(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void doMigrate() throws IOException {
        String sql = IOUtils.toString(new ClassPathResource("/static/sql/create_tables.sql").getInputStream(), StandardCharsets.UTF_8);
        jdbcTemplate.execute(sql);
    }
}
