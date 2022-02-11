package edu.wm.cs.masc.mutation.builders.generic;

import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class BuilderMainClass {

    public static TypeSpec.Builder getClassBody(String strClassName, boolean isFinal) {
        if (isFinal)
            return TypeSpec.classBuilder(strClassName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        else {
            return getClassBody(strClassName);
        }
    }

    public static TypeSpec.Builder getClassBody(String strClassName) {
        return TypeSpec.classBuilder(strClassName)
                .addModifiers(Modifier.PUBLIC);
    }
}
