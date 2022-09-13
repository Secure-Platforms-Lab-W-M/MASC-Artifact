package edu.wm.cs.masc.resultAnalysis;

import edu.wm.cs.masc.utils.config.PropertiesReader;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ResultAnalysisPropertiesReader {
    PropertiesReader propertiesReader;
    String toolName, toolLocation, toolRunCommand;
    String codeCompileCommand, outputReportDirectory, outputFileName, stopCondition;

    public ResultAnalysisPropertiesReader(String path) throws ConfigurationException {
        propertiesReader = new PropertiesReader(path);

        toolName = propertiesReader.getValueForAKey("toolName");
        toolLocation = propertiesReader.getValueForAKey("toolLocation");
        toolRunCommand = propertiesReader.getValueForAKey("toolRunCommand");
        codeCompileCommand = propertiesReader.getValueForAKey("codeCompileCommand");
        outputReportDirectory = propertiesReader.getValueForAKey("outputReportDirectory");
        outputFileName = propertiesReader.getValueForAKey("outputFileName");
        stopCondition = propertiesReader.getValueForAKey("stopCondition");
    }

    public String getToolRunCommand(String dir) {
        return toolRunCommand.replace("{}", System.getProperty("user.dir") + "\\app\\outputs\\" + dir);
    }

    public String getOutputReportFileWithDir(){
        return outputReportDirectory + "\\" + outputFileName;
    }

}
