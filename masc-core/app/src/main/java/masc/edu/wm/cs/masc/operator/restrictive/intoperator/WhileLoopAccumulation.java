package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

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
        String template = MisuseType.getTemplate(this, 2);
        s.append(String.format(template,"i"));

        // Return the generated string
        return s.toString();

    }
}
