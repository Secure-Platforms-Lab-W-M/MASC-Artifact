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
        JSONArray beforeMutation = getResult("/Users/scottmarsden/Documents/reports/class-report.sarif");
        JSONArray afterMutation = getResult("/Users/scottmarsden/Documents/reports/class-report.sarif");
        compareResult(beforeMutation,afterMutation);
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

    static void compareResult(JSONArray before, JSONArray after){
        //Bug in message. In the actual program the line numbers may differ
        ArrayList beforeMessages = extractResult(before,"message");
        ArrayList afterMessages = extractResult(after,"message");
        ArrayList beforeLocations = extractResult(before,"locations");
        ArrayList afterLocations = extractResult(after,"locations");

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
}