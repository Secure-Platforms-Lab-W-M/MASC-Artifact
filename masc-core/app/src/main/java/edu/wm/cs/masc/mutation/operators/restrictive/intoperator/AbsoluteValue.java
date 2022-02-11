package edu.wm.cs.masc.mutation.operators.restrictive.intoperator;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class AbsoluteValue extends AIntOperator {

    public AbsoluteValue(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        String s = "Math.abs(" + iterationCount + ")";
        return MisuseType.getTemplate(this, s);
    }
}
