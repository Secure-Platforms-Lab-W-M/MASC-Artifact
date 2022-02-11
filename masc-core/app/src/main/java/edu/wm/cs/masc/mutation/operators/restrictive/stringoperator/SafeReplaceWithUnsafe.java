package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

public class SafeReplaceWithUnsafe extends AStringOperator {

    public SafeReplaceWithUnsafe(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(secureParam).append("\".")
                .append("replace(\"")
                .append(secureParam).append("\", \"")
                .append(insecureParam).append("\"));");
        return s.toString();
    }
}
