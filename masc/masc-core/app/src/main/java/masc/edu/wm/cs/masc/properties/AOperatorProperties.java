package masc.edu.wm.cs.masc.properties;


import masc.edu.wm.cs.masc.config.PropertiesReader;
import org.apache.commons.configuration2.ex.ConfigurationException;

public abstract class AOperatorProperties {

    protected final String type;
    protected final String outputDir;
    protected final String apiName;
    protected final String className;
    protected PropertiesReader reader;
    private final String otherClassName;

    public String getOtherClassName() {
        return otherClassName;
    }

    public String getType() {
        return type;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public String getApiName() {
        return apiName;
    }

    public String getClassName() {
        return className;
    }

    public PropertiesReader getReader() {
        return reader;
    }

    public AOperatorProperties(String path) throws ConfigurationException {
        reader = new PropertiesReader(path);
        type = reader.getValueForAKey("type");
        outputDir = reader.getValueForAKey("outputDir");
        apiName = reader.getValueForAKey("apiName");
        className = reader.getValueForAKey("className");
        otherClassName = reader.getValueForAKey("otherClassName");
    }

    public AOperatorProperties(String type, String outputDir,
                               String apiName, String className,
                               String otherClassName) {
        this.type = type;
        this.outputDir = outputDir;
        this.apiName = apiName;
        this.className = className;
        this.otherClassName = otherClassName;
    }
}
