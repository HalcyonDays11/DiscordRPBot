package com.dreaminsteam.rpbot.commands;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.dreaminsteam.rpbot.utilities.DiceFormula;
import com.dreaminsteam.rpbot.utilities.RollResult;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class CastCommand implements CommandExecutor{
	
	@Command(aliases = {"!cast"}, description="Cast a spell, with (A)dvantage, (B)urden, or in (C)ombat.", usage = "!cast [incantation] <A|B|C>, e.g. !cast lumos A", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		String spellStr = args[0];
		spellStr = spellStr.toLowerCase();
		
		Spell spell = DatabaseUtil.findSpell(spellStr);
		if(spell == null){
			return "**Spell Not Found!** \"" + spellStr + "\" doesn't appear in the spell list.";
		}
		
		String spellModifiers = "";
		if(args.length > 1){
			spellModifiers = args[1];
		}
		spellModifiers = spellModifiers.toLowerCase();
		
		boolean advantage = spellModifiers.contains("a");
		boolean combat = spellModifiers.contains("c");
		boolean burden = spellModifiers.contains("b");
		
		DiceFormula formula = player.getCurrentYear().getDiceFormula();
		RollResult result = formula.rollDiceWithModifiers(advantage, burden, combat);
		
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
		ret.append("\n*" + result.getRollFormula() + " =>* ***" + result.getDiceRolls().toString() + (result.getModifier() >= 0 ? " + " : " - ") + Math.abs(result.getModifier()) + " + I don't think this has a thing yet?***");

		return ret.toString();
	}

}
