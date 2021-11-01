package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class WhileLoopAccumulation extends AIntOperator {

    public WhileLoopAccumulation(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        StringBuilder s = new StringBuilder();
        s.append(super.mutation());
        s.append("int i = 0;\n")
                .append("while (i < ").append(iterationCount).append("){\n\t")
                .append("i++;\n}\n");
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(password).append("\", ")
                .append("salt").append(", i);");
        System.out.println(s.toString());
        return s.toString();
    }
}
