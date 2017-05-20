package com.dreaminsteam.rpbot.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiceRoller {

	public static Random rando = new Random();
	
	public static List<Integer> rollThemBones(String formula){
		String[] split = formula.split("d");
		
		String numberOfDiceStr = split[0];
		int numberOfDice = Integer.parseInt(numberOfDiceStr);
		if(numberOfDice > 500){
			return null;
		}
		String typeOfDiceStr = split[1];
		int typeOfDice = Integer.parseInt(typeOfDiceStr);
		
		List<Integer> results = new ArrayList<>();
		for(int i = 0; i < numberOfDice; i++){
			results.add(rando.nextInt(typeOfDice) + 1);
		}
		
		return results;
	}
}
