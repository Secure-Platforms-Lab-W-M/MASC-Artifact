package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;

public class NullGPSLocationLocator implements Locator {

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
