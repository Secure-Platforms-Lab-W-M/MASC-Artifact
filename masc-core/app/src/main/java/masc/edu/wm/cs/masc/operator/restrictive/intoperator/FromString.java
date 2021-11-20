package masc.edu.wm.cs.masc.operator.restrictive.intoperator;
import masc.edu.wm.cs.masc.properties.IntOperatorProperties;

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
