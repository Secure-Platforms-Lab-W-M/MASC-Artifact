package masc.edu.wm.cs.masc.operator.flexible;

import masc.edu.wm.cs.masc.properties.FlexibleOperatorProperties;

public class AIOEmptyAbstractClassImplementsInterface extends AFlexibleOperator{
    public AIOEmptyAbstractClassImplementsInterface(
            FlexibleOperatorProperties p) {
        super(p);
        this.setClassNameForClass(p.getOtherClassName());
    }
}
