package edu.wm.cs.masc.similarity.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import edu.wm.cs.masc.similarity.selector.Selector;
import edu.wm.cs.masc.similarity.selector.SelectorLocationFactory;
import edu.wm.cs.masc.similarity.model.MutationType;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;

public class SelectorsProcessor {

	
	public static List<MutationLocation> process(HashMap<MutationType, List<MutationLocation>> locations){
		
		List<MutationLocation> selectedLocations = new ArrayList<MutationLocation>();
		SelectorLocationFactory factory = SelectorLocationFactory.getInstance();
		
		for(Entry<MutationType, List<MutationLocation>> entry : locations.entrySet()){
			Selector selector = factory.getSelector(entry.getKey());
			MutationLocation selectedLocation = selector.select(entry.getValue());
			selectedLocations.add(selectedLocation);
		}
		
		return selectedLocations;
	}
}
