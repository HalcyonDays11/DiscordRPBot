package com.dreaminsteam.rpbot.db.models;

import java.util.Calendar;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "spellbook")
public class Spellbook {
	
	public static final int POINTS_PER_BONUS = 3;
	public static final int DEFAULT_MAX_BONUS = 3;
	
	@DatabaseField(foreign=true, columnName="player_id", foreignAutoRefresh=true)
	private Player player;
	
	@DatabaseField(foreign=true, columnName="spell_id", foreignAutoRefresh=true)
	private Spell spell;
	
	@DatabaseField(generatedId = true) private Long id;
	@DatabaseField private int currentIndividualModifierPoints = 0;
	@DatabaseField private int castAttemptsToday = 0;
	@DatabaseField private Date lastCastAttempt;
	
	public Spellbook(){
		//ORMLite requires an empty constructor
	}
	
	public Spellbook(Player player, Spell spell){
		this.player = player;
		this.spell = spell;
	}
	
	public int getIndividualModifier(){
		return getIndividualModifier(DEFAULT_MAX_BONUS);
	}
	
	public int getIndividualModifier(int maxModifier){
		return Math.min(maxModifier, currentIndividualModifierPoints/3);
	}
	
	public boolean hasPracticedToday(Date today){
		if(lastCastAttempt == null){
			return false;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastCastAttempt);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		
		if(calendar.after(today)){
			return true;
		}else{
			return false;
		}
	}
	
	public void practiceSpell(){
		Date now = new Date();
		boolean practiced = false;
		if(hasPracticedToday(now)){
			if(castAttemptsToday < 3){
				castAttemptsToday++;
				currentIndividualModifierPoints++;
				practiced = true;
			}
		}else{
			castAttemptsToday = 1;
			currentIndividualModifierPoints++;
			practiced = true;
		}
		if(practiced){
			lastCastAttempt = now;
		}
	}

}
