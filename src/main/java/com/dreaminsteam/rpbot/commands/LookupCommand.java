package com.dreaminsteam.rpbot.commands;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class LookupCommand implements CommandExecutor{
	
	@Command(aliases = {"!lookup"}, description="Lookup spell information and DC.", usage = "!lookup [spellIncantation]")
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		String spell = args[0];
		spell = spell.toLowerCase();
		
		
		
		StringBuilder ret = new StringBuilder();
		return ret.toString();
	}

}
