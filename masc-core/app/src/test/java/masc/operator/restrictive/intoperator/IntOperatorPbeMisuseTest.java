package masc.operator.restrictive.intoperator;

import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;
import org.junit.Before;

public class IntOperatorPbeMisuseTest extends AIntOperatorTest{

    @Before
    public void setUp() throws Exception {
        p = new IntOperatorProperties("src/main/resources/IntOperatorPbeMisuse.properties");
        String s = "byte[] salt = {80,45,56};\n" +
                "javax.crypto.spec.PBEKeySpec(\"very_secure\".toCharArray(), salt, %s);";
        template = s;
    }

}
