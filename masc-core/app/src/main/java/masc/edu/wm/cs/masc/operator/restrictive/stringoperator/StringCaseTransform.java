package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

public class StringCaseTransform extends AStringOperator {

    public StringCaseTransform(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        return String.format("%s.%s(\"%s\".toUpperCase(java.util.Locale.English));",
                api_name, invocation, insecureParam.toLowerCase());
    }
}
