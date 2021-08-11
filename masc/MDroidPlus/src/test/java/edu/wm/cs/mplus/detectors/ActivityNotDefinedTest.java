package edu.wm.cs.mplus.detectors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import edu.wm.cs.mplus.detectors.xml.ActivityNotDefinedDetector;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.operators.activity.ActivityNotDefined;

public class ActivityNotDefinedTest {

	public static void main(String[] args) throws Exception {
		
		String root = "/home/scratch/mtufano/workspaces/java/MuDroid/MPlus/test/";
		Path source = Paths.get(root+"AndroidManifest_orig.xml");
		Path target = Paths.get(root+"AndroidManifest.xml");
		
		//Duplicate file
		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		
		//Run Detector
		ActivityNotDefinedDetector detector = new ActivityNotDefinedDetector();
		
		//Check Locations identified
		List<MutationLocation> locations = detector.analyzeApp(root);
		

		System.out.println("Size: "+locations.size());
		MutationLocation loc = locations.get(1);
		System.out.println("Start Line: "+loc.getStartLine());
		System.out.println("End Line: "+loc.getEndLine());
		
		//Run Mutation Operator
		ActivityNotDefined operator = new ActivityNotDefined();
		operator.performMutation(loc);
		System.out.println("Mutation Performed!");

		//Check output
		
		//Clean files
		Files.delete(target);
	}

}
