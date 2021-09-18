package edu.wm.cs.mplus.selector;

import java.util.List;

import edu.wm.cs.mplus.model.location.MutationLocation;

public interface Selector {
	
	
	MutationLocation select(List<MutationLocation> locations);

}
