package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.model.location.VisibilityMutationLocation;
import edu.wm.cs.masc.similarity.helper.FileHelper;

public class ViewComponentNotVisibleLocator implements Locator {


	private static final String VISIBILITY = "android.view.View.INVISIBLE";

	@Override
	public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
		List<MutationLocation> exactMutationLocations = new ArrayList<MutationLocation>();

		for(MutationLocation loc : locations){

			try{
				//Fix start column
				loc.setStartColumn(loc.getStartColumn()+1);

				List<String> lines = FileHelper.readLines(loc.getFilePath());

				String targetLine = lines.get(loc.getLine());

				//If the call is not an assignment, we skip this one
				int assignIndex = targetLine.indexOf("=");
				if(assignIndex<0){
					continue;
				}
				
				//Identify the object name
				String object = "";
				String lhs = targetLine.substring(0, assignIndex).trim();
				String[] parts = lhs.split(" ");
				if(parts.length == 1){
					object = parts[0];
				} else if (parts.length == 2){
					object = parts[1];
				} else{
					continue;
				}
				
				//Check null objects
				if(object == null || object.equalsIgnoreCase("null")){
					continue;
				}
				

				//Build exact mutation location		
				VisibilityMutationLocation mLoc = new VisibilityMutationLocation();
				mLoc.setFilePath(loc.getFilePath());
				mLoc.setStartLine(loc.getEndLine());
				mLoc.setLine(loc.getEndLine());
				mLoc.setView(object);
				mLoc.setVisibility(VISIBILITY);

				exactMutationLocations.add(mLoc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return exactMutationLocations;
	}

}
