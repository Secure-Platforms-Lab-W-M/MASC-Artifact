package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.model.location.TimedMutationLocation;

public class LengthyBackEndServiceLocator implements Locator {

	private static final int MILLISECONDS = 10000;

	@Override
	public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
		List<MutationLocation> exactMutationLocations = new ArrayList<MutationLocation>();

		for(MutationLocation loc : locations){

			try{
				TimedMutationLocation mLoc = findExactLocation(loc);

				exactMutationLocations.add(mLoc);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return exactMutationLocations;
	}

	private TimedMutationLocation findExactLocation(MutationLocation loc) {
		//Fix start column
		loc.setStartColumn(loc.getStartColumn()+1);

		TimedMutationLocation mLoc = new TimedMutationLocation();
		mLoc.setFilePath(loc.getFilePath());
		mLoc.setStartLine(loc.getEndLine());
		mLoc.setLine(loc.getEndLine());
		mLoc.setSeconds(MILLISECONDS);
		return mLoc;
	}

}
