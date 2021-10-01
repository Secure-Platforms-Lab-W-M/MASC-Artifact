package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

public class ValueInVariable extends AStringOperator {

    public ValueInVariable(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        // String variableName = "insecureParam";
        String line1 = String.format("String %s = \"%s\";", variableName, insecureParam);
        String line2 = String.format("%s.%s(%s);", api_name, invocation, variableName);
        return line1 + "\n" + line2;
        //return "String " +
        //        variableName +
        //        " = \"" +
        //        insecureParam +
        //        "\";\n" +
        //        api_name +
        //        "." +
        //        invocation +
        //        "(" +
        //        variableName +
        //        ");";
    }
}
