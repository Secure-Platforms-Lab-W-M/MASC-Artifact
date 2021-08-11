package edu.wm.cs.mplus.operators;

import edu.wm.cs.mplus.model.location.MutationLocation;

public interface MutationOperator {

	boolean performMutation(MutationLocation location);
	
}
