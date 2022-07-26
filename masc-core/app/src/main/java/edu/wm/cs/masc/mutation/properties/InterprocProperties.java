package edu.wm.cs.masc.mutation.properties;

import org.apache.commons.configuration2.ex.ConfigurationException;

public class InterprocProperties extends AOperatorProperties {
    private final String otherClassName;
//    private final ArrayList<String> imports;

//    public ArrayList<String> getImports() {
//        return imports;
//    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getSecureParam() {
        return secureParam;
    }

    public String getInsecureParam() {
        return insecureParam;
    }

    public String getVariableName() {
        return variableName;
    }

    public String getInvocation() {
        return invocation;
    }

    public Boolean getTry_catch() {
        return try_catch;
    }

    public String getOtherClassName() {return otherClassName;}

    private final String propertyName;
    
    private final String secureParam;
    private final String insecureParam;
    private final String variableName;
    private final String invocation;
    private final Boolean try_catch;

    public InterprocProperties(String path)
            throws ConfigurationException {
        super(path);

        propertyName = reader.getValueForAKey("propertyName");
        secureParam = reader.getValueForAKey("secureParam");
        insecureParam = reader.getValueForAKey("insecureParam");
        variableName = reader.getValueForAKey("variableName");
        invocation = reader.getValueForAKey("invocation");
        try_catch  = Boolean.valueOf(reader.getValueForAKey("try_catch"));
        otherClassName = reader.getValueForAKey("otherClassName");
    }
}
