package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.model.location.TimedMutationLocation;
import edu.wm.cs.masc.similarity.helper.FileHelper;

public class LenghtyGUIListenerLocator implements Locator {

	private static final int MILLISECONDS = 10000;

	
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

				//System.out.println("API call: "+apiCall);

				//Find onClick method
				int methodNameStart = apiCall.indexOf("onClick");
				if(methodNameStart == -1){
					continue;
				}
				int insertionPoint = apiCall.indexOf("{", methodNameStart) + 1;
				
				int startColumn = start + insertionPoint;
				
				//Build exact mutation location
				TimedMutationLocation mLoc = new TimedMutationLocation();
				mLoc.setFilePath(loc.getFilePath());
				mLoc.setStartLine(loc.getStartLine());
				mLoc.setEndLine(loc.getEndLine());
				mLoc.setStartColumn(startColumn);
				mLoc.setSeconds(MILLISECONDS);

				exactMutationLocations.add(mLoc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return exactMutationLocations;
	}

}
