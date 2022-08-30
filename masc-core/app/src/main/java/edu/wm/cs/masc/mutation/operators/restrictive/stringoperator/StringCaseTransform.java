package edu.wm.cs.masc.mutation.operators.restrictive.stringoperator;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

import java.util.Locale;

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
                .append("\".toUpperCase(java.util.Locale.ENGLISH))")
                .append(";");
        return s.toString();
    }
}
