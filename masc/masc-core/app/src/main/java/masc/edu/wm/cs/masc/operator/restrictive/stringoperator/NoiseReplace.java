package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

public class NoiseReplace extends AStringOperator {

    public NoiseReplace(StringOperatorProperties properties) {
      super(properties);

    }

    @Override
    public String mutation() {
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
}
