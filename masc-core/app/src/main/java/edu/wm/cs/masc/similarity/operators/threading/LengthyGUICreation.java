package edu.wm.cs.masc.similarity.operators.threading;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.model.location.TimedMutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;
import edu.wm.cs.masc.similarity.helper.FileHelper;

public class LengthyGUICreation implements MutationOperator {

	@Override
	public boolean performMutation(MutationLocation location) {
		
		TimedMutationLocation mLocation = (TimedMutationLocation) location;
		
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
				linesToConsider += "\n"+lines.get(i);
			} else {
				linesToConsider += lines.get(i);
				newLineFlag = true;
			}
		}
		
		String sub1 = linesToConsider.substring(0, location.getStartColumn());
		String sub2 = linesToConsider.substring(location.getStartColumn());
		//sub2 = sub2.replaceAll(";", ";\n");
		
		String sleep = "\n try {\n" +
				"Thread.sleep(" + mLocation.getSeconds() + ");\n" +
				"} catch (InterruptedException e) {\n" +
				"e.printStackTrace();\n" +
				"}\n";
		
		String mutatedLine = sub1 + sleep  + sub2;
		newLines.add(mutatedLine);
		
		
		//Add lines after the MutationLocation
		for(int i=location.getEndLine()+1; i < lines.size() ; i++){
			newLines.add(lines.get(i));
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}
	

}
