package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.utilities.DiceFormula;
import com.dreaminsteam.rpbot.utilities.RollResult;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class CastCommand implements CommandExecutor{
	
	@Command(aliases = {"!cast"}, description="Cast a spell, with (A)dvantage, (B)urden, or in (C)ombat.", usage = "!cast [incantation] <A|B|C>, e.g. !cast lumos A", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		//For now, assume an interesting year
		DiceFormula formula = DiceFormula.sixthYearFormula;
		
		//For now, assume a mid level spell difficulty
		int difficultyCheck = 12;
		
		String spell = args[0];
		
		String spellModifiers = "";
		if(args.length > 1){
			spellModifiers = args[1];
		}
		spellModifiers = spellModifiers.toLowerCase();
		
		boolean advantage = spellModifiers.contains("a");
		boolean combat = spellModifiers.contains("c");
		boolean burden = spellModifiers.contains("b");
		
		RollResult result = formula.rollDiceWithModifiers(advantage, burden, combat);
		
		StringBuilder ret = new StringBuilder();
		
		if(result.getTotal() >= difficultyCheck){
			ret.append("**Spell success! **");
		}else{
			if(result.getTheoreticalTotal() >= difficultyCheck){
				ret.append("**Sell missed! **");
			}else{
				ret.append("**Spell failed! **");
			}
		}
		ret.append("(You rolled **" + result.getTotal() + "**. Target was " + difficultyCheck + ".)\n");
		ret.append("Die Roll " + result.getRollFormula() + " = " + result.getDiceRolls().toString() + "\n");
		ret.append("Modifier = " + (result.getModifier() > 0 ? "+" : "-") + Math.abs(result.getModifier()) + "\n");
		ret.append("Spell Practice Bonus = " + "I don't think this has a thing yet?");
		
		return ret.toString();
	}

}
