package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class Overflow extends AIntOperator{

    public Overflow(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        StringBuilder s = new StringBuilder();
        s.append(super.mutation());
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(password).append("\", ")
                .append("salt").append(", Integer.MAX_VALUE + Integer.MAX_VALUE + 2 + ")
                .append(iterationCount).append(");");
        return s.toString();
    }
}
