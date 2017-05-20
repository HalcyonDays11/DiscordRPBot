
package com.dreaminsteam.rpbot.commands;

import java.sql.SQLException;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.dreaminsteam.rpbot.db.models.Spellbook;
import com.dreaminsteam.rpbot.utilities.DiceFormula;
import com.dreaminsteam.rpbot.utilities.RollResult;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CastCommand implements CommandExecutor{
	
	@Command(aliases = {"!cast"}, description="Cast a spell, with (A)dvantage, (B)urden, or in (C)ombat.", usage = "!cast [incantation] <A|B|C|V|W> <number of destiny points>, e.g. !cast lumos A 1", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		if(args.length < 1){
			return "You forgot to say a spell!";	
		}
		
		String spellStr = args[0];
		if(spellStr == null || spellStr.isEmpty()){
			return "You forgot to say a spell!";
		}
		spellStr = spellStr.toLowerCase();
		
		Spell spell = DatabaseUtil.findSpell(spellStr);
		if(spell == null){
			return "**Spell Not Found!** \"" + spellStr + "\" doesn't appear in the spell list.";
		}
		
		Spellbook spellbook = DatabaseUtil.getOrCreateSpellbook(player, spell);
		
		String spellModifiers = "";
		if(args.length > 1){
			spellModifiers = args[1];
		}
		spellModifiers = spellModifiers.toLowerCase();
		
		boolean advantage = spellModifiers.contains("a");
		boolean combat = spellModifiers.contains("c");
		boolean burden = spellModifiers.contains("b");
		boolean nonverbal = spellModifiers.contains("v");
		boolean wandless = spellModifiers.contains("w");
		
		boolean hasSituation = advantage || combat || burden || nonverbal || wandless;
		
		String destinyModifier = "";
		if (hasSituation && args.length > 2){
			destinyModifier = args[2];
		} else if (!hasSituation && args.length > 1){
			destinyModifier = args[1];
		}
		
		int destinyPoints;
		
		if (destinyModifier == null || "".equals(destinyModifier)){
			destinyPoints = 0;
		} else {
			try {
				destinyPoints = Integer.parseInt(destinyModifier);
			} catch (NumberFormatException e){
				destinyPoints = 0;
			}
		}
		
		if (!player.canUseDestinyPoints(destinyPoints)){
			return user.mention() + " You don't have enough destiny to cast this spell!"; 
		}
		
		player.useDestinyPoints(destinyPoints);
		
		DiceFormula formula = player.getCurrentYear().getDiceFormula();
		RollResult result = formula.rollDiceWithModifiers(advantage, burden, combat, nonverbal, wandless, destinyPoints);
		result.setPersonalModifier(spellbook.getIndividualModifier(spell.getDC()));
		
		StringBuilder ret = new StringBuilder();
		
		int difficultyCheck = spell.getDC();
		if(result.getTotal() >= difficultyCheck){
			ret.append(user.mention() + " ** Spell Succeeds! **");
		}else{
			if(result.getTheoreticalTotal() >= difficultyCheck){
				ret.append(user.mention() + " ** Spell Missed! **");
			}else{
				ret.append(user.mention() + " ** Spell Failed! **");
			}
		}
		ret.append("(You rolled **" + result.getTotal() + "** , " + spell.getPrettyIncantation() + " DC " + difficultyCheck + ")");
		ret.append("\n*" + result.getRollFormula() + " =>* ***" + result.getDiceRolls().toString() + 
				(result.getModifier() >= 0 ? " + " : " - ") + Math.abs(result.getModifier()) + 
				" + " + result.getPersonalModifier() + 
				(destinyPoints > 0 ? (" + "  + destinyPoints + " destiny") : "") +
				"***");

		if (destinyPoints > 0){
			try {
				DatabaseUtil.getPlayerDao().createOrUpdate(player);
			} catch (SQLException e) {
				e.printStackTrace();
				ret.append("\n **Error - destiny points may not be properly updated**");
			}
		}

		
		return ret.toString();
	}

}
