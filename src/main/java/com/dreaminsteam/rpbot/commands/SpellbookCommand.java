package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.dreaminsteam.rpbot.db.models.Spellbook;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class SpellbookCommand implements CommandExecutor{

	@Command(aliases = {"!spellbook"}, description="See your own stats with a specific spell.", usage="!spellbook [incantation]")
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args) throws Exception{
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		if(args.length < 1){
			return null;
		}
		
		String spellStr = args[0];
		if(spellStr == null){
			return null;
		}
		spellStr = spellStr.toLowerCase();
		
		Spell spell = DatabaseUtil.findSpell(spellStr);
		if(spell == null){
			return "**Spell Not Found!** \"" + spellStr + "\" doesn't appear in the spell list.";
		}
		
		Spellbook spellbook = DatabaseUtil.getOrCreateSpellbook(player, spell);
		if(spellbook != null){
			int individualModifier = spellbook.getIndividualModifier(spell.getDC() - 1);
			int progressTowardsNextBonus = spellbook.getProgressTowardsNextBonus();
			
			StringBuilder sb = new StringBuilder();
			sb.append("**" + spell.getPrettyIncantation() + "**  ");
			sb.append("(DC" + spell.getDC() + ")  ");
			sb.append("Personal modifier is **+" + individualModifier + "** with ");
			sb.append(" **" + progressTowardsNextBonus + "/" + Spellbook.POINTS_PER_BONUS + "** towards next bonus.");
			user.getOrCreatePMChannel().sendMessage(sb.toString());
			return user.mention() + " Information has been DM'd to you.";
		}
		return null;
		
	}
	
}
