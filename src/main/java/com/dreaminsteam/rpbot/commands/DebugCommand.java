package com.dreaminsteam.rpbot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class DebugCommand implements CommandExecutor {

	@Command(aliases = {"!debug"}, description="Debug Info", usage = "!debug [any number of args]", async = true)
	public String debug(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		StringBuilder sb = new StringBuilder();
		
		sb.append("User: " + user.getName());
		sb.append("\n");
		sb.append("channel: " + channel.getName());
		sb.append("\n");
		sb.append("command:" + command);
		sb.append("\n");
		sb.append("arguments: " + args.length);
		for (String arg : args){
			sb.append("\t" + arg + "\n");
		}
		
		return sb.toString();
	}
}
