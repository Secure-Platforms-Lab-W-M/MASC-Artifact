package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class IterationMultipleCall extends AIntOperator {

    public IterationMultipleCall(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        StringBuilder s = new StringBuilder();
        s.append(super.mutation());
        s.append("for (int i = 0; i < ").append(iterationCount)
                .append("; i++){\n\t");
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(password).append("\", ")
                .append("salt").append(", i);\n")
                .append("}");
        return s.toString();
    }
}
