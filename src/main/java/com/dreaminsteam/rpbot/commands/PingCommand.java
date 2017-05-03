package com.dreaminsteam.rpbot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class PingCommand implements CommandExecutor{

	@Command(aliases = {"!ping"}, description="Simple Test Command", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		//channel 	= the channel the command was sent on
		//user		= the user who sent the message
		//apiClient	= the underlying client
		//command 	= the command that was sent ("!ping")
		//args 		= the message, minus the command, tokenized by spaces
		
		//Return whatever you want to respond with
		return "Pong!";
	}
}
