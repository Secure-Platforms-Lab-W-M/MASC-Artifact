package edu.wm.cs.masc.mutation.operators.restrictive.byteoperator;

import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;

public class CurrentTime extends AByteOperator {

    public CurrentTime(ByteOperatorProperties p) {
        super(p);
    }

    @Override
    public String mutation() {
        return "String " + tempVariableName + "=\"\";\n" +
                "java.text.SimpleDateFormat formatter%d = new java.text.SimpleDateFormat" +
                "(\"yyyy-MM-dd 'at' HH:mm:ss z\");\n" +
                "java.sql.Date date%d = new java.sql.Date(System.currentTimeMillis());\n" +
                tempVariableName + " = formatter%d.format(date%d);\n" +
                api_name + " " + api_variable + " = " + "new " + api_name +
                "(" + tempVariableName + ".getBytes(),0,8);";
    }
}
