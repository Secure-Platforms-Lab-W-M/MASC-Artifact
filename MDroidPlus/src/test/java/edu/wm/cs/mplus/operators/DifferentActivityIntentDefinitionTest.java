package edu.wm.cs.mplus.operators;

import edu.wm.cs.mplus.model.location.DifferentActivityMutationLocation;
import edu.wm.cs.mplus.operators.activity.DifferentActivityIntentDefinition;

public class DifferentActivityIntentDefinitionTest {
	
	private static final String prefix = "/home/scratch/mtufano/workspaces/java/MuDroid";
	private static final String path = "/MPlus/test/NewIntentActivity.java";
	
	public static void main(String[] args) {
		DifferentActivityMutationLocation mLocation = new DifferentActivityMutationLocation();
		
		mLocation.setFilePath(prefix+path);
		mLocation.setLine(150);
		mLocation.setStartColumn(44);
		mLocation.setEndColumn(67);
		mLocation.setImportLine(3);
		mLocation.setOtherActivity("com.evancharlton.mileage.dao.NewActivity");
		
		DifferentActivityIntentDefinition operator = new DifferentActivityIntentDefinition();
		operator.performMutation(mLocation);
	}
}
