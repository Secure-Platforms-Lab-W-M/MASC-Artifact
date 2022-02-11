package edu.wm.cs.masc.similarity.operators;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;

public interface MutationOperator {

	boolean performMutation(MutationLocation location);
	
}
