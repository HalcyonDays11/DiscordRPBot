package com.dreaminsteam.rpbot.db;

import java.sql.SQLException;

import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.H2DatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseUtil {
	
	private static Dao<Spell, String> spellDao;
	private static Dao<Player, String> playerDao;
	private static ConnectionSource connectionSource;
	
	public static void setupConnection() throws SQLException{
		connectionSource = new JdbcConnectionSource("jdbc:h2:./data/RPBotData");
		
		spellDao = DaoManager.createDao(connectionSource, Spell.class);
		playerDao = DaoManager.createDao(connectionSource, Player.class);
		
	}
	
	public static void setupTestDb() throws Exception{
		if (!playerDao.isTableExists()){
			TableUtils.createTable(playerDao);
		}
//		if (!spellDao.isTableExists()){
//			TableUtils.createTable(spellDao);
//		}
		
//		Player kat = new Player();
//		kat.setSnowflakeId("1234");
//		kat.setName("Kat");
//		
//		Player jess = new Player();
//		jess.setName("Jess");
//		jess.setSnowflakeId("5678");
//		
//		playerDao.create(kat);
//		playerDao.create(jess);
	}
	
	public static Dao<Player, String> getPlayerDao() {
		return playerDao;
	}
	
	public static Dao<Spell, String> getSpellDao() {
		return spellDao;
	}
	
	public static void disconnect() throws Exception{
		connectionSource.close();
	}
	
	public static ConnectionSource getConnectionSource() {
		return connectionSource;
	}

	
}
