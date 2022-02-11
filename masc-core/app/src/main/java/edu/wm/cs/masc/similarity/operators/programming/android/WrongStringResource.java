package edu.wm.cs.masc.similarity.operators.programming.android;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;
import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.helper.StringGenerator;

public class WrongStringResource implements MutationOperator {

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
		
		int startS = linesToConsider.indexOf("<string");
		
		if(startS < 0){
			return false;
		}
		
		int start = linesToConsider.indexOf(">", startS)+1;
		
		int end = linesToConsider.indexOf("</string>");
		
		String sub1 = linesToConsider.substring(0, start);
		String sub2 = linesToConsider.substring(end);

		String randomString = StringGenerator.generateRandomString();
		
		String mutatedLine = sub1 + randomString + sub2;
		newLines.add(mutatedLine);


		//Add lines after the MutationLocation
		for(int i=location.getEndLine()+1; i < lines.size() ; i++){
			newLines.add(lines.get(i));
		}


		FileHelper.writeLines(location.getFilePath(), newLines);

		return true;
	}
	
	
	@Deprecated
	public boolean performMutation_old(MutationLocation location) {

		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			
			if(i == location.getStartLine()){
				if(currLine.contains(">") && currLine.contains("</")){
					int start = currLine.indexOf(">")+1;
					int end = currLine.indexOf("</");
					if(start < end){
						String sub1 = currLine.substring(0, start);
						String value = currLine.substring(start, end);
						String sub2 = currLine.substring(end);
						
						//This could introduce compiling errors in xml files
						//value = StringMutator.performMutation(value);
						value = StringGenerator.generateRandomString();
						
						currLine = sub1 + value + sub2;
					}
				}
			}
			
			newLines.add(currLine);
			
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}

}
