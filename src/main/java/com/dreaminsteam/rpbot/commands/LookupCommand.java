package com.dreaminsteam.rpbot.commands;

import java.sql.SQLException;

import com.dreaminsteam.rpbot.db.DatabaseUtil;
import com.dreaminsteam.rpbot.db.models.Spell;
import com.j256.ormlite.dao.Dao;

import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;

public class LookupCommand implements CommandExecutor{
	
	@Command(aliases = {"!lookup"}, description="Lookup spell information and DC.", usage = "!lookup [spellIncantation]")
	public String onCommand(IChannel channel, IUser user, IDiscordClient apiClient, String command, String[] args){
		String spell = args[0];
		spell = spell.toLowerCase();
		
		Dao<Spell,String> spellDao = DatabaseUtil.getSpellDao();
		StringBuilder ret = new StringBuilder();
		try {
			Spell spellObject = spellDao.queryForId(spell);
			if(spellObject == null){
				ret.append("**No such spell found!** I don't know about the spell " + spell + ".  Check your spelling and try again.");
			}else{
				String spellName = spellObject.getSpellName();
				String description = spellObject.getDescription();
				int difficultyCheck = spellObject.getDifficultyCheck();
				ret.append("**" + spellName + "** (DC" + difficultyCheck + ") " + description);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			ret.append("**OH NO!** Get an admin to check my logs, there's an error!");
		}
		
		return ret.toString();
	}

}
