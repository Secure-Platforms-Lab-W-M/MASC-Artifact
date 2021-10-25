package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

public class StringCaseTransform extends AStringOperator {

    public StringCaseTransform(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(insecureParam.toLowerCase())
                .append("\".toUpperCase(java.util.Locale.English))")
                .append(";");
        return s.toString();
    }
}
