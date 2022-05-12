package masc.operator.restrictive.byteoperator;

import edu.wm.cs.masc.mutation.operators.restrictive.byteoperator.ByteLoop;
import edu.wm.cs.masc.mutation.operators.restrictive.byteoperator.ByteReuse;
import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;
import edu.wm.cs.masc.mutation.operators.IOperator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ByteReuseTest {

    private IOperator operator;

    @Before
    public void beforeClass() throws Exception {
        operator = new ByteReuse(new ByteOperatorProperties(
                "src/main/resources/IVParameterSpec.properties"));
    }


    @Test
    public void byte_reuse() {
        String expected = "String cryptoTemp= \"octogons\";\n" +
                "javax.crypto.spec.IvParameterSpec ivSpec = " +
                "new javax.crypto.spec.IvParameterSpec(cryptoTemp.getBytes(),0,8);\n" +
                //* Note: Here is our mutated misuse. We reuse the cryptoTemp Byte input.
                "javax.crypto.spec.IvParameterSpec ivSpec2 = " +
                "new javax.crypto.spec.IvParameterSpec(cryptoTemp.getBytes(),0,8);\n"+

                //* Note: Here we test for our base misuse case
                "Cipher c = Cipher.getInstance(“AES”);\n"+
                "c.init(Cipher.ENCRYPT_MODE, ivSpec);\n"+
                "c.init(Cipher.ENCRYPT_MODE, ivSpec2);\n"+

                //* Note: Here we test for our evasive developer
                // emulation mutated misuse cases
                "c.init(Cipher.ENCRYPT_MODE, Arrays.copyOf(ivSpec2, ivSpec2.length()));\n"+
                "c.init(Cipher.ENCRYPT_MODE, ivSpec2.clone());";
        assertEquals(expected, operator.mutation());
    }

}