package com.wqbill.opendota;

import org.springframework.core.io.ClassPathResource;
import skadistats.clarity.processor.runner.SimpleRunner;
import skadistats.clarity.source.MappedFileSource;
import skadistats.clarity.source.Source;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class ParserTest {
    public static void main(String[] args) {

    }

    public void test() throws IOException {
        String replayUrl = "http://replay<cluster>.valve.net/570/<match_id>_<replay_salt>.dem.bz2";
        int cluster = 227;
        long match_id = 5130564964L;
        // 找不到
        int replay_salt = 1323404802;
        Properties properties = new Properties();
        properties.load(new ClassPathResource("application.properties").getInputStream());
        File file = new File(properties.getProperty("dem.example.path"));
    }
}
