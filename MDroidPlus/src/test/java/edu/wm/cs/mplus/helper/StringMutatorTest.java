package edu.wm.cs.mplus.helper;

import edu.wm.cs.mplus.helper.StringMutator;

public class StringMutatorTest {

	private static final String toMutate = "this is a string";
	private static final int iteration = 200000;
	
	public static void main(String[] args) {
		
		
		for(int i = 0; i<iteration; i++){
			String mutated = StringMutator.performMutation(toMutate);
			System.out.println(mutated);
		}
	}

}
