package com.dreaminsteam.rpbot.utilities;

import java.util.List;

public class DiceFormula {

	public static final DiceFormula firstYearFormula = new DiceFormula(DiceType.D4, 0); //defines the different classes and what properties they have
	public static final DiceFormula secondYearFormula = new DiceFormula(DiceType.D6, 1);
	public static final DiceFormula thirdYearFormula = new DiceFormula(DiceType.D8, 2);
	public static final DiceFormula forthYearFormula = new DiceFormula(DiceType.D10, 3);
	public static final DiceFormula fifthYearFormula = new DiceFormula(DiceType.D12, 4);
	public static final DiceFormula sixthYearFormula = new DiceFormula(DiceType.D14, 5);
	public static final DiceFormula seventhYearFormula = new DiceFormula(DiceType.D16, 6);
	public static final DiceFormula graduateAFormula = new DiceFormula(DiceType.D18, 7);
	public static final DiceFormula graduateBFormula = new DiceFormula(DiceType.D18, 8);
	public static final DiceFormula graduateCFormula = new DiceFormula(DiceType.D18, 9);
	public static final DiceFormula masteryFormula = new DiceFormula(DiceType.D20, 10);
	
	private DiceType die; //names the properties
	private int defaultModifier; 
	
	public DiceFormula(DiceType die, int defaultModifier){ //shows how the properties are listed
		this.die = die;
		this.defaultModifier = defaultModifier;
	} 
	
	public RollResult rollDiceDefault(){
		String formula = "1d" + die.getDieValue(); //go to that list below and pull the value that's in the ()
		List<Integer> rollThemBones = DiceRoller.rollThemBones(formula); 
		return new RollResult(rollThemBones, defaultModifier);
	}
	
	public RollResult rollDiceWithModifiers(boolean withAdvantage, boolean withBurden, boolean inCombat){
		int normalDie = die.ordinal(); //finds the number of the current die in the enumeration
		int standardModifier = defaultModifier; //This is what you would normally get.
		
		
		int dieToRoll = normalDie;
		int modifier = standardModifier;
		
		if(withAdvantage){
			dieToRoll = Math.min(normalDie+1, DiceType.values().length - 1); //Defines the new die, which is the current die+1, no greater than the max dice option.
		}
		if(inCombat){
			modifier = 0;
		}
		if(withBurden){
			modifier = modifier - 1;
		}
		
		DiceType die = DiceType.values()[dieToRoll];
		String formula = "1d" + die.getDieValue();
		List<Integer> dieRoll = DiceRoller.rollThemBones(formula);
		return new RollResult(dieRoll, modifier);
	}
	
	public static enum DiceType{ //list of DiceType. defined as name (D4) value (4) and placement in the list (0)
		D4(4), //This is 0
		D6(6), //This is 1
		D8(8), //This is 2
		D10(10), //This is 3
		D12(12), //This is 4
		D14(14), //This is 5
		D16(16), //This is 6
		D18(18), //This is 7
		D20(20); //This is 8
		
		private int dieValue;
		private DiceType(int dieValue){
			this.dieValue = dieValue;
		}
		
		public int getDieValue(){
			return dieValue;
		}
	}
}
