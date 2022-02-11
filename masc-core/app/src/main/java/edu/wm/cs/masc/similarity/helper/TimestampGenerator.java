package edu.wm.cs.masc.similarity.helper;

import java.util.Random;

public class TimestampGenerator {
	
	
	
	public static long generateRandomTimestamp(){
		Random rnd = new Random();

		// Get an Epoch value roughly between 1940 and 2040
		// -946771200000L = January 1, 1940
		// Add up to 70 years to it (using modulus on the next long)
		return -946771200000L + (Math.abs(rnd.nextLong()) % (100L * 365 * 24 * 60 * 60 * 1000));
	}

}
