package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class FromString extends AIntOperator {

    public FromString(IntOperatorProperties properties) {
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
                .append("salt").append(", ")
                .append("Integer.parseInt(\"").append(iterationCount)
                .append("\"));");
        return s.toString();
    }
}
