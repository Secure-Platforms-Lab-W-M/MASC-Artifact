package edu.wm.cs.masc.mutation.operators.flexible;

import edu.wm.cs.masc.mutation.properties.FlexibleOperatorProperties;

public class AIOEmptyAbstractClassImplementsInterface extends AFlexibleOperator{
    public AIOEmptyAbstractClassImplementsInterface(
            FlexibleOperatorProperties p) {
        super(p);
        this.setClassNameForClass(p.getOtherClassName());
    }
}
