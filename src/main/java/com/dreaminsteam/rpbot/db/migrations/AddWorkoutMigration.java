package com.dreaminsteam.rpbot.db.migrations;

import java.sql.SQLException;
import java.util.Date;

import com.dreaminsteam.rpbot.db.DatabaseMigration;
import com.dreaminsteam.rpbot.db.models.Player;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableInfo;

public class AddWorkoutMigration extends DatabaseMigration<Player, String>{

	@Override
	public void migrateIfNeeded(Dao<Player, String> dao) {
		try{
			dao.queryForEq("canWorkoutToday", "true");
		}
		catch(SQLException e){
			try {
				dao.executeRawNoArgs("IF NOT EXISTS TABLEALTER TABLE players ADD COLUMN canWorkoutToday BOOLEAN;");
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		
		try{
			dao.queryForEq("lastWorkoutDate", new Date());
		}
		catch(SQLException e){
			try {
				dao.executeRawNoArgs("ALTER TABLE players ADD COLUMN lastWorkoutDate DATE;");
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		
		try{
			dao.queryForEq("currentAgility", 0);
		}
		catch(SQLException e){
			try {
				dao.executeRawNoArgs("ALTER TABLE players ADD COLUMN currentAgility INTEGER;");
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		

	}
	
}
