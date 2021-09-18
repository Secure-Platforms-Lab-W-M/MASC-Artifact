package masc.edu.wm.cs.masc.operator.flexible;

import masc.edu.wm.cs.masc.builders.flexible.AnonymousObjectBuilderAllInOne;
import masc.edu.wm.cs.masc.utility.CustomFileReader;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.io.IOException;

public class AnonymousObjectBuilderAllInOneTest {
    final static String resource_path = "src/test/resources/operator.generation/AnonymousObjectBuilderAllInOneTest/";

    //BareBone_X509TrustManagerCanBypassExt
    //BareBone_X509TrustManagerCanBypass
    @Test
    public void getAbstractClassAnonymousObjectInClassWithMainTrue() throws ClassNotFoundException, IOException {
        String expectedOutput = CustomFileReader.readFileAsString(
                resource_path +
                        "getAbstractClassAnonymousObjectInClassWithMainTrue.txt");
        String output = AnonymousObjectBuilderAllInOne.getAbstractClassAnonymousObjectInClassWithMain(
                "javax.net.ssl.HostnameVerifier",
                "BareBone_X509TrustManagerCanBypass",
                true
        ).toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void getAbstractClassAnonymousObjectInClassWithMainFalse() throws ClassNotFoundException, IOException {
        String expectedOutput = CustomFileReader.readFileAsString(
                resource_path +
                        "getAbstractClassAnonymousObjectInClassWithMainFalse.txt");
        String output = AnonymousObjectBuilderAllInOne.getAbstractClassAnonymousObjectInClassWithMain(
                "javax.net.ssl.HostnameVerifier",
                "BareBone_X509TrustManagerCanBypass",
                false
        ).toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void getAbstractClassAnonymousObjectInClassWithMainFalseWithConstructor() throws IOException, ClassNotFoundException {
        String expectedOutput = CustomFileReader
                .readFileAsString(
                        resource_path +
                                "getAbstractClassAnonymousObjectInClassWithMainFalseWithConstructor.txt");

        String output = AnonymousObjectBuilderAllInOne.getAbstractClassAnonymousObjectInClassWithMain(
                "javax.net.ssl.SSLContext",
                "BareBone_X509TrustManagerCanBypass",
                false
        ).toString();
        assertEquals(expectedOutput, output);
    }
}