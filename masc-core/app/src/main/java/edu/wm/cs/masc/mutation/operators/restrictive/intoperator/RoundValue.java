package edu.wm.cs.masc.mutation.operators.restrictive.intoperator;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class RoundValue extends AIntOperator {

    public RoundValue(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        String s = "Math.round(" + iterationCount + ")";
        return MisuseType.getTemplate(this, s);
    }
}
