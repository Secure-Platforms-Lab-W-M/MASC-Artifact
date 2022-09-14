package edu.wm.cs.masc.mutation.operators.restrictive.byteoperator;

import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;

public class ByteReuse extends AByteOperator {

    public ByteReuse(ByteOperatorProperties p) {
        super(p);
    }

    //* Note: Reusing the tempVariableName. This can instantiate
    // misuses that reuse IVs for example. We simply alter the
    // api_variable and reuse the tempVariable. This file has a
    // corresponding test file ByteReuseTest.java. The test passes!

    @Override
    public String mutation() {
        return
                //* Create a 16 byte-long string. In Java, 1 char = 2 bytes.
                // So, we want 8 chars.
                "String " + tempVariableName + "= \"octogons\";\n" +
                //* Pass the 16 byte string into our api call.
                //  Store the result in an api_variable
                api_name + " " + api_variable + " = " + "new " + api_name +
                "(" + tempVariableName + ".getBytes(),0,8);\n" +
                //* Repeat using same tempVariable, and store in a new object
                api_name + " " + api_variable + "2 = " + "new " + api_name +
                "(" + tempVariableName + ".getBytes(),0,8);\n" +

                //* Now, instantiate the base misuse case of eg. reusing iv
                "Cipher c = Cipher.getInstance(“AES“);\n" +
                //* Note: We use api_variable first (eg. ivSpec)
                "c.init(Cipher.ENCRYPT_MODE, "+api_variable+");\n"+
                //* Note: We use api_variable2 second (eg. ivSpec2)
                "c.init(Cipher.ENCRYPT_MODE, "+api_variable+"2);\n"+
                //* The misuse case has now been instantiated: eg. reusing iv

                //* Now, let us emulate an evasive developer in two forms
                //  both of these forms use api_variable2 first (eg. ivSpec2)
                "c.init(Cipher.ENCRYPT_MODE, Arrays.copyOf("+api_variable+"2, "+api_variable+"2.length()));\n"+
                "c.init(Cipher.ENCRYPT_MODE, "+api_variable+"2.clone());";

                //* Note: The test for this file exists in
                // "masc/operator/restrictive/byteoperator/ByteReuseTest.java"
    }
}

