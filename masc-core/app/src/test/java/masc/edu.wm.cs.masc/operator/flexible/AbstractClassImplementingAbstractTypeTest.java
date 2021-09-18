package masc.edu.wm.cs.masc.operator.flexible;

import masc.edu.wm.cs.masc.builders.flexible.AbstractClassImplementingAbstractType;
import masc.edu.wm.cs.masc.utility.CustomFileReader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

public class AbstractClassImplementingAbstractTypeTest {

    final static String resource_path =
            "src/test/resources/operator.generation/AbstractClassImplementingAbstractTypeTest/";

    @Test
    public void InterfaceImplementationBuilderTestSpec0() throws ClassNotFoundException, IOException {
        String expectedOutput;
        String output;

        expectedOutput = CustomFileReader.readFileAsString(
                resource_path + "InterfaceImplementationBuilderTestSpec0.txt");
        output = AbstractClassImplementingAbstractType.InterfaceImplementationBuilder(
                "BareBone_X509TrustManagerCanBypassExt",
                "javax.net.ssl.X509TrustManager",
                "BareBone_X509TrustManagerCanBypass",
                true
        )[0].toString();

        assertEquals(expectedOutput, output);

    }

    //BareBone_X509TrustManagerCanBypassExt
    //BareBone_X509TrustManagerCanBypass
    @Test
    public void InterfaceImplementationBuilderTestSpec0WithTrue() throws ClassNotFoundException, IOException {
        String expectedOutput;
        String output;

        expectedOutput = CustomFileReader.readFileAsString(
                resource_path + "InterfaceImplementationBuilderTestSpec0WithTrue.txt");
        output = AbstractClassImplementingAbstractType.InterfaceImplementationBuilder(
                "BareBone_X509TrustManagerCanBypassExt",
                "javax.net.ssl.HostnameVerifier",
                "BareBone_X509TrustManagerCanBypass",
                true
        )[0].toString();

        assertEquals(expectedOutput, output);

    }


    @Test
    public void InterfaceImplementationBuilderTestSpec0WithFalse() throws ClassNotFoundException, IOException {
        String expectedOutput;
        String output;

        expectedOutput = CustomFileReader.readFileAsString(
                resource_path + "InterfaceImplementationBuilderTestSpec0WithFalse.txt");
        output = AbstractClassImplementingAbstractType.InterfaceImplementationBuilder(
                "BareBone_X509TrustManagerCanBypassExt",
                "javax.net.ssl.HostnameVerifier",
                "BareBone_X509TrustManagerCanBypass",
                false
        )[0].toString();

        assertEquals(expectedOutput, output);
    }

    @Test
    public void InterfaceImplementationBuilderTestSpec1() throws ClassNotFoundException, IOException {
        String expectedOutput;
        String output;

        expectedOutput = CustomFileReader.readFileAsString(resource_path + "InterfaceImplementationBuilderTestSpec1.txt");
        output = AbstractClassImplementingAbstractType.InterfaceImplementationBuilder(
                "BareBone_X509TrustManagerCanBypassExt",
                "javax.net.ssl.X509TrustManager",
                "BareBone_X509TrustManagerCanBypass",
                true
        )[1].toString();

        assertEquals(expectedOutput, output);

    }

    @Test
    public void SubClassBuilderTestSpec0() throws IOException, ClassNotFoundException {
        String expectedOutput;
        String output;

        expectedOutput = CustomFileReader.readFileAsString(resource_path + "SubClassBuilderTestSpec0.txt");
        output = AbstractClassImplementingAbstractType.SubClassBuilder(
                "BareBone_X509TrustManagerCanBypassExt",
                "javax.net.ssl.X509ExtendedTrustManager",
                "BareBone_X509TrustManagerCanBypass",
                true
        )[0].toString();
        assertEquals(expectedOutput, output);

    }

    @Test
    public void SubClassBuilderTestSpec0WithTrue() throws IOException, ClassNotFoundException {
        String expectedOutput;
        String output;

        expectedOutput = CustomFileReader.readFileAsString(resource_path + "SubClassBuilderTestSpec0WithTrue.txt");
        output = AbstractClassImplementingAbstractType.SubClassBuilder(
                "BareBone_X509TrustManagerCanBypassExt",
                "javax.net.ssl.SSLSocket",
                "BareBone_X509TrustManagerCanBypass",
                true
        )[0].toString();
         assertEquals(expectedOutput, output);
    }

    @Test
    public void SubClassBuilderTestSpec0WithFalse() throws IOException, ClassNotFoundException {
        String expectedOutput;
        String output;

        expectedOutput = CustomFileReader.readFileAsString(
                resource_path + "SubClassBuilderTestSpec0WithFalse.txt");
        output = AbstractClassImplementingAbstractType.SubClassBuilder(
                "BareBone_X509TrustManagerCanBypassExt",
                "javax.net.ssl.SSLSocket",
                "BareBone_X509TrustManagerCanBypass",
                false
        )[0].toString();

//        assertEquals(expectedOutput, output);
        String[] expected_lines = expectedOutput.split("\n");
        String[] output_lines = output.split("\n");
        for(int i =0; i<expected_lines.length; i++){
            assertEquals(expected_lines[i], output_lines[i]);
        }

    }

}
