package masc.edu.wm.cs.masc.properties;

import org.apache.commons.configuration2.ex.ConfigurationException;

public class NewOperatorProperties extends AOperatorProperties{

    private final String invocation;
    private final String password;
    private final String salt;
    private final String iterationCount;


    public NewOperatorProperties(String path)
            throws ConfigurationException {
        super(path);
        invocation = reader.getValueForAKey("invocation");
        password = reader.getValueForAKey("password");
        salt = reader.getValueForAKey("salt");
        iterationCount = reader.getValueForAKey("iterationCount");
    }

    public String getInvocation() {return invocation;}

    public String getPassword() {return password;}

    public String getSalt() {return salt;}

    public String getIterationCount() {return iterationCount;}



}
