package com.dreaminsteam.rpbot.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RollResult {

	private final List<Integer> diceRolls;
	private int modifier;
	
	public RollResult(List<Integer> diceRolls){
		this(diceRolls, 0);
	}
	
	public RollResult(List<Integer> diceRolls, int modifier){
		this.diceRolls = diceRolls;
		this.modifier = modifier;
	}
	
	public int getTotal(){
		AtomicInteger result = new AtomicInteger(0);
		diceRolls.stream().forEach(die -> result.addAndGet(die));
		return result.addAndGet(modifier);
	}
	
	public List<Integer> getDiceRolls(){
		return new ArrayList<Integer>(diceRolls);
	}
	
	public int getModifier(){
		return modifier;
	}
	
	public void setModifier(int modifier){
		this.modifier = modifier;
	}
}
