package edu.wm.cs.masc.similarity.operators.activity;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.model.location.DifferentActivityMutationLocation;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;

public class WrongMainActivity  implements MutationOperator {

	@Override
	public boolean performMutation(MutationLocation location) {
		
		DifferentActivityMutationLocation mLocation = (DifferentActivityMutationLocation) location;
		
		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			//Mutate activity
			if(i == location.getLine()){
				String sub1 = currLine.substring(0, location.getStartColumn());
				String sub2 = currLine.substring(location.getEndColumn());

				currLine = sub1 + mLocation.getOtherActivity() + sub2;
			}

			newLines.add(currLine);
			
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}

}
