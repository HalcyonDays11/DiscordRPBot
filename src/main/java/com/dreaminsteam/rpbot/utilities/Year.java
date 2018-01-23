package com.dreaminsteam.rpbot.utilities;

import java.util.List;

import com.dreaminsteam.rpbot.utilities.DiceFormula.DiceType;

public enum Year {
	MUGGLE(new DiceFormula(DiceType.D0, 0, 0), 0, "Muggle", "muggle", 0), //0
	LETTER(new DiceFormula(DiceType.D1, 0, 0), 0, "Letter", "i got my letter!", 0), //1
	SUMMER(new DiceFormula(DiceType.D2, 0, 0), 0, "Summer Camp", "summer camp", 0), //2
	FIRST(new DiceFormula(DiceType.D4, 0, 0), 0, "First Year", "first years", 1), //3
	SECOND(new DiceFormula(DiceType.D6, 1, 0), 1, "Second Year", "second years", 2), //4
	THIRD(new DiceFormula(DiceType.D8, 2, 0), 2, "Third Year", "third years", 3), //5
	FOURTH(new DiceFormula(DiceType.D10, 3, 0), 3, "Fourth Year", "fourth years", 4), //6
	FIFTH(new DiceFormula(DiceType.D12, 4, 0), 4, "Fifth Year", "fifth years", 5), //7
	SIXTH(new DiceFormula(DiceType.D14, 5, 0), 5, "Sixth Year", "sixth years", 6), //8
	SEVENTH(new DiceFormula(DiceType.D16, 6, 0), 6,"Seventh Year", "seventh years", 7), //9
	GRAD_A(new DiceFormula(DiceType.D18, 7, 0), 7, "Graduate +1", "gradA", 8), //10
	GRAD_B(new DiceFormula(DiceType.D18, 8, 1), 8, "Graduate +2", "gradB", 9), //11
	GRAD_C(new DiceFormula(DiceType.D18, 9, 2), 9, "Graduate +3", "gradC", 10), //12
	MASTERY(new DiceFormula(DiceType.D20, 10, 3), 10, "Mastery", "professor", 11), //13
	HYPO(new DiceFormula(DiceType.D22, 11, 4), 11, "Hypo", "Hypo", 12); //14 Hypothetical for moving around the list?
	
	private final DiceFormula formula;
	private final String prettyName;
	private final String roleName;
	private final int masterableDC;
	private final int equivalentOrdinal;
	
	private Year(DiceFormula formula, int masterableDC, String prettyName, String roleName, int equivalentOrdinal){
		this.formula = formula;
		this.prettyName = prettyName;
		this.roleName = roleName;
		this.masterableDC = masterableDC;
		this.equivalentOrdinal = equivalentOrdinal;
	}
	
	public DiceFormula getDiceFormula(){
		return formula.copy();
	}
	
	public int getMasterableDC() {
		return masterableDC;
	}
	
	public String getPrettyName(){
		return prettyName;
	}
	
	public String getRoleName() {
		return roleName;
	}
	
	public int getDailyDestinyPoints() {
		return Math.max(ordinal() - 2, 0) * 2;
	}
	
	public int getEquivalentOrdinal() {
		return equivalentOrdinal;
	}
	
	public static Year getYearFromRoleList(List<String> roles){
		for(Year year : Year.values()){
			for(String role : roles){
				if(role.equalsIgnoreCase(year.getRoleName())){
					return year;
				}
			}
		}
		return Year.MUGGLE;
	}
}
