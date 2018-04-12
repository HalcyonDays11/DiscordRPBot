package com.dreaminsteam.rpbot.db.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "spells")
public class Spell {
	
	@DatabaseField(id = true) private String incantation = "";
	@DatabaseField private String prettyIncantation;
	@DatabaseField private int yearvisible = -1;
	@DatabaseField private String name = "";
	@DatabaseField (dataType = DataType.LONG_STRING) private String description = "";
	@DatabaseField private int dc = -1;
	@DatabaseField (dataType = DataType.LONG_STRING) private String links = "";
	
	private transient Map<String, Double> spellLinkMap;
	
	public Spell(){
		//ORMLite requires an empty constructor.
	}
	
	public String getIncantation() {
		return incantation;
	}

	public void setIncantation(String incantation) {
		this.incantation = incantation;
	}

	public String getPrettyIncantation() {
		return prettyIncantation;
	}

	public void setPrettyIncantation(String prettyIncantation) {
		this.prettyIncantation = prettyIncantation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDC() {
		return dc;
	}

	public void setDC(int dc) {
		this.dc = dc;
	}
	
	public int getYearVisible() {
		return yearvisible;
	}

	public void setYearVisible(int yearVisible) {
		this.yearvisible = yearVisible;
	}

	public Map<String,Double> getSpellLinkMap() {
		if(links == null || links.isEmpty()) {
			return new HashMap<String,Double>();
		}
		
		if(spellLinkMap != null) {
			return new HashMap<String,Double>(spellLinkMap);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			spellLinkMap = mapper.readValue(links, new TypeReference<HashMap<String, Double>>() {});
		} catch (IOException e) {
			e.printStackTrace();
			spellLinkMap = new HashMap<String,Double>();
		}		
		return new HashMap<String,Double>(spellLinkMap);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dc;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((incantation == null) ? 0 : incantation.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (dc != other.dc)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (incantation == null) {
			if (other.incantation != null)
				return false;
		} else if (!incantation.equals(other.incantation))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

	
}
