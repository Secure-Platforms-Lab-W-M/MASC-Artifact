package edu.wm.cs.mplus.detectors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.detectors.xml.InvalidColorDetector;
import edu.wm.cs.mplus.operators.gui.android.InvalidColor;

public class InvalidColorTest {
	public static void main(String[] args) throws Exception {

		String root = "/Users/cvendome/Documents/workspace/MPlus/test/";
		Path source = Paths.get(root + "res/values/colors_orig.xml");
		Path target = Paths.get(root + "res/values/colors.xml");

		// Duplicate file
		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

		// Run Detector
		InvalidColorDetector detector = new InvalidColorDetector();

		// Check Locations identified
		List<MutationLocation> locations = detector.analyzeApp(root);

		System.out.println("Size: " + locations.size());
		MutationLocation loc = locations.get(1);
		System.out.println("Line: " + loc.getLine());
		System.out.println("Start Col: " + loc.getStartColumn());
		System.out.println("End Col: " + loc.getEndColumn());

		// Run Mutation Operator
		InvalidColor operator = new InvalidColor();
		operator.performMutation(loc);
		System.out.println("Mutation Performed!");

		// Check output

		// Clean files
		Files.delete(target);
	}
}
