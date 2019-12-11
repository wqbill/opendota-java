package com.wqbill.opendota.util

@NoArg
data class MatchDetail(
        val result: Result
)

@NoArg
data class Result(
        val barracks_status_dire: Int,
        val barracks_status_radiant: Int,
        val cluster: Int,
        val dire_score: Int,
        val duration: Int,
        val engine: Int,
        val first_blood_time: Int,
        val flags: Int,
        val game_mode: Int,
        val human_players: Int,
        val leagueid: Int,
        val lobby_type: Int,
        val match_id: Long,
        val match_seq_num: Long,
        val negative_votes: Int,
        val players: List<Player>,
        val positive_votes: Int,
        val pre_game_duration: Int,
        val radiant_score: Int,
        val radiant_win: Boolean,
        val start_time: Int,
        val tower_status_dire: Int,
        val tower_status_radiant: Int
)

@NoArg
data class Player(
        val ability_upgrades: List<AbilityUpgrade>,
        val account_id: Long,
        val assists: Int,
        val backpack_0: Int,
        val backpack_1: Int,
        val backpack_2: Int,
        val deaths: Int,
        val denies: Int,
        val gold: Int,
        val gold_per_min: Int,
        val gold_spent: Int,
        val hero_damage: Int,
        val hero_healing: Int,
        val hero_id: Int,
        val item_0: Int,
        val item_1: Int,
        val item_2: Int,
        val item_3: Int,
        val item_4: Int,
        val item_5: Int,
        val kills: Int,
        val last_hits: Int,
        val leaver_status: Int,
        val level: Int,
        val player_slot: Int,
        val scaled_hero_damage: Int,
        val scaled_hero_healing: Int,
        val scaled_tower_damage: Int,
        val tower_damage: Int,
        val xp_per_min: Int
)

@NoArg
data class AbilityUpgrade(
        val ability: Int,
        val level: Int,
        val time: Int
)