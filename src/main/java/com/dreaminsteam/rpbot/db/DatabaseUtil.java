package com.dreaminsteam.rpbot.db;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.dreaminsteam.rpbot.utilities.Year;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.H2DatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class DatabaseUtil {
	
	private static Dao<Spell, String> spellDao;
	private static Dao<Player, String> playerDao;
	private static ConnectionSource connectionSource;
	
	public static void setupConnection() throws SQLException{
		connectionSource = new JdbcConnectionSource("jdbc:h2:./data/RPBotData");
		
		spellDao = DaoManager.createDao(connectionSource, Spell.class);
		playerDao = DaoManager.createDao(connectionSource, Player.class);
		
	}
	
	public static void setupDbIfNecessary() throws Exception{
		if(!playerDao.isTableExists()){
			TableUtils.createTable(playerDao);
		}
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
	
	public static Player createOrUpdatePlayer(IUser userObj, IGuild guild){
		try{
			List<Player> playerList = getPlayerDao().queryForEq("snowflakeId", userObj.getStringID());
			Player player;
			if(!playerList.isEmpty()){
				player = playerList.get(0);
			}else{
				player = new Player(userObj.getName(), userObj.getStringID());
			}
			List<IRole> rolesForGuild = userObj.getRolesForGuild(guild);
			List<String> roleNames = rolesForGuild.stream().map(role -> role.getName()).collect(Collectors.toList());
			Year yearFromRoleList = Year.getYearFromRoleList(roleNames);
			player.setCurrentYear(yearFromRoleList);
			
			getPlayerDao().createOrUpdate(player);
			return player;
		}catch(Exception e){
			System.out.println("Error querying for player: ");
			e.printStackTrace();
			return null;
		}
	}
	
	public static Spell findSpell(String incantation){
		try{
			List<Spell> spellList = getSpellDao().queryForEq("incantation", incantation);
			if(spellList.isEmpty()){
				return null;
			}else{
				return spellList.get(0);
			}
		}catch(Exception e){
			System.out.println("Error querying for spell: ");
			e.printStackTrace();
			return null;
		}
	}

	
}
