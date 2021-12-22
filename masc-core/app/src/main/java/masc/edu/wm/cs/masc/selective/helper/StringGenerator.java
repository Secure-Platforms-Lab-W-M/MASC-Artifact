package edu.wm.cs.mplus.helper;

import java.util.UUID;

public class StringGenerator {

	
	public static String generateRandomString(){
		
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
