package masc.operator.restrictive.intoperator;

import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;
import org.junit.Before;

public class IntOperatorShortKeyMisuseTest extends AIntOperatorTest{

    @Before
    public void setUp() throws Exception {
        p = new IntOperatorProperties("src/main/resources/IntOperatorShortKeyMisuse.properties");
        String s = "KeyGenerator keyGen = KeyGenerator.getInstance" +
                "(\"Blowfish\");\n" +
                "keyGen.init(%s);";
        template = s;
    }


}
