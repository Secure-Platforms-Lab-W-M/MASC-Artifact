package edu.wm.cs.masc.resultAnalysis;

import edu.wm.cs.masc.sarifPar.src.main.java.sarifPar;
import edu.wm.cs.masc.utils.commandPrompt.CPOutput;
import edu.wm.cs.masc.utils.commandPrompt.CommandPrompt;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.checkerframework.checker.units.qual.C;


import java.io.*;

public class ResultAnalyzer {
    ResultAnalysisPropertiesReader propertiesReader;
    String mutatedAppsLocation;

    public ResultAnalyzer(String path, String outputDir) throws ConfigurationException {
        propertiesReader = new ResultAnalysisPropertiesReader(path);
        if(outputDir!=null)
            this.mutatedAppsLocation = outputDir;
        else
            this.mutatedAppsLocation = propertiesReader.mutatedAppsLocation;
    }

    public void analyzeResult() {
        compileAll();
    }

    private void compileAll() {
        File file = new File(mutatedAppsLocation);
        File[] files = file.listFiles();
        System.out.println("\n");
        CommandPrompt cp = new CommandPrompt();

        if(files != null) {
            System.out.println("Analyzing results form mutated apps in - " + file.getAbsolutePath());
            for(File f: files) {
                String compileCommand = "cd " + mutatedAppsLocation + "/" + f.getName() + " && " + propertiesReader.codeCompileCommand;
                CPOutput output = cp.run_command(compileCommand);

                if(!output.error) {
                    CPOutput output2 = cp.run_command( "cd " + propertiesReader.toolLocation + " && " + propertiesReader.getToolRunCommand(f.getName()));
                    if(!output2.error) {
                        try {
                            int length = sarifPar.getResult(propertiesReader.getOutputReportFileWithDir()).length();
                            if(length == 0)
                            {
                                System.out.println("\t- Mutant for " + f.getName() + " is UNKILLED");
                                if(propertiesReader.stopOnUnkilled())
                                    break;
                            }
                            else
                                System.out.println("\t- Mutant for " + f.getName() + " is killed");
                        } catch (FileNotFoundException e) {
                            System.out.printf("No file by the name of %s in folder %s after tool %s was run%n", propertiesReader.outputFileName, propertiesReader.outputReportDirectory, propertiesReader.toolName);
                            if(propertiesReader.stopOnError())
                                break;
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.printf("Result analysis failed for command %s%n", output2.command);
                            if(propertiesReader.stopOnError())
                                break;
                        }
                    }
                    else {
                        System.out.println(output2.getCombinedOutput());
                        System.out.printf("Tool didn't run for command %s%n", output2.command);
                        if(propertiesReader.stopOnError())
                            break;
                    }
                }
                else {
                    System.out.println(output.getCombinedOutput());
                    System.out.printf("Compilation failed for command: %s%n", output.command);
                    if(propertiesReader.stopOnError())
                        break;
                }
            }
        }
        else {
            System.out.println("Specified output directory does not exist!");
        }
    }



//    private boolean run_command(String command) {
//        try {
//            Runtime rt = Runtime.getRuntime();
////            Process pr = rt.exec("cmd /c " + command);
//            String[] cmd = {"cmd", "/c", "echo", "test"};
//            Process pr = rt.exec(cmd);
//
//
//            boolean printed1 = printAllFromAStream(pr.getErrorStream());
//            boolean printed2 = printAllFromAStream(pr.getInputStream());
//
//            if(printed1 || printed2) System.out.println("\n");
//
//
//            return pr.waitFor() == 0;
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//
//    private boolean printAllFromAStream(InputStream inputStream) throws IOException {
//        BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
//        boolean printed = false;
//
//        String line;
//        while((line=input.readLine()) != null) {
//            System.out.println(line);
//            printed = true;
//        }
//
//        return printed;
//    }

}
