package edu.wm.cs.masc.similarity.operators.gui.android;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.helper.IntegerGenerator;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;

public class InvalidIDFindView implements MutationOperator {

	private static final int MINIMUM = 1;
	private static final int MAXIMUM = 1000;

	
	@Override
	public boolean performMutation(MutationLocation location) {
		
		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		//Add lines before the MutationLocation
		for(int i=0; i < location.getStartLine(); i++){
			newLines.add(lines.get(i));
		}
		
		
		//Apply mutation
		String linesToConsider = "";
		boolean newLineFlag = false;
		for(int i = location.getStartLine(); i <= location.getEndLine(); i++){
			if(newLineFlag){
				linesToConsider += " "+lines.get(i);
			} else {
				linesToConsider += lines.get(i);
				newLineFlag = true;
			}
		}
		
		String sub1 = linesToConsider.substring(0, location.getStartColumn());
		String sub2 = linesToConsider.substring(location.getEndColumn());
		int newID = IntegerGenerator.generateRandomInt(MINIMUM, MAXIMUM);
		
		String mutatedLine = sub1 + newID + sub2;
		newLines.add(mutatedLine);
		
		
		//Add lines after the MutationLocation
		for(int i=location.getEndLine()+1; i < lines.size() ; i++){
			newLines.add(lines.get(i));
		}
		
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
		
	}

}
