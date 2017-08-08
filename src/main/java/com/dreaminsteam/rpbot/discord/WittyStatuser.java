package com.dreaminsteam.rpbot.discord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.pmw.tinylog.Logger;

import it.sauronsoftware.cron4j.Scheduler;

public class WittyStatuser {

	public static Map<DiscordBot, WittyStatuser> instanceMap = new HashMap<>();
	
	private List<String> wittyStatus = new ArrayList<>(); //Fill in the blank for "Playing <blank>"
	private DiscordBot bot;
	private Random rand;
	private Scheduler scheduler;
	
	{
		wittyStatus.add("dirty.");
		wittyStatus.add("with your feelings.");
		wittyStatus.add("my own game.");
		wittyStatus.add("a doctor on the TV.");
		wittyStatus.add("on.");
		wittyStatus.add("games with your heart.");
		wittyStatus.add("with fire.");
		wittyStatus.add("that funky music.");
		wittyStatus.add("with the boys.");
		wittyStatus.add("movies in my head.");
		wittyStatus.add("you like a fiddle.");
		wittyStatus.add("warden to your soul.");
		wittyStatus.add("that song.");
		wittyStatus.add("it all night long.");
		wittyStatus.add("hell to get you to heaven.");
		wittyStatus.add("doctor to my disease.");
		wittyStatus.add("God.");
	}
	
	public static void scheduleWittyStatusChanges(DiscordBot bot){
		if(!instanceMap.containsKey(bot)){
			instanceMap.put(bot, new WittyStatuser(bot));
		}
	}
	
	private WittyStatuser(DiscordBot bot){
		this.bot = bot;
		this.rand = new Random();
		setStatus();
		scheduler = new Scheduler();
		scheduler.schedule("*/5 * * * *", () -> {
			setStatus();
		});
		scheduler.start();
	}
	
	private void setStatus(){
		int nextInt = rand.nextInt(wittyStatus.size());
		String status = wittyStatus.get(nextInt);
		bot.setStatus(status);
		Logger.debug("Changed status to: " + status);
	}
	
}
