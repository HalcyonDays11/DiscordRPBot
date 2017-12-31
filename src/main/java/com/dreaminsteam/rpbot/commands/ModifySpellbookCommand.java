package com.dreaminsteam.rpbot.commands;

import java.util.List;

import org.pmw.tinylog.Logger;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.dreaminsteam.rpbot.db.models.Spellbook;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;

public class ModifySpellbookCommand implements CommandExecutor{
	
	@Command(aliases = "!modifySpellbook", description="Modify a player's spellbook. **Admin Only!**", usage="!modifySpellbook [playerId] [incantation] [newModifier], e.g. *!modifySpellbook 123456 lumos 2*", async=true)
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
			return "I can't find a player with that ID... try using **!playerInfo** to get the list of players.";
		}
		
		Spell spell = DatabaseUtil.getSpellDao().queryForId(incantation);
		if(spell == null){
			if(newModifierStr.equalsIgnoreCase("0")){
				QueryBuilder<Spellbook,Long> queryBuilder = DatabaseUtil.getSpellbookDao().queryBuilder();
				Where<Spellbook,Long> query = queryBuilder.where().eq("player_id", player.getSnowflakeId()).and().eq("spell_id", incantation);
				Spellbook spellbook = query.queryForFirst();
				DatabaseUtil.getSpellbookDao().delete(spellbook);
				return "Unmatched spellbook removed.";
			}
			return "I can't find a spell with that incanataion... try using **!lookup** to get a list of spells.";
		}
		
		Spellbook spellbook = DatabaseUtil.getOrCreateSpellbook(player, spell);
		
		try{
			int modifier = Integer.parseInt(newModifierStr);
			spellbook.setModifier(modifier);
			DatabaseUtil.getSpellbookDao().createOrUpdate(spellbook);
		}catch(Exception e){
			Logger.error(e, "Error attempting to update spellbook.");
			return "I... don't think your modifier is a real number. Try again?";
		}
		return user.mention() + "  Spellbook has been modified.";
	}

}
