//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;
import org.json.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

class main {
    //NEED TO ADD HANDLING FOR MORE THAN TWO SARIF FILES FOR LEVELS
    // This can be done within main. The results just need to be added and arguments can be changed
    //This should not be difficult
    // Results is an arrayList of ArrayList containing the caught mutations from SARIF file
    // Can be changed to just be an ArrayList of combined results if changed
    // logic in the operator flow analysis portions will need to be slightly altered to check
    // the entire array before declaring failure
    // Easiest way will be to add an arrayList to results of each result from each SARIF file produced
    public static void main(String[] args) throws FileNotFoundException, IOException {

        if  (args.length != 4){
            System.out.println("Please Provide: before/after SARIF files, properties file, and file path to MASC");
        }
        //"/Users/scottmarsden/Documents/reports/source-java-report.sarif"
        //"/Users/scottmarsden/Documents/reports/class-report.sarif"
        JSONArray beforeMutation = getResult(args[0]);
        JSONArray afterMutation = getResult(args[1]);
        ArrayList caughtMutations = compareResult(beforeMutation,afterMutation);
        ArrayList results = new ArrayList();
        results.add(caughtMutations);

        //System.out.println(caughtMutations);
        //findMutation("main(String)",1,caughtMutations);
        dataFlowAnalysis(args[2], args[3], results);
        //"/Users/scottmarsden/Documents/GitHub/MASC-Spring21-635/masc-core/app/src/main/resources/Cipher.properties"
    }



    //gets results from sarif file
    static JSONArray getResult (String file) throws FileNotFoundException, IOException {
        //Scanner scnr = new Scanner();

        JSONObject sarif = new JSONObject(getJson(file));
        JSONArray runs = (JSONArray) sarif.get("runs");
        JSONObject extract = runs.getJSONObject(0);
        JSONArray results = extract.getJSONArray("results");

        return results;



    }

    static ArrayList compareResult(JSONArray before, JSONArray after){
        //Bug in message. In the actual program the line numbers may differ
        ArrayList beforeMessages = extractResult(before,"message");
        ArrayList afterMessages = extractResult(after,"message");
        //ArrayList beforeLocations = extractResult(before,"locations");
        //ArrayList afterLocations = extractResult(after,"locations");

        beforeMessages = removeLineNumbers(beforeMessages);
        afterMessages = removeLineNumbers(afterMessages);
        //System.out.println("Before Loop (After):");
        //for (int i = 0; i < afterMessages.size(); i++){
        //      System.out.println(afterMessages.get(i));
        //}
        //System.out.println(beforeMessages);
        for(int i = 0; i < beforeMessages.size(); i++){
            for (int j = 0; j < afterMessages.size(); j++){
                //System.out.println(beforeMessages.get(i));
                //System.out.println(afterMessages.get(j));
                if (beforeMessages.get(i).toString().equals(afterMessages.get(j).toString())){
                    //System.out.println("Sucess");
                    beforeMessages.set(i,"");
                    afterMessages.set(j,"");
                    //break;
                }
            }

        }
        beforeMessages = clean(beforeMessages);
        afterMessages = clean(afterMessages);
        /*System.out.println("After Loop (Before):");
        for (int i = 0; i < beforeMessages.size(); i++){
            System.out.println(beforeMessages.get(i));
        } */
        /*System.out.println("After Loop 2 (After):");
        for (int i = 0; i < afterMessages.size(); i++){
            System.out.println(afterMessages.get(i));
        } */
        return afterMessages;
        //System.out.println(beforeMessages.get(0).toString().equals(afterMessages.get(0).toString()));
        //System.out.println(afterMessages);
    }

    //Gets the relevant text of the message that does not include line numbers since these change
    static ArrayList removeLineNumbers(ArrayList messages){
        ArrayList cleanMessages = new ArrayList();
        for(int i = 0; i < messages.size(); i++){
            String currMessage = messages.get(i).toString();
            StringBuilder sb = new StringBuilder(currMessage);
            ArrayList startIndex = new ArrayList();
            ArrayList endIndex = new ArrayList();
            for (int j = 0; j < currMessage.length(); j++){
                if (currMessage.charAt(j) == '['){
                    startIndex.add(j);

                }
                if (currMessage.charAt(j) == ']'){
                    endIndex.add(j);

                }

            }
            for (int j = startIndex.size()-1; j > -1; j--){
                //int index = (int)endIndex.get(j) - (int)startIndex.get(j);
                //System.out.println(index);
                for (int k = (int) startIndex.get(j); k < (int)endIndex.get(j)+1; k++ ){
                    sb.deleteCharAt((int)startIndex.get(j));
                    currMessage = sb.toString();

                }


            }
            cleanMessages.add(currMessage);
        }
        return cleanMessages;
    }

    static ArrayList extractResult(JSONArray results, String key){
        ArrayList extraction = new ArrayList();
        for(int i = 0; i < results.length(); i++){
            JSONObject result = results.getJSONObject(i);
            extraction.add(result.get(key));
        }

        return extraction;
    }

    static String getJson(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scnr = new Scanner(file);
        String JSONtext = "";
        while (scnr.hasNext()){
            JSONtext = JSONtext + scnr.nextLine();
        }

        return JSONtext;
    }

    static ArrayList clean(ArrayList list){
        int len = list.size();
        for(int i = len-1; i > -1; i--){
            if(list.get(i).toString().equals("")){
                list.remove(i);
            }
        }
        return list;
    }

    //Currently designed with MAIN scope in mind
    static void dataFlowAnalysis(String propertiesFile, String mascFilePath, ArrayList results) throws FileNotFoundException{
        File file = new File(propertiesFile);
        Scanner scnr = new Scanner(file);
        String outputDirectory = "";
        String type = "";
        String className = "";
        String apiName = "";
        while (scnr.hasNext()){
            String line = scnr.nextLine();

            if (line.contains("apiName") == true){

                apiName = line;
            }
            if (line.contains("className") == true){

                className = line;
            }
            if (line.contains("type") == true){

                type = line;
            }
            if (line.contains("outputDir") == true){
                outputDirectory = line;
            }
        }

        outputDirectory = outputDirectory.substring(12);
        apiName = apiName.substring(10);
        className = className.substring(12);
        String fullPath = mascFilePath + outputDirectory;
        if (type.contains("StringOperator")){
            stringFlowAnalysis(fullPath, apiName, className, results);

        }
        if (type.contains("IntOperator")){
            intFlowAnalysis(fullPath, apiName, className, results);

        }
        if (type.contains("Interproc")){
            interprocFlowAnalysis(fullPath, apiName, className, results);

        }
        if (type.contains("ByteOperator")){
            byteFlowAnalysis(fullPath, apiName, className, results);

        }


    }
    //Can be changed to create an array of files instead and utilize looping
    //Performs analysis on the String type operator

    static void stringFlowAnalysis(String fullPath, String apiName, String className, ArrayList results) throws FileNotFoundException{
        //Documents⁩ ▸ ⁨GitHub⁩ ▸ ⁨MASC-Spring21-635⁩ ▸ ⁨masc-core⁩ ▸ ⁨app⁩ ▸ ⁨outputs⁩
        //Need to move sarifParse into Masc-core so just the output directory can be used as a relative file path
        //String fullPath = "/Users/scottmarsden/Documents/GitHub/MASC-Spring21-635/masc-core/" + outputDir;
        File stringDifferentCase = new File(fullPath + "/StringDifferentCase/" + className.toString() + ".java");
        File stringNoiseReplace = new File(fullPath + "/StringNoiseReplace/" +  className + ".java");
        File stringSafeReplaceWithUnsafe = new File(fullPath + "/StringSafeReplaceWithUnsafe/" + className + ".java");
        File stringCaseTransform = new File(fullPath + "/StringStringCaseTransform/" + className + ".java");
        File stringUnsafeReplaceWithUnsafe = new File(fullPath + "/StringUnsafeReplaceWithUnsafe/" + className + ".java");
        File stringValueInVariable = new File(fullPath + "/StringValueInVariable/" + className + ".java");
        //Scanner scnr = new Scanner(stringDifferentCase);

        //Creates array of extracted mutant code
        ArrayList mutationLevels = new ArrayList();
        mutationLevels.add(getJavaMutant(stringDifferentCase, apiName));
        mutationLevels.add(getJavaMutant(stringNoiseReplace, apiName));
        mutationLevels.add(getJavaMutant(stringSafeReplaceWithUnsafe, apiName));
        mutationLevels.add(getJavaMutant(stringCaseTransform, apiName));
        mutationLevels.add(getJavaMutant(stringUnsafeReplaceWithUnsafe, apiName));
        mutationLevels.add(getJavaMutant(stringValueInVariable, apiName));



        //Place holder variable for testing. Will populate function with actual results
        //Will be array list of results. Each index containing the sarif results
        ArrayList stringResults = results;


        //Compares levels of mutations starting with most basic
        //Once it fails the program stops analysis
        //Can be updated to make the call to the crypto api detector 
        for (int i = 0; i < mutationLevels.size(); i++){
            //System.out.println(mutationLevels.get(i));
            if (findMutation(mutationLevels.get(i).toString(),1,(ArrayList) stringResults.get(i)) == false){
                System.out.println("Failed at level: " + i);
                System.out.println("Mutation: " + mutationLevels.get(i));
                break;
            }
            else{
                System.out.println("Found Mutation Level: " + i);
                System.out.println("Mutation: " + mutationLevels.get(i));
            }
        }

    }
    static void byteFlowAnalysis(String fullPath, String apiName, String className, ArrayList results) throws FileNotFoundException{
        //Documents⁩ ▸ ⁨GitHub⁩ ▸ ⁨MASC-Spring21-635⁩ ▸ ⁨masc-core⁩ ▸ ⁨app⁩ ▸ ⁨outputs⁩
        //Need to move sarifParse into Masc-core so just the output directory can be used as a relative file path
        //String fullPath = "/Users/scottmarsden/Documents/GitHub/MASC-Spring21-635/masc-core/" + outputDir;
        File byteLoop = new File(fullPath + "/ByteByteLoop/" + className + ".java");
        File currentTime = new File(fullPath + "/ByteCurrentTime/" + className + ".java");

        //Scanner scnr = new Scanner(stringDifferentCase);

        //Creates array of extracted mutant code
        ArrayList mutationLevels = new ArrayList();
        mutationLevels.add(getJavaMutant(byteLoop, apiName));
        mutationLevels.add(getJavaMutant(currentTime, apiName));



        //Place holder variable for testing. Will populate function with actual results
        //Will be array list of results. Each index containing the sarif results
        ArrayList byteResults = results;



        //Compares levels of mutations starting with most basic
        //Once it fails the program stops analysis
        //Can be updated to make the call to the crypto api detector
        for (int i = 0; i < mutationLevels.size(); i++){
            //System.out.println(mutationLevels.get(i));
            if (findMutation(mutationLevels.get(i).toString(),1,(ArrayList) byteResults.get(0)) == false){
                System.out.println("Failed at level: " + i);
                System.out.println("Mutation: " + mutationLevels.get(i));
                break;
            }
            else{
                System.out.println("Found Mutation Level: " + i);
                System.out.println("Mutation: " + mutationLevels.get(i));
            }
        }

    }
    static void intFlowAnalysis(String fullPath, String apiName, String className, ArrayList results) throws FileNotFoundException{
        //Documents⁩ ▸ ⁨GitHub⁩ ▸ ⁨MASC-Spring21-635⁩ ▸ ⁨masc-core⁩ ▸ ⁨app⁩ ▸ ⁨outputs⁩
        //Need to move sarifParse into Masc-core so just the output directory can be used as a relative file path
        //String fullPath = "/Users/scottmarsden/Documents/GitHub/MASC-Spring21-635/masc-core/" + outputDir;
        File absoluteValue = new File(fullPath + "/IntAbsoluteValue/" + className + ".java");
        File arithmetic = new File(fullPath + "/IntArithmetic/" +  className + ".java");
        File fromString = new File(fullPath + "/IntFromString/" + className + ".java");
        File iterationMultipleCall = new File(fullPath + "/IntIterationMultipleCall/" + className + ".java");
        File MisuseType = new File(fullPath + "/IntMisuseType/" + className + ".java");
        File nestedClass = new File(fullPath + "/IntNestedClass/" + className + ".java");
        File overflow = new File(fullPath + "/IntOverflow/" + className + ".java");
        File roundValue = new File(fullPath + "/IntRoundValue/" +  className + ".java");
        File valueInVariable = new File(fullPath + "/IntValueInVariable/" + className + ".java");
        File valueInVariableArithmetic = new File(fullPath + "/IntValueInVariableArithmetic/" + className + ".java");
        File whileLoopAccumulation = new File(fullPath + "/IntWhileLoopAccumulation/" + className + ".java");
        //Scanner scnr = new Scanner(stringDifferentCase);

        //Creates array of extracted mutant code
        ArrayList mutationLevels = new ArrayList();
        mutationLevels.add(getJavaMutant(absoluteValue, apiName));
        mutationLevels.add(getJavaMutant(arithmetic, apiName));
        mutationLevels.add(getJavaMutant(fromString, apiName));
        mutationLevels.add(getJavaMutant(iterationMultipleCall, apiName));
        mutationLevels.add(getJavaMutant(MisuseType, apiName));
        mutationLevels.add(getJavaMutant(nestedClass, apiName));
        mutationLevels.add(getJavaMutant(overflow, apiName));
        mutationLevels.add(getJavaMutant(roundValue, apiName));
        mutationLevels.add(getJavaMutant(valueInVariable, apiName));
        mutationLevels.add(getJavaMutant(valueInVariableArithmetic, apiName));
        mutationLevels.add(getJavaMutant(whileLoopAccumulation, apiName));



        //Place holder variable for testing. Will populate function with actual results
        //Will be array list of results. Each index containing the sarif results
        ArrayList intResults = results;



        //Compares levels of mutations starting with most basic
        //Once it fails the program stops analysis
        //Can be updated to make the call to the crypto api detector
        for (int i = 0; i < mutationLevels.size(); i++){
            //System.out.println(mutationLevels.get(i));
            if (findMutation(mutationLevels.get(i).toString(),1,(ArrayList) intResults.get(0)) == false){
                System.out.println("Failed at level: " + i);
                System.out.println("Mutation: " + mutationLevels.get(i));
                break;
            }
            else{
                System.out.println("Found Mutation Level: " + i);
                System.out.println("Mutation: " + mutationLevels.get(i));
            }
        }

    }

    static void interprocFlowAnalysis(String fullPath, String apiName, String className, ArrayList results) throws FileNotFoundException{

        //Need to move sarifParse into Masc-core so just the output directory can be used as a relative file path
        //String fullPath = "/Users/scottmarsden/Documents/GitHub/MASC-Spring21-635/masc-core/" + outputDir;
        File interProc = new File(fullPath + "/InterProcOperator/" + className + ".java");


        //Creates array of extracted mutant code
        ArrayList mutationLevels = new ArrayList();
        mutationLevels.add(getJavaMutant(interProc, apiName));




        //Place holder variable for testing. Will populate function with actual results
        //Will be array list of results. Each index containing the sarif results
        ArrayList interProcResults = results;



        //Compares levels of mutations starting with most basic
        //Once it fails the program stops analysis
        //Can be updated to make the call to the crypto api detector
        for (int i = 0; i < mutationLevels.size(); i++){
            //System.out.println(mutationLevels.get(i));
            if (findMutation(mutationLevels.get(i).toString(),1,(ArrayList) interProcResults.get(0)) == false){
                System.out.println("Failed at level: " + i);
                System.out.println("Mutation: " + mutationLevels.get(i));
                break;
            }
            else{
                System.out.println("Found Mutation Level: " + i);
                System.out.println("Mutation: " + mutationLevels.get(i));
            }
        }

    }
    //Can be changed to handle multiple mutations in a file
    //Currently Only looks for one mutant
    static String getJavaMutant(File javafile, String apiName) throws FileNotFoundException{
        String mutant = "";
        Scanner scnr = new Scanner(javafile);
        while (scnr.hasNext()){
            String line = scnr.nextLine();
            //System.out.println(line);
            if (line.contains(apiName) == true){
                mutant = line;
                break;
            }
        }

        return mutant;
    }


    static Boolean findMutation(String mutationType,int mutationNumber, ArrayList results){
        int mutCount = 0;
        boolean found = false;
        for (int i = 0; i < results.size(); i++){

            if(results.get(i).toString().contains(mutationType)){
                mutCount = mutCount + 1;
                found = true;

            }
        }

        if (found){

                System.out.println("Mutation Found");
                System.out.println("Number of Mutation Found: " + mutCount + "/" + mutationNumber + " times");
                return true;
        }
        else{
            System.out.println("Mutation Not Found");
            return false;
        }
    }
}