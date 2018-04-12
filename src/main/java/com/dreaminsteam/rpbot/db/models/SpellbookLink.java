package com.dreaminsteam.rpbot.db.models;

import java.util.List;

public class SpellbookLink {
	private final int bonus;
	private final double percentage;
	
	public SpellbookLink(int bonus, double percentage) {
		this.bonus = bonus;
		this.percentage = percentage;
	}
	
	public int getActualBonus() {
		return (int) Math.round(bonus * percentage);
	}
}
