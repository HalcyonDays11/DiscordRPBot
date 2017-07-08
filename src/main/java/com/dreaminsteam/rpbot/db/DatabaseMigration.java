package com.dreaminsteam.rpbot.db;

import java.util.ArrayList;
import java.util.List;

import com.dreaminsteam.rpbot.db.migrations.AddWorkoutMigration;
import com.dreaminsteam.rpbot.db.migrations.FixLearnLimitationMigration;
import com.dreaminsteam.rpbot.db.models.Player;
import com.j256.ormlite.dao.Dao;

public abstract class DatabaseMigration<T, S> {

	public abstract void migrateIfNeeded(Dao<T, S> dao);
	
	private static List<DatabaseMigration<Player, String>> playerMigrations = new ArrayList<DatabaseMigration<Player, String>>();
	
	static {
		playerMigrations.add(new AddWorkoutMigration());
		playerMigrations.add(new FixLearnLimitationMigration());
	}
	
	public static void performMigrations(){
		Dao<Player,String> playerDao = DatabaseUtil.getPlayerDao();
		playerMigrations.forEach((migration) -> {
			migration.migrateIfNeeded(playerDao);
		});
	}
	
	
}
