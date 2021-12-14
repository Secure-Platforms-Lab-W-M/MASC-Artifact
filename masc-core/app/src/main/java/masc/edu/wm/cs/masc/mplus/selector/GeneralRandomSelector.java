package edu.wm.cs.mplus.selector;

import java.util.List;

import edu.wm.cs.mplus.helper.IntegerGenerator;
import edu.wm.cs.mplus.model.location.MutationLocation;

public class GeneralRandomSelector implements Selector{

	@Override
	public MutationLocation select(List<MutationLocation> locations) {
		int randomIndex = IntegerGenerator.generateRandomInt(0, locations.size()-1);
			
		return locations.get(randomIndex);
	}

}
