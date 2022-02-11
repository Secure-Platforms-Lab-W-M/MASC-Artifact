package edu.wm.cs.masc.mutation.operators.restrictive.intoperator;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class WhileLoopAccumulation extends AIntOperator {

    public WhileLoopAccumulation(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        // Create String Builder
        StringBuilder s = new StringBuilder();

        // Write the while loop
        s.append("int i = 0;\n")
                .append("while (i < ").append(iterationCount).append("){\n\t")
                .append("i++;\n}\n");

        // Get and use the misuse template
        s.append(MisuseType.getTemplate(this, "i"));

        // Return the generated string
        return s.toString();

    }
}
