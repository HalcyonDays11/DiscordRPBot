package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.db.SpellParser;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class RefreshSpellsCommand implements CommandExecutor {

	@Command(aliases = {"!refreshAllSpells"}, description="Repopulate all spell information from the Google Doc", usage = "!refreshAllSpells", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception{
		
		SpellParser.parseSpells();
		
		return user.mention() + " the global spell database has been refreshed!";
	}
		
}
