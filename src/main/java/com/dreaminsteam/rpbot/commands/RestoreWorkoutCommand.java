package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.utilities.PlayerResetHandler;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class RestoreWorkoutCommand implements CommandExecutor{

	@Command(aliases = {"!restoreWorkouts"}, description="Restore the ability to workout for the day for an individual, or the entire server. **Admin Only!**", usage = "!restoreWorkouts <playerId>", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception {
		
		if(!CommandUtils.hasAdminRole(user, channel)){
			return user.mention() + "  Sorry, you're just not swole enough.";
		}
		
		if (args == null || args.length == 0){
			PlayerResetHandler.resetWorkout();
			
			return user.mention() + "  Success! Workouts have been enabled for all students.";
		}
		
		String playerId = args[0];
		Player player = DatabaseUtil.getPlayerDao().queryForId(playerId);
		
		if(player == null){
			return " I can't find a player with that ID... try using **!playerInfo** to get the list of players.";
		}
		
		player.allowWorkout(true);
		
		DatabaseUtil.getPlayerDao().update(player);
		
		return user.mention() + "  Success! " + player.getName() + " can workout again today.";
		
	}
}
