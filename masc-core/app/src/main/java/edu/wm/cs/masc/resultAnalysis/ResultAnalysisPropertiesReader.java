package edu.wm.cs.masc.resultAnalysis;

import edu.wm.cs.masc.utils.config.PropertiesReader;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ResultAnalysisPropertiesReader {
    PropertiesReader propertiesReader;
    String toolName, toolLocation, toolRunCommand;
    String codeCompileCommand, outputReportDirectory, outputFileName,
            stopCondition, mutatedAppsLocation;

    public ResultAnalysisPropertiesReader(String path) throws ConfigurationException {
        propertiesReader = new PropertiesReader(path);

        toolName = propertiesReader.getValueForAKey("toolName");
        toolLocation = propertiesReader.getValueForAKey("toolLocation");
        toolRunCommand = propertiesReader.getValueForAKey("toolRunCommand");
        codeCompileCommand = propertiesReader.getValueForAKey("codeCompileCommand");
        outputReportDirectory = propertiesReader.getValueForAKey("outputReportDirectory");
        outputFileName = propertiesReader.getValueForAKey("outputFileName");
        stopCondition = propertiesReader.getValueForAKey("stopCondition");
        mutatedAppsLocation = propertiesReader.getValueForAKey("mutatedAppsLocation");
    }

    public String getToolRunCommand(String dir) {
        return toolRunCommand.replace("{}", System.getProperty("user.dir") + "/app/outputs/" + dir);
    }

    public String getOutputReportFileWithDir(){
        return outputReportDirectory + "/" + outputFileName;
    }

    public boolean stopOnError() {
        return stopCondition.equalsIgnoreCase("OnError") || stopCondition.equalsIgnoreCase("OnErrorOrUnkilled");
    }

    public boolean stopOnUnkilled() {
        return stopCondition.equalsIgnoreCase("OnUnkilledMutant") || stopCondition.equalsIgnoreCase("OnErrorOrUnkilled");
    }

}
