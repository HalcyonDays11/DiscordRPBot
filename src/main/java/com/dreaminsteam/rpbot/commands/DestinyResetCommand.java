package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.util.DestinyPointResetHandler;
import com.dreaminsteam.rpbot.utilities.Year;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class DestinyResetCommand implements CommandExecutor{
		
		@Command(aliases = {"!resetDestiny"}, description="reset all Destiny Points.  Usable only by Professors", usage = "!resetDestiny", async = true)
		public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
			Player callingPlayer = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
			if(callingPlayer.getCurrentYear() != Year.MASTERY){
				return user.mention() + " Sorry, you are not yet a master of your own destiny.";
			}
			
			DestinyPointResetHandler.resetAllDestinyPoints();
			
			return user.mention() + " Success!";
			
		}
}
