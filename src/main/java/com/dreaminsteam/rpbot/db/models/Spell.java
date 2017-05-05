package com.dreaminsteam.rpbot.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "spells")
public class Spell {
	
	@DatabaseField(id = true) private String incantation;
	@DatabaseField private String spellName;
	@DatabaseField private String description;
	@DatabaseField private int difficultyCheck;
	
	public Spell(){
		//ORMLite requires an empty constructor.
	}
	
	public String getIncantation() {
		return incantation;
	}

	public void setIncantation(String incantation) {
		this.incantation = incantation;
	}

	public String getSpellName() {
		return spellName;
	}

	public void setSpellName(String spellName) {
		this.spellName = spellName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDifficultyCheck() {
		return difficultyCheck;
	}

	public void setDifficultyCheck(int difficultyCheck) {
		this.difficultyCheck = difficultyCheck;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + difficultyCheck;
		result = prime * result + ((incantation == null) ? 0 : incantation.hashCode());
		result = prime * result + ((spellName == null) ? 0 : spellName.hashCode());
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
		Spell other = (Spell) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (difficultyCheck != other.difficultyCheck)
			return false;
		if (incantation == null) {
			if (other.incantation != null)
				return false;
		} else if (!incantation.equals(other.incantation))
			return false;
		if (spellName == null) {
			if (other.spellName != null)
				return false;
		} else if (!spellName.equals(other.spellName))
			return false;
		return true;
	}
	
	

	
}
