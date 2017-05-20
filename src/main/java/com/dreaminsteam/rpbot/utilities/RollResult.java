package com.dreaminsteam.rpbot.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RollResult {

	private final List<Integer> diceRolls;
	private int modifier;
	private int personalModifier;
	private String rollFormula;
	
	private int theoreticalModifier;
	
	private int destinyModifier;
	
	public RollResult(String rollFormula, List<Integer> diceRolls){
		this(rollFormula, diceRolls, 0, 0, 0);
	}
	
	public RollResult(String rollFormula, List<Integer> diceRolls, int modifier){
		this(rollFormula, diceRolls, modifier, modifier, 0);
	}
	
	public RollResult(String rollFormula, List<Integer> diceRolls, int modifier, int theoreticalModifier, int destiny){
		this.rollFormula = rollFormula;
		this.diceRolls = diceRolls;
		this.modifier = modifier;
		this.theoreticalModifier = theoreticalModifier;
		this.destinyModifier = destiny;
	}
	
	public int getTotal(){
		AtomicInteger result = new AtomicInteger(0);
		diceRolls.stream().forEach(die -> result.addAndGet(die));
		return result.addAndGet(modifier + personalModifier + destinyModifier);
	}
	
	public int getTheoreticalTotal(){
		AtomicInteger result = new AtomicInteger(0);
		diceRolls.stream().forEach(die -> result.addAndGet(die));
		return result.addAndGet(theoreticalModifier + personalModifier + destinyModifier);
	}
	
	public List<Integer> getDiceRolls(){
		return new ArrayList<Integer>(diceRolls);
	}
	
	public void setPersonalModifier(int personalModifier){
		this.personalModifier = personalModifier;
	}
	
	public int getPersonalModifier(){
		return personalModifier;
	}
	
	public int getModifier(){
		return modifier;
	}
	
	public void setModifier(int modifier){
		this.modifier = modifier;
	}
	
	public void setDestinyModifier(int destinyModifier) {
		this.destinyModifier = destinyModifier;
	}
	
	public int getDestinyModifier() {
		return destinyModifier;
	}

	public String getRollFormula() {
		return rollFormula;
	}

	public void setRollFormula(String rollFormula) {
		this.rollFormula = rollFormula;
	}

	public int getTheoreticalModifier() {
		return theoreticalModifier;
	}

	public void setTheoreticalModifier(int theoreticalModifier) {
		this.theoreticalModifier = theoreticalModifier;
	}
}
