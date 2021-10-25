package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

public class ValueInVariable extends AStringOperator {

    public ValueInVariable(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        StringBuilder sb = new StringBuilder();
        sb.append("String ")
                .append(variableName)
                .append(" = \"")
                .append(insecureParam)
                .append("\";\n");
        sb.append(api_name)
                .append(".")
                .append(invocation)
                .append("(")
                .append(variableName)
                .append(");");
        return sb.toString();
    }
}
