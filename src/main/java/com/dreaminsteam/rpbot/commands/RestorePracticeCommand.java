package com.dreaminsteam.rpbot.commands;

import java.sql.SQLException;

import org.pmw.tinylog.Logger;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spellbook;
import com.dreaminsteam.rpbot.utilities.PlayerResetHandler;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class RestorePracticeCommand implements CommandExecutor{
	
	@Command(aliases = {"!restorePractice"}, description="Restore daily practice limit for a single user or the entire server. **Admin Only!**", usage = "!restorePractice <playerId>", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception {
		
		if(!CommandUtils.hasAdminRole(user, channel)){
			return user.mention() + "  Nice try, go do your homework.";
		}
		
		if (args == null || args.length == 0){
			PlayerResetHandler.resetPractice();
			
			return user.mention() + "  Success! Practice limit has been restored for all students.";
		}
		
		String playerId = args[0];
		Player player = DatabaseUtil.getPlayerDao().queryForId(playerId);
		
		if(player == null){
			return " I can't find a player with that ID... try using **!playerInfo** to get the list of players.";
		}
		
		player.allowPractice(true);
		
		Dao<Spellbook,Long> spellbookDao = DatabaseUtil.getSpellbookDao();
		UpdateBuilder<Spellbook, Long> updateColumn = spellbookDao.updateBuilder().updateColumnValue("castAttemptsToday", 0);
		updateColumn.where().eq("player_id", player.getSnowflakeId());
		
		try{
			spellbookDao.update(updateColumn.prepare());
			DatabaseUtil.getPlayerDao().update(player);			
		}catch(SQLException e){
			Logger.error(e);
		}
		
		return user.mention() + "  Success! " + player.getName() + " now has **" + player.getAvailableDestinyPoints() + "** destiny points for today.";
		
	}

}
