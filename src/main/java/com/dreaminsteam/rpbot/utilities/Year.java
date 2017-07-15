package com.dreaminsteam.rpbot.utilities;

import java.util.List;

import com.dreaminsteam.rpbot.utilities.DiceFormula.DiceType;

public enum Year {
	LETTER(new DiceFormula(DiceType.D0, 0), "Letter", "i got my letter!"),
	SUMMER(new DiceFormula(DiceType.D2, 0), "Summer Camp", "summer camp"),
	FIRST(new DiceFormula(DiceType.D4, 0), "First Year", "first years"),
	SECOND(new DiceFormula(DiceType.D6, 1), "Second Year", "second years"),
	THIRD(new DiceFormula(DiceType.D8, 2), "Third Year", "third years"),
	FOURTH(new DiceFormula(DiceType.D10, 3), "Fourth Year", "fourth years"),
	FIFTH(new DiceFormula(DiceType.D12, 4), "Fifth Year", "fifth years"),
	SIXTH(new DiceFormula(DiceType.D14, 5), "Sixth Year", "sixth years"),
	SEVENTH(new DiceFormula(DiceType.D16, 6), "Seventh Year", "seventh years"),
	GRAD_A(new DiceFormula(DiceType.D18, 7), "Graduate +1", "gradA"),
	GRAD_B(new DiceFormula(DiceType.D18, 8), "Graduate +2", "gradB"),
	GRAD_C(new DiceFormula(DiceType.D18, 9), "Graduate +3", "gradC"),
	MASTERY(new DiceFormula(DiceType.D20, 10), "Mastery", "professor"),
	HYPO(new DiceFormula(DiceType.D22, 11), "Hypo", "Hypo"); //Hypothetical for moving around the list?
	
	private final DiceFormula formula;
	private final String prettyName;
	private final String roleName;
	
	private Year(DiceFormula formula, String prettyName, String roleName){
		this.formula = formula;
		this.prettyName = prettyName;
		this.roleName = roleName;
	}
	
	public DiceFormula getDiceFormula(){
		return formula;
	}
	
	public String getPrettyName(){
		return prettyName;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public int getDailyDestinyPoints() {
		return (ordinal() + 1) * 2;
	}
	
	public static Year getYearFromRoleList(List<String> roles){
		for(String role : roles){
			for(Year year : Year.values()){
				if(role.equalsIgnoreCase(year.getRoleName())){
					return year;
				}
			}
		}
		return Year.FIRST;
	}
}
