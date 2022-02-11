package edu.wm.cs.masc.similarity.processors;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wm.cs.masc.similarity.detectors.MutationLocationDetector;
import edu.wm.cs.masc.similarity.model.MutationType;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;


public class TextBasedDetectionsProcessor {

	public static HashMap<MutationType, List<MutationLocation>> process(String rootPath, List<MutationLocationDetector> detectors){
		HashMap<MutationType, List<MutationLocation>> locations = new HashMap<>();
		for(MutationLocationDetector mutationLocationDetector : detectors) {
			
			try {
				locations.put(mutationLocationDetector.getType(), mutationLocationDetector.analyzeApp(rootPath));
			} catch (Exception e) {
				Logger.getAnonymousLogger().log(Level.SEVERE, "Error running detector: "+mutationLocationDetector.getType());
				e.printStackTrace();
			}
	   }
		return locations;
	}
		
	
	
}
