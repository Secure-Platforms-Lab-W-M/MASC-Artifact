package edu.wm.cs.masc.mutation.operators.flexible;

import edu.wm.cs.masc.mutation.properties.FlexibleOperatorProperties;

public class AIOEmptyInterfaceExtendsInterface extends AFlexibleOperator {
    public AIOEmptyInterfaceExtendsInterface(
            FlexibleOperatorProperties p) {
        super(p);
        this.setClassNameForClass(p.getOtherClassName());
    }
}
