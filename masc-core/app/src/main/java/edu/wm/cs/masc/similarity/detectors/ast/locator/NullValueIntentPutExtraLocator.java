package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;

public class NullValueIntentPutExtraLocator implements Locator {

	@Override
	public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
		List<MutationLocation> exactMutationLocations = new ArrayList<MutationLocation>();

		for(MutationLocation loc : locations){

			try{
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

				//Select arguments in parenthesis
				int parStart = apiCall.indexOf("(")+1;
				int parEnd = apiCall.lastIndexOf(")");
				String parString = apiCall.substring(parStart, parEnd);
				String[] arguments = parString.split(",");
				if(arguments.length<2 || arguments.length > 2){
					continue;
				}

				//Select second argument
				String value = arguments[1];
				int relativeStartColumn = apiCall.indexOf(value);			
				int startColumn = relativeStartColumn + loc.getStartColumn();
				int endColumn = startColumn + value.length();

				//Build exact mutation location
				loc.setStartColumn(startColumn);
				loc.setEndColumn(endColumn);

				exactMutationLocations.add(loc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return exactMutationLocations;
	}

}
