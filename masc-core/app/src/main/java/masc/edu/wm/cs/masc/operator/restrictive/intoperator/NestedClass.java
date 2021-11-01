package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class NestedClass extends AIntOperator {

    public NestedClass(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        StringBuilder s = new StringBuilder();
        s.append(super.mutation());
        s.append("class NestedClass{\n\t").append("int getIteration(){\n\t\t")
                .append("return " + iterationCount).append(";\n\t}\n}\n");
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(password).append("\", ")
                .append("salt").append(", new NestedClass().getIteration());");
        return s.toString();
    }
}
