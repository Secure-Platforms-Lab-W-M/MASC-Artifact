package edu.wm.cs.masc.mutation.operators.restrictive.intoperator;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class ValueInVariable extends AIntOperator {

    public ValueInVariable(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        // Create String Builder
        StringBuilder s = new StringBuilder();

        // Write code that declares and instantiates variable
        s.append("int ").append(variableName)
                .append(" = ").append(iterationCount)
                .append(";\n");

        // Get and use the misuse template
        s.append(MisuseType.getTemplate(this, variableName));

        // Return the generated string
        return s.toString();
    }
}
