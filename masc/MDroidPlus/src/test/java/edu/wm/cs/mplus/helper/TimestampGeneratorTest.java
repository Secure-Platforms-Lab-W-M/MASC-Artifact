package edu.wm.cs.mplus.helper;

import java.util.Date;

import edu.wm.cs.mplus.helper.TimestampGenerator;

public class TimestampGeneratorTest {

	private static final int iteration = 200000;

	public static void main(String[] args) {

		for(int i = 0; i<iteration; i++){
			Date randomDate = new Date(TimestampGenerator.generateRandomTimestamp());
			System.out.println(randomDate);
		}
	}
}
