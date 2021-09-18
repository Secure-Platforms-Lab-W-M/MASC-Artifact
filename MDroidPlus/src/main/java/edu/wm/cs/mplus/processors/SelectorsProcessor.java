package edu.wm.cs.mplus.processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import edu.wm.cs.mplus.model.MutationType;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.selector.Selector;
import edu.wm.cs.mplus.selector.SelectorLocationFactory;

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
