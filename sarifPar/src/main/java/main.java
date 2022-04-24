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
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //System.out.println("Hello World!"); // Display the string.
        JSONArray beforeMutation = getResult("/Users/scottmarsden/Documents/reports/source-java-report.sarif");
        JSONArray afterMutation = getResult("/Users/scottmarsden/Documents/reports/class-report.sarif");
        ArrayList caughtMutations = compareResult(beforeMutation,afterMutation);
        findMutation("main(String)",1,caughtMutations);
        dataFlowAnalysis("/Users/scottmarsden/Documents/GitHub/MASC-Spring21-635/masc-core/app/src/main/resources/Cipher.properties");
    }



    //gets results from sarif file
    static JSONArray getResult (String file) throws FileNotFoundException, IOException {
        //Scanner scnr = new Scanner();

        JSONObject sarif = new JSONObject(getJson(file));
        JSONArray runs = (JSONArray) sarif.get("runs");
        JSONObject extract = runs.getJSONObject(0);
        JSONArray results = extract.getJSONArray("results");
        //JSONArray result = (JSONArray) runs.get("result");
        //JSONObject results = (JSONObject) runs.("results");
        //System.out.println(runs.get(0));
        //for (int i = 0; i < results.length(); i++){
          //  System.out.println(results.get(i));
        //}

        //System.out.println(results.getJSONObject(0));
        //JSONObject result0 = results.getJSONObject(0);
        //System.out.println(result0.get("locations"));
        //System.out.println(result0.get("message"));
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
        System.out.println("Before Loop (After):");
        for (int i = 0; i < afterMessages.size(); i++){
              System.out.println(afterMessages.get(i));
        }
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
        System.out.println("After Loop (Before):");
        for (int i = 0; i < beforeMessages.size(); i++){
            System.out.println(beforeMessages.get(i));
        }
        System.out.println("After Loop 2 (After):");
        for (int i = 0; i < afterMessages.size(); i++){
            System.out.println(afterMessages.get(i));
        }
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
    static void dataFlowAnalysis(String propertiesFile) throws FileNotFoundException{
        File file = new File(propertiesFile);
        Scanner scnr = new Scanner(file);
        String outputDirectory = "";
        String type = "";
        while (scnr.hasNext()){
            String line = scnr.nextLine();

            if (line.contains("type") == true){

                type = line;
            }
            if (line.contains("outputDir") == true){
                outputDirectory = line;
            }
        }
        //System.out.println(type);
        //System.out.println(outputDirectory);
        outputDirectory = outputDirectory.substring(12);
        if (type.contains("StringOperator")){
            stringFlowAnalysis(outputDirectory);

        }


    }
    //Can be changed to create an array of files instead and utilize looping
    //Performs analysis on the String type operator
    static void stringFlowAnalysis(String outputDir) throws FileNotFoundException{
        //Documents⁩ ▸ ⁨GitHub⁩ ▸ ⁨MASC-Spring21-635⁩ ▸ ⁨masc-core⁩ ▸ ⁨app⁩ ▸ ⁨outputs⁩
        //Need to move sarifParse into Masc-core so just the output directory can be used as a relative file path
        String fullPath = "/Users/scottmarsden/Documents/GitHub/MASC-Spring21-635/masc-core/" + outputDir;
        File stringDifferentCase = new File(fullPath + "/StringDifferentCase/CryptoTest.java");
        File stringNoiseReplace = new File(fullPath + "/StringNoiseReplace/CryptoTest.java");
        File stringSafeReplaceWithUnsafe = new File(fullPath + "/StringSafeReplaceWithUnsafe/CryptoTest.java");
        File stringCaseTransform = new File(fullPath + "/StringStringCaseTransform/CryptoTest.java");
        File stringUnsafeReplaceWithUnsafe = new File(fullPath + "/StringUnsafeReplaceWithUnsafe/CryptoTest.java");
        File stringValueInVariable = new File(fullPath + "/StringValueInVariable/CryptoTest.java");
        //Scanner scnr = new Scanner(stringDifferentCase);

        //Creates array of extracted mutant code
        ArrayList mutationLevels = new ArrayList();
        mutationLevels.add(getJavaMutant(stringDifferentCase));
        mutationLevels.add(getJavaMutant(stringNoiseReplace));
        mutationLevels.add(getJavaMutant(stringSafeReplaceWithUnsafe));
        mutationLevels.add(getJavaMutant(stringCaseTransform));
        mutationLevels.add(getJavaMutant(stringUnsafeReplaceWithUnsafe));
        mutationLevels.add(getJavaMutant(stringValueInVariable));
        //System.out.println(getJavaMutant(stringDifferentCase));
        //String stringDiffCaseMutant = getJavaMutant(stringDifferentCase)
        //String stringNoiseReplaceMutant = getJavaMutant(stringNoiseReplace);
        //String stringSafeReplaceWithUnsafeMutant = getJavaMutant(stringSafeReplaceWithUnsafe);
        //String stringCaseTransformMutant = getJavaMutant(stringCaseTransform);
        //String stringUnsafeReplaceWithUnsafeMutant = getJavaMutant(stringUnsafeReplaceWithUnsafe);
        //String stringValueInVariableMutant = getJavaMutant(stringValueInVariable);


        //Place holder variable for testing. Will populate function with actual results
        //Will be array list of results. Each index containing the sarif results
        ArrayList stringResults = new ArrayList(6);
        ArrayList blah = new ArrayList();
        stringResults.add(blah);
        stringResults.add(blah);
        stringResults.add(blah);
        stringResults.add(blah);
        stringResults.add(blah);
        stringResults.add(blah);
        stringResults.add(blah);

        //Compares levels of mutations starting with most basic
        //Once it fails the program stops analysis
        //Can be updated to make the call to the crypto api detector 
        for (int i = 0; i < mutationLevels.size(); i++){
            //System.out.println(mutationLevels.get(i));
            if (findMutation(mutationLevels.get(i).toString(),1,(ArrayList) stringResults.get(0)) == false){
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
    static String getJavaMutant(File javafile) throws FileNotFoundException{
        String mutant = "";
        Scanner scnr = new Scanner(javafile);
        while (scnr.hasNext()){
            String line = scnr.nextLine();
            //System.out.println(line);
            if (line.contains("javax.crypto.Cipher") == true){
                mutant = line;
                break;
            }
        }

        return mutant;
    }
    //Look at MDROID and see the operators and enum approach 
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