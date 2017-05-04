package com.dreaminsteam.rpbot.utilities;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DiceFormula {

	public static DiceFormula firstYearFormula = new DiceFormula(DiceType.D4, 0); //defines the different classes and what properties they have
	public static DiceFormula secondYearFormula = new DiceFormula(DiceType.D6, 1);
	public static DiceFormula thirdYearFormula = new DiceFormula(DiceType.D8, 2);
	public static DiceFormula forthYearFormula = new DiceFormula(DiceType.D10, 3);
	public static DiceFormula fifthYearFormula = new DiceFormula(DiceType.D12, 4);
	public static DiceFormula sixYearFormula = new DiceFormula(DiceType.D14, 5);
	public static DiceFormula seventhYearFormula = new DiceFormula(DiceType.D16, 6);
	public static DiceFormula GraduateAFormula = new DiceFormula(DiceType.D18, 7);
	public static DiceFormula GraduateBFormula = new DiceFormula(DiceType.D18, 8);
	public static DiceFormula GraduateCFormula = new DiceFormula(DiceType.D18, 9);
	public static DiceFormula MasteryFormula = new DiceFormula(DiceType.D20, 10);
	
	private DiceType die; //names the properties
	private int defaultModifier; 
	
	public DiceFormula(DiceType die, int defaultModifier){ //shows how the properties are listed
		this.die = die;
		this.defaultModifier = defaultModifier;
	} 
	
	public int rollDiceDefault(){
		String formula = "1d" + die.getDieValue(); //go to that list below and pull the value that's in the ()
		List<Integer> rollThemBones = DiceRoller.rollThemBones(formula); 
		int diceRolls = addList(rollThemBones);
		return diceRolls + defaultModifier; //I think this is bad??
	}
	
	public int rollDiceWithModifiers(boolean withAdvantage, boolean withBurden, boolean inCombat){
		if(withAdvantage){
			int currentDie = die.ordinal(); //finds the number of the current die in the list
			int newDie = Math.max(currentDie+1, 8); //defines the new die, which is the current die+1, no greater than the 8th option
			DiceType dice = DiceType.values()[newDie]; //this assigns a name "dice" to the new die
			
			String formula = "1d" + dice.getDieValue(); //go to that list below and pull the value that's in the ()
			List<Integer> rollThemBones = DiceRoller.rollThemBones(formula);
			int diceRolls = addList(rollThemBones);
			return diceRolls + defaultModifier;
			
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
