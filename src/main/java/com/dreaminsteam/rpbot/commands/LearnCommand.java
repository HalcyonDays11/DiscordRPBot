package com.dreaminsteam.rpbot.commands;

import java.util.Date;

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

public class LearnCommand implements CommandExecutor{
	
	@Command(aliases = {"!learn"}, description="Attempt to practice a spell.", usage="!learn [spell] <A|B|C|V|W>, e.g. !learn lumos A")
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		Player player = DatabaseUtil.createOrUpdatePlayer(user, channel.getGuild());
		
		if(args.length < 1){
			return user.mention() + " attempts to learn without specifying *what* to learn, and so ends up surfing the internet instead of studying.";
		}
		
		args = CastCommand.normalizeArgs(args);
		
		String spellStr = args[0];
		if(spellStr == null){
			return null;
		}
		
		Date today = new Date();
		if(!player.canPracticeToday(today)){
			return user.mention() + " You've already practiced a spell today!  Try again tomorrow.";
		}
		
		Spell spell = DatabaseUtil.findSpell(spellStr);
		if(spell == null){
			return "  **Spell Not Found!**  \"" + spellStr + "\" doesn't appear in the spell list.";
		}
		
		Spellbook spellbook = DatabaseUtil.getOrCreateSpellbook(player, spell);
		
		if(spellbook.hasMastered()){
			return user.mention() + " You've already mastered this spell!";
		}
		
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
			return user.mention() + " You don't have enough destiny to practice this spell!"; 
		}
		
		player.useDestinyPoints(destinyPoints);
		
		int difficultyCheck = spell.getDC();
		
		DiceFormula formula = player.getCurrentYear().getDiceFormula();
		RollResult result = formula.rollDiceWithModifiers(advantage, burden, combat, nonverbal, wandless, destinyPoints);
		result.setPersonalModifier(spellbook.getIndividualModifier(difficultyCheck));
		
		StringBuilder ret = new StringBuilder();
		boolean success = false;
		if(result.getTotal() >= difficultyCheck){
			ret.append(user.mention() + " ** Practice Succeeds! **");
			success = true;
		}else{
			if(result.getTheoreticalTotal() >= difficultyCheck){
				ret.append(user.mention() + " ** Practice Missed! **");
			}else{
				ret.append(user.mention() + " ** Practice Failed! **");
			}
		}
		ret.append("(You rolled **" + result.getTotal() + "** , " + spell.getPrettyIncantation() + " DC " + difficultyCheck + ")");
		ret.append("\n*" + result.getRollFormula() + " \u2192* ***" + result.getDiceRolls().toString() + 
				(result.getModifier() >= 0 ? " + " : " - ") + Math.abs(result.getModifier()) + 
				" + " + result.getPersonalModifier() + 
				(destinyPoints > 0 ? (" + "  + destinyPoints + " destiny") : "") +
				"***");
		
		spellbook.practiceSpell(success, today);
		if(success || spellbook.castAttemptsAtMax()){
			player.updateLastPracticeDate(today);
		}else{
			ret.append("  **You have " + spellbook.getCastAttemptsRemaining() + " more attempts today.**");
		}
		
		try{
			DatabaseUtil.getSpellbookDao().createOrUpdate(spellbook);
			DatabaseUtil.getPlayerDao().createOrUpdate(player);
		}catch(Exception e){
			e.printStackTrace();
			return "Couldn't store result!  Practice never happened.";
		}
		
		return ret.toString();
	}

}
