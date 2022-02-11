package edu.wm.cs.masc.mutation.operators.restrictive.intoperator;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class ValueInVariableArithmetic extends AIntOperator {

    public ValueInVariableArithmetic(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        int iterCount = Integer.parseInt(iterationCount);
        int term1 = 30;
        int term2 = iterCount - term1;

        // Create String Builder
        StringBuilder s = new StringBuilder();

        // Write code that declares and instantiates variable
        s.append("int ").append(variableName)
                .append(" = ").append(term1)
                .append(";\n");

        // Get and use the misuse template
        s.append(MisuseType.getTemplate(this,
                variableName + " + " + term2));

        // Return the generated string
        return s.toString();

    }
}
