package edu.wm.cs.mplus.detectors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import edu.wm.cs.mplus.detectors.xml.SDKVersionDetector;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.operators.programming.android.SDKVersion;

public class SDKVersionTest {

	public static void main(String[] args) throws Exception {
		
		String root = "/home/scratch/mtufano/workspaces/java/MuDroid/MPlus/test/";
		Path source = Paths.get(root+"AndroidManifest_orig.xml");
		Path target = Paths.get(root+"AndroidManifest.xml");
		
		//Duplicate file
		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		
		//Run Detector
		SDKVersionDetector detector = new SDKVersionDetector();
		
		//Check Locations identified
		List<MutationLocation> locations = detector.analyzeApp(root);
		

		System.out.println("Size: "+locations.size());
		MutationLocation loc = locations.get(0);
		System.out.println("Line: "+loc.getLine());
		System.out.println("Start Line: "+loc.getStartColumn());
		System.out.println("End Line: "+loc.getEndColumn());
		
		//Run Mutation Operator
		SDKVersion operator = new SDKVersion();
		operator.performMutation(loc);
		System.out.println("Mutation Performed!");

		//Check output
		
		//Clean files
		Files.delete(target);
	}

}
