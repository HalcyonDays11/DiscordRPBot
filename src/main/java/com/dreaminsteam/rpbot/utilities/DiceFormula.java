package com.dreaminsteam.rpbot.utilities;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DiceFormula {

	public static DiceFormula firstYearFormula = new DiceFormula(DiceType.D4, 1); //I'm creating a new formula for first years, and the formula has a two boxes, and the DiceType box had a d4, and the int box has a 1  
	
	private DiceType die; 
	private int defaultModifier; 
	
	public DiceFormula(DiceType die, int defaultModifier){
		this.die = die;
		this.defaultModifier = defaultModifier;
	} 
	
	public int rollDiceDefault(){
		String formula = "1d" + die.getDieValue();
		List<Integer> rollThemBones = DiceRoller.rollThemBones(formula);
		int diceRolls = addList(rollThemBones);
		return diceRolls + defaultModifier;
	}
	
	public int rollDiceWithModifiers(boolean withAdvantage, boolean withBurden, boolean inCombat){
		if(withAdvantage){
			int currentDie = die.ordinal(); //Ok, this finds the number of the current die in the list, yes?
			int newDie = Math.max(currentDie+1, 8); //And this defines what the new die should be, which is the current die+1, no greater than the 8th option, yes?
			DiceType dice = DiceType.values()[newDie]; //And this assigns a name "dice" to the new die, yeah?
			
			String formula = "1d" + dice.getDieValue();
			List<Integer> rollThemBones = DiceRoller.rollThemBones(formula);
			return addList(rollThemBones);
			
		}
		if(withBurden){
			String formula = "1d" + die.getDieValue();
			List<Integer> rollThemBones = DiceRoller.rollThemBones(formula);
			return addList(rollThemBones);
		}
		if(inCombat){
			String formula = "1d" + die.getDieValue();
			List<Integer> rollThemBones = DiceRoller.rollThemBones(formula);
			int diceRolls = addList(rollThemBones);
			return diceRolls;
		}
		return 0;
	}
	
	private int addList(List<Integer> rolls){
		AtomicInteger result = new AtomicInteger(0);
		rolls.stream().forEach(die -> result.addAndGet(die));
		return result.get();
	}
	
	public static enum DiceType{ //ok, so every time the above code looks for DiceType, it looks here? And each one has a name (D4) a value (4) and a number in the list (0)?
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
