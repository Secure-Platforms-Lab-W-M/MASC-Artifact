package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.helper.FileHelper;

public class InvalidFilePathLocator implements Locator {

	@Override
	public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
		List<MutationLocation> exactMutationLocations = new ArrayList<MutationLocation>();

		for(MutationLocation loc : locations){
			try{
				findExactLocation(loc);

				exactMutationLocations.add(loc);
			}catch(Exception e){
				e.printStackTrace();
			}

		}

		return exactMutationLocations;
	}

	private void findExactLocation(MutationLocation loc) {
		//Fix start column
		loc.setStartColumn(loc.getStartColumn()+1);

		List<String> lines = FileHelper.readLines(loc.getFilePath());

		//Select API call substring
		int start = loc.getStartColumn();
		int end = loc.getStartColumn()+loc.getLength();

		String linesToConsider = "";
		boolean newLine = false;
		for(int i = loc.getStartLine(); i <= loc.getEndLine(); i++){
			if(newLine){
				linesToConsider += " "+lines.get(i);
			} else {
				linesToConsider += lines.get(i);
				newLine = true;
			}
		}

		String apiCall = linesToConsider.substring(start, end);

		//System.out.println("API call: "+apiCall);


		//Select arguments in parenthesis
		int parStart = apiCall.indexOf("(")+1;
		int parEnd = apiCall.lastIndexOf(")");

		//Select second argument		
		int startColumn = parStart + loc.getStartColumn();
		int endColumn =  parEnd + loc.getStartColumn();

		//System.out.println("To Replace: "+linesToConsider.substring(startColumn, endColumn));

		//Build exact mutation location
		loc.setStartColumn(startColumn);
		loc.setEndColumn(endColumn);
	}

}
