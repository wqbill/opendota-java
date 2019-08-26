package com.wqbill.opendota.store;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import com.wqbill.opendota.commons.Callback;
import com.wqbill.opendota.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class Queries {
    @Autowired
    Utility utility;
    JsonNode players;
    JsonNode match;
    public void insertMatch(JsonNode match,Map options, Callback cb)  {
        this.match=match;
        ObjectMapper objectMapper = new ObjectMapper();
        players = objectMapper.readTree(match.get("players") != null ? match.get("players").toString() : null);
        JsonNode abilityUpgrades = null;
        Map savedAbilityLvls = ImmutableMap.of(5288, "track",5368, "greevils_greed");
        function tellFeed (cb) {
        if (options.origin == = 'scanner' || options.doTellFeed) {
            redis.xadd('feed', 'maxlen', '~', '10000', '*', 'data', JSON.stringify({ ...match, origin:options.origin }),
            cb);
        } else {
            cb();
        }
  }

        function decideLogParse (cb) {
        if (match.leagueid) {
            db.select('leagueid')
                    .from('leagues')
                    .where('tier', 'premium')
                    .orWhere('tier', 'professional')
                    .asCallback((err, leagueids) =>{
                if (err) {
                    return cb(err);
                }
                options.doLogParse = options.doLogParse
                        || utility.isProMatch(match, leagueids.map(l = > l.leagueid));
                return cb(err);
            });
        } else {
            cb();
        }
  }

        function updateMatchGcData (cb) {
        if (options.type == = 'gcdata') {
            db.raw('UPDATE matches SET series_id = ?, series_type = ? WHERE match_id = ?',[match.series_id, match.series_type, match.match_id]).
            asCallback(cb);
        } else {
            cb();
        }
  }

        function upsertMatch (cb) {
        if (!options.doLogParse) {
            // Skip this if not a pro match (doLogParse true) and not inserting gcdata (series_id/type)
            return cb();
        }
        return db.transaction((trx) = > {
                function upsertMatch(cb){
                upsert(trx, 'matches', match, {
                        match_id:match.match_id,
        },cb);
      }

        function upsertPlayerMatches (cb) {
                async.each(players ||[],(pm, cb) =>{
            pm.match_id = match.match_id;
            // Add lane data
            if (pm.lane_pos) {
            const laneData = utility.getLaneFromPosData(pm.lane_pos, isRadiant(pm));
                pm.lane = laneData.lane || null;
                pm.lane_role = laneData.lane_role || null;
                pm.is_roaming = laneData.is_roaming || null;
            }
            upsert(trx, 'player_matches', pm, {
                    match_id:pm.match_id,
                    player_slot:pm.player_slot,
          },cb);
        },cb);
      }

        function upsertPicksBans (cb) {
                async.each(match.picks_bans ||[],(p, cb) =>{
            // order is a reserved keyword
            p.ord = p.order;
            p.match_id = match.match_id;
            upsert(trx, 'picks_bans', p, {
                    match_id:p.match_id,
                    ord:p.ord,
          },cb);
        },cb);
      }

        function upsertMatchPatch (cb) {
        if (match.start_time) {
            return upsert(trx, 'match_patch', {
                    match_id:match.match_id,
                    patch:constants.patch[utility.getPatchIndex(match.start_time)].name,
          },{
                match_id:
                match.match_id,
            },cb);
        }
        return cb();
      }

        function upsertTeamMatch (cb) {
        const arr = [];
        if (match.radiant_team_id) {
            arr.push({
                    team_id:match.radiant_team_id,
                    match_id:match.match_id,
                    radiant:true,
          });
        }
        if (match.dire_team_id) {
            arr.push({
                    team_id:match.dire_team_id,
                    match_id:match.match_id,
                    radiant:false,
          });
        }
        async.each(arr, (tm, cb) =>{
            upsert(trx, 'team_match', tm, {
                    team_id:tm.team_id,
                    match_id:tm.match_id,
          },cb);
        },cb);
      }

        function upsertTeamRankings (cb) {
        return updateTeamRankings(match, options).then(cb).catch(cb);
      }

        function upsertMatchLogs (cb) {
        if (!match.logs) {
            return cb();
        }
        return trx.raw('DELETE FROM match_logs WHERE match_id = ?',[match.match_id])
          .asCallback((err) = > {
        if (err) {
            return cb(err);
        }
        return async.eachLimit(match.logs, 10, (e, cb) =>{
            cleanRowPostgres(db, 'match_logs', e, (err, cleanedRow) =>{
                if (err) {
                    return cb(err);
                }
                return trx('match_logs').insert(cleanedRow).asCallback(cb);
            });
        },cb);
          });
      }

        function exit (err) {
        if (err) {
            console.error(err);
            trx.rollback(err);
        } else {
            trx.commit();
        }
        cb(err);
      }

        async.series({
                upsertMatch,
                upsertPlayerMatches,
                upsertPicksBans,
                upsertMatchPatch,
                upsertTeamMatch,
                upsertTeamRankings,
                upsertMatchLogs,
        }, exit);
    });
  }

        function upsertMatchCassandra (cb) {
        // console.log('[INSERTMATCH] upserting into Cassandra');
        return cleanRowCassandra(cassandra, 'matches', match, (err, match) =>{
            if (err) {
                return cb(err);
            }
      const obj = serialize(match);
            if (!Object.keys(obj).length) {
                return cb(err);
            }
      const query = util.format(
                    'INSERT INTO matches (%s) VALUES (%s)',
                    Object.keys(obj).join(','),
                    Object.keys(obj).map(() = > '?').join(','),
      );
      const arr = Object.keys(obj).map(k = > ((obj[k] == = 'true' || obj[k] == = 'false') ? JSON.parse(obj[k]) : obj[k]))
            ;
            return cassandra.execute(query, arr, {
                    prepare:true,
      },(err) =>{
                if (err) {
                    return cb(err);
                }
                return async.each(players ||[],(pm, cb) =>{
                    pm.match_id = match.match_id;
                    cleanRowCassandra(cassandra, 'player_matches', pm, (err, pm) =>{
                        if (err) {
                            return cb(err);
                        }
            const obj2 = serialize(pm);
                        if (!Object.keys(obj2).length) {
                            return cb(err);
                        }
            const query2 = util.format(
                                'INSERT INTO player_matches (%s) VALUES (%s)',
                                Object.keys(obj2).join(','),
                                Object.keys(obj2).map(() = > '?').join(','),
            );
            const
                        arr2 = Object.keys(obj2).map(k = > ((obj2[k] == = 'true' || obj2[k] == = 'false') ? JSON.parse(obj2[k]) : obj2[k]))
                        ;
                        return cassandra.execute(query2, arr2, {
                                prepare:true,
            },cb);
                    });
                },cb);
            });
        });
  }

        function updatePlayerCaches (cb) {
        // console.log('[INSERTMATCH] upserting into Cassandra player_caches');
    const copy = createMatchCopy(match, players);
        return insertPlayerCache(copy, cb);
  }

        function telemetry (cb) {
        // console.log('[INSERTMATCH] updating telemetry');
    const types = {
                api:'matches_last_added',
                parsed:'matches_last_parsed',
    };
        if (types[options.type]) {
            redis.lpush(types[options.type], JSON.stringify({
                    match_id:match.match_id,
                    duration:match.duration,
                    start_time:match.start_time,
      }));
            redis.ltrim(types[options.type], 0, 9);
        }
        if (options.type == = 'parsed') {
            redisCount(redis, 'parser');
        }
        if (options.origin == = 'scanner') {
            redisCount(redis, 'added_match');
        }
        return cb();
  }

        function clearMatchCache (cb) {
                redis.del(`match:${match.match_id}`, cb);
  }

        function clearPlayerCaches (cb) {
                async.each((match.players ||[]).filter(player = > Boolean(player.account_id)),(player, cb) =>{
            async.each(cacheFunctions.getKeys(), (key, cb) =>{
                cacheFunctions.update({key, account_id:player.account_id },cb);
            },cb);
        },cb);
  }

        function decideCounts (cb) {
        if (options.skipCounts) {
            return cb();
        }
        if (options.origin == = 'scanner') {
            // 分析存入数据库
            return redis.lpush('countsQueue', JSON.stringify(match), cb);
        }
        return cb();
  }

        function decideScenarios (cb) {
        if (options.doScenarios) {
            return redis.lpush('scenariosQueue', match.match_id, cb);
        }
        return cb();
  }

        function decideParsedBenchmarks (cb) {
        if (options.doParsedBenchmarks) {
            return redis.lpush('parsedBenchmarksQueue', match.match_id, cb);
        }
        return cb();
  }

        function decideMmr (cb) {
                async.each(match.players, (p, cb) = > {
        if (options.origin == = 'scanner'
                && match.lobby_type == = 7
                && p.account_id
                && p.account_id != = utility.getAnonymousAccountId()
                && config.ENABLE_RANDOM_MMR_UPDATE) {
            redis.lpush('mmrQueue', JSON.stringify({
                    match_id:match.match_id,
                    account_id:p.account_id,
        }),cb);
        } else {
            cb();
        }
    },cb);
  }

        function decideProfile (cb) {
                async.each(match.players, (p, cb) = > {
        if ((match.match_id % 100) < Number(config.SCANNER_PLAYER_PERCENT)
                && options.origin == = 'scanner'
                && p.account_id
                && p.account_id != = utility.getAnonymousAccountId()) {
            upsert(db, 'players', {account_id:p.account_id },{
                account_id:
                p.account_id
            },cb);
        } else {
            cb();
        }
    },cb);
  }

        function decideGcData (cb) {
        // Don't get replay URLs for event matches
        if (options.origin == = 'scanner' && match.game_mode != = 19 && (match.match_id % 100) < Number(config.GCDATA_PERCENT)) {
            redis.rpush('gcQueue', JSON.stringify({
                    match_id:match.match_id,
      }),cb);
        } else {
            cb();
        }
  }

        function decideMetaParse (cb) {
                // metaQueue.add()
                cb();
  }

        function decideReplayParse (cb) {
        // (!utility.isSignificant(match) && !options.forceParse)
        if (options.skipParse || (match.game_mode == = 19 && !options.forceParse)) {
            // skipped or event games
            // not parsing this match
            return cb();
        }
        // determine if any player in the match is tracked
        return async.some(match.players, (p, cb) =>{
            redis.zscore('tracked', String(p.account_id), (err, score) =>cb(err, Boolean(score)));
        },(err, hasTrackedPlayer) =>{
            if (err) {
                return cb(err);
            }
      const{
                doLogParse
            } =options;
      const doParse = hasTrackedPlayer || options.forceParse || doLogParse;
            if (doParse) {
                return queue.addJob('parse', {
                        data:{
                    match_id:
                    match.match_id,
                            radiant_win:match.radiant_win,
                            start_time:match.start_time,
                            duration:match.duration,
                            replay_blob_key:match.replay_blob_key,
                            pgroup:match.pgroup,
                            doLogParse,
                            ability_upgrades:abilityUpgrades,
                            allowBackup:options.allowBackup,
                            origin:options.origin,
                },
        },{
                    priority:
                    options.priority,
                            attempts:options.attempts || 15,
                },cb);
            }
            return cb();
        });
  }
        async.series({
                preprocess,
                tellFeed,
                decideLogParse,
                updateMatchGcData,
                upsertMatch,
                upsertMatchCassandra,
                updatePlayerCaches,
                clearMatchCache,
                clearPlayerCaches,
                telemetry,
                decideCounts,
                decideScenarios,
                decideParsedBenchmarks,
                decideMmr,
                decideProfile,
                decideGcData,
                decideMetaParse,
                decideReplayParse,
        }, (err, results) =>{
            cb(err, results.decideReplayParse);
        });
    }

    private Object preprocess (Callback cb) {
        // don't insert anonymous account id
        if (players!=null) {
            players.forEach(p -> {
            if (utility.getAnonymousAccountId().equals(p.get("account_id").asLong())) {
                ((ObjectNode)p).remove("account_id");
            }
      });
        }
        // if we have a pgroup from earlier, use it to fill out hero_ids (used after parse)
        if (players!=null && match.get("pgroup")!=null) {
            players.forEach((p-> {
            if (match.get("pgroup").get(p.get("player_slot").asInt())!=null) {
                //对象不是值是否有问题
                ((ObjectNode)p).set("hero_id",match.get("pgroup").get(p.get("player_slot").asInt()).get("hero_id"));
            }
      });
        }
        // build match.pgroup so after parse we can figure out the account_ids for each slot
        if (players!=null && match.get("pgroup")==null) {
            match.pgroup = {};
            players.forEach(p -> {
                    match.pgroup[p.player_slot] = {
                            account_id:p.account_id || null,
                    hero_id:p.hero_id,
                    player_slot:p.player_slot,
        };
      });
        }
        // ability_upgrades_arr
        if (players!=null) {
            players.forEach(p-> {
            if (p.ability_upgrades) {
                p.ability_upgrades_arr = p.ability_upgrades.map(au = > au.ability);
          const abilityLvls = {};
                p.ability_upgrades.forEach((au) = > {
                if (au.ability in savedAbilityLvls){
                    abilityLvls[au.ability] = (abilityLvls[au.ability] || 0) + 1;
              const abilityUpgrade = Object.assign({}, au, {
                            level:abilityLvls[au.ability],
              });
                    abilityUpgrades.push(abilityUpgrade);
                }
          });
            }
      });
        }
        cb();
    }


}
