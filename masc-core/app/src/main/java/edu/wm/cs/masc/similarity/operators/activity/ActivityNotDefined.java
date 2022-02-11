package edu.wm.cs.masc.similarity.operators.activity;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;

public class ActivityNotDefined implements MutationOperator {

	@Override
	public boolean performMutation(MutationLocation location) {

		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			//Discard lines to remove in the new source code file
			if(i < location.getStartLine() || i > location.getEndLine()){
				newLines.add(currLine);
			}
			
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}

}
