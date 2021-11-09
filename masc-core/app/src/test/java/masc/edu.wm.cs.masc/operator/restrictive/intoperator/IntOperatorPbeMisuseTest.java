package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

import masc.edu.wm.cs.masc.properties.IntOperatorProperties;
import org.junit.Before;

public class IntOperatorPbeMisuseTest extends AIntOperatorTest{

    @Before
    public void setUp() throws Exception {
        p = new IntOperatorProperties("src/test/resources/properties/IntOperatorPbeMisuse.properties");
        StringBuilder s = new StringBuilder();
        s.append("byte[] salt = {80,45,56};\n");
        s.append("javax.crypto.spec.PBEKeySpec(\"very_secure\".toCharArray(), salt, %s);");
        template = s.toString();
    }

}
