package edu.wm.cs.masc.mutation.operators.restrictive.intoperator;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class IterationMultipleCall extends AIntOperator {

    public IterationMultipleCall(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append("for (int i = 0; i < ")
                .append(iterationCount)
                .append("; i++){\n\t");
        String template = MisuseType.getTemplate(this, "i");
        template = template.replace("\n", "\n\t");
        s.append(template);
        s.append("\n}");
        return s.toString();
    }
}
