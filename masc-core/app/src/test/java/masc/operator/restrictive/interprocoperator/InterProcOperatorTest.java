package masc.operator.restrictive.interprocoperator;

import edu.wm.cs.masc.mutation.operators.restrictive.interprocoperator.InterProcOperator;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;
import edu.wm.cs.masc.utils.file.CustomFileReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class InterProcOperatorTest {
    InterprocProperties cipherProperties, cipher_trycatchProperties;
    InterProcOperator cipherOperator, cipher_trycatchOperator;

    @Before
    public void setUp() throws Exception {
        String dir = "src/main/resources/";

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
    public void insecure_call() throws IOException {


        String mCipherInsecureCall = CustomFileReader.readFileAsString(
                "src/test/resources/operator/restrictive/" +
                        "interprocoperator/output_cipher.txt"
        );
        assertEquals(mCipherInsecureCall, cipherOperator.insecure_call());

    }

    @Test
    public void insecure_call_trycatch() throws IOException {

        String mCipherInsecureCall = CustomFileReader.readFileAsString(
                "src/test/resources/operator/restrictive/" +
                        "/interprocoperator/output_cipher_trycatch.txt");
        assertEquals(mCipherInsecureCall,
                cipherOperator.insecure_call_trycatch());

    }

    @Test
    public void mutation() throws IOException {
        if (cipher_trycatchProperties.getTry_catch()) {
            insecure_call_trycatch();
        } else {
            insecure_call();
        }
    }
}