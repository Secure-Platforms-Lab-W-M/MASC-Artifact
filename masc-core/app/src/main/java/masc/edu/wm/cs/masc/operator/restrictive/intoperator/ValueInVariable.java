package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class ValueInVariable extends AIntOperator {

    public ValueInVariable(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append("int ").append(variableName).append(" = ")
                .append(iterationCount).append(";\n");
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(password).append("\", ")
                .append(salt).append(", ")
                .append(variableName).append(")")
                .append(";");
        System.out.println(s.toString());
        return s.toString();
    }
}
