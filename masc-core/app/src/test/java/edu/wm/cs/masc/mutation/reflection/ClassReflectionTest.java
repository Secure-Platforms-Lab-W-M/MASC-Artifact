package edu.wm.cs.masc.mutation.reflection;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


public class ClassReflectionTest {
    @Test
    public void testGetModifiers() {
        assertEquals("public final",
                ClassReflection.getModifiers("java.lang.String"));
        assertEquals("public abstract interface",
                ClassReflection.getModifiers("javax.net.ssl.TrustManager"));
        assertEquals("public abstract", ClassReflection.getModifiers(
                "javax.net.ssl.X509ExtendedTrustManager"));

    }

    @Test
    public void isFinalTest() {
        assertTrue(ClassReflection.isFinal("java.lang.String"));
        assertFalse(ClassReflection.isFinal("javax.net.ssl.TrustManager"));

    }

    @Test
    public void getClassNameWithoutPackageTest() throws ClassNotFoundException {
        assertEquals(ClassReflection.getClassNameWithoutPackage(
                "javax.net.ssl.X509TrustManager"), "X509TrustManager");
        assertEquals(ClassReflection.getClassNameWithoutPackage(
                        "javax.net.ssl.X509ExtendedTrustManager"),
                "X509ExtendedTrustManager");
    }

    @Test
    public void getPackageName() throws ClassNotFoundException {

        assertEquals(ClassReflection.getPackageName(
                "javax.net.ssl.X509TrustManager"), "javax.net.ssl");

    }


    @Test
    public void getInterfaces() {
    }

    @Test
    public void getClasses() {
    }

    @Test
    public void getAbstractClasses() {
    }

    @Test
    public void isFinal() {
    }

    @Test
    public void getClassNameWithoutPackage() {
    }

    @Test
    public void testGetPackageName() {
    }

    @Test
    public void getModifiers() {
    }

    @Test
    public void getSmallestConstructorParameterSize()
            throws ClassNotFoundException {

        int expectedSize = 0;
        int actualSize = 0;
        actualSize = ClassReflection.getSmallestConstructorParameterSize(
                "javax.net.ssl.X509TrustManager");
        Assert.assertEquals(expectedSize, actualSize);

        actualSize = ClassReflection
                .getSmallestConstructorParameterSize(
                        "java.security.MessageDigest");
        expectedSize = 1;
        Assert.assertEquals(expectedSize, actualSize);

        actualSize = ClassReflection
                .getSmallestConstructorParameterSize(
                        "javax.net.ssl.SSLContext");
        expectedSize = 3;
        Assert.assertEquals(expectedSize, actualSize);
    }

    @Test
    public void getSmallestConstructorWithParameter() {
    }

    @Test
    public void doesClassRequireConstructor() throws ClassNotFoundException {

        boolean actual = ClassReflection
                .doesClassRequireConstructor(
                        "javax.net.ssl.SSLContext");
        assertTrue(actual);


        actual = ClassReflection
                .doesClassRequireConstructor(
                        "javax.net.ssl.SSLSessionBindingEvent");
        assertTrue(actual);

        assertFalse(
                ClassReflection
                        .doesClassRequireConstructor(
                                "javax.net.ssl.SSLParameters"));
    }
}
