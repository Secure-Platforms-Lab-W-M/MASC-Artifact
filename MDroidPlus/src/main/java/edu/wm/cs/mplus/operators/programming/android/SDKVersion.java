package edu.wm.cs.mplus.operators.programming.android;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.mplus.helper.FileHelper;
import edu.wm.cs.mplus.helper.IntegerGenerator;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.operators.MutationOperator;

public class SDKVersion implements MutationOperator{

	public static final int MIN_VERSION = 2;
	public static final int MAX_VERSION = 23;

	@Override
	public boolean performMutation(MutationLocation location) {
		
		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			if(i == location.getLine()){
				//Apply mutation
				String sub1 = currLine.substring(0, location.getStartColumn());
				String toMutate = currLine.substring(location.getStartColumn(), location.getEndColumn());
				String sub2 = currLine.substring(location.getEndColumn());

				int currVersion = Integer.parseInt(toMutate);
				int newVersion = IntegerGenerator
						.generateRandomInt(MIN_VERSION, MAX_VERSION);
				
				while(newVersion == currVersion){
					newVersion = IntegerGenerator.generateRandomInt(MIN_VERSION, MAX_VERSION);
				}
				
				currLine = sub1 + newVersion + sub2;
			}
			
			newLines.add(currLine);
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}

}
