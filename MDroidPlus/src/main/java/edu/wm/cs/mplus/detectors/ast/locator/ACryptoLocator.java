package edu.wm.cs.mplus.detectors.ast.locator;

import edu.wm.cs.mplus.helper.FileHelper;
import edu.wm.cs.mplus.model.location.MutationLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class ACryptoLocator implements Locator {

	@Override
	public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
		List<MutationLocation> exactMutationLocations = new ArrayList<MutationLocation>();

		for(MutationLocation loc : locations){

			try{
				MutationLocation newLoc = findExactLocation(loc);

				if(newLoc != null){
					exactMutationLocations.add(loc);
				}

			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return exactMutationLocations;
	}

	private MutationLocation findExactLocation(MutationLocation loc) {
		//Fix start column
		loc.setStartColumn(loc.getStartColumn()+1);

		List<String> lines = FileHelper.readLines(loc.getFilePath());

		//Select API call substring
		String linesToConsider = "";
		boolean newLine = false;
		for(int i = loc.getStartLine(); i <= loc.getEndLine(); i++){
			if(newLine){
				linesToConsider += " "+lines.get(i);
			} else {
				linesToConsider += lines.get(i);
				newLine = true;
			}
		}
						
		//Check if new Intent(..) is followed by a method call
		int nextCharIndex = loc.getStartColumn()+loc.getLength();
		if(nextCharIndex < linesToConsider.length()){
			char nextChar = linesToConsider.charAt(nextCharIndex);
			if(nextChar == '.' || nextChar == ')'){
				return null;
			}
		}

		//Build exact mutation location
		loc.setEndColumn(loc.getStartColumn()+loc.getLength());

		return loc;
	}

}
