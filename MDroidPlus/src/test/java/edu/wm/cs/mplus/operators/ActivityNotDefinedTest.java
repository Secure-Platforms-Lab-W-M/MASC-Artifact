package edu.wm.cs.mplus.operators;

import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.operators.activity.ActivityNotDefined;

public class ActivityNotDefinedTest {
	
	private static final String prefix = "/home/scratch/mtufano/workspaces/java/MuDroid";
	private static final String path = "/MPlus/test/AndroidManifest.xml";
	
	public static void main(String[] args) {
		MutationLocation mLocation = new MutationLocation();
		
		mLocation.setFilePath(prefix+path);
		mLocation.setStartLine(20);
		mLocation.setEndLine(25);
		
		ActivityNotDefined operator = new ActivityNotDefined();
		operator.performMutation(mLocation);
	}
}
