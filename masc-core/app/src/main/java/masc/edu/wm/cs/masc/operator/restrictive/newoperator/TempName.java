package masc.edu.wm.cs.masc.operator.restrictive.newoperator;

import masc.edu.wm.cs.masc.operator.IOperator;
import masc.edu.wm.cs.masc.properties.NewOperatorProperties;

public class TempName implements IOperator {

    protected final String api_name;
    protected final String invocation;
    protected final String password;
    protected final String salt;
    protected final String iterationCount;

    public TempName(NewOperatorProperties p) {
        this.api_name = p.getApiName();
        this.invocation = p.getInvocation();
        this.password = p.getPassword();
        this.salt = p.getSalt();
        this.iterationCount = p.getIterationCount();
    }

    public TempName(String api_name, String invocation,
                           String password, String salt,
                           String iterationCount) {
        this.api_name = api_name;
        this.invocation = invocation;
        this.password = password;
        this.salt = salt;
        this.iterationCount = iterationCount;
    }

    @Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(password).append("\", ")
                .append(salt).append(", ")
                .append(iterationCount).append(")")
                .append(";");
        System.out.println(s.toString());
        return s.toString();
    }
}
