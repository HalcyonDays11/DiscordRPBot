package com.dreaminsteam.rpbot.commands;

import java.util.List;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.dreaminsteam.rpbot.db.models.Spellbook;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class ModifySpellbookCommand implements CommandExecutor{
	
	@Command(aliases = "!modifySpellbook", description="Admin only!  Modify a player's spellbook.", usage="!modifySpellbook [playerId] [incantation] [newModifier], e.g. !modifySpellbook 123456 lumos 2", async=true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception{
		boolean hasAdminRole = CommandUtils.hasAdminRole(user, channel);
		if(!hasAdminRole){
			return "**Whoops!** Sorry, this command is admin only, and you don't have an administrative role.";
		}
		
		if(args.length < 3){
			return "Incorrect arguments! Usage: !modifySpellbook [playerId] [incantation] [newModifier]";
		}
		
		String playerId = args[0];
		String incantation = args[1];
		String newModifierStr = args[2];
		
		Player player = DatabaseUtil.getPlayerDao().queryForId(playerId);
		
		if(player == null){
			return "I can't find a player with that ID... try using !playerInfo to get the list of players.";
		}
		
		Spell spell = DatabaseUtil.getSpellDao().queryForId(incantation);
		if(spell == null){
			return "I can't find a spell with that incanataion... try using lookup.";
		}
		
		Spellbook spellbook = DatabaseUtil.getOrCreateSpellbook(player, spell);
		
		try{
			int modifier = Integer.parseInt(newModifierStr);
			spellbook.setModifier(modifier);
			DatabaseUtil.getSpellbookDao().createOrUpdate(spellbook);
		}catch(Exception e){
			e.printStackTrace();
			return "I... don't think your modifier is a real number.  Try again?";
		}
		return user.mention() + " Spellbook has been modified.";
	}

}
