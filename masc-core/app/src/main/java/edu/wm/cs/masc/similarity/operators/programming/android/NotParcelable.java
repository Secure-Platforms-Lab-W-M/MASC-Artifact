package edu.wm.cs.masc.similarity.operators.programming.android;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.masc.similarity.model.location.MutationLocation;
import edu.wm.cs.masc.similarity.model.location.ParcelableMutationLocation;
import edu.wm.cs.masc.similarity.operators.MutationOperator;
import edu.wm.cs.masc.similarity.helper.FileHelper;

public class NotParcelable  implements MutationOperator {

	@Override
	public boolean performMutation(MutationLocation location) {
		
		ParcelableMutationLocation mLocation = (ParcelableMutationLocation) location;
		
		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());
		
		for(int i=0; i < lines.size(); i++){
			
			String currLine = lines.get(i);
			
			//Remove implement
			if(currLine.contains("Parcelable") && currLine.contains("implements")){
				int startImpl = currLine.indexOf("implements");
				int startSer = currLine.indexOf("Parcelable", startImpl);
				
				if(startImpl >= startSer){
					return false;
				}
				
				String before = currLine.substring(startImpl, startSer);
				String after = currLine.substring(startSer);

				if(!before.contains(",") && !after.contains(",")){
					currLine = currLine.replace("Parcelable", "");
					currLine = currLine.replace("implements", "");
				} else if(!before.contains(",") && after.contains(",")){
					int index = currLine.indexOf(",", startSer);
					String sub1 = currLine.substring(0, startSer);
					String sub2 = currLine.substring(index+1);
					currLine = sub1 + sub2;
				} else{
					int index = currLine.lastIndexOf(",", startSer);
					String sub1 = currLine.substring(0, index);
					String sub2 = currLine.substring(startSer+"Parcelable".length());
					currLine = sub1 + sub2;
				}

			}
			
			//Discard lines to remove in the new source code file
			if(!isToDelete(i, mLocation.getOverrideToDelete())){
				newLines.add(currLine);
			}
			
		}
		
		FileHelper.writeLines(location.getFilePath(), newLines);
		
		return true;
	}
	
	
	private static boolean isToDelete(int i, List<Integer> overrideToDelete){
		
		for(Integer line : overrideToDelete){
			int val = line.intValue();
			if(val == i){
				return true;
			}
		}
		
		return false;
	}

}
