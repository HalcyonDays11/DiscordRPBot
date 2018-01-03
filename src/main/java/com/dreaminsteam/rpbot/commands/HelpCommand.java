package com.dreaminsteam.rpbot.commands;

import java.util.List;

import com.dreaminsteam.rpbot.RPBot;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler.SimpleCommand;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class HelpCommand implements CommandExecutor {

	@Command(aliases = {"!help"}, description="a helpful list of all availble commands", usage = "!help", async = true)
	public String printAvailableCommands(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		StringBuilder sb = new StringBuilder();
		sb.append("things you can do: \n");
		int i = 1;
		for (SimpleCommand cmd : RPBot.getBotInstance().getCommands()){
			sb.append("\t **" + cmd.getCommandAnnotation().aliases()[0] + "**: " + cmd.getCommandAnnotation().description() + "\n");
			sb.append("\t \t Usage: " + cmd.getCommandAnnotation().usage());
			sb.append("\n\n");
			if(i++ % 10 == 0){
				user.getOrCreatePMChannel().sendMessage(sb.toString());
				sb = new StringBuilder();
			}
		}
		if(sb.length() > 0){
			user.getOrCreatePMChannel().sendMessage(sb.toString());
		}
		return user.mention() + " Command list has been PM'd to you.";
	}
}
