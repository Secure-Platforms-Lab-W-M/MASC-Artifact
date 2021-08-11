package masc.edu.wm.cs.masc.operator.restrictive.byteoperator;

import masc.edu.wm.cs.masc.properties.ByteOperatorProperties;

public class CurrentTime extends AByteOperator {

    public CurrentTime(ByteOperatorProperties p) {
        super(p);
    }

    @Override
    public String mutation() {
        return "String " + tempVariableName + "=\"\";\n" +
                "java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat" +
                "(\"yyyy-MM-dd 'at' HH:mm:ss z\");\n" +
                "java.sql.Date date = new java.sql.Date(System.currentTimeMillis());\n" +
                tempVariableName + " = formatter.format(date);\n" +
                api_name + " " + api_variable + " = " + "new " + api_name +
                "(" + tempVariableName + ".getBytes(),0,8);";
    }
}
