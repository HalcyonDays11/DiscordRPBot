package com.dreaminsteam.rpbot.commands;

import java.sql.SQLException;
import java.util.Date;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class WorkoutCommand implements CommandExecutor{
	
	public static final int POINTS_PER_WORKOUT = 3; //Why
	
	@Command(aliases = {"!workout"}, description="Get in your daily workout.", usage="!workout")
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		if(!player.canWorkoutToday()){
			return user.mention() + " You're going to pull something!  You've already worked out once today.";
		}
		
		int currentAgility = player.getCurrentAgility();
		player.setCurrentAgility(currentAgility + 1);
		player.updateLastWorkoutDate(new Date());
		
		try {
			DatabaseUtil.getPlayerDao().update(player);
			return user.mention() + " Great workout!";
		} catch (SQLException e) {
			e.printStackTrace();
			return user.mention() + " Uh... the gym wasn't working... try working out again.";
		}
	}
}
