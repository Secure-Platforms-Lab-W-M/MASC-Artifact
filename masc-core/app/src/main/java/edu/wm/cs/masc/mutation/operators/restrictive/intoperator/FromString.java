package edu.wm.cs.masc.mutation.operators.restrictive.intoperator;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class FromString extends AIntOperator {

    public FromString(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        String s = "Integer.parseInt(\"" + iterationCount + "\")";
        return MisuseType.getTemplate(this, s);
    }
}
