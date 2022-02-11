package edu.wm.cs.masc.mutation.builders.flexible;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainClass;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainMethod;
import edu.wm.cs.masc.mutation.builders.generic.BuilderTypeSpec;
import edu.wm.cs.masc.mutation.reflection.MemberReflection;

import javax.lang.model.element.Modifier;


public class AbstractClassImplementingAbstractType {

    private static TypeSpec getAbstractClass(
            String abstractClassName,
            String superTypeWithPackageName,
            boolean isSuperTypeInterface,
            boolean valueForBooleanReturn
    ) throws ClassNotFoundException {

        //define an abstract class as specified by name
        TypeSpec.Builder
                resultSpec = TypeSpec
                .classBuilder(abstractClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        // if the abstract super type is interface, implement; otherwise extend
        if (isSuperTypeInterface)
            resultSpec.addSuperinterface(Class.forName(superTypeWithPackageName));
        else {
            resultSpec.superclass(Class.forName(superTypeWithPackageName));
        }

        // find members of the abstract super type
        MemberReflection member = new MemberReflection(superTypeWithPackageName);

//        BuilderTypeSpec.addOverridingMethodsInTypeSpec(valueForBooleanReturn, resultSpec, member, true);
        BuilderTypeSpec.addOverridingMethodsInTypeSpec(valueForBooleanReturn, resultSpec, member);
        return resultSpec.build();
    }

    private static TypeSpec useAbstractClassInMain(
            String abstractClassName,
            String abstractTypeWithPackageName,
            String mainClassName
    ) throws ClassNotFoundException {

        TypeSpec.Builder resultSpec;

        //create main class
        resultSpec = BuilderMainClass.getClassBody(mainClassName);

        //create name of getter method
        String methodNameForGetAnonymousObject = "get" + abstractClassName;

        //create getter method of anonymous inner class object
        MethodSpec.Builder inner_method = AnonymousInnerClass
                .generateAnonymousObjectGetterMethod(
                        abstractTypeWithPackageName,
                        true,
                        methodNameForGetAnonymousObject,
                        abstractClassName
                );
        //create main method
        MethodSpec.Builder main_method = BuilderMainMethod.getMethodSpec();

        //add call to anonymous inner class object getter method
        main_method.addCode(methodNameForGetAnonymousObject + "();\n");

        //add both methods in main class
        resultSpec.addMethod(inner_method.build());
        resultSpec.addMethod(main_method.build());

        return resultSpec.build();
    }

    /**
     * InterfaceImplementationBuilder returns two type specs. the first one contains
     * an abstract class that implements the given interface,
     * and overrides all the methods of the interface. The second one contains a Main Class
     * with a getter method that returns an anonymous object of the create abstract class,
     * and a main method that calls the getter method.
     *
     * @param abstractClassName                       name of the abstract class to be created
     * @param interfaceWithPackageName                interface with package name which will be implemented
     * @param mainClassName                           name of the main class where anonymous inner class object will be created from the abstract className
     * @param valueForBooleanReturnInOverridenMethods boolean return value to be used for overriding methods which return a boolean value
     * @return The TypeSpec[] object containing both TypeSpecs.
     * @throws ClassNotFoundException in case the interface is not found
     */
    public static TypeSpec[] InterfaceImplementationBuilder(String abstractClassName,
                                                            String interfaceWithPackageName,
                                                            String mainClassName,
                                                            boolean valueForBooleanReturnInOverridenMethods)
            throws ClassNotFoundException {

        TypeSpec[] typeSpecs = new TypeSpec[2];
        typeSpecs[0] = getAbstractClass(
                abstractClassName,
                interfaceWithPackageName,
                true,
                valueForBooleanReturnInOverridenMethods);
        typeSpecs[1] = useAbstractClassInMain(
                abstractClassName,
                interfaceWithPackageName,
                mainClassName);
        return typeSpecs;
    }

    /**
     * SubClassBuilder returns two type specs. the first one contains
     * an abstract class that extends the given abstract class,
     * and overrides all the methods. The second one contains a Main Class
     * with a getter method that returns an anonymous object of the create abstract class,
     * and a main method that calls the getter method.
     *
     * @param abstractClassName                       name of the abstract class to be created
     * @param superClassWithPackageName               abstract class with package name which will be extended
     * @param mainClassName                           name of the main class where anonymous inner class object will be created from the abstract className
     * @param valueForBooleanReturnInOverridenMethods boolean return value to be used for overriding methods which return a boolean value
     * @return The TypeSpec[] object containing both TypeSpecs.
     * @throws ClassNotFoundException in case the interface is not found
     */
    public static TypeSpec[] SubClassBuilder(String abstractClassName,
                                             String superClassWithPackageName,
                                             String mainClassName,
                                             boolean valueForBooleanReturnInOverridenMethods)
            throws ClassNotFoundException {
        TypeSpec[] typeSpecs = new TypeSpec[2];
        typeSpecs[0] = getAbstractClass(
                abstractClassName,
                superClassWithPackageName,
                false,
                valueForBooleanReturnInOverridenMethods);
        typeSpecs[1] = useAbstractClassInMain(
                abstractClassName,
                superClassWithPackageName,
                mainClassName);
        return typeSpecs;
    }
}
