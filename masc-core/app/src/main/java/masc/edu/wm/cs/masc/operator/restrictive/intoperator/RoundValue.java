package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

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
