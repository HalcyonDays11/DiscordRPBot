package com.dreaminsteam.rpbot.utilities;

import java.util.List;



public class DiceFormula {
	private DiceType die; //names the properties
	private int defaultModifier; 
	
	public void setStandardModifier(int modifier){
		this.defaultModifier = modifier;
	}
	
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
			dieToRoll = Math.max(dieToRoll-3, 0); //Defines the new die, which is the current die-5, no less than 0.
			modifier = modifier / 2;
		}
		if(noWand){
			dieToRoll = Math.max(dieToRoll-5, 0); //Defines the new die, which is the current die-2, no less than 0.
			modifier = 0;
		}
		
		DiceType die = DiceType.values()[dieToRoll];
		String formula = "1d" + die.getDieValue();
		List<Integer> dieRoll = DiceRoller.rollThemBones(formula);
		return new RollResult(formula, dieRoll, modifier, maxModifier, destiny);
	}
	
	public static enum DiceType{ //list of DiceType. defined as name (D4) value (4) and placement in the list (0)
		D0(0), //This is 0
		D2(2), //This is 1
		D4(4), //This is 2
		D6(6), //This is 3
		D8(8), //This is 4
		D10(10), //This is 5
		D12(12), //This is 6
		D14(14), //This is 7
		D16(16), //This is 8
		D18(18), //This is 9
		D20(20), //This is 10
		D22(22); //This is 11
		
		
		private int dieValue;
		private DiceType(int dieValue){
			this.dieValue = dieValue;
		}
		
		public int getDieValue(){
			return dieValue;
		}
	}
}
