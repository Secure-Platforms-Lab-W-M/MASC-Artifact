package edu.wm.cs.masc.mutation.builders.flexible;
//edu.wm.cs.mascDeprecated.builders.flexible
import edu.wm.cs.masc.mutation.builders.flexible.AnonymousInnerClass;
import edu.wm.cs.masc.utils.file.CustomFileReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;


public class AnonymousInnerClassTest {
    final static String resource_path = "src/test/resources/operator.generation/AnonymousInnerClassTest/";

    //BareBone_X509TrustManagerCanBypassExt
    //BareBone_X509TrustManagerCanBypass

    @Test
    public void generateAnonymousObjectGetterMethodWithTrue() throws ClassNotFoundException, IOException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resource_path + "generateAnonymousObjectGetterMethodWithTrue.txt");
        String output =
                AnonymousInnerClass
                        .generateAnonymousObjectGetterMethod(
                                "javax.net.ssl.HostnameVerifier",
                                true,
                                "BareBone_X509TrustManagerCanBypass",
                                "BareBone_X509TrustManagerCanBypassExt",
                                true
                        ).build().toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void generateAnonymousObjectGetterMethodWithFalse() throws ClassNotFoundException, IOException {
        String expectedOutput = CustomFileReader.readFileAsString(resource_path + "generateAnonymousObjectGetterMethodWithFalse.txt");
        String output =
                AnonymousInnerClass
                        .generateAnonymousObjectGetterMethod(
                                "javax.net.ssl.HostnameVerifier",
                                false,
                                "BareBone_X509TrustManagerCanBypass",
                                "BareBone_X509TrustManagerCanBypassExt",
                                true
                        ).build().toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void generateAnonymousObjectGetterMethodWithNullInterface() throws ClassNotFoundException, IOException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resource_path + "generateAnonymousObjectGetterMethodWithNullInterface.txt");
        String output =
                AnonymousInnerClass
                        .generateAnonymousObjectGetterMethod(
                                "javax.net.ssl.X509TrustManager",
                                false,
                                "BareBone_X509TrustManagerCanBypass",
                                "BareBone_X509TrustManagerCanBypassExt",
                                true
                        ).build().toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void generateAnonymousObjectGetterMethodWithNullSuperclass() throws ClassNotFoundException, IOException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resource_path + "generateAnonymousObjectGetterMethodWithNullSuperclass.txt");
        String output =
                AnonymousInnerClass
                        .generateAnonymousObjectGetterMethod(
                                "javax.net.ssl.X509TrustManager",
                                false,
                                "BareBone_X509TrustManagerCanBypass",
                                "BareBone_X509TrustManagerCanBypassExt",
                                true
                        ).build().toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testGenerateAnonymousObjectGetterMethodBasic() throws ClassNotFoundException, IOException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resource_path + "GenerateAnonymousObjectGetterMethodBasic.txt");
        String output = AnonymousInnerClass
                .generateAnonymousObjectGetterMethod(
                        "javax.net.ssl.X509ExtendedTrustManager",
                        true,
                        "getBareboneExt"
                ).build().toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void addOverridingMethodsOfAbstractTypeInMethodSpec() {
    }

    @Test
    public void testGenerateAnonymousObjectGetterMethod1() {
    }
}