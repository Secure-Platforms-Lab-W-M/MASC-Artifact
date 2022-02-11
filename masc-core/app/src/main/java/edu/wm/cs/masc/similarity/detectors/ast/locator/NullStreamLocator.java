package edu.wm.cs.masc.similarity.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.helper.FileHelper;
import edu.wm.cs.masc.similarity.model.location.ObjectMutationLocation;

public class NullStreamLocator implements Locator {

	@Override
	public List<MutationLocation> findExactLocations(List<MutationLocation> locations) {
		List<MutationLocation> exactMutationLocations = new ArrayList<MutationLocation>();

		for(MutationLocation loc : locations){

			try{
				ObjectMutationLocation mLoc = findExactLocation(loc);

				if(mLoc != null){
					exactMutationLocations.add(mLoc);
				}

			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return exactMutationLocations;
	}

	private ObjectMutationLocation findExactLocation(MutationLocation loc) {
		List<String> lines = FileHelper.readLines(loc.getFilePath());

		//Fix start column
		loc.setStartColumn(loc.getStartColumn()+1);

		String line = lines.get(loc.getLine());
		String targetLine = line.substring(loc.getStartColumn());

		//identify object name
		int endCol = targetLine.indexOf(".close") + loc.getStartColumn();
		String object = line.substring(loc.getStartColumn(), endCol);

		//Skip method invocations e.g. "obj.getInputStream()" which cannot be assigned to null
		if(object.contains("(") || object.contains(")")){
			return null;
		}

		//Build exact mutation location		
		ObjectMutationLocation mLoc = new ObjectMutationLocation();
		mLoc.setFilePath(loc.getFilePath());
		mLoc.setStartLine(loc.getStartLine());
		mLoc.setLine(loc.getLine());
		mLoc.setStartColumn(loc.getStartColumn());
		mLoc.setEndColumn(endCol);
		mLoc.setObject(object);
		return mLoc;
	}

}
