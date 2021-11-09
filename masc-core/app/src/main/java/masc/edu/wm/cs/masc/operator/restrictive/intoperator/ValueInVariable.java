package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

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
        String template = MisuseType.getTemplate(this);
        s.append(String.format(template, variableName));

        // Return the generated string
        return s.toString();
    }
}
