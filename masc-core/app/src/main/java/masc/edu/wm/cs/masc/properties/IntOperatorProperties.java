package masc.edu.wm.cs.masc.properties;

import org.apache.commons.configuration2.ex.ConfigurationException;

public class IntOperatorProperties extends AOperatorProperties{

    private final String invocation;
    private final String password;
    private final String salt;
    private final String iterationCount;
    private final String variableName;
    private final String misuse;
    private final String algorithm;
    private final String keyGenVarName;


    public IntOperatorProperties(String path)
            throws ConfigurationException {
        super(path);
        invocation = reader.getValueForAKey("invocation");
        password = reader.getValueForAKey("password");
        salt = reader.getValueForAKey("salt");
        iterationCount = reader.getValueForAKey("iterationCount");
        variableName = reader.getValueForAKey("variableName");
        misuse = reader.getValueForAKey("misuse");
        algorithm = reader.getValueForAKey("algorithm");
        keyGenVarName = reader.getValueForAKey("keyGenVarName");
    }

    public String getInvocation() {return invocation;}

    public String getPassword() {return password;}

    public String getSalt() {return salt;}

    public String getIterationCount() {return iterationCount;}

    public String getVariableName() {return variableName;}

    public String getMisuse() {return misuse;}

    public String getAlgorithm() {return algorithm;}

    public String getKeyGenVarName(){return keyGenVarName;}



}
