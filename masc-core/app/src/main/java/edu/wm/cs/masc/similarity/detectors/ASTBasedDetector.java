package edu.wm.cs.masc.similarity.detectors;

import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;

public abstract class ASTBasedDetector extends MutationLocationDetector{

	public abstract List<MutationLocation> analyzeApp(String rootPath);

}
