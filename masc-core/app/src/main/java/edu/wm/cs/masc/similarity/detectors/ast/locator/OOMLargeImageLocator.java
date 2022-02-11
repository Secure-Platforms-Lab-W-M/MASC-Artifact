package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.ImageMutationLocation;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.helper.FileHelper;

public class OOMLargeImageLocator implements Locator {

	private static final int WIDTH = 100000;
	private static final int HEIGHT = 100000;
	private static final boolean FILTER = true;
	
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


				//Select arguments in parenthesis
				int parStart = apiCall.indexOf("(")+1;
				int parEnd = apiCall.lastIndexOf(")");
				String parString = apiCall.substring(parStart, parEnd);
				
				int startSecondArgument = parString.indexOf(",");
				
				int startColumn = loc.getStartColumn() + parStart + startSecondArgument;
				int endColumn = loc.getStartColumn() + parEnd;
				

				//Build exact mutation location
				ImageMutationLocation mLoc = new ImageMutationLocation();
				mLoc.setFilePath(loc.getFilePath());
				mLoc.setStartLine(loc.getStartLine());
				mLoc.setEndLine(loc.getEndLine());
				mLoc.setStartColumn(startColumn);
				mLoc.setEndColumn(endColumn);
				mLoc.setWidth(WIDTH);
				mLoc.setHeight(HEIGHT);
				mLoc.setFilter(FILTER);
				
				exactMutationLocations.add(mLoc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return exactMutationLocations;
	}

}
