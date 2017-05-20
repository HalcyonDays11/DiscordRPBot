package com.dreaminsteam.rpbot.commands;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.dreaminsteam.rpbot.utilities.DiceRoller;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class SimpleRollCommand implements CommandExecutor{
	
	@Command(aliases = {"!roll"}, description = "Simple dice roll command", usage = "!roll [n]d[m], e.g. !roll 4d8",  async = true)
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		String diceFormula = "1d20";
		if(args.length > 0){			
			diceFormula = args[0];
		}
		List<Integer> rollThemBones = DiceRoller.rollThemBones(diceFormula);
		if(rollThemBones == null){
			return "There is a practical limit of 500 dice.  If you need to roll more than that, you are wrong.";
		}
		if(rollThemBones.isEmpty()){
			return "The rolling of non-existent dice is strongly discouraged.";
		}
		
		AtomicInteger result = new AtomicInteger(0);
		rollThemBones.stream().forEach(die -> result.addAndGet(die));
		
		return "***" + diceFormula + "*** → " + rollThemBones.toString() + " = " + result;
	}

}
