package edu.wm.cs.masc.similarity.operators.gui.android;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.model.location.ViewMutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;

public class InvalidViewFocus  implements MutationOperator {

	@Override
	public boolean performMutation(MutationLocation location) {
		
		ViewMutationLocation mLocation = (ViewMutationLocation) location;
		
		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			newLines.add(currLine);
			
			//Mutate view focus
			if(i == location.getLine()){
				String newLine = mLocation.getView() + ".requestFocus();";
				newLines.add(newLine);		
			}
			
			
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}

}
