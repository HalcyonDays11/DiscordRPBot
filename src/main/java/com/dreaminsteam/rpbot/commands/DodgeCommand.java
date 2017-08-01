
package com.dreaminsteam.rpbot.commands;

import java.sql.SQLException;

import org.pmw.tinylog.Logger;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Player;
import com.dreaminsteam.rpbot.utilities.DiceFormula;
import com.dreaminsteam.rpbot.utilities.RollResult;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class DodgeCommand implements CommandExecutor{
	
	@Command(aliases = {"!dodge"}, description="Dodge a spell in combat with (A)dvantage or (B)urden.", usage = "!dodge <A|B> <number of destiny points>, e.g. *!dodge B 1*", async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		Player currentPlayer = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		String dodgeModifier = "";
		if(args.length > 0){
			dodgeModifier = args[0];
		}
		dodgeModifier = dodgeModifier.toLowerCase();
		
		boolean advantage = dodgeModifier.contains("a");
		boolean burden = dodgeModifier.contains("b");
		
		boolean hasSituation = advantage || burden;
		
		
		String destinyModifier = "";
		if (hasSituation && args.length > 1){
			destinyModifier = args[1];
		} else if (!hasSituation && args.length > 0){
			destinyModifier = args[0];
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
		
		if (!currentPlayer.canUseDestinyPoints(destinyPoints)){
			return user.mention() + "  You don't have enough destiny to do that!"; 
		}
		
		currentPlayer.useDestinyPoints(destinyPoints);
		DiceFormula formula = currentPlayer.getCurrentYear().getDiceFormula(); 
		formula.setStandardModifier(0);
		RollResult result =  formula.rollDiceWithModifiers(advantage, burden, false, false, false, destinyPoints);
		
		result.setPersonalModifier(currentPlayer.getCurrentAgility() / WorkoutCommand.POINTS_PER_WORKOUT);
		
		StringBuilder ret = new StringBuilder();
		
		ret.append(user.mention() + "  You rolled **" + result.getTotal() + "**");
		ret.append("\n*" + result.getRollFormula() + " \u2192* ***" + result.getDiceRolls().toString() + 
				(result.getModifier() >= 0 ? " + " : " - ") + Math.abs(result.getModifier()) + 
				" + " + result.getPersonalModifier() + 
				(destinyPoints > 0 ? (" + "  + destinyPoints + " destiny") : "") +
				"***");

		if (destinyPoints > 0){
			try {
				DatabaseUtil.getPlayerDao().createOrUpdate(currentPlayer);
			} catch (SQLException e) {
				Logger.error(e, "Error updating player");
				ret.append("\n **Error - destiny points may not be properly updated**");
			}
		}

		
		return ret.toString();
	}

}
