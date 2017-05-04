package com.dreaminsteam.rpbot.utilities;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DiceFormula {

	public static DiceFormula firstYearFormula = new DiceFormula(DiceType.D4, 1);
	
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
			//Dunno what you want here
			return -1;
		}
		if(withBurden){
			int currentDie = die.ordinal(); //This is the order in the list
			int newDie = Math.min(currentDie, 0); //Can't go below zero...
			DiceType dice = DiceType.values()[newDie];
			
			String formula = "1d" + dice.getDieValue();
			List<Integer> rollThemBones = DiceRoller.rollThemBones(formula);
			return addList(rollThemBones);
		}
		if(inCombat){
			return -1;
		}
		return 0;
	}
	
	private int addList(List<Integer> rolls){
		AtomicInteger result = new AtomicInteger(0);
		rolls.stream().forEach(die -> result.addAndGet(die));
		return result.get();
	}
	
	public static enum DiceType{
		D4(4),
		D6(6),
		D8(8),
		D10(10),
		D12(12),
		D20(20);
		
		private int dieValue;
		private DiceType(int dieValue){
			this.dieValue = dieValue;
		}
		
		public int getDieValue(){
			return dieValue;
		}
	}
}
