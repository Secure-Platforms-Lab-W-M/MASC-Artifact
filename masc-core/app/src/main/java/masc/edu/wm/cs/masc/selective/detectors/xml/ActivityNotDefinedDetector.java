package edu.wm.cs.mplus.detectors.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.wm.cs.mplus.detectors.TextBasedDetector;
import edu.wm.cs.mplus.model.MutationType;
import edu.wm.cs.mplus.model.location.MutationLocation;

public class ActivityNotDefinedDetector extends TextBasedDetector {

	private static String MANIFEST = "AndroidManifest.xml";
	
	
	public ActivityNotDefinedDetector(){
		this.type = MutationType.ACTIVITY_NOT_DEFINED;
	}
	
	@Override
	public List<MutationLocation> analyzeApp(String rootPath) throws Exception {
		Stack<String> stack = new Stack<>();
		List<MutationLocation> locations = new ArrayList<MutationLocation>();
	
		String path = rootPath+File.separator+MANIFEST;
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		String line = null;
		int startLine = 0;
		

		boolean isActivityTag = false;
		int currentLine = 0;
		while( (line = reader.readLine() ) != null){
	
			if( line.contains("<activity")){
				startLine = currentLine;
				isActivityTag  = true;
				stack.add("activity");

			}else if(isActivityTag && line.contains("<") && !line.contains("</") ){
				stack.add("other");
			}
			
			if(isActivityTag && (line.contains("</") || line.contains("/>") || line.contains("-->"))){
				stack.pop();
			}
			
			if(isActivityTag && stack.isEmpty()){
				locations.add(buildLocation(path, startLine, currentLine));
				isActivityTag  = false;
			}
			
			currentLine++;
		}
		reader.close();
		
		return locations;
	}

	private MutationLocation buildLocation( String path, int startLine, int currentLine) {

		int endLine = currentLine;
		
		MutationLocation location = new MutationLocation();
		location.setFilePath(path);
		location.setStartLine(startLine);
		location.setEndLine(endLine);
		location.setType(this.getType());
		return location;
		
		
	}

}
