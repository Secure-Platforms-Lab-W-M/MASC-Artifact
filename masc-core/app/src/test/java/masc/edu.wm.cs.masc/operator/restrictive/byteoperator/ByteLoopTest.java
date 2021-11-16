package masc.edu.wm.cs.masc.operator.restrictive.byteoperator;

import masc.edu.wm.cs.masc.properties.ByteOperatorProperties;
import masc.edu.wm.cs.masc.operator.IOperator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ByteLoopTest {

    private IOperator b;

    @Before
    public void beforeClass() throws Exception {
        b = new ByteLoop(new ByteOperatorProperties(
                "src/main/resources/IVParameterSpec.properties"));
    }

    @Test
    public void loop_byte() {
        String expected = "String cryptoTemp=\"\";\n" +
                "for(int i = 65; i < 75; i++){\n" +
                "    cryptoTemp += (char) i;\n" +
                "}\n" +
                "javax.crypto.spec.IvParameterSpec ivSpec = " +
                "new javax.crypto.spec.IvParameterSpec(cryptoTemp.getBytes(),0,8);";
        assertEquals(expected, b.mutation());
    }

}