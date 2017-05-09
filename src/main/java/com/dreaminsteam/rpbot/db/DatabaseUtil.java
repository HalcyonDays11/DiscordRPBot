package com.dreaminsteam.rpbot.db;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.dreaminsteam.rpbot.db.models.Spellbook;
import com.dreaminsteam.rpbot.utilities.Year;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.H2DatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class DatabaseUtil {
	
	private static Dao<Spell, String> spellDao;
	private static Dao<Player, String> playerDao;
	private static Dao<Spellbook, Long> spellbookDao;
	private static ConnectionSource connectionSource;
	
	public static void setupConnection() throws SQLException{
		connectionSource = new JdbcConnectionSource("jdbc:h2:./data/RPBotData");
		
		spellDao = DaoManager.createDao(connectionSource, Spell.class);
		playerDao = DaoManager.createDao(connectionSource, Player.class);
		spellbookDao = DaoManager.createDao(connectionSource, Spellbook.class);
	}
	
	public static void setupDbIfNecessary(boolean clearExisting) throws Exception{
		if(!playerDao.isTableExists()){
			TableUtils.createTable(playerDao);
		}else{
			if(clearExisting){
				TableUtils.dropTable(playerDao, true);
				TableUtils.createTable(playerDao);
			}
		}
		
		if(!spellbookDao.isTableExists()){
			TableUtils.createTable(spellbookDao);
		}else{
			if(clearExisting){
				TableUtils.dropTable(spellbookDao, true);
				TableUtils.createTable(spellbookDao);
			}
		}
	}
	
	public static Dao<Player, String> getPlayerDao() {
		return playerDao;
	}
	
	public static Dao<Spell, String> getSpellDao() {
		return spellDao;
	}
	public static Dao<Spellbook, Long> getSpellbookDao() {
		return spellbookDao;
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
				player = new Player(userObj.getStringID(), userObj.getName());
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

	public static Spellbook getOrCreateSpellbook(Player player, Spell spell){
		try {
			QueryBuilder<Spellbook,Long> queryBuilder = getSpellbookDao().queryBuilder();
			Where<Spellbook,Long> query = queryBuilder.where().eq("player_id", player.getSnowflakeId()).and().eq("spell_id", spell.getIncantation());
			Spellbook spellbook = query.queryForFirst();
			if(spellbook == null){
				spellbook = new Spellbook(player, spell);
			}
			return spellbook;
		} catch (SQLException e) {
			System.out.println("Error querying for spellbook: ");
			e.printStackTrace();
			return new Spellbook(player, spell);
		}
	}
	
	public static void updateSpellbook(Spellbook spellbook){
		try {
			getSpellbookDao().createOrUpdate(spellbook);
		} catch (SQLException e) {
			System.out.println("Error saving spellbook: ");
			e.printStackTrace();
		}
	}
	
}
