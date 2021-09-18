package masc.edu.wm.cs.masc.operator.flexible;

import masc.edu.wm.cs.masc.builders.flexible.EmptyAbstractClassImplementingAbstractType;
import masc.edu.wm.cs.masc.utility.CustomFileReader;


import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class EmptyAbstractClassImplementingAbstractTypeTest {
    public static String resources = "src/test/resources/operator.generation/EmptyAbstractClassImplementingAbstractTypeTest/";

    @Test
    public void SubClassBuilderTestSpec0() throws IOException, ClassNotFoundException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resources +
                        "EmptyAbstractClassWhichExtends.txt");


        String output = EmptyAbstractClassImplementingAbstractType
                .SubClassBuilder(
                        "BareBone_X509TrustManagerCanBypassExt",
                        "javax.net.ssl.X509ExtendedTrustManager",
                        "BareBone_X509TrustManagerCanBypass",
                        true
                )[0].toString();

        assertEquals(expectedOutput, output);

    }

    @Test
    public void SubClassBuilderTestSpec1() throws IOException, ClassNotFoundException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resources +
                        "EmptyAbstractClassWhichExtendsMain.txt");


        String output = EmptyAbstractClassImplementingAbstractType
                .SubClassBuilder(
                        "BareBone_X509TrustManagerCanBypassExt",
                        "javax.net.ssl.X509ExtendedTrustManager",
                        "BareBone_X509TrustManagerCanBypass",
                        true
                )[1].toString();

        assertEquals(expectedOutput, output);
    }

    @Test
    public void SubClassBuilderTestSpec0WithNonMandatoryConstructor() throws IOException, ClassNotFoundException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resources + "SubClassBuilderTestSpec0WithNonMandatoryConstructor.txt");


        String output = EmptyAbstractClassImplementingAbstractType
                .SubClassBuilder(
                        "BareBone_X509TrustManagerCanBypassExt",
                        "javax.net.ssl.SSLParameters",
                        "BareBone_X509TrustManagerCanBypass",
                        true
                )[0].toString();

        assertEquals(expectedOutput, output);

    }

    @Test
    public void SubClassBuilderTestSpec0WithMandatoryConstructor() throws IOException, ClassNotFoundException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resources +
                        "SubClassBuilderTestSpec0WithMandatoryConstructor.txt");


        String output = EmptyAbstractClassImplementingAbstractType
                .SubClassBuilder(
                        "BareBone_X509TrustManagerCanBypassExt",
                        "javax.net.ssl.SSLContext",
                        "BareBone_X509TrustManagerCanBypass",
                        true
                )[0].toString();

        assertEquals(expectedOutput, output);

    }

    @Test
    public void InterfaceImplementationBuilderTestSpec0() throws IOException, ClassNotFoundException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resources +
                        "EmptyAbstractClassWhichImplements.txt");

        String output = EmptyAbstractClassImplementingAbstractType
                .InterfaceImplementationBuilder(
                        "BareBone_X509TrustManagerCanBypassExt",
                        "javax.net.ssl.X509TrustManager",
                        "BareBone_X509TrustManagerCanBypass",
                        true
                )[0].toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void InterfaceImplementationBuilderTestSpec1() throws IOException, ClassNotFoundException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resources +
                        "EmptyAbstractClassWhichImplementsMain.txt");

        String output = EmptyAbstractClassImplementingAbstractType
                .InterfaceImplementationBuilder(
                        "BareBone_X509TrustManagerCanBypassExt",
                        "javax.net.ssl.X509TrustManager",
                        "BareBone_X509TrustManagerCanBypass",
                        true
                )[1].toString();

        assertEquals(expectedOutput, output);
    }


    @Test
    public void InterfaceImplementationBuilderTestSpec1ReturnTrue() throws IOException, ClassNotFoundException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resources +
                        "EmptyAbstractClassWhichImplementsMainWithTrue.txt");

        String output = EmptyAbstractClassImplementingAbstractType
                .InterfaceImplementationBuilder(
                        "BareBone_X509TrustManagerCanBypassExt",
                        "javax.net.ssl.HostnameVerifier",
                        "BareBone_X509TrustManagerCanBypass",
                        true
                )[1].toString();

        assertEquals(expectedOutput, output);
    }

    @Test
    public void InterfaceImplementationBuilderTestSpec1ReturnFalse() throws IOException, ClassNotFoundException {
        String expectedOutput = CustomFileReader
                .readFileAsString(resources +
                        "EmptyAbstractClassWhichImplementsMainWithFalse.txt");

        String output = EmptyAbstractClassImplementingAbstractType
                .InterfaceImplementationBuilder(
                        "BareBone_X509TrustManagerCanBypassExt",
                        "javax.net.ssl.HostnameVerifier",
                        "BareBone_X509TrustManagerCanBypass",
                        false
                )[1].toString();

        assertEquals(expectedOutput, output);
    }

}
