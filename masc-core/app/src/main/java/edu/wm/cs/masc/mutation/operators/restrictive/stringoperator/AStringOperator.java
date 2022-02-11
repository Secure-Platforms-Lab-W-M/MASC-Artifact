package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

public abstract class AStringOperator implements IOperator {
    protected final String api_name;
    protected final String invocation;
    protected final String secureParam;
    protected final String insecureParam;
    protected final String noise;
    protected final String variableName;

    public AStringOperator(StringOperatorProperties p) {
        this.api_name = p.getApiName();
        this.invocation = p.getInvocation();
        this.secureParam = p.getSecureParam();
        this.insecureParam = p.getInsecureParam();
        this.noise = p.getNoise();
        this.variableName = p.getVariableName();
    }

    public AStringOperator(String api_name, String invocation,
                           String secureParam, String insecureParam,
                           String noise, String variableName) {
        this.api_name = api_name;
        this.invocation = invocation;
        this.secureParam = secureParam;
        this.insecureParam = insecureParam;
        this.noise = noise;
        this.variableName = variableName;
    }
}
