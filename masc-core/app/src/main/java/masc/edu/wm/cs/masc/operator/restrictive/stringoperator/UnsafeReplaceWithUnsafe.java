package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

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
