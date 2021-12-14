package edu.wm.cs.mplus.operators.backend;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.mplus.helper.FileHelper;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.model.location.TimedMutationLocation;
import edu.wm.cs.mplus.operators.MutationOperator;

public class LengthyBackEndService implements MutationOperator{

	@Override
	public boolean performMutation(MutationLocation location) {
		
		
		TimedMutationLocation mLocation = (TimedMutationLocation) location;
		
		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			newLines.add(currLine);
			
			if(i == location.getLine()){
				//Apply mutation
				String newLine = "try {\n" +
								"Thread.sleep(" + mLocation.getSeconds() + ");\n" +
								"} catch (InterruptedException e) {\n" +
								"e.printStackTrace();\n" +
								"}";
				
				newLines.add(newLine);
				
			}
			
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}

}
