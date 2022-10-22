package edu.wm.cs.masc.automatedAnalysis.Wrappers.CogniCrypt;

import edu.wm.cs.masc.automatedAnalysis.Wrappers.IWrapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CCWrapper implements IWrapper {
    // String artifact, outputReport;

//    public CCWrapper(String artifact, String outputReport) {
//        this.artifact = artifact;
//        this.outputReport = outputReport;
//    }

//    public static void printList(List<String> list) {
//        for(String item: list)
//            System.out.println(item);
//    }

    public String getSARIFReport(String artifact, String outputReportPath) {

        List<String> content = new ArrayList<>();
        loadReport(content, outputReportPath);
        content = trimTop(content);
        content = trimBottom(content);

        ArrayList<Integer> classIndices = new ArrayList<>();
        for(int i = 0; i < content.size(); i++) {
            if(content.get(i).startsWith("Findings"))
                classIndices.add(i);
        }

        ArrayList<CCDetectedVulnerability> vulnerabilities = new ArrayList<>();
        for(int i = 0; i < classIndices.size(); i++) {
            List<String> thisFinding;
            if(i == classIndices.size()-1)
                thisFinding = content.subList(classIndices.get(i), content.size());
            else
                thisFinding = content.subList(classIndices.get(i), classIndices.get(i+1));
            vulnerabilities.addAll(findVulnerabilitiesInAClass(thisFinding, artifact));
        }

        StringBuilder finalContent = new StringBuilder("{\n" +
                "   \"runs\":[\n" +
                "      {\n" +
                "         \"results\":[\n");

        for(int i = 0; i < vulnerabilities.size(); i++) {
            if(i < vulnerabilities.size() - 1)
                finalContent.append(vulnerabilities.get(i).getJSON()).append(",\n");
            else
                finalContent.append(vulnerabilities.get(vulnerabilities.size() - 1).getJSON()).append("\n");
        }
//        if(vulnerabilities.size() > 0)
//            finalContent.append(vulnerabilities.get(vulnerabilities.size() - 1).getJSON()).append("\n");

        finalContent.append("         ]\n" + "      }\n" + "   ]\n" + "}");

        return finalContent.toString();
    }

    private List<String> trimBottom(List<String> content) {
        for(int i = 0; i < content.size(); i++) {
            if(content.get(i).contains("====================== CogniCrypt Summary ========================="))
            {
                content = content.subList(0, i);
                break;
            }
        }
        return content;
    }

    private List<String> trimTop(List<String> content) {
        for(int i = 0; i < content.size(); i++) {
            if(content.get(i).startsWith("Findings in"))
            {
                content = content.subList(i, content.size());
                break;
            }
        }
        return content;
    }

    private void loadReport(List<String> content, String outputReportPath) {
        try {

            Scanner scanner = new Scanner(new File(outputReportPath));
            new ArrayList<>();
            while (scanner.hasNext()){
                String item = scanner.nextLine();
                if(!item.trim().isEmpty())
                    content.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<CCDetectedVulnerability> findVulnerabilitiesInAClass(List<String> contents, String artifact) {
        String cName = contents.get(0).split("Class:")[1].trim();
        ArrayList<CCDetectedVulnerability> vulnerabilities = new ArrayList<>();
        ArrayList<Integer> methodLocations = new ArrayList<>();

        for(int i = 1; i < contents.size(); i++) {
            if(contents.get(i).trim().startsWith("in Method"))
                methodLocations.add(i);
        }

        for(int i = 0; i < methodLocations.size(); i++) {
            if(i < methodLocations.size() - 1)
                vulnerabilities.addAll(findVulnerabilitiesInAMethod(contents.subList(methodLocations.get(i), methodLocations.get(i+1)), artifact, cName));
            else
                vulnerabilities.addAll(findVulnerabilitiesInAMethod(contents.subList(methodLocations.get(i), contents.size()), artifact, cName));
        }

//        vulnerabilities.addAll(findVulnerabilitiesInAMethod(contents, artifact, cName));

        return vulnerabilities;


    }

    private ArrayList<CCDetectedVulnerability> findVulnerabilitiesInAMethod(List<String> contents, String artifact, String cName) {
        String methodName = contents.get(0).split("Method:")[1].trim().split(" ", 2)[1];

        ArrayList<CCDetectedVulnerability> vulnerabilities = new ArrayList<>();

        for(int i = 1; i < contents.size(); i+=3) {
            CCDetectedVulnerability v = new CCDetectedVulnerability(cName, methodName, artifact, (i-1)/3, contents.subList(i, i+3));
            vulnerabilities.add(v);
        }
        return vulnerabilities;
    }

}
