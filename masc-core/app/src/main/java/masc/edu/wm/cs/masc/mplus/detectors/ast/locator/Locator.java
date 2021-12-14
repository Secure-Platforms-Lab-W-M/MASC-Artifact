package edu.wm.cs.mplus.detectors.ast.locator;

import java.util.List;

import edu.wm.cs.mplus.model.location.MutationLocation;

public interface Locator {

	List<MutationLocation> findExactLocations(List<MutationLocation> locations);
	
}
