package edu.wm.cs.masc.mutation.builders.generic;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.Modifier;

public class BuilderMainMethod {
    public static MethodSpec.Builder getMethodSpec() {
        return MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello");
//                .build();
    }

    public static MethodSpec.Builder getMethodSpecWithException() {
        return MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addException(Exception.class)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello");
//                .build();
    }
}
