package edu.wm.cs.masc.similarity.detectors.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.wm.cs.masc.similarity.model.MutationType;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.detectors.TextBasedDetector;

public class MissingPermissionDetector extends TextBasedDetector {

	private static String MANIFEST = "AndroidManifest.xml";
	
	
	public MissingPermissionDetector(){
		this.type = MutationType.MISSING_PERMISSION_MANIFEST;
	}
	
	@Override
	public List<MutationLocation> analyzeApp(String rootPath) throws Exception {
		Stack<String> stack = new Stack<>();
		List<MutationLocation> locations = new ArrayList<MutationLocation>();
	
		String path = rootPath+File.separator+MANIFEST;
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		String line = null;
		int startLine = 0;
		

		boolean isPermissionTag = false;
		int currentLine = 0;
		while( (line = reader.readLine() ) != null){
	
			if( line.contains("<uses-permission")){
				startLine = currentLine;
				isPermissionTag  = true;
				stack.add("uses-permission");

			}else if(isPermissionTag && line.contains("<") && !line.contains("</") ){
				stack.add("other");
			}
			
			if(isPermissionTag && (line.contains("</") || line.contains("/>") || line.contains("-->"))){
				stack.pop();
			}
			
			if(isPermissionTag && stack.isEmpty()){
				locations.add(buildLocation(path, startLine, currentLine));
				isPermissionTag  = false;
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
