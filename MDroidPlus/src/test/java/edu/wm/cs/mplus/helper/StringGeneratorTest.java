package edu.wm.cs.mplus.helper;

import edu.wm.cs.mplus.helper.StringGenerator;

public class StringGeneratorTest {

	private static final int iteration = 200000;
	
	public static void main(String[] args) {
		
		
		for(int i = 0; i<iteration; i++){
			String randomString = StringGenerator.generateRandomString();
			System.out.println(randomString);
		}
	}

}
