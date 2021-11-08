package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

public class MisuseType {

    public static String getTemplate(AIntOperator op){
        StringBuilder s = new StringBuilder();
        s.append("byte[] salt = ").append(op.salt).append(";\n");
        s.append(op.api_name)
                .append(".")
                .append(op.invocation)
                .append("(\"").append(op.password).append("\", ")
                .append("salt, %s);");
        return s.toString();
    }


}
