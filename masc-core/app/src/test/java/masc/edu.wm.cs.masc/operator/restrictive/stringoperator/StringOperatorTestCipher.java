package masc.edu.wm.cs.masc.operator.restrictive.stringoperator;

import masc.edu.wm.cs.masc.properties.StringOperatorProperties;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringOperatorTestCipher {

    StringOperatorProperties p;

    @Before
    public void setUp() throws Exception {
        p = new StringOperatorProperties("src/main/resources/Cipher.properties");

    }

    @Test
    public void safeToUnsafe() {
//        aesGCMNoPaddingReplaceDES
        String expected = "javax.crypto.Cipher.getInstance" +
                "(\"AES/GCM/NoPadding\".replace" +
                "(\"AES/GCM/NoPadding\", \"AES\"));";

        assertEquals(expected, new SafeReplaceWithUnsafe(p).mutation().replace("%d",""));
    }

    @Test
    public void valueInVariable() {
        String expected = "String cryptoVariable = \"AES\";\n" +
                "javax.crypto.Cipher.getInstance(cryptoVariable);";
        assertEquals(expected, new ValueInVariable(p).mutation().replace("%d",""));
    }

    @Test
    public void noiseReplace() {
        String expected = "javax.crypto.Cipher.getInstance(\"A~ES\".replace" +
                "(\"~\", \"\");";
        assertEquals(expected, new NoiseReplace(p).mutation().replace("%d",""));
    }

    @Test
    public void differentCase() {
        String expected = "javax.crypto.Cipher.getInstance(\"aes\");";
        assertEquals(expected, new DifferentCase(p).mutation().replace("%d",""));

    }

    @Test
    public void stringCaseTransform() {
        String expected = "javax.crypto.Cipher.getInstance(\"aes\"" +
                ".toUpperCase(java.util" +
                ".Locale.English));";
        assertEquals(expected, new StringCaseTransform(p).mutation().replace("%d",""));
    }

    @Test
    public void unsafeToUnsafe() {
        String expected = "javax.crypto.Cipher.getInstance(\"AES\"" +
                ".replace(\"AES\", \"AES\"));";
        assertEquals(expected, new UnsafeReplaceWithUnsafe(p).mutation().replace("%d",""));
    }

}