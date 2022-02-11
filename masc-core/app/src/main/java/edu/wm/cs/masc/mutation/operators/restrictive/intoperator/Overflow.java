package edu.wm.cs.masc.mutation.operators.restrictive.intoperator;

import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class Overflow extends AIntOperator{

    public Overflow(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        String s = "Integer.MAX_VALUE + Integer.MAX_VALUE + 2 + " + iterationCount;
        return MisuseType.getTemplate(this, s);

    }
}
