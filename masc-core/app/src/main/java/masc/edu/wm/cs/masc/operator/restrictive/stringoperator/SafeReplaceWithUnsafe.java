package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

public class SafeReplaceWithUnsafe extends AStringOperator {

    public SafeReplaceWithUnsafe(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        return String.format("%s.%s(\"%s\".replace(\"%s\", \"%s\"));",
                api_name, invocation, secureParam,
                secureParam, insecureParam);
    }
}
