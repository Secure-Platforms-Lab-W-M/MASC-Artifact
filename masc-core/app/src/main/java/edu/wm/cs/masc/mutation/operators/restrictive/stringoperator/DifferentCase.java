package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

public class DifferentCase extends AStringOperator {

    public DifferentCase(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"")
                .append(insecureParam.toLowerCase())
                .append("\");");
        return s.toString();
    }
}
