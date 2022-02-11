package edu.wm.cs.masc.similarity.selector;

import java.util.List;

import edu.wm.cs.masc.similarity.helper.IntegerGenerator;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;

public class GeneralRandomSelector implements Selector{

	@Override
	public MutationLocation select(List<MutationLocation> locations) {
		int randomIndex = IntegerGenerator.generateRandomInt(0, locations.size()-1);
			
		return locations.get(randomIndex);
	}

}
