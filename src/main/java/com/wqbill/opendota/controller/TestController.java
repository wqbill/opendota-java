package com.wqbill.opendota.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("test")
public class TestController {
    private final JdbcTemplate jdbcTemplate;

    public TestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @RequestMapping
    public void apps() {
        Map<String, Object> map = jdbcTemplate.queryForMap("select now()");
        map.forEach((x, y) -> System.out.println(x + "\t" + y));
    }
}
