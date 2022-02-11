package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.model.location.ParcelableMutationLocation;

public class NotParcelableLocator implements Locator {

	private static final String METHOD_1 = "writeToParcel";
	private static final String METHOD_2 = "describeContents";


	@Override
	public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
		List<MutationLocation> exactMutationLocations = new ArrayList<MutationLocation>();

		for(MutationLocation loc : locations){
			try{
				ParcelableMutationLocation mLoc = findExactLocation(loc);

				exactMutationLocations.add(mLoc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return exactMutationLocations;
	}


	private ParcelableMutationLocation findExactLocation(MutationLocation loc) {
		List<String> lines = FileHelper.readLines(loc.getFilePath());

		ParcelableMutationLocation mLoc = new ParcelableMutationLocation();
		mLoc.setFilePath(loc.getFilePath());
		List<Integer> overrides = new ArrayList<Integer>();

		//Generate list of overrides
		for(int i = 0; i < lines.size(); i++){
			String line = lines.get(i);

			//Check for the @Override tag
			if(line.contains("@Override")){
				if(containsParcelableMethod(lines.get(i+1))){
					overrides.add(i);
				}
			}
		}

		mLoc.setOverrideToDelete(overrides);
		return mLoc;
	}


	private static boolean containsParcelableMethod(String line){
		if(line.contains(METHOD_1) || line.contains(METHOD_2)){
			return true;
		}

		return false;
	}

}
