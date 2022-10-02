package edu.wm.cs.masc.automatedAnalysis;

import edu.wm.cs.masc.automatedAnalysis.Wrappers.CogniCrypt.CCWrapper;
import edu.wm.cs.masc.automatedAnalysis.Wrappers.IWrapper;
import edu.wm.cs.masc.sarifPar.src.main.java.SARIFPar;
import edu.wm.cs.masc.utils.commandPrompt.CPOutput;
import edu.wm.cs.masc.utils.commandPrompt.CommandPrompt;
import edu.wm.cs.masc.utils.config.PropertiesReader;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;


import java.io.*;

public class ResultAnalyzer {
    ResultAnalysisPropertiesReader propertiesReader;
    String mutatedAppsLocation;
    IWrapper wrapper = null;

    public ResultAnalyzer(PropertiesReader propertiesReader) throws ConfigurationException {
        this.propertiesReader = new ResultAnalysisPropertiesReader(propertiesReader);
        mutatedAppsLocation = this.propertiesReader.getMutatedAppsLocation();
        if(propertiesReader.contains("wrapper")) {
            if(this.propertiesReader.getWrapper().equalsIgnoreCase("CogniCrypt"))
                wrapper = new CCWrapper();
            else
                throw new ConfigurationException("The specified wrapper does not exist.");
        }
    }

    public void runAnalysis() {
        File file = new File(mutatedAppsLocation);
        File[] files = file.listFiles();
        System.out.println("\n");
        CommandPrompt cp = new CommandPrompt();

        if(files != null) {
            System.out.println("Analyzing results from mutated apps in - " + file.getAbsolutePath());
            for(File f: files) {
                String compileCommand = "cd " + mutatedAppsLocation + "/" + f.getName() + " && " + propertiesReader.codeCompileCommand;
                CPOutput output = cp.run_command(compileCommand);

                if(!output.error) {
                    CPOutput output2 = cp.run_command( "cd " + propertiesReader.toolLocation + " && " + propertiesReader.getToolRunCommand(f.getName()));
                    if(!output2.error) {
                        try {
                            int length;
                            if(wrapper == null)
                                length = SARIFPar.getResult(propertiesReader.getOutputReportFileWithDir()).length();
                            else
                                length = SARIFPar.getResultFromString(wrapper.getSARIFReport(f.getName(), propertiesReader.getOutputReportFileWithDir())).length();

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
}
