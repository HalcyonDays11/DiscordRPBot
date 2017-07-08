package com.dreaminsteam.rpbot.db.migrations;

import java.sql.SQLException;

import com.dreaminsteam.rpbot.db.DatabaseMigration;
import com.dreaminsteam.rpbot.db.models.Player;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;

public class FixLearnLimitationMigration extends DatabaseMigration<Player, String>{

	@Override
	public void migrateIfNeeded(Dao<Player, String> dao) {
		try{
			dao.queryForEq("canPracticeToday", true);
		}catch(SQLException e){
			try {
				dao.executeRawNoArgs("ALTER TABLE players ADD COLUMN canPracticeToday BOOLEAN;");
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
	}

}
