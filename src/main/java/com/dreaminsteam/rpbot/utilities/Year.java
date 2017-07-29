package com.dreaminsteam.rpbot.utilities;

import java.util.List;

import com.dreaminsteam.rpbot.utilities.DiceFormula.DiceType;

public enum Year {
	MUGGLE(new DiceFormula(DiceType.D0, 0, 0), "Muggle", "muggle"), //0
	LETTER(new DiceFormula(DiceType.D1, 0, 0), "Letter", "i got my letter!"), //1
	SUMMER(new DiceFormula(DiceType.D2, 0, 0), "Summer Camp", "summer camp"), //2
	FIRST(new DiceFormula(DiceType.D4, 0, 0), "First Year", "first years"), //3
	SECOND(new DiceFormula(DiceType.D6, 1, 0), "Second Year", "second years"), //4
	THIRD(new DiceFormula(DiceType.D8, 2, 0), "Third Year", "third years"), //5
	FOURTH(new DiceFormula(DiceType.D10, 3, 0), "Fourth Year", "fourth years"), //6
	FIFTH(new DiceFormula(DiceType.D12, 4, 0), "Fifth Year", "fifth years"), //7
	SIXTH(new DiceFormula(DiceType.D14, 5, 0), "Sixth Year", "sixth years"), //8
	SEVENTH(new DiceFormula(DiceType.D16, 6, 0), "Seventh Year", "seventh years"), //9
	GRAD_A(new DiceFormula(DiceType.D18, 7, 0), "Graduate +1", "gradA"), //10
	GRAD_B(new DiceFormula(DiceType.D18, 8, 1), "Graduate +2", "gradB"), //11
	GRAD_C(new DiceFormula(DiceType.D18, 9, 2), "Graduate +3", "gradC"), //12
	MASTERY(new DiceFormula(DiceType.D20, 10, 3), "Mastery", "professor"), //13
	HYPO(new DiceFormula(DiceType.D22, 11, 4), "Hypo", "Hypo"); //14 Hypothetical for moving around the list?
	
	private final DiceFormula formula;
	private final String prettyName;
	private final String roleName;
	
	private Year(DiceFormula formula, String prettyName, String roleName){
		this.formula = formula;
		this.prettyName = prettyName;
		this.roleName = roleName;
	}
	
	public DiceFormula getDiceFormula(){
		return formula.copy();
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
	
	public static Year getYearFromRoleList(List<String> roles){
		for(String role : roles){
			for(Year year : Year.values()){
				if(role.equalsIgnoreCase(year.getRoleName())){
					return year;
				}
			}
		}
		return Year.MUGGLE;
	}
}
