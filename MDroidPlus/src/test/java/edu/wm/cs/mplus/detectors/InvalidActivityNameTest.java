package edu.wm.cs.mplus.detectors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import edu.wm.cs.mplus.detectors.xml.InvalidActivityNameDetector;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.operators.activity.InvalidActivityName;

public class InvalidActivityNameTest {

	public static void main(String[] args) throws Exception {
		
		String root = "/home/scratch/mtufano/workspaces/java/MuDroid/MPlus/test/";
		Path source = Paths.get(root+"AndroidManifest_orig.xml");
		Path target = Paths.get(root+"AndroidManifest.xml");
		
		//Duplicate file
		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		
		//Run Detector
		InvalidActivityNameDetector detector = new InvalidActivityNameDetector();
		
		//Check Locations identified
		List<MutationLocation> locations = detector.analyzeApp(root);
		

		System.out.println("Size: "+locations.size());
		MutationLocation loc = locations.get(1);
		System.out.println("Line: "+loc.getLine());
		System.out.println("Start Col: "+loc.getStartColumn());
		System.out.println("End Col: "+loc.getEndColumn());
		
		//Run Mutation Operator
		InvalidActivityName operator = new InvalidActivityName();
		operator.performMutation(loc);
		System.out.println("Mutation Performed!");

		//Check output
		
		//Clean files
		Files.delete(target);
	}

}
