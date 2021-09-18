package masc.edu.wm.cs.masc.builders.flexible;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainClass;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainMethod;
import masc.edu.wm.cs.masc.reflection.ClassReflection;

public class AnonymousObjectBuilderAllInOne {

    public static TypeSpec getAbstractClassAnonymousObjectInClassWithMain(
            String abstractClassNameWithPackage,
            String classNameForMain,
            boolean returnValueBoolean
    )
            throws ClassNotFoundException {

        TypeSpec.Builder resultTypeSpec;

        //define class
        resultTypeSpec = BuilderMainClass.getClassBody(classNameForMain);

        //define public static void main method
        MethodSpec.Builder mainMethodBuilder = BuilderMainMethod.getMethodSpec();

        String abstractClassNameWithoutPackage =
                ClassReflection.getClassNameWithoutPackage(abstractClassNameWithPackage);

        // name of function that will be returning the anonymous inner class object
        String methodNameGetAnonymousObject = "get" + abstractClassNameWithoutPackage;

        // create method that returns anonymous inner class object
        MethodSpec.Builder returnAnonymousObjectMethodBuilder =
                AnonymousInnerClass
                        .generateAnonymousObjectGetterMethod(
                                abstractClassNameWithPackage,
                                returnValueBoolean,
                                methodNameGetAnonymousObject,
                                abstractClassNameWithPackage,
                                true
                                );
        //
        mainMethodBuilder.addStatement(methodNameGetAnonymousObject + "();");

        //add anonymous object creation method inside the class spec
        resultTypeSpec.addMethod(returnAnonymousObjectMethodBuilder.build());

        //add public static void main method inside the class
        resultTypeSpec.addMethod(mainMethodBuilder.build());

        //return build class spec
        return resultTypeSpec.build();
    }

}
