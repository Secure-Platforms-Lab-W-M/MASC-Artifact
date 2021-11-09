package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

public class MisuseType {

    public static String getTemplate(AIntOperator op){
        StringBuilder s = new StringBuilder();
        if (op.misuse.equals("PBE")) {
            s.append("byte[] salt = ").append(op.salt).append(";\n");
            s.append(op.api_name)
                    .append(".")
                    .append(op.invocation)
                    .append("(\"").append(op.password).append("\", ")
                    .append("salt, %s);");
        }
        else{
            s.append("KeyGenerator ")
                    .append(op.keyGenVarName)
                    .append(" = KeyGenerator.getInstance(\"")
                    .append(op.algorithm)
                    .append("\");\n");
            s.append(op.keyGenVarName);
            s.append(".init(%s);");

        }
        return s.toString();
    }


}
