package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

public class Overflow extends AIntOperator{

    public Overflow(IntOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {

        String s = "Integer.MAX_VALUE + Integer.MAX_VALUE + 2 + " + iterationCount;
        String template = MisuseType.getTemplate(this, 2);
        return String.format(template, s);

    }
}
