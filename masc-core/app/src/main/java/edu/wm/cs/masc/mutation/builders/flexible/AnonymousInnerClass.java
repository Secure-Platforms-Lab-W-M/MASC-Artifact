package edu.wm.cs.masc.mutation.builders.flexible;

import com.squareup.javapoet.MethodSpec;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMethodSpec;
import edu.wm.cs.masc.mutation.reflection.ClassReflection;
import edu.wm.cs.masc.mutation.reflection.MemberReflection;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnonymousInnerClass {

    /**
     * this method is used to return a getter method spec that returns an
     * anonymous inner class object
     *
     * @param abstractClassNameWithPackage       name of the anonymous object
     *                                           creating class with package,
     *                                           e.g. javax.net.ssl
     *                                           .X509TrustManager
     * @param returnValueBoolean                 return value to be used for
     *                                           methods that return boolean
     * @param methodNameGetAnonymousObject       name of the method which
     *                                           will be returning the
     *                                           anonymous inner class object
     * @param typeOfAnonymousInnerClassExtension type of the object which
     *                                           will be returned as
     *                                           anonymous inner class
     *                                           object, generally an
     *                                           extension of an
     *                                           interface/abstract class
     * @return MethodSpec.Builder
     * @throws ClassNotFoundException in case abstractClassNameWithPackage is
     *                                not found
     */
    static MethodSpec.Builder generateAnonymousObjectGetterMethod(
            String abstractClassNameWithPackage,
            boolean returnValueBoolean,
            String methodNameGetAnonymousObject,
            String typeOfAnonymousInnerClassExtension,
            boolean addOverridableMethodsInInnerClass
    ) throws ClassNotFoundException {
        //define method spec which will return anonymous inner class object
        MethodSpec.Builder returnAnonymousObjectMethodBuilder =
                MethodSpec.methodBuilder(methodNameGetAnonymousObject)
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(Class.forName(abstractClassNameWithPackage));

        String statementOfAnonymousInnerClassObject = "return new ";
        if (typeOfAnonymousInnerClassExtension == null) {
            //is the return method type abstract class
            statementOfAnonymousInnerClassObject += abstractClassNameWithPackage + "(";

        } else {
            //or a class that extends abstract class
            statementOfAnonymousInnerClassObject += typeOfAnonymousInnerClassExtension + "(";
        }

        //check if the class requires constructor
        if (ClassReflection
                .doesClassRequireConstructor(abstractClassNameWithPackage)) {
            Constructor<?> constructor =
                    ClassReflection
                            .getSmallestConstructorWithParameter(
                                    abstractClassNameWithPackage);
            if (typeOfAnonymousInnerClassExtension == null ||
                    typeOfAnonymousInnerClassExtension
                            .equals(abstractClassNameWithPackage)) {

                assert constructor != null;

                statementOfAnonymousInnerClassObject +=
                        MemberReflection
                                .getStringForDummyParameters(
                                        constructor.getParameterTypes(),
                                        returnValueBoolean);

                statementOfAnonymousInnerClassObject += "){\n";
            } else {
                statementOfAnonymousInnerClassObject += "){\n";
                statementOfAnonymousInnerClassObject +=
                        BuilderMethodSpec
                                .createDummyConstructorWithSuper(
                                        constructor, returnValueBoolean)
                                .build()
                                .toString();
            }
        } else {
            statementOfAnonymousInnerClassObject += "){\n";
        }
        returnAnonymousObjectMethodBuilder.addCode(
                statementOfAnonymousInnerClassObject
        );

        //add overridable methods inside anonymous object
        if (addOverridableMethodsInInnerClass) {
            returnAnonymousObjectMethodBuilder =
                    addOverridingMethodsOfAbstractTypeInMethodSpec(
                            abstractClassNameWithPackage,
                            returnValueBoolean,
                            returnAnonymousObjectMethodBuilder);
        }

        returnAnonymousObjectMethodBuilder.addCode("};");
        return returnAnonymousObjectMethodBuilder;
    }

    /**
     * this method is used to return a getter method spec that returns an
     * anonymous inner class object
     *
     * @param abstractClassNameWithPackage       name of the anonymous object
     *                                           creating class with package,
     *                                           e.g. javax.net.ssl
     *                                           .X509TrustManager
     * @param returnValueBoolean                 return value to be used for
     *                                           methods that return boolean
     * @param methodNameGetAnonymousObject       name of the method which
     *                                           will be returning the
     *                                           anonymous inner class object
     * @param typeOfAnonymousInnerClassExtension type of the object which
     *                                           will be returned as
     *                                           anonymous inner class
     *                                           object, generally an
     *                                           extension of an
     *                                           interface/abstract class
     * @return
     * @throws ClassNotFoundException
     */
    static MethodSpec.Builder generateAnonymousObjectGetterMethod(
            String abstractClassNameWithPackage,
            boolean returnValueBoolean,
            String methodNameGetAnonymousObject,
            String typeOfAnonymousInnerClassExtension
    ) throws ClassNotFoundException {
        return generateAnonymousObjectGetterMethod(
                abstractClassNameWithPackage,
                returnValueBoolean,
                methodNameGetAnonymousObject,
                typeOfAnonymousInnerClassExtension,
                false);
    }

    public static MethodSpec.Builder addOverridingMethodsOfAbstractTypeInMethodSpec(
            String abstractClassNameWithPackage,
            boolean returnValueBoolean,
            MethodSpec.Builder returnAnonymousObjectMethodBuilder)
            throws ClassNotFoundException {
        MemberReflection member = new MemberReflection(
                abstractClassNameWithPackage);

        for (Method method : member.getEligibleMethods()) {
            returnAnonymousObjectMethodBuilder.addCode("@Override\n");
            String methodSignature = MemberReflection
                    .getGeneratedMethod(method, returnValueBoolean);
            returnAnonymousObjectMethodBuilder.addCode(methodSignature);
        }
        return returnAnonymousObjectMethodBuilder;
    }

    static MethodSpec.Builder generateAnonymousObjectGetterMethod(
            String abstractClassNameWithPackage,
            boolean returnValueBoolean,
            String methodNameGetAnonymousObject
    ) throws ClassNotFoundException {
        return generateAnonymousObjectGetterMethod(
                abstractClassNameWithPackage,
                returnValueBoolean,
                methodNameGetAnonymousObject,
                null,
                true);
    }

}
