package com.dreaminsteam.rpbot.db.models;

import java.util.Calendar;
import java.util.Date;

import com.dreaminsteam.rpbot.utilities.Year;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "players")
public class Player {

	@DatabaseField(id = true) private String snowflakeId; // This is a Discord thing.  It's the global, unique identifier for the user.
	@DatabaseField private String name;
	@DatabaseField private Date lastPracticedDate;
	@DatabaseField private Year currentYear;
	@DatabaseField private int usedDestiny = 0;
	
	public Player() {
		//ORMLite requires an empty constructor.
	}
	
	public Player(String snowflakeId, String name){
		this.snowflakeId = snowflakeId;
		this.name = name;
	}
	
	public String getSnowflakeId() {
		return snowflakeId;
	}

	public void setSnowflakeId(String snowflakeId) {
		this.snowflakeId = snowflakeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Year getCurrentYear() {
		return currentYear;
	}
	
	public void setCurrentYear(Year currentYear) {
		this.currentYear = currentYear;
	}
	
	public boolean canPracticeToday(Date today){
		if(lastPracticedDate == null){
			return true;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(lastPracticedDate);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		
		if(calendar.getTime().after(today)){
			return false;
		}else{
			return true;
		}
	}
	
	public int getAvailableDestinyPoints(){
		return currentYear.getDailyDestinyPoints() - usedDestiny;
	}
	
	public int getUsedDestinyPoints(){
		return usedDestiny;
	}
	
	public boolean useDestinyPoints(int numPoints){
		if (!canUseDestinyPoints(numPoints)){
			return false;
		}
		
		usedDestiny += numPoints;
		return true;
	}
	
	public boolean canUseDestinyPoints(int numPoints){
		return numPoints >= 0 && numPoints <= getAvailableDestinyPoints();
	}
	
	public void updateLastPracticeDate(Date today){
		lastPracticedDate = today;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((snowflakeId == null) ? 0 : snowflakeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (snowflakeId == null) {
			if (other.snowflakeId != null)
				return false;
		} else if (!snowflakeId.equals(other.snowflakeId))
			return false;
		return true;
	}

	
	
}
