package edu.wm.cs.mplus.operators.programming.oo;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.mplus.helper.FileHelper;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.operators.MutationOperator;

public class NotSerializable  implements MutationOperator{

	@Override
	public boolean performMutation(MutationLocation location) {

		List<String> newLines = new ArrayList<String>();
		List<String> lines = FileHelper.readLines(location.getFilePath());

		for(int i=0; i < lines.size(); i++){

			String currLine = lines.get(i);

			//Remove implement
			if(currLine.contains("Serializable") && currLine.contains("implements")){
				int startImpl = currLine.indexOf("implements");
				int startSer = currLine.indexOf("Serializable", startImpl);
				
				if(startImpl >= startSer){
					return false;
				}
				
				String before = currLine.substring(startImpl, startSer);
				String after = currLine.substring(startSer);

				if(!before.contains(",") && !after.contains(",")){
					currLine = currLine.replace("Serializable", "");
					currLine = currLine.replace("implements", "");
				} else if(!before.contains(",") && after.contains(",")){
					int index = currLine.indexOf(",", startSer);
					String sub1 = currLine.substring(0, startSer);
					String sub2 = currLine.substring(index+1);
					currLine = sub1 + sub2;
				} else{
					int index = currLine.lastIndexOf(",", startSer);
					String sub1 = currLine.substring(0, index);
					String sub2 = currLine.substring(startSer+"Serializable".length());
					currLine = sub1 + sub2;
				}

			}

			newLines.add(currLine);

		}

		FileHelper.writeLines(location.getFilePath(), newLines);

		return true;
	}

}
