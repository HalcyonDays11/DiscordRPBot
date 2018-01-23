package com.dreaminsteam.rpbot.commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class LookupCommand implements CommandExecutor{
	
	@Command(aliases = {"!lookup"}, description="Lookup spell information and DC, or get a list of all spells.", usage = "!lookup <spellIncantation>", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception{
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		if (args.length == 0){
			CloseableIterator<Spell> allSpells = DatabaseUtil.getSpellDao().queryBuilder().orderBy("prettyIncantation", true).where().le("yearVisible", player.getCurrentYear().getEquivalentOrdinal()).iterator();
			listAllSpells(allSpells, apiClient, user);
			return user.mention() + "  Information has been DM'd to you.";
		}  else {	
			args = CastCommand.normalizeArgs(args);
			
			String spellName = args[0];
			
			if(spellName.equalsIgnoreCase("all")) {
				CloseableIterator<Spell> allSpells = DatabaseUtil.getSpellDao().queryBuilder().orderBy("prettyIncantation", true).iterator();
				listAllSpells(allSpells, apiClient, user);
				return user.mention() + "  Information has been DM'd to you.";
			}
			
			List<Spell> queryForEq = DatabaseUtil.getSpellDao().queryForEq("incantation", spellName.trim().toLowerCase().replace(" ", "_"));
			
			if (queryForEq == null || queryForEq.size() != 1){
				return "**Spell Not Found!** \"" + spellName + "\" doesn't appear in the spell list.";
			}
			Spell spell = queryForEq.get(0);
			if (spell == null){
				return "**Spell Not Found!** \"" + spellName + "\" doesn't appear in the spell list.";
			}
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("**" + spell.getPrettyIncantation() + "**");
			if (!"".equals(spell.getName())){
				sb.append(", *" + spell.getName());
			}
			sb.append("* ");
			
			if (spell.getDC() > 0){
				sb.append(" (DC " + spell.getDC() + ")  ");
			}
			
			if (!"".equals(spell.getDescription())){
				sb.append(spell.getDescription());
			}
			
			return sb.toString();
		}
	}
	
	private void listAllSpells(CloseableIterator<Spell> allSpells, IDiscordClient apiClient, IUser user) throws IOException {
		StringBuilder ret = new StringBuilder();
		int count = 0;
		while (allSpells.hasNext()){
			Spell spell = allSpells.next();
			ret.append(spell.getIncantation());
			if (spell.getDC() > 0){
				ret.append("\t(DC " + spell.getDC() + ")");
			}
			ret.append("\n");
			count++;
			if (count >= 50){
				apiClient.getOrCreatePMChannel(user).sendMessage(ret.toString());
				count = 0;
				ret = new StringBuilder();
			}
		}
		allSpells.close();
		if (count > 0){
			apiClient.getOrCreatePMChannel(user).sendMessage(ret.toString());
		}
	}
}
