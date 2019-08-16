package com.wqbill.opendota.service;

import com.wqbill.opendota.util.Utility;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Teams {
    private final JdbcTemplate jdbcTemplate;

    public Teams(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void doTeams() {
        String sql = "select distinct team_id from team_match order by team_id desc";
        List<Map<String, Object>> teamIds = jdbcTemplate.queryForList(sql);
        teamIds.forEach(x -> {
            Map<String, Object> map = new HashMap<>();
            map.put("start_at_team_id", x.get("team_id"));
            Utility.ApiJob apiJob = Utility.generateJob("api_team_info_by_team_id", map);
        });
    }
}
