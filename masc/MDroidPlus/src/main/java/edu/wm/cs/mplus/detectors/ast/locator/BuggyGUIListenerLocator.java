package edu.wm.cs.mplus.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.mplus.model.location.MutationLocation;

public class BuggyGUIListenerLocator implements Locator {

	@Override
	public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
		List<MutationLocation> exactMutationLocations = new ArrayList<MutationLocation>();

		for(MutationLocation loc : locations){
			try{
				findExactLocation(loc);
				
				exactMutationLocations.add(loc);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}

		return exactMutationLocations;
	}

	private void findExactLocation(MutationLocation loc) {
		//Fix start column
		loc.setStartColumn(loc.getStartColumn()+1);			

		//Build exact mutation location
		loc.setEndColumn(loc.getStartColumn()+loc.getLength());
	}
	
}
