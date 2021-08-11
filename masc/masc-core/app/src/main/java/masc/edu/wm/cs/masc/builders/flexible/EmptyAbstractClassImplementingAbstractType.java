package masc.edu.wm.cs.masc.builders.flexible;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainClass;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainMethod;
import masc.edu.wm.cs.masc.builders.generic.BuilderMethodSpec;
import masc.edu.wm.cs.masc.reflection.ClassReflection;

import javax.lang.model.element.Modifier;

public class EmptyAbstractClassImplementingAbstractType {

    private static TypeSpec getEmptyAbstractClass(
            String emptyAbstractClassName,
            String superTypeWithPackageName,
            boolean isSuperTypeInterface
    ) throws ClassNotFoundException {
        return getEmptyAbstractClass(
                emptyAbstractClassName,
                superTypeWithPackageName,
                isSuperTypeInterface,
                false);
    }

    private static TypeSpec getEmptyAbstractClass(
            String emptyAbstractClassName,
            String superTypeWithPackageName,
            boolean isSuperTypeInterface,
            boolean constructorBoolean
    ) throws ClassNotFoundException {


        TypeSpec.Builder resultSpec;
        resultSpec = TypeSpec
                .classBuilder(emptyAbstractClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        if (isSuperTypeInterface)
            resultSpec.addSuperinterface(Class.forName(superTypeWithPackageName));
        else {
            resultSpec.superclass(Class.forName(superTypeWithPackageName));
        }

        //does it require a constructor?
        if (ClassReflection.doesClassRequireConstructor(superTypeWithPackageName)) {
            //then add it to the body of empty abstract class
            resultSpec
                    .addMethod(
                            BuilderMethodSpec
                                    .getConstructorMethodSpec(
                                superTypeWithPackageName,
                                constructorBoolean)
                    .build());
        }

        return resultSpec.build();
    }

    private static TypeSpec useEmptyAbstractClassInMain(
            String emptyAbstractClassName,
            String interfaceWithPackageName,
            String mainClassName,
            boolean returnValueBoolean
    ) throws ClassNotFoundException {

        TypeSpec.Builder resultSpec;

        resultSpec = BuilderMainClass.getClassBody(mainClassName);

        String methodNameForGetAnonymousObject = "get" + emptyAbstractClassName;
        MethodSpec.Builder inner_method = AnonymousInnerClass
                .generateAnonymousObjectGetterMethod(
                        interfaceWithPackageName,
                        returnValueBoolean,
                        methodNameForGetAnonymousObject,
                        emptyAbstractClassName,
                        true

                );

        MethodSpec.Builder main_method = BuilderMainMethod.getMethodSpec();
        main_method.addCode(methodNameForGetAnonymousObject + "();\n");

        resultSpec.addMethod(inner_method.build());
        resultSpec.addMethod(main_method.build());

        return resultSpec.build();
    }

    public static TypeSpec[] InterfaceImplementationBuilder(String emptyAbstractClassName,
                                                            String interfaceWithPackageName,
                                                            String mainClassName,
                                                            boolean returnValueBoolean)
            throws ClassNotFoundException {

        TypeSpec[] typeSpecs = new TypeSpec[2];
        typeSpecs[0] = getEmptyAbstractClass(
                emptyAbstractClassName,
                interfaceWithPackageName,
                true);
        typeSpecs[1] = useEmptyAbstractClassInMain(
                emptyAbstractClassName,
                interfaceWithPackageName,
                mainClassName,
                returnValueBoolean);
        return typeSpecs;
    }

    public static TypeSpec[] SubClassBuilder(String emptyAbstractClassName,
                                             String superClassWithPackageName,
                                             String mainClassName,
                                             boolean returnValueBoolean)
            throws ClassNotFoundException {
        TypeSpec[] typeSpecs = new TypeSpec[2];
        typeSpecs[0] = getEmptyAbstractClass(
                emptyAbstractClassName,
                superClassWithPackageName,
                false);
        typeSpecs[1] = useEmptyAbstractClassInMain(
                emptyAbstractClassName,
                superClassWithPackageName,
                mainClassName,
                returnValueBoolean);
        return typeSpecs;
    }


}
