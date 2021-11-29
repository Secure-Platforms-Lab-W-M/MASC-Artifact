package masc.edu.wm.cs.masc.operator.restrictive.byteoperator;

import masc.edu.wm.cs.masc.properties.ByteOperatorProperties;
import masc.edu.wm.cs.masc.operator.IOperator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CurrentTimeTest {

    IOperator b;

    @Before
    public void setUp() throws Exception {
        b = new CurrentTime(new ByteOperatorProperties(
                "src/main/resources/IVParameterSpec.properties"));
    }

    @Test
    public void current_time() {
        String expected = "String cryptoTemp=\"\";\n" +
                "java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat" +
                "(\"yyyy-MM-dd 'at' HH:mm:ss z\");\n" +
                "java.sql.Date date = new java.sql.Date(System.currentTimeMillis());\n" +
                "cryptoTemp = formatter.format(date);\n" +
                "javax.crypto.spec.IvParameterSpec ivSpec = " +
                "new javax.crypto.spec.IvParameterSpec(cryptoTemp.getBytes(),0,8);";
        assertEquals(expected, b.mutation().replace("%d", ""));

    }
}