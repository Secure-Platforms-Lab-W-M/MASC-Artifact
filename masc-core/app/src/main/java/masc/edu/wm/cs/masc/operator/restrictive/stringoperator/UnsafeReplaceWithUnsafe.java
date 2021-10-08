package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

public class UnsafeReplaceWithUnsafe extends AStringOperator{
    public UnsafeReplaceWithUnsafe(StringOperatorProperties p) {
        super(p);
    }

    @Override
    public String mutation() {

        return String.format("%s.%s(\"%s\".replace(\"%s\",\"%s\"));",
                api_name, invocation, insecureParam, insecureParam, insecureParam);
        //return api_name +
        //        "." +
        //        invocation +
        //        "(\"" + insecureParam + "\"." +
        //        "replace(\"" +
        //        insecureParam + "\", \"" +
        //        insecureParam + "\"));";
    }
}
