package com.dreaminsteam.rpbot.discord;


import java.util.List;

import de.btobastian.sdcf4j.CommandExecutor;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.CommandHandler.SimpleCommand;
import de.btobastian.sdcf4j.handler.Discord4JHandler;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.Event;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.shard.ReconnectFailureEvent;

public class DiscordBot{

	private IDiscordClient discordClient;
	private CommandHandler cmdHandler;
	
	public void connectToServer(final String token) {
		if(isConnectedToServer()){
			throw new IllegalStateException("Already connected to server!");
		}
		
		ClientBuilder builder = new ClientBuilder();
		builder.withToken(token);
		builder.withRecommendedShardCount();
		builder.setMaxReconnectAttempts(100);
		builder.registerListener(new IListener<ReconnectFailureEvent>(){
			@Override
			public void handle(ReconnectFailureEvent event) {
				if(event.isShardAbandoned()){
					if(!discordClient.isLoggedIn() || discordClient.getShardCount() < 1){
						disconnect();
						connectToServer(token);
					}
				}
			}
		});
		discordClient = builder.build();
		discordClient.login();
		cmdHandler = new Discord4JHandler(discordClient);
	}
	
	public void setStatus(String status){
		if(discordClient.isLoggedIn()){
			discordClient.changePlayingText(status);
		}
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
