package com.dreaminsteam.rpbot.utilities;

import java.util.List;

public class DiceFormula {
	private DiceType die; //names the properties
	private int defaultModifier; 
	
	public DiceFormula(DiceType die, int defaultModifier){ //shows how the properties are listed
		this.die = die;
		this.defaultModifier = defaultModifier;
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
			dieToRoll = Math.min(dieToRoll+1, DiceType.values().length - 1); //Defines the new die, which is the current die+1, no greater than the max dice option.
		}
		if(inCombat){
			modifier = 0;
		}
		if(withBurden){
			modifier = modifier - 1;
			maxModifier = maxModifier - 1;
		}
		if(noWords){
			dieToRoll = Math.max(dieToRoll-1, 0); //Defines the new die, which is the current die-1, no less than 0.
		}
		if(noWand){
			dieToRoll = Math.max(dieToRoll-2, 0); //Defines the new die, which is the current die-2, no less than 0.
		}
		
		DiceType die = DiceType.values()[dieToRoll];
		String formula = "1d" + die.getDieValue();
		List<Integer> dieRoll = DiceRoller.rollThemBones(formula);
		return new RollResult(formula, dieRoll, modifier, maxModifier, destiny);
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
