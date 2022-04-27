package edu.wm.cs.masc.mutation.properties;

import org.apache.commons.configuration2.ex.ConfigurationException;

public class StringOperatorProperties extends AOperatorProperties {

    private final String invocation;
    private final String secureParam;
    private final String insecureParam;
    private final String noise;
    private final String variableName;

    public StringOperatorProperties(String path)
            throws ConfigurationException {
        super(path);
        invocation = reader.getValueForAKey("invocation");

//        Note: insecureObjectName to be used by InjectInsecureObject.java
        insecureObjectName = reader.getValueForAKey("insecureObjectName");

//        secureAPIName = reader.getValueForAKey("secureAPIName");
//        insecureAPIName = reader.getValueForAKey("insecureAPIName");

        secureParam = reader.getValueForAKey("secureParam");
        insecureParam = reader.getValueForAKey("insecureParam");
        noise = reader.getValueForAKey("noise");
        variableName = reader.getValueForAKey("variableName");
    }

    public String getInvocation() {
        return invocation;
    }

    public String getSecureParam() {
        return secureParam;
    }

    public String getInsecureParam() {
        return insecureParam;
    }

    public String getNoise() {
        return noise;
    }

    public String getVariableName() {
        return variableName;
    }

}
