package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

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
