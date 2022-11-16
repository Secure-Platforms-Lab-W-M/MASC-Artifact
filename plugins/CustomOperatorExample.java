package plugins;

import edu.wm.cs.masc.mutation.operators.custom.ACustomGenericOperator;
import edu.wm.cs.masc.mutation.properties.CustomOperatorProperties;

public class CustomOperatorExample extends ACustomGenericOperator{
    public CustomOperatorExample(CustomOperatorProperties p) {
        super(p);
    }
    @Override
    public String mutation() {
        return "javax.crypto.Cipher.getInstance(\"aes\");";
    }
}