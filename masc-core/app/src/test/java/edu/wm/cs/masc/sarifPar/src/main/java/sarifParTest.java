package edu.wm.cs.masc.sarifPar.src.main.java;

import org.json.JSONArray;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class sarifParTest {

//    // This test uses files from app/outputs. Hence the test fails if the program is not built first.
//    @Test
//    //Can be expanded to test int,byte,and interproc. User needs to have output folders with those contained
//    //This can be run after testing the basic MASC
//    public void stringOpFlowAnalysis() throws IOException {
//        JSONArray test1 = sarifPar.getResult("src/main/resources/sarifTests/testSarif.sarif");
//        JSONArray test2 = sarifPar.getResult("src/main/resources/sarifTests/resultsSarif.sarif");
//        ArrayList test = sarifPar.compareSarifResults(test1,test2);
//        int result = sarifPar.stringOpFlowAnalysis("outputs","javax.crypto.Cipher","CryptoTest",test);
//        assertSame(0,result);
//
//    }
    @Test
    public void compareSarifResults() throws IOException {
        JSONArray before = SARIFPar.getResult("src/main/resources/sarifTests/testSarif.sarif");
        JSONArray after = SARIFPar.getResult("src/main/resources/sarifTests/testSarif.sarif");
        JSONArray test = new JSONArray();
        //System.out.println(sarifPar.compareSarifResults(before,after).toString());
        //System.out.println(test.toString());
        int result = test.toString().compareTo(SARIFPar.compareSarifResults(before,after).toString());
        assertSame(0,result);


    }
    @Test
    public void findMutation() throws IOException {
        ArrayList test = new ArrayList();
        test.add("System.out.println(\"Hello World\");");
        test.add("public static void main(java.lang.String[] args) throws java.lang.Exception {");
        test.add("javax.crypto.Cipher.getInstance(\"A~ES\".replace(\"~\", \"\");");
        Boolean results = SARIFPar.findMutation("javax.crypto.Cipher",1,test);
        assertSame(true,results);

    }

    @Test
    public void getJavaMutant() throws IOException {
        File f = new File("src/main/resources/sarifTests/CryptoTest.java");
        String test = SARIFPar.getJavaMutant(f,"javax.crypto.Cipher");
        //javax.crypto.Cipher.getInstance("A~ES".replace("~", "");
        //System.out.println(test);
        test = test.substring(4);
        //System.out.println(test);
        String test1 = "javax.crypto.Cipher.getInstance(\"A~ES\".replace(\"~\", \"\");";
        Boolean result = test.equals(test1);
        assertSame(true,result);

    }

}