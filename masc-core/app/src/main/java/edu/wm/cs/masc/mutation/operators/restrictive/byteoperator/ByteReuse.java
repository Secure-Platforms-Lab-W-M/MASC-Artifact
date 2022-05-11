package edu.wm.cs.masc.mutation.operators.restrictive.byteoperator;

import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;

public class ByteReuse extends AByteOperator {

    public ByteReuse(ByteOperatorProperties p) {
        super(p);
    }

    @Override
    public String mutation() {
        return "String " + tempVariableName + "= \"example\";\n" +

                api_name + " " + api_variable + " = " + "new " + api_name +
                "(" + tempVariableName + ".getBytes(),0,8);\n" +

                api_name + " " + api_variable + "2 = " + "new " + api_name +
                "(" + tempVariableName + ".getBytes(),0,8);";

        //* Note: Reusing the tempVariableName. This can instantiate
        //*                        misuses that reuse IVs for example.
        //* We simply alter the api_variable and reuse the tempVariable.
        //* This file has a corresponding test file ByteReuseTest.java
        //* The test passes!
    }
}

