package com.dreaminsteam.rpbot.commands;

import java.sql.SQLException;
import java.util.List;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.dreaminsteam.rpbot.db.models.Spellbook;
import com.j256.ormlite.dao.Dao;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;

public class SpellbookCommand implements CommandExecutor{

	@Command(aliases = {"!spellbook"}, description="See your own stats with a specific spell.", usage="!spellbook [incantation]")
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception{
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		if(args.length < 1){
			listAllSpells(user, player.getSnowflakeId(), false);
			return user.mention() + " Information has been DM'd to you.";
		}
		
		args = CastCommand.normalizeArgs(args);
		
		String spellStr = args[0];
		if(spellStr == null){
			return null;
		}
		
		if(spellStr.matches("^[0-9]+$")){
			boolean hasAdminRole = CommandUtils.hasAdminRole(user, channel);
			if(!hasAdminRole){
				return user.mention() + " Woah, now... you can't just go looking at other people's spellbooks.";
			}
			listAllSpells(user, spellStr, true);
			return user.mention() + " Information has been DM'd to you.";
		}
		
		spellStr = spellStr.toLowerCase();
		
		Spell spell = DatabaseUtil.findSpell(spellStr);
		if(spell == null){
			return "**Spell Not Found!** \"" + spellStr + "\" doesn't appear in the spell list.";
		}
		
		Spellbook spellbook = DatabaseUtil.getOrCreateSpellbook(player, spell);
		if(spellbook != null){
			StringBuilder sb = new StringBuilder();
			listIndividualSpellbook(spellbook, sb);
			user.getOrCreatePMChannel().sendMessage(sb.toString());
			return user.mention() + " Information has been DM'd to you.";
		}
		return null;
	}
	
	private String listAllSpells(IUser user, String playerSnowflakeId, boolean other) throws SQLException{
		Dao<Spellbook,Long> spellbookDao = DatabaseUtil.getSpellbookDao();
		List<Spellbook> spellbooks = spellbookDao.queryForEq("player_id", playerSnowflakeId);
		IPrivateChannel pmChannel = user.getOrCreatePMChannel();
		if(other){
			pmChannel.sendMessage("Spellbooks for player id: " + playerSnowflakeId);
		}
		iterateThroughSpellbooks(spellbooks, pmChannel, other);
		return user.mention() + " Information has been DM'd to you.";
	}
	
	private void iterateThroughSpellbooks(List<Spellbook> spellbooks, IPrivateChannel pmChannel, boolean other){
		if(spellbooks.isEmpty()){
			pmChannel.sendMessage(other ? "They" : "You" + " have not practiced any spells.");
		}else{
			StringBuilder sb = new StringBuilder(other ? "They" : "You" + " have practiced the following spells: \n");
			for(int i = 0; i < spellbooks.size(); i++){
				Spellbook spellbook = spellbooks.get(i);
				listIndividualSpellbook(spellbook, sb);
				if(i < spellbooks.size() - 1){
					sb.append("\n");
				}
				if(i > 0 && i % 15 == 0){
					pmChannel.sendMessage(sb.toString());
					sb = new StringBuilder();
				}
			}
			if(!sb.toString().isEmpty()){				
				pmChannel.sendMessage(sb.toString());
			}
		}
	}
	
	private void listIndividualSpellbook(Spellbook spellbook, StringBuilder sb){
		Spell spell = spellbook.getSpell();
		int individualModifier = spellbook.getIndividualModifier(spell.getDC() - 1);
		int progressTowardsNextBonus = spellbook.getProgressTowardsNextBonus();
		
		sb.append("**" + spell.getPrettyIncantation() + "**  ");
		sb.append("(DC" + spell.getDC() + ")  ");
		sb.append("Personal modifier is **+" + individualModifier + "** with ");
		sb.append(" **" + progressTowardsNextBonus + "/" + Spellbook.POINTS_PER_BONUS + "** towards next bonus.");
	}
	
}
