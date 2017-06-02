package com.dreaminsteam.rpbot;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import org.h2.engine.Database;
import org.reflections.Reflections;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.SpellParser;
import com.dreaminsteam.rpbot.discord.DiscordBot;
import com.dreaminsteam.rpbot.discord.WittyStatuser;
import com.dreaminsteam.rpbot.util.DestinyPointResetHandler;
import com.dreaminsteam.rpbot.util.PathUtil;
import com.dreaminsteam.rpbot.web.Webserver;

import de.btobastian.sdcf4j.CommandExecutor;

public class RPBot {
	
	public static String DISCORD_TOKEN_KEY = "DISCORD_BOT_TOKEN";

	private static RPBot instance;
	
	private DiscordBot bot;
	private Webserver webserver;
	private Properties secrets = null;
	
	public boolean setup() throws Exception{
		DestinyPointResetHandler.setupResetHandler();
		
		webserver = new Webserver(4567);
		webserver.initializeWebServer();
		
		setupSecrets();
		if(secrets == null){
			System.out.println("!!No secrets file exists!!");
			System.out.println("Create a file called \"Secrets.properties\" in the main config folder.");
			System.out.println("It should contain your Discord bot token in the form:");
			System.out.println("DISCORD_BOT_TOKEN = yourtokenhere");
			return false;
		}
		String secretToken = secrets.getProperty(DISCORD_TOKEN_KEY);
		
		bot = new DiscordBot();
		bot.connectToServer(secretToken);
		
		Reflections reflect = new Reflections("com.dreaminsteam.rpbot.commands");
		Class<CommandExecutor> exec = (Class<CommandExecutor>) Class.forName("de.btobastian.sdcf4j.CommandExecutor");
		Set<Class<? extends CommandExecutor>> subTypesOf = reflect.getSubTypesOf(exec);
		for (Class <? extends CommandExecutor> klass : subTypesOf){
			bot.registerCommand(klass.getConstructor().newInstance());
		}
		
		WittyStatuser.scheduleWittyStatusChanges(bot);
		DatabaseUtil.setupConnection();
		DatabaseUtil.setupDbIfNecessary(false);
		SpellParser.parseSpells();
		return true;
	}
	
	private void setupSecrets(){
		URL resource = getClass().getClassLoader().getResource("Secrets.properties");
		if(resource == null){
			File configFile = PathUtil.getConfigFile("Secrets.properties");
			try {
				resource = configFile.toURI().toURL();
			} catch (Throwable t) {
				t.printStackTrace();
				return;
			}
		}
		
		try(InputStream fis = resource.openStream()){
			secrets = new Properties();
			secrets.load(fis);
		} catch (Throwable t) {
			secrets = null;
		}
	}
	
	public void shutdown(){
		bot.disconnect();
	}
	
	public static DiscordBot getBotInstance(){
		return instance.bot;
	}
	
	public static void main(String[] args) throws Exception {
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
