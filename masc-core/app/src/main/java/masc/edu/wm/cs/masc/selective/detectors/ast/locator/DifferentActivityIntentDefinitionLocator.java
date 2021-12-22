package edu.wm.cs.mplus.detectors.ast.locator;

import java.util.ArrayList;
import java.util.List;

import edu.wm.cs.mplus.helper.FileHelper;
import edu.wm.cs.mplus.helper.IntegerGenerator;
import edu.wm.cs.mplus.model.location.DifferentActivityMutationLocation;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.processors.SourceCodeProcessor;

public class DifferentActivityIntentDefinitionLocator implements Locator {

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
				String[] arguments = parString.split(",");
				if(arguments.length<2 || arguments.length > 2){
					continue;
				}
				
				//Skip if the new Intent is an instance of: Intent(String action, Uri uri)
				String action = arguments[0];
				if(action.toLowerCase().contains("intent.action")){
					continue;
				}

				//Select second argument
				String activity = arguments[1];
				int relativeStartColumn = apiCall.indexOf(activity);			
				int startColumn = relativeStartColumn + loc.getStartColumn();
				int endColumn = startColumn + activity.length();

				//System.out.println("To Replace: "+linesToConsider.substring(startColumn, endColumn));

				//Get all activities and select a random different activity
				List<String> allActivities = SourceCodeProcessor.getInstance().getActivities();
				int randomIndex = IntegerGenerator.generateRandomInt(0, allActivities.size()-1);
				String otherActivity = allActivities.get(randomIndex);
				int attempts = 0;
				int maxAttempts = 20;
				while(otherActivity.contains(activity.trim())){
					System.out.println("Lines: "+linesToConsider);
					System.out.println("Attemtp: "+(attempts++));
					System.out.println("CurrentActivity: "+activity.trim());
					System.out.println("OtherActivity: "+otherActivity);
					System.out.println("Activities: "+allActivities.size());
					randomIndex = IntegerGenerator.generateRandomInt(0, allActivities.size()-1);
					otherActivity = allActivities.get(randomIndex);
					
					System.out.println("NewIndex: "+randomIndex);
					
					if(attempts >= maxAttempts){
						break;
					}
				}

				//Build exact mutation location
				DifferentActivityMutationLocation mLoc = new DifferentActivityMutationLocation();
				mLoc.setFilePath(loc.getFilePath());
				mLoc.setStartLine(loc.getStartLine());
				mLoc.setEndLine(loc.getEndLine());
				mLoc.setStartColumn(startColumn);
				mLoc.setEndColumn(endColumn);
				mLoc.setOtherActivity(otherActivity);

				exactMutationLocations.add(mLoc);
			}catch(Exception e){
				e.printStackTrace();
			}

		}

		return exactMutationLocations;
	}

}
