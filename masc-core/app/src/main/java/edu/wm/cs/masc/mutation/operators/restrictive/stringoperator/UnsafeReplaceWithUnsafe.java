package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

public class UnsafeReplaceWithUnsafe extends AStringOperator{
    public UnsafeReplaceWithUnsafe(StringOperatorProperties p) {
        super(p);
    }

    @Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(insecureParam).append("\".")
                .append("replace(\"")
                .append(insecureParam).append("\", \"")
                .append(insecureParam).append("\"));");
        return s.toString();
    }
}
