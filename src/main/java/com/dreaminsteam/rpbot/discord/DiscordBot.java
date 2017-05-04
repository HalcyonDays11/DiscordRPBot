package com.dreaminsteam.rpbot.discord;


import java.util.List;

import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.CommandHandler.SimpleCommand;
import de.btobastian.sdcf4j.handler.Discord4JHandler;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

public class DiscordBot{

	private IDiscordClient discordClient;
	private CommandHandler cmdHandler;
	
	public void connectToServer(String token) {
		if(isConnectedToServer()){
			throw new IllegalStateException("Already connected to server!");
		}
		
		ClientBuilder builder = new ClientBuilder();
		builder.withToken(token);
		discordClient = builder.build();
		discordClient.login();
		cmdHandler = new Discord4JHandler(discordClient);
	}
	
	public void disconnect() {
		if(discordClient.isLoggedIn()){			
			discordClient.logout();
		}
		discordClient = null;
		cmdHandler = null;
	}

	public boolean isConnectedToServer() {
		return discordClient != null && discordClient.isLoggedIn();
	}
	
	public void registerCommand(CommandExecutor commandExecutor){
		if(cmdHandler == null){
			throw new IllegalStateException("Not connected to server yet.");
		}
		cmdHandler.registerCommand(commandExecutor);
	}
	
	public List<SimpleCommand> getCommands(){
		return cmdHandler.getCommands();
	}
	
	
	
}
