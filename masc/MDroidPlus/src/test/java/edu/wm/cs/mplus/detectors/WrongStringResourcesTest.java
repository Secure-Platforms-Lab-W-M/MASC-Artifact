package edu.wm.cs.mplus.detectors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.wm.cs.mplus.detectors.MutationLocationDetector;
import edu.wm.cs.mplus.detectors.xml.WrongStringResourceDetector;
import edu.wm.cs.mplus.model.MutationType;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.detectors.MutationLocationListBuilder;
import edu.wm.cs.mplus.processors.MutationsProcessor;
import edu.wm.cs.mplus.processors.TextBasedDetectionsProcessor;

public class WrongStringResourcesTest {



	public static void main(String[] args) throws IOException{

		//Root path; this is the path containing the Android.xml file
		//String rootPath ="/home/scratch/mtufano/tmp/android-mileage-master/trunk";
		//String rootPath ="/home/f85/mtufano/Downloads/hopscotch-master";
		//String rootPath = "/home/scratch/mtufano/tmp/android-async-http-master";
		//String rootPath = "/home/scratch/mtufano/tmp/inperson-sdk-android-master";
		//String rootPath = "/home/scratch/mtufano/tmp/google-http-java-client-dev";
		//String rootPath = "/home/scratch/mtufano/tmp/zxing-master";
		String rootPath = "/scratch/mtufano.scratch/tmp/MuDroid_tests/apps/android-mileage/trunk";

		//Path to the folder containing the jars required to compile the app. This is required to resolve the variables/attributes bindings
		String binariesFolder ="/scratch/mtufano.scratch/workspaces/java/MuDroid/MPlus/libs4ast";
		//rootPath = "/Users/semeru/Downloads/frigus02-car-report-0a6340ea843f/app/src/main";

		String appName ="mileage";

		//Path where the mutants' folders are going to be created
		String mutantsFolder = "/scratch/mtufano.scratch/tmp/MuDroid_tests/apps/mutants";	


		List<MutationLocationDetector> textBasedDetectors = new ArrayList<>();
		textBasedDetectors.add(new WrongStringResourceDetector());


		//1. Run detection phase for Text-based detectors
		HashMap<MutationType, List<MutationLocation>> locations = TextBasedDetectionsProcessor.process(rootPath, textBasedDetectors);

		Set<MutationType> keys = locations.keySet();
		List<MutationLocation> list = null;
		for (MutationType mutationType : keys) {
			list = locations.get(mutationType);
			for (MutationLocation mutationLocation : list) {
				System.out.println("File: "+mutationLocation.getFilePath()+", start line:" + mutationLocation.getStartLine()+", end line: "+mutationLocation.getEndLine()+", start column"+mutationLocation.getStartColumn());
			}
		}



/*
		
		//2. Run detection phase for AST-based detectors
		//2.1 Preprocessing: Find locations to target API calls (including calls to constructors)
		String packname = "com.evancharlton.mileage";
		SourceCodeProcessor scp = SourceCodeProcessor.getInstance();
		locations.putAll( scp.processFolder(rootPath, binariesFolder, packname) );

		//2.2. Call the detectors on each location in order to find any extra information required for each case.
		locations = scp.findExtraInfoRequired(locations);


*/
		//3. Build MutationLocation List
		List<MutationLocation> mutationLocationList = MutationLocationListBuilder.buildList(locations);
		System.out.println("Total Locations: "+mutationLocationList.size());

		//3. Run mutation phase
		MutationsProcessor mProcessor = new MutationsProcessor(rootPath, appName, mutantsFolder);
		mProcessor.process(mutationLocationList);
	}
}
