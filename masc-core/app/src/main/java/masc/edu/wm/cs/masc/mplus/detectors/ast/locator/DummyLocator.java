package edu.wm.cs.mplus.detectors.ast.locator;

import java.util.List;

import edu.wm.cs.mplus.model.location.MutationLocation;

public class DummyLocator implements Locator {

	@Override
	public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
		
		//Nothing to do here.
		
		return locations;
	}

}
