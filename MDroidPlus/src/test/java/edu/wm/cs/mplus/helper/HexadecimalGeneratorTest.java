package edu.wm.cs.mplus.helper;

import edu.wm.cs.mplus.helper.HexadecimalGenerator;

public class HexadecimalGeneratorTest {

	private static final int iteration = 200000;
	
	public static void main(String[] args) {
		
		
		for(int i = 0; i<iteration; i++){
			String randomHex = HexadecimalGenerator.generateColor();
			System.out.println(randomHex);
		}
	}

}
