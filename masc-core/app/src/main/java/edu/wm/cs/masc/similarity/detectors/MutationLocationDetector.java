package edu.wm.cs.masc.similarity.detectors;

import java.util.List;

import edu.wm.cs.masc.similarity.model.MutationType;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;

public abstract class MutationLocationDetector {
	
	//Folder path where the analysis starts
	protected String rootPath;
	protected MutationType type;
	
	public abstract List<MutationLocation> analyzeApp(String rootPath) throws Exception;

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public MutationType getType() {
		return type;
	}

	public void setType(MutationType type) {
		this.type = type;
	}

	
	
	
	

}
