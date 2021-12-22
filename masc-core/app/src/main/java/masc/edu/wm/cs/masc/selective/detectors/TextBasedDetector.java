package edu.wm.cs.mplus.detectors;

import java.util.List;

import edu.wm.cs.mplus.model.location.MutationLocation;

public abstract class TextBasedDetector  extends MutationLocationDetector {

	
	public abstract List<MutationLocation> analyzeApp(String rootPath) throws Exception;

}
