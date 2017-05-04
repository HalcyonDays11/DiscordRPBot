package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.RPBot;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler.SimpleCommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class HelpCommand implements CommandExecutor {

	@Command(aliases = {"!help"}, description="a helpful list of all availble commands", async = true)
	public String printAvailableCommands(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		StringBuilder sb = new StringBuilder();
		sb.append("things you can do: \n");
		for (SimpleCommand cmd : RPBot.getBotInstance().getCommands()){
			sb.append("\t **" + cmd.getCommandAnnotation().aliases()[0] + "**: " + cmd.getCommandAnnotation().description());
			sb.append("\n");
		}
		return sb.toString();
	}
}
