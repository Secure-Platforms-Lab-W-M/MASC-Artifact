package plugins;

import edu.wm.cs.masc.mutation.operators.restrictive.byteoperator.AByteOperator;
import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;

public class ByteOperatorExample extends AByteOperator {
    public ByteOperatorExample(ByteOperatorProperties p) {
        super(p);
    }

    @Override
    public String mutation() {
        return "String " + tempVariableName + "=\"\";\n" +
                "for(int i = 65; i < 75; i++){\n" +
                "    " + tempVariableName +
                " += (char) i;\n" +
                "}\n" +
                api_name + " " + api_variable + " = " + "new " + api_name +
                "(" + tempVariableName + ".getBytes(),0,8);";
    }
}