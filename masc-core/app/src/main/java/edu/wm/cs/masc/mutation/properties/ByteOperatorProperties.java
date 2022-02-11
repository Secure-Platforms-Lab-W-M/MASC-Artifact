package edu.wm.cs.masc.mutation.properties;

import org.apache.commons.configuration2.ex.ConfigurationException;

public class ByteOperatorProperties extends AOperatorProperties {
    private final String tempVariableName;
    private final String apiVariable;


    public String getTempVariableName() {
        return tempVariableName;
    }

    public String getApiVariable() {
        return apiVariable;
    }

    public ByteOperatorProperties(String path)
            throws ConfigurationException {
        super(path);
        this.tempVariableName = reader.getValueForAKey("tempVariableName");
        this.apiVariable = reader.getValueForAKey("apiVariable");
    }
}
