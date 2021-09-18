package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

public class StringCaseTransform extends AStringOperator {

    public StringCaseTransform(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        return api_name +
                "." +
                invocation +
                "(\"" + insecureParam.toLowerCase() +
                "\".toUpperCase(java.util.Locale.English))" +
                ";";
    }
}
