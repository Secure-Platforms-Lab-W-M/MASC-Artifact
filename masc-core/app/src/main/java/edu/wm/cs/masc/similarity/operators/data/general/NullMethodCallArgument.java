package edu.wm.cs.masc.similarity.operators.data.general;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;
import edu.wm.cs.masc.similarity.helper.FileHelper;

public class NullMethodCallArgument implements MutationOperator {

	@Override
	public boolean performMutation(MutationLocation location) {
		
		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			if(i == location.getLine()){
				//Apply mutation
				String sub1 = currLine.substring(0, location.getStartColumn());
				String sub2 = currLine.substring(location.getEndColumn());

				currLine = sub1 + "null" + sub2;
			}
			
			newLines.add(currLine);
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}

}
