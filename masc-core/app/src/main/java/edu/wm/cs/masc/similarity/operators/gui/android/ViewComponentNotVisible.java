package edu.wm.cs.masc.similarity.operators.gui.android;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.model.location.VisibilityMutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;

public class ViewComponentNotVisible  implements MutationOperator {

	@Override
	public boolean performMutation(MutationLocation location) {
		
		VisibilityMutationLocation mLocation = (VisibilityMutationLocation) location;
		
		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			newLines.add(currLine);
			
			//Mutate view visibility
			if(i == location.getLine()){
				String newLine = mLocation.getView() + ".setVisibility(" + mLocation.getVisibility() + ");";
				newLines.add(newLine);		
			}
			
			
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}

}
