package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;

public class DummyLocator implements Locator {

	@Override
	public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
		
		//Nothing to do here.
		
		return locations;
	}

}
