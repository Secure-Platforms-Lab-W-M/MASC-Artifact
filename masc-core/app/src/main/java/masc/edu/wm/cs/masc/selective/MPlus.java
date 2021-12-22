package edu.wm.cs.mplus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.wm.cs.mplus.detectors.MutationLocationDetector;
import edu.wm.cs.mplus.detectors.MutationLocationListBuilder;
import edu.wm.cs.mplus.helper.PlacementChecker;
import edu.wm.cs.mplus.model.MutationType;
import edu.wm.cs.mplus.model.location.MutationLocation;
import edu.wm.cs.mplus.processors.MutationsProcessor;
import edu.wm.cs.mplus.processors.SourceCodeProcessor;
import edu.wm.cs.mplus.operators.OperatorBundle;
import edu.wm.cs.mplus.processors.TextBasedDetectionsProcessor;

public class MPlus {

    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter
                .ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        try {
            runMPlus(args);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LocalDateTime then = LocalDateTime.now();
        System.out.println(dtf.format(now));
        System.out.println(dtf.format(then));
    }

    public static void runMPlus(String[] args) throws IOException {

        //Usage Error
        if (args.length != 6) {
            System.out.println("******* ERROR: INCORRECT USAGE *******");
            System.out.println("Argument List:");
            System.out.println("1. Binaries path");
            System.out.println("2. App Source Code path");
            System.out.println("3. App Name");
            System.out.println("4. Mutants path");
            System.out.println(
                    "5. Directory containing the operator.properties file");
            System.out.println("6. Multithread generation (true/false)");
            return;
        }
//        String binariesFolder = "C:\\Users\\zaser\\Documents\\GitHub\\CSci435-Fall21-MASC\\MDroidPlus\\libs4ast";
//        String rootPath = "C:\\Users\\zaser\\Documents\\inputFile";
//        String appName = "MDroid";
//        String mutantsFolder = "C:\\Users\\zaser\\Documents\\mdroidcrap";
//        String operatorsDir = "C:\\Users\\zaser\\Documents\\GitHub\\CSci435-Fall21-MASC\\MDroidPlus\\src\\main\\java\\edu\\wm\\cs\\mplus";
//        boolean multithread = true;

                //Getting arguments
        String binariesFolder = args[0];
        String rootPath = args[1];
        String appName = args[2];
        String mutantsFolder = args[3];
        String operatorsDir = args[4];
        boolean multithread = Boolean.parseBoolean(args[5]);

        //Read selected operators
        OperatorBundle operatorBundle = new OperatorBundle(operatorsDir);
        System.out.println(operatorBundle.printSelectedOperators());


        //Text-Based operators selected
        List<MutationLocationDetector> textBasedDetectors = operatorBundle
                .getTextBasedDetectors();

        //1. Run detection phase for Text-based detectors
        HashMap<MutationType, List<MutationLocation>> locations =
                TextBasedDetectionsProcessor
                        .process(rootPath, textBasedDetectors);

        Set<MutationType> keys = locations.keySet();
        List<MutationLocation> list = null;
        for (MutationType mutationType : keys) {
            list = locations.get(mutationType);
            for (MutationLocation mutationLocation : list) {
                System.out.println("File: " + mutationLocation
                        .getFilePath() + ", start line:" + mutationLocation
                        .getStartLine() + ", end line: " + mutationLocation
                        .getEndLine() + ", start column" + mutationLocation
                        .getStartColumn());
            }
        }

        //2. Run detection phase for AST-based detectors
        //2.1 Preprocessing: Find locations to target API calls (including
        // calls to constructors)
        //SourceCodeProcessor scp = SourceCodeProcessor.getInstance(); (not
        // safe, if MPlus is executed on different apps)
        SourceCodeProcessor scp = new SourceCodeProcessor(operatorBundle);
        locations.putAll(scp.processFolder(rootPath, binariesFolder, appName));

        //2.2. Call the detectors on each location in order to find any extra
        // information required for each case.
        locations = scp.findExtraInfoRequired(locations);

        //3. Build MutationLocation List
        List<MutationLocation> mutationLocationList =
                MutationLocationListBuilder
                .buildList(locations);
        System.out.println("Total Locations: " + mutationLocationList.size());

        //3. Run mutation phase
        MutationsProcessor mProcessor = new MutationsProcessor(rootPath,
                appName, mutantsFolder);

        if (multithread) {
            mProcessor.processMultithreaded(mutationLocationList);
        } else {
            mProcessor.process(mutationLocationList);
        }

        // 4. minimal JDT-AST based location reachability check
        PlacementChecker checker = new PlacementChecker(rootPath,
                binariesFolder);
        checker.process(mutationLocationList);


    }

}
