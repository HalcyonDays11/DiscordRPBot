
package com.dreaminsteam.rpbot.commands;

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
	
	@Command(aliases = {"!cast"}, description="Cast a spell, with (A)dvantage, (B)urden, in (C)ombat, non-(V)erbal, or (W)andless.", usage = "!cast [incantation] <A|B|C|V|W>, e.g. !cast lumos A", async = true)
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
		
		DiceFormula formula = player.getCurrentYear().getDiceFormula();
		RollResult result = formula.rollDiceWithModifiers(advantage, burden, combat, nonverbal, wandless);
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
		ret.append("\n*" + result.getRollFormula() + " =>* ***" + result.getDiceRolls().toString() + (result.getModifier() >= 0 ? " + " : " - ") + Math.abs(result.getModifier()) + " + " + result.getPersonalModifier() + "***");

		return ret.toString();
	}

}
