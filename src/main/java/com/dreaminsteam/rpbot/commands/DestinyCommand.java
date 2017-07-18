package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.utilities.Year;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class DestinyCommand implements CommandExecutor {

	@Command(aliases = {"!destiny"}, description="Find out how many Destiny Points you have remaining today.", usage="!destiny")
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception{
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		if (args.length > 0 && args[0].equalsIgnoreCase("total")){
			Year year = player.getCurrentYear();
			return user.mention() + "  Your rank of " + year.getPrettyName() + " affords you " + year.getDailyDestinyPoints() + " daily destiny points.";
		} //How does this return? I've never actually seen this come up lol.
		
		return user.mention() + "  You have " + player.getAvailableDestinyPoints() + " destiny points remaining for today."; 
		
		
				

	}
}
