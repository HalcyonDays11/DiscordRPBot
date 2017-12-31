package com.dreaminsteam.rpbot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class BotCheckCommand implements CommandExecutor {

	@Command(aliases = {"!botCheck"}, description="MH Bot Role Call!!", usage = "!botCheck", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception{
		
		return "Back, you scurvy braggarts, you rogues!";
	}
		
}
