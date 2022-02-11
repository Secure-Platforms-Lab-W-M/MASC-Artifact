package edu.wm.cs.masc.similarity.selector;

import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;

public interface Selector {
	
	
	MutationLocation select(List<MutationLocation> locations);

}
