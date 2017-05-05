package com.dreaminsteam.rpbot.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "players")
public class Player {

	@DatabaseField(id = true) private String snowflakeId; // This is a Discord thing.  It's the global, unique identifier for the user.
	@DatabaseField private String name;
	
	public Player() {
		//ORMLite requires an empty constructor.
	}
	
	public Player(String snowflakeId, String name){
		this.snowflakeId = snowflakeId;
		this.name = name;
	}
	
	
}
