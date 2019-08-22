package com.wqbill.opendota.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wqbill.opendota.util.Const.SEPARATOR;

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

    @Data
    public static class ApiJob {
        private String url;
        private String title;
        private String type;
        private Map<String, Object> payload;

        public Object get(String key) {
            return payload.get(key);
        }
    }

    /**
     * Creates a job object for enqueueing that contains details such as the Steam API endpoint to hit
     */
    static String apiUrl = "http://api.steampowered.com";

    public static ApiJob generateJob(String type, Map<String, Object> payload) {
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
 * */
    public static void getData(String apiUrl){
        Object u= null;
    }
}