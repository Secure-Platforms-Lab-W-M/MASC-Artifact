package plugins;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
import edu.wm.cs.masc.mutation.operators.restrictive.stringoperator.*;

public class StringOperatorExample extends AStringOperator {
    // Don't touch this constructor. Don't add any other constructors.
    public StringOperatorExample(StringOperatorProperties properties) {
        super(properties);
    }

    // Add as many variables, methods, is-a, and has-a relationships as you like. 
    @Override
    public String mutation() {
        // Place your code for mutation here. 
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"")
                .append(insecureParam.toLowerCase())
                .append("\");");
        return s.toString();
    }
}
