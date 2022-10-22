package plugins;
import edu.wm.cs.masc.mutation.operators.restrictive.intoperator.*;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class IntOperatorExample_2 extends AIntOperator {
    public IntOperatorExample_2(IntOperatorProperties p) {
        super(p);
    }

    @Override
    public String mutation() {
        // Get the iteration count as an integer
        int iterCount = Integer.parseInt(iterationCount);
        int term1 = 30;
        int term2 = iterCount - term1;

        String s = term1 + " + " + term2;
        return MisuseType.getTemplate(this, s);
    }
}
