package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringOperatorTestMessageDigest {
    StringOperator operator;
    @Before
    public void setUp() throws Exception {
        operator = new StringOperator(
                new StringOperatorProperties("src/test/resources/properties/MessageDigest.properties"));
    }

    @Test
    public void safeToUnsafe(){
        String expected = "java.security.MessageDigest.getInstance(\"SHA-256\".replace" +
                "(\"SHA-256\", \"MD5\"));";

        assertEquals(expected, operator.safeReplaceWithUnsafe());
    }

    @Test
    public void valueInVariable() {
        String expected = "String cryptoVariable = \"MD5\";\n" +
                "java.security.MessageDigest.getInstance(cryptoVariable);";
        assertEquals(expected, operator.valueInVariable());
    }

    @Test
    public void noiseReplace() {
        String expected = "java.security.MessageDigest.getInstance(\"M~D5\".replace(\"~\", \"\");";
        assertEquals(expected, operator.noiseReplace());
    }

    @Test
    public void differentCase(){
        String expected = "java.security.MessageDigest.getInstance(\"md5\");";
        assertEquals(expected, operator.differentCaseTranform());

    }

    @Test
    public void stringCaseTransform(){
        String expected = "java.security.MessageDigest.getInstance(\"md5\".toUpperCase(java.util" +
                ".Locale.English));";
        assertEquals(expected, operator.stringCaseTransform());
    }

}