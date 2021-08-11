package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

public class ValueInVariable extends AStringOperator {

    public ValueInVariable(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        // String variableName = "insecureParam";
        return "String " +
                variableName +
                " = \"" +
                insecureParam +
                "\";\n" +
                api_name +
                "." +
                invocation +
                "(" +
                variableName +
                ");";
    }
}
