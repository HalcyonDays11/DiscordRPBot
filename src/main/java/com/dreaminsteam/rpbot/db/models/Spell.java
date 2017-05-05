package com.dreaminsteam.rpbot.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "seplls")
public class Spell {
	
	@DatabaseField(id = true) private String incantation;
	@DatabaseField private String spellName;
	@DatabaseField private String description;
	@DatabaseField private int difficultyCheck;
	
	public Spell(){
		//ORMLite requires an empty constructor.
	}

}
