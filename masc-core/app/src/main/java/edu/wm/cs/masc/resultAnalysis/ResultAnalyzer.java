package edu.wm.cs.masc.resultAnalysis;

import edu.wm.cs.masc.sarifPar.src.main.java.sarifPar;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.*;
import java.util.Scanner;

public class ResultAnalyzer {
    ResultAnalysisPropertiesReader propertiesReader;
    String outputDirectory = "app/outputs";

    public ResultAnalyzer(String path) throws ConfigurationException {
        propertiesReader = new ResultAnalysisPropertiesReader(path);
    }

    public void analyzeResult() {
        compileAll();
    }

    private void compileAll() {
        File file = new File(outputDirectory);
        File[] files = file.listFiles();
        System.out.println("\n");

        if(files != null) {
            for(File f: files) {
                boolean compiled = run_command("pushd app\\outputs\\" + f.getName() + " & " + propertiesReader.codeCompileCommand);

                if(compiled) {
                    System.out.println(f.getName());
                    boolean toolRan = run_command("pushd " + propertiesReader.toolLocation + " & " + propertiesReader.getToolRunCommand(f.getName()));
                    if(toolRan) {
                        try {
                            int length = sarifPar.getResult(propertiesReader.getOutputReportFileWithDir()).length();
                            if(length == 0)
                                System.out.println("\t- Mutant for " + f.getName() + " is unkilled");
                            else
                                System.out.println("\t- Mutant killed");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
//                                        break;
                }
            }
        }
    }

    private boolean run_command(String command) {
        return run_command(command, false);
    }

    private boolean run_command(String command, boolean verbose) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("cmd /c " + command);

            if(verbose) {
                boolean printed1 = printAllFromAStream(pr.getErrorStream());
                boolean printed2 = printAllFromAStream(pr.getInputStream());

                if(printed1 || printed2) System.out.println("\n");
            }

            return pr.waitFor() == 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean printAllFromAStream(InputStream inputStream) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
        boolean printed = false;

        String line;
        while((line=input.readLine()) != null) {
            System.out.println(line);
            printed = true;
        }

        return printed;
    }

}
