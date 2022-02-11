package edu.wm.cs.masc.mutation.operators.restrictive.intoperator;

public class MisuseType {

    public static String getTemplate(AIntOperator op, String intParameter){
        StringBuilder s = new StringBuilder();
        if (op.misuse.equals("PBE")) {
            s.append("byte[] salt%d = ").append(op.salt).append(";\n");
            s.append(op.api_name)
                    .append(".")
                    .append(op.invocation)
                    .append("(\"").append(op.password)
                    .append("\".toCharArray(), ")
                    .append("salt%d, " + intParameter + ");");
        }
        else{
            s.append("KeyGenerator ")
                    .append(op.keyGenVarName)
                    .append(" = KeyGenerator.getInstance(\"")
                    .append(op.algorithm)
                    .append("\");\n");
            s.append(op.keyGenVarName);
            s.append(".init(" + intParameter + ");");

        }
        return s.toString();
    }


}
