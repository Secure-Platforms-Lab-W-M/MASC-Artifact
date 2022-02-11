package edu.wm.cs.masc.mutation.reflection;

import edu.wm.cs.masc.mutation.builders.generic.BuilderMethodSpec;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;

public class MemberReflectionTest {
    @Test
    public void createDummyConstructor() throws ClassNotFoundException {
        Constructor<?> c = ClassReflection
                .getSmallestConstructorWithParameter(
                        "javax.net.ssl.SSLSessionBindingEvent");
        assert c != null;
        String output = BuilderMethodSpec
                .createDummyConstructorWithSuper(c,true)
                .build()
                .toString();

        String expected = "Constructor() {\n" +
                "  super(null, null);\n" +
                "}\n";
        Assert.assertEquals(expected, output);
    }

//    @Test
//    public void formatParametersTest(){
//        String input = "public abstract void checkClientTrusted(java.security.cert.X509Certificate[],java.lang.String) throws java.security.cert.CertificateException";
//        String output = "public abstract void checkClientTrusted(java.security.cert.X509Certificate[] a0, java.lang.String a1) throws java.security.cert.CertificateException";
//        Assert.assertEquals(output, MemberReflection.formatParameters(input));
//        input = "public abstract void checkClientTrusted(java.security.cert.X509Certificate[]) throws java.security.cert.CertificateException";
//        output = "public abstract void checkClientTrusted(java.security.cert.X509Certificate[] a0) throws java.security.cert.CertificateException";
//        Assert.assertEquals(output, MemberReflection.formatParameters(input));
//        input = "public abstract void checkClientTrusted() throws java.security.cert.CertificateException";
//        output = "public abstract void checkClientTrusted() throws java.security.cert.CertificateException";
//        Assert.assertEquals(output, MemberReflection.formatParameters(input));
//    }
}
