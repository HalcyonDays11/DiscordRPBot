package com.dreaminsteam.rpbot;

import java.io.InputStream;
import java.util.Properties;

import com.dreaminsteam.rpbot.commands.PingCommand;
import com.dreaminsteam.rpbot.discord.DiscordBot;

public class RPBot {
	
	public static String DISCORD_TOKEN_KEY = "DISCORD_BOT_TOKEN";

	private static RPBot instance;
	
	private DiscordBot bot;
	private Properties secrets = null;
	
	public boolean setup(){
		setupSecrets();
		if(secrets == null){
			System.out.println("!!No secrets file exists!!");
			System.out.println("Create a file called \"Secrets.properties\" in the resources folder.");
			System.out.println("It should contain your Discord bot token in the form:");
			System.out.println("DISCORD_BOT_TOKEN = yourtokenhere");
			return false;
		}
		String secretToken = secrets.getProperty(DISCORD_TOKEN_KEY);
		
		bot = new DiscordBot();
		bot.connectToServer(secretToken);
		
		bot.registerCommand(new PingCommand());
		return true;
	}
	
	private void setupSecrets(){
		try(InputStream fis = getClass().getClassLoader().getResourceAsStream("Secrets.properties");){
			secrets = new Properties();
			secrets.load(fis);
		} catch (Throwable t) {
			secrets = null;
		}
	}
	
	public void shutdown(){
		bot.disconnect();
	}
	
	public static void main(String[] args){
		instance = new RPBot();
		boolean setup = instance.setup();
		if(!setup){
			System.exit(-1);
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				instance.shutdown();
			}
		});
	}
}
