package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class Arithmetic extends AIntOperator {

    public Arithmetic(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        int iterCount = Integer.parseInt(iterationCount);
        int term1 = (int) (Math.random() * 2 * iterCount) - iterCount;
        int term2 = iterCount - term1;
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(password).append("\", ")
                .append(salt).append(", ")
                .append(term1 + " + " + term2).append(")")
                .append(";");
        System.out.println(s.toString());
        return s.toString();
    }
}
