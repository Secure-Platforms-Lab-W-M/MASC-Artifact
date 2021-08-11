package edu.wm.cs.mplus.helper;

import java.util.Random;

public class StringMutator {

	private static final int STRING_MUTATORS = 1;
	
	public static String performMutation(String str){
		
		
		if(str.trim().isEmpty()){
			return randomString();
		}
		
		//Randomly select a String Mutator Operator
		int randomMutator = (int) Math.round((Math.random() * STRING_MUTATORS));
		switch (randomMutator) {
	        case 0:  return swapCharacters(str);               
	        default:  return deleteCharacter(str);                 
		}
                 
	
	}
	
	
	private static String swapCharacters(String str){
		//System.out.println("*Swapping Chars*");
		char[] characters = str.toCharArray();
		int minimum = 0;
		int maximum = characters.length-1;
		
		int i = minimum + (int)(Math.random() * maximum);
		int j = minimum + (int)(Math.random() * maximum);
		
		//Swaping characters
		char temp = characters[i];
		characters[i] = characters[j];
		characters[j] = temp;
		
		String newStr = new String(characters);
		return newStr;
	}
	
	
	
	private static String randomString(){
		return StringGenerator.generateRandomString();
	}
	
	private static String deleteCharacter(String str){
		//System.out.println("*Deleting Chars*");
		int minimum = 0;
		int maximum = str.length()-1;
		
		if(maximum > 1){
			Random rnd = new Random();
			int rndInt = rnd.nextInt( maximum-1);
		    if(rndInt == 0){
		    	rndInt = 1;
		    }
			int i = minimum + rndInt;

			String sub1 = str.substring(0, i);
			String sub2 = "";
			if(i+1 < maximum-1 ){
				sub2 = str.substring(i+1);
			}
			
			String newStr = sub1 + sub2;
			
			return newStr;
		}else{
			return "";
		}
		
	}
	
	
	
	
}
