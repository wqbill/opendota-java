CREATE TABLE IF NOT EXISTS matches (
		match_id BIGINT PRIMARY KEY,
		match_seq_num BIGINT,
		radiant_win boolean,
		start_time INTEGER,
		duration INTEGER,
		tower_status_radiant INTEGER,
		tower_status_dire INTEGER,
		barracks_status_radiant INTEGER,
		barracks_status_dire INTEGER,
		cluster INTEGER,
		first_blood_time INTEGER,
		lobby_type INTEGER,
		human_players INTEGER,
		leagueid INTEGER,
		positive_votes INTEGER,
		negative_votes INTEGER,
		game_mode INTEGER,
		ENGINE INTEGER,
		radiant_score INTEGER,
		dire_score INTEGER,
		picks_bans json,
		radiant_team_id INTEGER,
		dire_team_id INTEGER,
		radiant_team_name VARCHAR ( 255 ),
		dire_team_name VARCHAR ( 255 ),
		radiant_team_complete SMALLINT,
		dire_team_complete SMALLINT,
		radiant_captain BIGINT,
		dire_captain BIGINT,
		chat json,
		objectives json,
		radiant_gold_adv json,
		radiant_xp_adv json,
		teamfights json,
		draft_timings json,
		version INTEGER,
		cosmetics json,
		series_id INTEGER,
		series_type INTEGER 
	);
CREATE INDEX matches_leagueid_idx ON matches ( leagueid );
CREATE TABLE IF NOT EXISTS player_matches (
		PRIMARY KEY ( match_id, player_slot ),
		match_id BIGINT REFERENCES matches ( match_id ) ON DELETE CASCADE,
		account_id BIGINT,
		player_slot INTEGER,
		hero_id INTEGER,
		item_0 INTEGER,
		item_1 INTEGER,
		item_2 INTEGER,
		item_3 INTEGER,
		item_4 INTEGER,
		item_5 INTEGER,
		backpack_0 INTEGER,
		backpack_1 INTEGER,
		backpack_2 INTEGER,
		kills INTEGER,
		deaths INTEGER,
		assists INTEGER,
		leaver_status INTEGER,
		gold INTEGER,
		last_hits INTEGER,
		denies INTEGER,
		gold_per_min INTEGER,
		xp_per_min INTEGER,
		gold_spent INTEGER,
		hero_damage INTEGER,
		tower_damage BIGINT,
		hero_healing BIGINT,
		`level` INTEGER,
		additional_units json,
		stuns REAL,
		max_hero_hit json,
		times json,
		gold_t json,
		lh_t json,
		dn_t json,
		xp_t json,
		obs_log json,
		sen_log json,
		obs_left_log json,
		sen_left_log json,
		purchase_log json,
		kills_log json,
		buyback_log json,
		runes_log json,
		connection_log json,
		lane_pos json,
		obs json,
		sen json,
		actions json,
		pings json,
		purchase json,
		gold_reasons json,
		xp_reasons json,
		killed json,
		item_uses json,
		ability_uses json,
		ability_targets json,
		damage_targets json,
		hero_hits json,
		damage json,
		damage_taken json,
		damage_inflictor json,
		runes json,
		killed_by json,
		kill_streaks json,
		multi_kills json,
		life_state json,
		damage_inflictor_received json,
		obs_placed INT,
		sen_placed INT,
		creeps_stacked INT,
		camps_stacked INT,
		rune_pickups INT,
		ability_upgrades_arr json,
		party_id INT,
		permanent_buffs json,
		lane INT,
		lane_role INT,
		is_roaming boolean,
		firstblood_claimed INT,
		teamfight_participation REAL,
		towers_killed INT,
		roshans_killed INT,
		observers_placed INT,
		party_size INT 
	);
CREATE INDEX player_matches_account_id_idx ON player_matches ( account_id );
CREATE INDEX player_matches_hero_id_idx ON player_matches ( hero_id );
CREATE TABLE IF NOT EXISTS players (
		account_id BIGINT PRIMARY KEY,
		steamid VARCHAR ( 32 ),
		avatar VARCHAR ( 255 ),
		avatarmedium VARCHAR ( 255 ),
		avatarfull VARCHAR ( 255 ),
		profileurl VARCHAR ( 255 ),
		personaname VARCHAR ( 255 ),
		plus boolean DEFAULT FALSE,
		last_login TIMESTAMP null,
		full_history_time TIMESTAMP null,
		cheese INTEGER DEFAULT 0,
		fh_unavailable boolean,
		loccountrycode VARCHAR ( 2 ),
		last_match_time TIMESTAMP null
	);
CREATE INDEX players_cheese_idx ON players ( cheese );
CREATE INDEX players_personaname_idx_gin ON players ( personaname  );
CREATE TABLE IF NOT EXISTS player_ratings ( PRIMARY KEY ( account_id, time ), account_id BIGINT, match_id BIGINT, solo_competitive_rank INTEGER, competitive_rank INTEGER, time TIMESTAMP  );
CREATE TABLE IF NOT EXISTS subscriptions ( PRIMARY KEY ( customer_id ), account_id BIGINT REFERENCES players ( account_id ) ON DELETE CASCADE, customer_id VARCHAR ( 255 ), amount INT, active_until date );
CREATE INDEX subscriptions_account_id_idx ON subscriptions ( account_id );
CREATE INDEX subscriptions_customer_id_idx ON subscriptions ( customer_id );
CREATE TABLE IF NOT EXISTS webhooks ( PRIMARY KEY ( hook_id ), hook_id varchar(36) UNIQUE, account_id BIGINT, url text NOT NULL, subscriptions json NOT NULL );
CREATE INDEX webhooks_account_id_idx ON webhooks ( account_id );
CREATE TABLE IF NOT EXISTS api_keys ( PRIMARY KEY ( account_id ), account_id BIGINT UNIQUE, api_key varchar(36) UNIQUE, customer_id text NOT NULL, subscription_id text NOT NULL );
CREATE TABLE IF NOT EXISTS api_key_usage (
		PRIMARY KEY ( account_id, api_key, ip, `timestamp` ),
		account_id BIGINT REFERENCES api_keys ( account_id ),
		customer_id text,
		api_key varchar(36),
		usage_count BIGINT,
		ip varchar(15),
	    `timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	);
CREATE INDEX api_keys_usage_account_id_idx ON api_key_usage ( account_id );
CREATE INDEX api_keys_usage_timestamp_idx ON api_key_usage ( `timestamp` );
CREATE TABLE IF NOT EXISTS user_usage ( account_id BIGINT, ip varchar(15), usage_count BIGINT, `timestamp` TIMESTAMP DEFAULT CURRENT_TIMESTAMP );
CREATE INDEX user_usage_account_id_idx ON user_usage ( account_id );
CREATE INDEX user_usage_timestamp_idx ON user_usage ( `timestamp` );
CREATE UNIQUE INDEX user_usage_unique_idx ON user_usage ( account_id, ip, TIMESTAMP );
CREATE TABLE IF NOT EXISTS notable_players (
		account_id BIGINT PRIMARY KEY,
		`name` VARCHAR ( 255 ),
		country_code VARCHAR ( 2 ),
		fantasy_role INT,
		team_id INT,
		team_name VARCHAR ( 255 ),
		team_tag VARCHAR ( 255 ),
		is_locked boolean,
		is_pro boolean,
		locked_until INTEGER 
	);
CREATE TABLE IF NOT EXISTS match_logs (
		match_id BIGINT REFERENCES matches ( match_id ) ON DELETE CASCADE,
		`time` INT,
		`type` VARCHAR ( 100 ),
		team SMALLINT,
		unit VARCHAR ( 100 ),
		`key` VARCHAR ( 1000 ),
		`value` INT,
		slot SMALLINT,
		player_slot SMALLINT,
		player1 INT,
		player2 INT,
		attackerhero boolean,
		targethero boolean,
		attackerillusion boolean,
		targetillusion boolean,
		inflictor VARCHAR ( 100 ),
		gold_reason SMALLINT,
		xp_reason SMALLINT,
		attackername VARCHAR ( 100 ),
		targetname VARCHAR ( 100 ),
		sourcename VARCHAR ( 100 ),
		targetsourcename VARCHAR ( 100 ),
		valuename VARCHAR ( 100 ),
		gold INT,
		lh INT,
		xp INT,
		x SMALLINT,
		y SMALLINT,
		z SMALLINT,
		entityleft boolean,
		ehandle INT,
		stuns REAL,
		hero_id SMALLINT,
		life_state SMALLINT,
		`level` SMALLINT,
		kills SMALLINT,
		deaths SMALLINT,
		assists SMALLINT,
		denies SMALLINT,
		attackername_slot SMALLINT,
		targetname_slot SMALLINT,
		sourcename_slot SMALLINT,
		targetsourcename_slot SMALLINT,
		player1_slot SMALLINT,
		obs_placed INT,
		sen_placed INT,
		creeps_stacked INT,
		camps_stacked INT,
		rune_pickups INT 
	);
CREATE INDEX match_logs_match_id_idx ON match_logs ( match_id );
CREATE INDEX match_logs_match_id_player_slot_idx ON match_logs ( match_id, player_slot );
CREATE INDEX match_logs_match_id_player1_slot_idx ON match_logs ( match_id, player1_slot ) ;
CREATE INDEX match_logs_match_id_attackername_slot_idx ON match_logs ( match_id, attackername_slot ) ;
CREATE INDEX match_logs_match_id_targetname_slot_idx ON match_logs ( match_id, targetname_slot ) ;
CREATE INDEX match_logs_match_id_sourcename_slot_idx ON match_logs ( match_id, sourcename_slot ) ;
CREATE INDEX match_logs_match_id_targetsourcename_slot_idx ON match_logs ( match_id, targetsourcename_slot ) ;
CREATE INDEX match_logs_match_id_valuename_idx ON match_logs ( match_id, valuename ) ;
CREATE INDEX match_logs_match_id_type_idx ON match_logs ( match_id, type );
CREATE INDEX match_logs_valuename_idx ON match_logs ( valuename ) ;
CREATE INDEX match_logs_type_idx ON match_logs ( type );
CREATE TABLE IF NOT EXISTS picks_bans (
		match_id BIGINT REFERENCES matches ( match_id ) ON DELETE CASCADE,
		is_pick boolean,
		hero_id INT,
		team SMALLINT,
		ord SMALLINT,
		PRIMARY KEY ( match_id, ord ) 
	);
CREATE TABLE IF NOT EXISTS leagues ( leagueid BIGINT PRIMARY KEY, ticket VARCHAR ( 255 ), banner VARCHAR ( 255 ), tier VARCHAR ( 255 ), NAME VARCHAR ( 255 ) );
CREATE TABLE IF NOT EXISTS teams ( team_id BIGINT PRIMARY KEY, NAME VARCHAR ( 255 ), tag VARCHAR ( 255 ), logo_url text );
CREATE TABLE IF NOT EXISTS heroes ( id INT PRIMARY KEY, NAME text, localized_name text, primary_attr text, attack_type text, roles json );
CREATE TABLE IF NOT EXISTS match_patch ( match_id BIGINT REFERENCES matches ( match_id ) ON DELETE CASCADE , patch text,PRIMARY KEY(match_id) );
CREATE TABLE IF NOT EXISTS team_match ( team_id BIGINT, match_id BIGINT REFERENCES matches ( match_id ) ON DELETE CASCADE, radiant boolean, PRIMARY KEY ( team_id, match_id ) );
CREATE TABLE IF NOT EXISTS match_gcdata ( match_id BIGINT PRIMARY KEY, cluster INT, replay_salt INT, series_id INT, series_type INT );
CREATE TABLE IF NOT EXISTS items ( id INT PRIMARY KEY, NAME text, cost INT, secret_shop SMALLINT, side_shop SMALLINT, recipe SMALLINT, localized_name text );
CREATE TABLE IF NOT EXISTS cosmetics (
		item_id INT PRIMARY KEY,
		`name` text,
		prefab text,
		creation_date TIMESTAMP ,
		image_inventory text,
		image_path text,
		item_description text,
		item_name text,
		item_rarity text,
		item_type_name text,
		used_by_heroes text 
	);
CREATE TABLE IF NOT EXISTS public_matches (
		match_id BIGINT PRIMARY KEY,
		match_seq_num BIGINT,
		radiant_win boolean,
		start_time INTEGER,
		duration INTEGER,
		avg_mmr INTEGER,
		num_mmr INTEGER,
		lobby_type INTEGER,
		game_mode INTEGER,
		avg_rank_tier DOUBLE PRECISION,
		num_rank_tier INTEGER,
		cluster INTEGER 
	);
CREATE INDEX public_matches_start_time_idx ON public_matches ( start_time );
CREATE INDEX public_matches_avg_mmr_idx ON public_matches ( avg_mmr );
CREATE INDEX public_matches_avg_rank_tier_idx ON public_matches ( avg_rank_tier );
CREATE TABLE IF NOT EXISTS public_player_matches ( PRIMARY KEY ( match_id, player_slot ), match_id BIGINT REFERENCES public_matches ( match_id ) ON DELETE CASCADE, player_slot INTEGER, hero_id INTEGER );
CREATE INDEX public_player_matches_hero_id_idx ON public_player_matches ( hero_id );
CREATE INDEX public_player_matches_match_id_idx ON public_player_matches ( match_id );
CREATE TABLE IF NOT EXISTS team_rating ( PRIMARY KEY ( team_id ), team_id BIGINT, rating REAL, wins INT, losses INT, last_match_time BIGINT );
CREATE INDEX team_rating_rating_idx ON team_rating ( rating );
CREATE TABLE IF NOT EXISTS hero_ranking ( PRIMARY KEY ( account_id, hero_id ), account_id BIGINT, hero_id INT, score DOUBLE PRECISION );
CREATE INDEX
hero_ranking_hero_id_score_idx ON hero_ranking ( hero_id, score );
CREATE TABLE IF NOT EXISTS queue (
		PRIMARY KEY ( id ),
		id bigint,
		type text,
		`timestamp` TIMESTAMP null,
		attempts INT,
		`data` json,
		next_attempt_time TIMESTAMP null,
		priority INT 
	);
CREATE INDEX queue_priority_id_idx ON queue ( priority, id );
CREATE TABLE IF NOT EXISTS mmr_estimates ( PRIMARY KEY ( account_id ), account_id BIGINT, estimate INT );
CREATE TABLE IF NOT EXISTS solo_competitive_rank ( PRIMARY KEY ( account_id ), account_id BIGINT, rating INT );
CREATE TABLE IF NOT EXISTS competitive_rank ( PRIMARY KEY ( account_id ), account_id BIGINT, rating INT );
CREATE TABLE IF NOT EXISTS rank_tier ( PRIMARY KEY ( account_id ), account_id BIGINT, rating INT );
CREATE TABLE IF NOT EXISTS leaderboard_rank ( PRIMARY KEY ( account_id ), account_id BIGINT, rating INT );
CREATE TABLE IF NOT EXISTS scenarios (
		hero_id SMALLINT,
		item text,
		time INTEGER,
		lane_role SMALLINT,
		games BIGINT DEFAULT 1,
		wins BIGINT,
		epoch_week INTEGER,
		UNIQUE ( hero_id, item(255), time, epoch_week ),
		UNIQUE ( hero_id, lane_role, time, epoch_week ) 
	);
CREATE TABLE IF NOT EXISTS team_scenarios (
		scenario text,
		is_radiant boolean,
		region SMALLINT,
		games BIGINT DEFAULT 1,
		wins BIGINT,
		epoch_week INTEGER,
		UNIQUE ( scenario(255), is_radiant, region, epoch_week ) 
	);
CREATE TABLE IF NOT EXISTS hero_search ( match_id BIGINT, teamA varchar(1000), teamB varchar(1000), teamAWin boolean, start_time INT );
CREATE INDEX hero_search_teamA_idx_gin ON hero_search ( teamA );
CREATE INDEX hero_search_teamB_idx_gin ON hero_search ( teamB );