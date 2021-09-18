package edu.wm.cs.mplus.helper;

import edu.wm.cs.mplus.helper.IntegerGenerator;

public class IntegerGeneratorTest {

	private static final int iteration = 200000;
	
	public static void main(String[] args) {
		
		System.out.println("Generation of integers between 1-10");
		for(int i = 0; i<iteration; i++){
			System.out.println(IntegerGenerator.generateRandomInt(1,10));
		}
		
		System.out.println("Generation of integers");
		for(int i = 0; i<iteration; i++){
			System.out.println(IntegerGenerator.generateRandomInt());
		}
	}

}
