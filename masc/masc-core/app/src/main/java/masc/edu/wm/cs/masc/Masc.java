package masc.edu.wm.cs.masc;

import com.squareup.javapoet.TypeSpec;
import masc.edu.wm.cs.masc.builders.flexible.EmptyAbstractClassImplementingAbstractType;

public class Masc {
    public static void main(String[] args) throws ClassNotFoundException {

        String classNameWithPackage = "javax.net.ssl.HostnameVerifier";
//        System.out.println(
//                AnonymousObjectBuilderAllInOne
//                        .getAbstractClassAnonymousObjectInClassWithMain(
//                                "javax.net.ssl.SSLContext",
//                                "BareBone", true)
//                        .toString());
//        TypeSpec t = AnonymousObjectBuilderAllInOne
//                .getAbstractClassAnonymousObjectInClassWithMain(
//                        "javax.net.ssl.SSLContext",
//                        "BareBone", true);

//
//        System.out.println(
//                EmptyAbstractClassImplementingInterface
//                        .getEmptyAbstractClassImplementingInterface(
//                                "FakeTrustManager",
//                                "javax.net.ssl.TrustManager").toString());

        TypeSpec[] typeSpecs;
//        typeSpecs = EmptyAbstractClassImplementingAbstractType.InterfaceImplementationBuilder(
//                "BareBone_X509TrustManagerCanBypassExt",
//                classNameWithPackage,
//                "BareBone_X509TrustManagerCanBypass",
//                true);

        typeSpecs = EmptyAbstractClassImplementingAbstractType.SubClassBuilder(
                "BareBone_X509TrustManagerCanBypassExt",
                classNameWithPackage,
                "BareBone_X509TrustManagerCanBypass",
                true);



//        System.out.println(
//                AbstractClassImplementingAbstractType
//                        .getAbstractClass(
//                                "BareBone_X509TrustManagerCanNotBypassExt",
//                                "javax.net.ssl.X509TrustManager",
//                                true
//                        ));
//        typeSpecs = AbstractClassImplementingAbstractType.SubClassBuilder(
//                "BareBone_X509TrustManagerCanBypassExt",
//                classNameWithPackage,
//                "BareBone_X509TrustManagerCanBypass",
//                false
//        );

        for(TypeSpec spec: typeSpecs){
            System.out.println(spec.toString());
        }

    }
}
