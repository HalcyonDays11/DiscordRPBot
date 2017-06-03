
package com.dreaminsteam.rpbot.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import sx.blah.discord.handle.obj.IUser;

public class DodgeCommand implements CommandExecutor{
	
	public static String[] normalizeArgs(String[] args){
		List<String> ret = new ArrayList<>();
		String spellStr = args[0];
		if(spellStr.startsWith("\"")){
			String spellPiece = "";
			int i = 1;
			while(!spellStr.endsWith("\"")){
				spellPiece = args[i++];
				spellStr += " " + spellPiece;
			}
			spellStr = spellStr.replace("\"", "");
			spellStr = spellStr.replace(" ", "_");
			ret.add(spellStr.toLowerCase());
			while(i < args.length){
				ret.add(args[i++]);
			}
			return ret.toArray(new String[ret.size()]);
		}else{
			return args;
		}
	}
	
	@Command(aliases = {"!dodge"}, description="Dodge a spell in combat with (A)dvantage or (B)urden.", usage = "!dodge <A|B> <number of destiny points>, e.g. **!dodge B 1**", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
			
		String spellModifiers = "";
		if(args.length > 1){
			spellModifiers = args[1];
		}
		spellModifiers = spellModifiers.toLowerCase();
		
		boolean advantage = spellModifiers.contains("a");
		boolean burden = spellModifiers.contains("b");
		
		boolean hasSituation = advantage || burden;
		
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
		RollResult result = formula.rollDodgeDiceWithModifiers(advantage, burden, destinyPoints);
		result.setPersonalModifier(spellbook.getIndividualModifier(spell.getDC()));
		
		StringBuilder ret = new StringBuilder();
		
		ret.append(user.mention() + " You rolled **" + result.getTotal() + "**");
		ret.append("\n*" + result.getRollFormula() + " \u2192* ***" + result.getDiceRolls().toString() + 
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
