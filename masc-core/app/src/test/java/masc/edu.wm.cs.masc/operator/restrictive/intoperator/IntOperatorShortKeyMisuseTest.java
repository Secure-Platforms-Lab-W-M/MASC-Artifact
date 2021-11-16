package masc.edu.wm.cs.masc.operator.restrictive.intoperator;

import masc.edu.wm.cs.masc.properties.IntOperatorProperties;
import org.junit.Before;

public class IntOperatorShortKeyMisuseTest extends AIntOperatorTest{

    @Before
    public void setUp() throws Exception {
        p = new IntOperatorProperties("src/main/resources/IntOperatorShortKeyMisuse.properties");
        StringBuilder s = new StringBuilder();
        s.append("KeyGenerator keyGen = KeyGenerator.getInstance(\"Blowfish\");\n");
        s.append("keyGen.init(%s);");
        template = s.toString();
    }


}
