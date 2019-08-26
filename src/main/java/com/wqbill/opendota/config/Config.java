package com.wqbill.opendota.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "opendota")

public class Config {
    public String STEAM_API_KEY;
    public String STEAM_USER;
    public String STEAM_PASS;
    public String ROLE;
    public String GROUP;
    public String START_SEQ_NUM;
    public String PROVIDER;
    public String STEAM_ACCOUNT_DATA;
    public String NODE_ENV;
    public String FRONTEND_PORT;
    public String RETRIEVER_PORT;
    public String PARSER_PORT;
    public String PROXY_PORT;
    public String ROOT_URL;
    public String RETRIEVER_HOST;
    public String GCDATA_RETRIEVER_HOST;
    public String PARSER_HOST;
    public String UI_HOST;
    public String PROXY_URLS;
    public String STEAM_API_HOST;
    public String POSTGRES_URL;
    public String POSTGRES_TEST_URL;
    public String READONLY_POSTGRES_URL;
    public String REDIS_URL;
    public String REDIS_TEST_URL;
    public String CASSANDRA_URL;
    public String CASSANDRA_TEST_URL;
    public String ELASTICSEARCH_URL;
    public String INIT_POSTGRES_HOST;
    public String INIT_CASSANDRA_HOST;
    public String RETRIEVER_SECRET;
    public String SESSION_SECRET;
    public String COOKIE_DOMAIN;
    public String UNTRACK_DAYS;
    public String GOAL;
    public String MMSTATS_DATA_INTERVAL;
    public String DEFAULT_DELAY;
    public String SCANNER_DELAY;
    public String MMR_PARALLELISM;
    public String PARSER_PARALLELISM;
    public String BENCHMARK_RETENTION_MINUTES;
    public String GCDATA_PERCENT;
    public String SCANNER_PERCENT;
    public String PUBLIC_SAMPLE_PERCENT;
    public String SCENARIOS_SAMPLE_PERCENT;
    public String BENCHMARKS_SAMPLE_PERCENT;
    public String ENABLE_MATCH_CACHE;
    public String ENABLE_PLAYER_CACHE;
    public String ENABLE_RANDOM_MMR_UPDATE;
    public String MAXIMUM_AGE_SCENARIOS_ROWS;
    public String MATCH_CACHE_SECONDS;
    public String PLAYER_CACHE_SECONDS;
    public String SCANNER_PLAYER_PERCENT;
    public String ENABLE_RETRIEVER_ADVANCED_AUTH;
    public String ENABLE_API_LIMIT;
    public String API_FREE_LIMIT;
    public String API_BILLING_UNIT;
    public String API_KEY_PER_MIN_LIMIT;
    public String NO_API_KEY_PER_MIN_LIMIT;
    public String ADMIN_ACCOUNT_IDS;
    public String BACKUP_RETRIEVER_PERCENT;
    public String GCDATA_PARALLELISM;
    public String STRIPE_SECRET;
    public String STRIPE_API_PLAN;
    public String ES_SEARCH_PERCENT;
    public String WEBHOOK_TIMEOUT;
    public String WEBHOOK_FEED_INTERVAL;
    public String TRACKED_ACCOUNT_URL;
}
