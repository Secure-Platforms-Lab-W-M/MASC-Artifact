package masc.edu.wm.cs.masc.operator.restrictive.interprocoperator;

import masc.edu.wm.cs.masc.properties.InterprocProperties;
import masc.edu.wm.cs.masc.utility.CustomFileReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class InterProcOperatorTest {
    InterprocProperties cipherProperties, cipher_trycatchProperties;
    InterProcOperator cipherOperator, cipher_trycatchOperator;

    @Before
    public void setUp() throws Exception {
        String dir = "src/test/resources/properties/";

        cipherProperties = new InterprocProperties(dir
                +
                "InterprocCipher.properties");
        cipher_trycatchProperties = new InterprocProperties(dir +
                "InterprocCipherTryCatch.properties");
        cipherOperator = new InterProcOperator(cipherProperties);
        cipher_trycatchOperator = new InterProcOperator(
                cipher_trycatchProperties);
    }

    @Test
    public void insecure_call() {

        try {
            String mCipherInsecureCall = CustomFileReader.readFileAsString(
                    "src/test/java/masc/edu/wm/cs/masc/operator/restrictive" +
                            "/interprocoperator/output_cipher.txt");
            assertEquals(mCipherInsecureCall, cipherOperator.insecure_call());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insecure_call_trycatch() {
        try {
            String mCipherInsecureCall = CustomFileReader.readFileAsString(
                    "src/test/java/masc/edu/wm/cs/masc/operator/restrictive" +
                            "/interprocoperator/output_cipher_trycatch.txt");
            assertEquals(mCipherInsecureCall,
                    cipherOperator.insecure_call_trycatch());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mutation() {
        if (cipher_trycatchProperties.getTry_catch()) {
            insecure_call_trycatch();
        } else {
            insecure_call();
        }
    }
}