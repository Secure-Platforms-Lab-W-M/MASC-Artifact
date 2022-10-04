package plugins;
import edu.wm.cs.masc.mutation.operators.restrictive.intoperator.AIntOperator;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class IntOperatorExample extends AIntOperator {
    public IntOperatorExample(IntOperatorProperties p) {
        super(p);
    }

    @Override
    public String mutation() {

        // Get the iteration count as an integer
        int iterCount = Integer.parseInt(iterationCount);
        int term1 = 30;
        int term2 = iterCount - term1;

        String s = term1 + " + " + term2;
        return getTemplate(s);
    }

    public String getTemplate(String intParameter){
        StringBuilder s = new StringBuilder();
        if (misuse.equals("PBE")) {
            s.append("byte[] salt%d = ").append(salt).append(";\n");
            s.append(api_name)
                    .append(".")
                    .append(invocation)
                    .append("(\"").append(password)
                    .append("\".toCharArray(), ")
                    .append("salt%d, " + intParameter + ");");
        }
        else{
            s.append("KeyGenerator ")
                    .append(keyGenVarName)
                    .append(" = KeyGenerator.getInstance(\"")
                    .append(algorithm)
                    .append("\");\n");
            s.append(keyGenVarName);
            s.append(".init(" + intParameter + ");");

        }
        return s.toString();
    }
}
