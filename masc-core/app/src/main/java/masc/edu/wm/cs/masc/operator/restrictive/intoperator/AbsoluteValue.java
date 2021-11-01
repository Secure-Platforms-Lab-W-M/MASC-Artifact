package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class AbsoluteValue extends AIntOperator {

    public AbsoluteValue(IntOperatorProperties properties) {
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
                .append("salt").append(", Math.abs(")
                .append(iterationCount).append("));");
        System.out.println(s.toString());
        return s.toString();
    }
}
