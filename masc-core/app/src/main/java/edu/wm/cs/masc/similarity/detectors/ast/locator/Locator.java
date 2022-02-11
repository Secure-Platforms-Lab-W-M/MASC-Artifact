package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;

public interface Locator {

	List<MutationLocation> findExactLocations(List<MutationLocation> locations);
	
}
