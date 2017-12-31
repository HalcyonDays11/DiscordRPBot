package com.dreaminsteam.rpbot.commands;

import java.sql.SQLException;
import java.util.Date;

import org.pmw.tinylog.Logger;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spellbook;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class WorkoutCommand implements CommandExecutor{
	
	public static final int POINTS_PER_WORKOUT = 28;

	
	@Command(aliases = {"!workout"}, description="Please use the new States bot! use s!help to learn more!", usage="!workout")
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		if(!player.canWorkoutToday()){
			return user.mention() + "  You should try out new Stats Bot! Use s!help to learn more! (Antiquated info: You're going to pull something! You've already worked out once today.)";
		}
		
		int currentAgility = player.getCurrentAgility();
		int agilityProgress = player.getCurrentAgility() % POINTS_PER_WORKOUT;
		int agilityModifier = player.getCurrentAgility() / POINTS_PER_WORKOUT;
		player.setCurrentAgility(currentAgility + 1);
		player.updateLastWorkoutDate(new Date());
		
		try {
			DatabaseUtil.getPlayerDao().update(player);
			return user.mention() + "  You should try out new Stats Bot! Use s!help to learn more! (Antiquated info: Great workout! Your agility bonus is **" + agilityModifier + "** and you have **" + (agilityProgress + "/" + POINTS_PER_WORKOUT) + "** points toward your next bonus.)";
		} catch (SQLException e) {
			Logger.error(e, "Error updating player");
			return user.mention() + "  Uh... the gym wasn't working... try working out again.";
		}
	}
}
