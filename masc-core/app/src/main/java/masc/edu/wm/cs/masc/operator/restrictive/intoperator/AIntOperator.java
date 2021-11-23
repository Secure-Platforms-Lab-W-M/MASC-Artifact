package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

import masc.edu.wm.cs.masc.operator.IOperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public abstract class AIntOperator implements IOperator {

    protected final String api_name;
    protected final String invocation;
    protected final String password;
    protected final String salt;
    protected final String iterationCount;
    protected final String variableName;
    protected final String misuse;
    protected final String algorithm;
    protected final String keyGenVarName;

    public AIntOperator(IntOperatorProperties p) {
        this.api_name = p.getApiName();
        this.invocation = p.getInvocation();
        this.password = p.getPassword();
        this.salt = p.getSalt();
        this.iterationCount = p.getIterationCount();
        this.variableName = p.getVariableName();
        this.misuse = p.getMisuse();
        this.algorithm = p.getAlgorithm();
        this.keyGenVarName = p.getKeyGenVarName();
    }

    public AIntOperator(String api_name, String invocation,
                        String password, String salt,
                        String iterationCount,
                        String variableName,
                        String misuse,
                        String algorithm,
                        String keyGenVarName) {
        this.api_name = api_name;
        this.invocation = invocation;
        this.password = password;
        this.salt = salt;
        this.iterationCount = iterationCount;
        this.variableName = variableName;
        this.misuse = misuse;
        this.algorithm = algorithm;
        this.keyGenVarName = keyGenVarName;
    }

    @Override
    public String mutation() {
        return "";
    }

}
