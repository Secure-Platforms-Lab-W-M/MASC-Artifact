package masc.operator.restrictive.byteoperator;

import edu.wm.cs.masc.mutation.operators.restrictive.byteoperator.ByteLoop;
import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;
import edu.wm.cs.masc.mutation.operators.IOperator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ByteLoopTest {

    private IOperator operator;

    @Before
    public void beforeClass() throws Exception {
        operator = new ByteLoop(new ByteOperatorProperties(
                "src/main/resources/IVParameterSpec.properties"));
    }

    @Test
    public void loop_byte() {
        // Note: Predictable or Constant IV Misuse
        String expected = "String cryptoTemp=\"\";\n" +
                "for(int i = 65; i < 75; i++){\n" +
                "    cryptoTemp += (char) i;\n" +
                "}\n" +
                "javax.crypto.spec.IvParameterSpec ivSpec = " +
                "new javax.crypto.spec.IvParameterSpec(cryptoTemp.getBytes(),0,8);";
        assertEquals(expected, operator.mutation());
    }

}