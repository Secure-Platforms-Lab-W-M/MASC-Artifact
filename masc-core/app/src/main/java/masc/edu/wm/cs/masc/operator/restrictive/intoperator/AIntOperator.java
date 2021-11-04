package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

import masc.edu.wm.cs.masc.operator.IOperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class AIntOperator implements IOperator {

    protected final String api_name;
    protected final String invocation;
    protected final String password;
    protected final String salt;
    protected final String iterationCount;
    protected final String variableName;

    public AIntOperator(IntOperatorProperties p) {
        this.api_name = p.getApiName();
        this.invocation = p.getInvocation();
        this.password = p.getPassword();
        this.salt = p.getSalt();
        this.iterationCount = p.getIterationCount();
        this.variableName = p.getVariableName();
    }

    public AIntOperator(String api_name, String invocation,
                        String password, String salt,
                        String iterationCount,
                        String variableName) {
        this.api_name = api_name;
        this.invocation = invocation;
        this.password = password;
        this.salt = salt;
        this.iterationCount = iterationCount;
        this.variableName = variableName;
    }

    @Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append("byte[] salt = ").append(salt).append(";\n");
        return s.toString();
    }
}
