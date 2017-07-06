package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class WorkoutCommand implements CommandExecutor{
	
	@Command(aliases = {"!workout"}, description="Get in your daily workout.", usage="!workout")
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		public static final int POINTS_PER_WORK = 3; //Why
		
		return user.mention() + " Great workout!";
	}
		
}

//I have no idea what I'm doing...
