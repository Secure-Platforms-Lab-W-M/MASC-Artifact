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

public class WrongStringResourceDetector extends TextBasedDetector {

	private static String STRINGS = "strings.xml";
	
	
	public WrongStringResourceDetector(){
		this.type = MutationType.WRONG_STRING_RESOURCE;
	}
	
	@Override
	public List<MutationLocation> analyzeApp(String rootPath) throws Exception {
		Stack<String> stack = new Stack<>();
		List<MutationLocation> locations = new ArrayList<MutationLocation>();
	
		String path = rootPath+File.separator+"res"+File.separator+"values"+File.separator+STRINGS;
		BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
		String line = null;
		int startLine = 0;
		

		boolean isStringTag = false;
		int currentLine = 0;
		while( (line = reader.readLine() ) != null){
	
			if( line.contains("<string ")){
				startLine = currentLine;
				isStringTag  = true;
			}
			
			if( isStringTag && line.contains("</string")){
				locations.add(buildLocation(path, startLine, currentLine));
				isStringTag  = false;
			}
			
			//Old implementation
			/*
			if( line.contains("<string")){
				startLine = currentLine;
				isStringTag  = true;
				stack.add("string");

			}else if(isStringTag && line.contains("<") && !line.contains("</") ){
				stack.add("other");
			}
			
			if(isStringTag && (line.contains("</") || line.contains("/>") || line.contains("-->"))){
				stack.pop();
			}
			
			if(isStringTag && stack.isEmpty()){
				locations.add(buildLocation(path, startLine, currentLine));
				isStringTag  = false;
			}
			*/
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
