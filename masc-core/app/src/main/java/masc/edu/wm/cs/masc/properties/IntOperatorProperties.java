package masc.edu.wm.cs.masc.properties;

import org.apache.commons.configuration2.ex.ConfigurationException;

public class IntOperatorProperties extends AOperatorProperties{

    protected final String invocation;
    protected final String password;
    protected final String salt;
    protected final String iterationCount;
    protected final String variableName;
    protected final String misuse;
    protected final String algorithm;
    protected final String keyGenVarName;


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
