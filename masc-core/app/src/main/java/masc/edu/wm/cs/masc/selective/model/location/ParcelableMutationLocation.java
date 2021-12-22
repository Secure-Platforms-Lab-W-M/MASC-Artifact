package edu.wm.cs.mplus.model.location;

import java.util.List;

public class ParcelableMutationLocation extends MutationLocation{

	
	private List<Integer> overrideToDelete;

	public List<Integer> getOverrideToDelete() {
		return overrideToDelete;
	}

	public void setOverrideToDelete(List<Integer> overrideToDelete) {
		this.overrideToDelete = overrideToDelete;
	}
	
}
