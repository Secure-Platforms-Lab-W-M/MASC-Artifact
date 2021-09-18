package masc.edu.wm.cs.masc.operator.restrictive.byteoperator;

import masc.edu.wm.cs.masc.operator.IOperator;
import masc.edu.wm.cs.masc.properties.ByteOperatorProperties;

public abstract class AByteOperator implements IOperator {


    protected final String api_name;
    protected final String api_variable;
    protected final String tempVariableName;

    public AByteOperator(ByteOperatorProperties p) {
        this.api_name = p.getApiName();
        this.api_variable = p.getApiVariable();
        this.tempVariableName = p.getTempVariableName();
    }
}
