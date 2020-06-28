package com.wqbill.opendota.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wqbill.opendota.commons.Callback;
import com.wqbill.opendota.commons.Url;
import com.wqbill.opendota.config.Config;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import sun.net.util.URLUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wqbill.opendota.util.Const.SEPARATOR;

@Component
@Slf4j
public class Utility {

    /*
     * Converts a steamid 64 to a steamid 32
     *
     * Takes and returns a string
     */
    public Long convert64to32(Long id) {
        return id - 76561197960265728L;
    }

    /*
     * Converts a steamid 64 to a steamid 32
     *
     * Takes and returns a string
     */
    public Long convert32to64(Long id) {
        return id + 76561197960265728L;
    }

    /**
     * The anonymous account ID used as a placeholder for player with match privacy settings on
     */
    public Long getAnonymousAccountId() {
        return 4294967295L;
    }

    @Data
    public static class ApiJob {
        private String url;
        private String title;
        private String type;
        private Boolean noRetry;
        private Boolean raw;
        private Boolean json;

        public void setPayload(Map<String, Object> payload) {
            this.payload = payload;
            if (payload.get("noRetry") != null) {
                setNoRetry(Boolean.valueOf(payload.get("noRetry").toString()));
            }
            if (payload.get("raw") != null) {
                setRaw(Boolean.valueOf(payload.get("raw").toString()));
            }
            if (payload.get("json") != null) {
                setJson(Boolean.valueOf(payload.get("json").toString()));
            }
        }

        private Map<String, Object> payload;

        public Object get(String key) {
            return payload.get(key);
        }
    }

    /**
     * Creates a job object for enqueueing that contains details such as the Steam API endpoint to hit
     */
    static String apiUrl = "http://api.steampowered.com";

    public ApiJob generateJob(String type, Map<String, Object> payload) {
        ApiJob apiJob = new ApiJob();
        String apiKey = "A33FF4A5D4F26785DF43E75B5245A531";
        StringBuilder stringBuilder = new StringBuilder(apiUrl);
        String match_id = (String) payload.get("match_id");
        String account_id = (String) payload.get("account_id");
        String matches_requested = (String) payload.get("matches_requested");
        String hero_id = (String) payload.get("hero_id");
        String leagueid = (String) payload.get("leagueid");
        String start_at_match_id = (String) payload.get("start_at_match_id");
        String summaries_id = (String) payload.get("summaries_id");
        String start_at_match_seq_num = (String) payload.get("start_at_match_seq_num");
        String seq_num = (String) payload.get("seq_num");
        String language = (String) payload.get("language");
        String skill = (String) payload.get("skill");
        String team_id = (String) payload.get("team_id");
        String iconname = (String) payload.get("iconname");
        String server_steam_id = (String) payload.get("server_steam_id");
        String start_at_team_id = (String) payload.get("start_at_team_id");
        String ugcid = (String) payload.get("ugcid");
        switch (type) {
            case "api_details":
                apiJob.setUrl(apiUrl + "/IDOTA2Match_570/GetMatchDetails/V001/?key=" + apiKey + "&match_id=" + match_id);
                apiJob.setTitle(type + SEPARATOR + match_id);
                apiJob.setType("api");
                apiJob.setPayload(payload);
                break;
            case "api_history":
                stringBuilder.append("/IDOTA2Match_570/GetMatchHistory/V001/?key=");
                if (StringUtils.isNotEmpty(account_id)) {
                    stringBuilder.append("&account_id=").append(account_id);
                }
                if (StringUtils.isNotEmpty(matches_requested)) {
                    stringBuilder.append("&matches_requested=").append(matches_requested);
                }
                if (StringUtils.isNotEmpty(hero_id)) {
                    stringBuilder.append("&hero_id=").append(hero_id);
                }
                if (StringUtils.isNotEmpty(leagueid)) {
                    stringBuilder.append("&league_id=").append(leagueid);
                }
                if (StringUtils.isNotEmpty(start_at_match_id)) {
                    stringBuilder.append("&start_at_match_id=").append(start_at_match_id);
                }
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setTitle(type + SEPARATOR + account_id);
                apiJob.setType("api");
                apiJob.setPayload(payload);
                break;
            case "api_summaries":
                stringBuilder.append("/ISteamUser/GetPlayerSummaries/v0002/?key=").append(apiKey);
                stringBuilder.append("&steamids=");
                List<Map> players = (List<Map>) payload.get("players");
                String steamids = players.stream().map(x -> (String) x.get("account_id")).collect(Collectors.joining(","));
                stringBuilder.append(steamids);
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setTitle(type + SEPARATOR + summaries_id);
                apiJob.setType("api");
                apiJob.setPayload(payload);
                break;
            case "api_sequence":
                stringBuilder.append("/IDOTA2Match_570/GetMatchHistoryBySequenceNum/V001/?key=").append(apiKey);
                stringBuilder.append("&start_at_match_seq_num=").append(start_at_match_seq_num);
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setTitle(type + SEPARATOR + seq_num);
                apiJob.setType("api");
                break;
            case "api_heroes":
                stringBuilder.append("/IEconDOTA2_570/GetHeroes/v0001/?key=").append(apiKey);
                stringBuilder.append("&language=").append(language);
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setTitle(type + SEPARATOR + language);
                apiJob.setType("api");
                apiJob.setPayload(payload);
                break;
            case "api_items":
                stringBuilder.append("/IEconDOTA2_570/GetGameItems/v1?key=").append(apiKey);
                stringBuilder.append("&language=").append(language);
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setType("api");
                break;
            case "api_leagues":
                apiJob.setUrl("http://www.dota2.com/webapi/IDOTA2League/GetLeagueInfoList/v001");
                apiJob.setTitle(type);
                apiJob.setType("api");
                apiJob.setPayload(payload);
                break;
            case "api_skill":
                stringBuilder.append("/IDOTA2Match_570/GetMatchHistory/v0001/?key=").append(apiKey);
                stringBuilder.append("&start_at_match_id=").append(start_at_match_id);
                stringBuilder.append("&skill=").append(skill);
                stringBuilder.append("&hero_id=").append(hero_id);
                stringBuilder.append("&min_players=10");
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setTitle(type + SEPARATOR + skill);
                apiJob.setType("api");
                apiJob.setPayload(payload);
                break;
            case "api_live":
                stringBuilder.append("/IDOTA2Match_570/GetLiveLeagueGames/v0001/?key=").append(apiKey);
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setTitle(type);
                apiJob.setType("api");
                apiJob.setPayload(payload);
                break;
            case "api_notable":
                apiJob.setUrl("http://www.dota2.com/webapi/IDOTA2Fantasy/GetProPlayerInfo/v001");
                apiJob.setTitle(type);
                apiJob.setType("api");
                apiJob.setPayload(payload);
                break;
            case "api_teams":
                stringBuilder.append("/IDOTA2Teams_570/GetTeamInfo/v1/?key=").append(apiKey);
                stringBuilder.append("&team_id=").append(team_id);
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setTitle(type);
                apiJob.setType("api");
                apiJob.setPayload(payload);
                break;
            case "api_item_schema":
                stringBuilder.append("/IEconItems_570/GetSchemaURL/v1?key=").append(apiKey);
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setType("api");
                break;
            case "api_item_icon":
                stringBuilder.append("/IEconDOTA2_570/GetItemIconPath/v1?key=").append(apiKey);
                stringBuilder.append("&iconname=").append(iconname);
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setType("api");
                break;
            case "api_top_live_game":
                stringBuilder.append("/IDOTA2Match_570/GetTopLiveGame/v1/?key=").append(apiKey);
                stringBuilder.append("&partner=0");
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setType("api");
                break;
            case "api_realtime_stats":
                stringBuilder.append("/IDOTA2MatchStats_570/GetRealtimeStats/v1?key=").append(apiKey);
                stringBuilder.append("&server_steam_id=").append(server_steam_id);
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setType("api");
                break;
            case "api_team_info_by_team_id":
                stringBuilder.append("/IDOTA2Match_570/GetTeamInfoByTeamID/v1?key=").append(apiKey);
                stringBuilder.append("&start_at_team_id=").append(start_at_team_id);
                stringBuilder.append("&teams_requested=1");
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setType("api");
                break;
            case "api_get_ugc_file_details":
                stringBuilder.append("/ISteamRemoteStorage/GetUGCFileDetails/v1/?key=").append(apiKey);
                stringBuilder.append("&appid=570&ugcid=").append(ugcid);
                apiJob.setUrl(stringBuilder.toString());
                apiJob.setType("api");
                break;
            case "parse":
                apiJob.setUrl((String) payload.get("url"));
                apiJob.setTitle(type + SEPARATOR + match_id);
                apiJob.setType(type);
                apiJob.setPayload(payload);
                break;
            case "steam_cdn_team_logos":
                apiJob.setUrl("https://steamcdn-a.akamaihd.net/apps/dota2/images/team_logos/" + team_id + ".png");
                apiJob.setType("steam_cdn");
                break;
        }
        return apiJob;
    }

    /**
     * A wrapper around HTTP requests that handles:
     * proxying
     * retries/retry delay
     * Injecting API key for Steam API
     * Errors from Steam API
     */
    @Autowired
    Config config;

    public void getData(Url url, Callback cb) {
        String u = url.getUrl();
        int delay = Integer.parseInt(config.DEFAULT_DELAY);
        int timeout = 5000;
        HttpUrl parse = HttpUrl.parse(u);
        boolean steamApi = StringUtils.equals(parse.host(), "api.steampowered.com");
        boolean stratzApi = StringUtils.equals(parse.host(), "api.stratz.com");
        String target;
        if (steamApi) {
            // choose an api key to use
            final String[] apiKeys = config.STEAM_API_KEY.split(",");
            HttpUrl.Builder builder = parse.newBuilder();
            builder.setQueryParameter("key", apiKeys[(int) Math.floor(Math.random() * apiKeys.length)]);
            // choose a steam api host
            final String[] apiHosts = config.STEAM_API_HOST.split(",");
            builder.host(apiHosts[(int) Math.floor(Math.random() * apiHosts.length)]);
        }
        target = parse.toString();
        log.info("{} - getData: {}", new Date(), target);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(target).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode json = null;
        try {
            json = objectMapper.readTree(response.body().byteStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode result = json.get("result");
        if (result != null) {
            cb.execute(json);
        }
    }


    /**
     * Determines if a player is radiant
     */
    public boolean isRadiant(JsonNode player) {
        return player.get("player_slot").asInt() < 128;
    }
}