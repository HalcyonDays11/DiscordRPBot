package com.dreaminsteam.rpbot.utilities;

import java.util.List;



public class DiceFormula {
	private DiceType die; //names the properties
	private int defaultModifier; 
	private int penaltyBuffer;
	
	public static final int BASE_ADVANTAGE_BONUS = 1;
	public static final int BASE_NONVERBAL_PENALTY = 6;
	public static final int BASE_WANDLESS_PENALTY = 7;
	
	
	public DiceFormula(DiceType die, int defaultModifier, int penaltyBuffer){ //shows how the properties are listed
		this.die = die;
		this.defaultModifier = defaultModifier;
		this.penaltyBuffer = penaltyBuffer;
	} 

	public void setStandardModifier(int modifier){
		this.defaultModifier = modifier;
	}
	
	public RollResult rollDiceDefault(){
		String formula = "1d" + die.getDieValue(); //go to that list below and pull the value that's in the ()
		List<Integer> rollThemBones = DiceRoller.rollThemBones(formula); 
		return new RollResult(formula, rollThemBones, defaultModifier);
	}
	
	public RollResult rollDiceWithModifiers(boolean withAdvantage, boolean withBurden, boolean inCombat, boolean noWords, boolean noWand, int destiny){
		int normalDie = die.ordinal(); //finds the number of the current die in the enumeration
		int standardModifier = defaultModifier; //This is what you would normally get.
		int maxModifier = defaultModifier; //This is what you could have, if not in combat.
		
		int dieToRoll = normalDie;
		int modifier = standardModifier;
		
		if(withAdvantage){
			//Go up one die level.
			dieToRoll = dieToRoll + BASE_ADVANTAGE_BONUS;
		}
		if(noWords){
			//Go down by some die, your modifier is cut in half.
			dieToRoll = dieToRoll - (BASE_NONVERBAL_PENALTY - penaltyBuffer);
			modifier = modifier / 2;
		}
		if(inCombat){
			//No modifier bonus.
			modifier = 0;
		}
		if(noWand){
			//Go down some die, your modifier is 0.
			dieToRoll = dieToRoll - BASE_WANDLESS_PENALTY;
			modifier = 0;
		}
		if(withBurden){
			//Modifier bonus and possible bonus decrease by one point. 
			modifier = modifier - 1;
			maxModifier = maxModifier - 1;
		}
		
		//This will cap the dieToRoll... no smaller than 0, no bigger than the last DiceType value.
		dieToRoll = Math.max(dieToRoll, 0); //The bigger of dieToRoll or zero
		dieToRoll = Math.min(dieToRoll, DiceType.values().length - 1); //The smaller of dieRoll or the highest die.
		
		DiceType die = DiceType.values()[dieToRoll];
		String formula = "1d" + die.getDieValue();
		List<Integer> dieRoll = DiceRoller.rollThemBones(formula);
		if(dieRoll.isEmpty()){
			//If the dieRoll is empty, special case to just return 0.
			return new RollResult(formula, true);
		}
		return new RollResult(formula, dieRoll, modifier, maxModifier, destiny);
	}
	
	public static enum DiceType{ //list of DiceType. defined as name (D0) value (0) and placement in the list (0)
		D0(0), //This is 0
		D1(1), //This is 1
		D2(2), //This is 2
		D4(4), //This is 3
		D6(6), //This is 4
		D8(8), //This is 5
		D10(10), //This is 6
		D12(12), //This is 7
		D14(14), //This is 8
		D16(16), //This is 9
		D18(18), //This is 10
		D20(20), //This is 11
		D22(22); //This is 12
		
		
		private int dieValue;
		private DiceType(int dieValue){
			this.dieValue = dieValue;
		}
		
		public int getDieValue(){
			return dieValue;
		}
	}
}
