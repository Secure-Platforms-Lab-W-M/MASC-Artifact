package edu.wm.cs.mplus.operators.io;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.mplus.helper.FileHelper;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.model.location.ObjectMutationLocation;
import edu.wm.cs.mplus.operators.MutationOperator;

public class NullStream  implements MutationOperator{

	@Override
	public boolean performMutation(MutationLocation location) {
		
		ObjectMutationLocation mLocation = (ObjectMutationLocation) location;
		
		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			//Null object
			if(i == location.getLine()){
				String newLine = mLocation.getObject() + " = null;";
				newLines.add(newLine);	
			}

			newLines.add(currLine);
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}

}
