package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;


public class StringOperator extends AStringOperator {

    public StringOperator(StringOperatorProperties properties) {
        super(properties);
    }

    public String valueInVariable() {
        StringBuilder sb = new StringBuilder();
        sb.append("String ")
                .append(variableName)
                .append(" = \"")
                .append(insecureParam)
                .append("\";\n");
        sb.append(api_name)
                .append(".")
                .append(invocation)
                .append("(")
                .append(variableName)
                .append(");");
        return sb.toString();
    }

    public String noiseReplace() {
        StringBuilder mutation = new StringBuilder();
        mutation
                .append(this.api_name)
                .append(".")
                .append(this.invocation)
                .append("(")
                .append("\"");

        int mid_index = this.insecureParam.length() / 2;
        // Cipher.getInstance("A$ES
        for (int i = 0; i < this.insecureParam.length(); i++) {
            if (i == mid_index) {
                mutation.append(noise);
            }
            mutation.append(this.insecureParam.charAt(i));
        }
        // Cipher.getInstance("A$ES".replace("$","");
        mutation
                .append("\"")
                .append(".replace(\"")
                .append(this.noise)
                .append("\", \"\");");
        return mutation.toString();
    }

    public String differentCaseTranform() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"")
                .append(insecureParam.toLowerCase())
                .append("\");");
        return s.toString();
    }

    public String stringCaseTransform() {

        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(insecureParam.toLowerCase())
                .append("\".toUpperCase(java.util.Locale.English))")
                .append(";");
        return s.toString();
    }

    public String safeReplaceWithUnsafe() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(secureParam).append("\".")
                .append("replace(\"")
                .append(secureParam).append("\", \"")
                .append(insecureParam).append("\"));");
        return s.toString();
    }

    public String unsafeReplaceWithUnsafe() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"").append(insecureParam).append("\".")
                .append("replace(\"")
                .append(insecureParam).append("\", \"")
                .append(insecureParam).append("\"));");
        return s.toString();
    }

    @Override
    public String mutation() {
        return null;
    }
}
