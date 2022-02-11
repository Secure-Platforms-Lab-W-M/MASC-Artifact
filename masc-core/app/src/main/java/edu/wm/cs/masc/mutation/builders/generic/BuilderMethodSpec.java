package edu.wm.cs.masc.mutation.builders.generic;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import edu.wm.cs.masc.mutation.reflection.ClassReflection;
import edu.wm.cs.masc.mutation.reflection.MemberReflection;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class BuilderMethodSpec {
    /**
     * Given a method, returns a MethodSpec.Builder object containing a
     * override type method
     * with public modifier, proper return type, and override annotation
     *
     * @param method is the Reflected method object
     * @return MethodSpec.Builder object
     */
    public static MethodSpec.Builder getOverridingPublicAbstractMethod(
            Method method) {
        MethodSpec.Builder currentMethod;
        currentMethod = MethodSpec
                .methodBuilder(method.getName())
                .returns(method.getReturnType())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);
        return currentMethod;
    }

    /**
     * Given a method, and a MethodSpec.Builder,
     * it adds a throws declaration within the MethodSpec.Builder object
     * based on the given method
     *
     * @param method               the object based on which MethodSpec
     *                             .Builder is created
     * @param currentMethodBuilder the MethodSpec object where the throws
     *                             will be added.
     */
    public static void addExceptionsToOverridingMethodSpec(Method method,
                                                           MethodSpec.Builder currentMethodBuilder) {
        // if the method throws exception, add throws statement for
        // overriding method
        if (method.getExceptionTypes().length > 0) {
            for (Class<?> c : method.getExceptionTypes()) {
                currentMethodBuilder.addException(c);
            }
        }
    }

    /**
     * Adds a return statement to the provided MethodSpec.Builder object
     *
     * @param valueForBooleanReturn specifies the value to be used as return
     * @param method                the method based on which the override
     *                              method's return type will be prepared
     * @param currentMethodBuilder  the MethodSpec builder object to be modified
     */
    public static void addReturnValueToOverridingMethodSpec(
            boolean valueForBooleanReturn, Method method,
            MethodSpec.Builder currentMethodBuilder) {
        // if the overriding method returns anything other than void, add a
        // return statement
        if (method.getReturnType().getName().compareToIgnoreCase("void") != 0) {
            if (method.getReturnType().getName()
                    .compareToIgnoreCase("boolean") == 0) {
                currentMethodBuilder
                        .addCode("return " + valueForBooleanReturn + ";");
            } else {
                currentMethodBuilder.addCode("return null;");
            }
        }
    }

    /**
     * Creates a constructor from the super class.
     *
     * @param superConstructor the Reflected constructor object
     * @param booleanParameter the boolean values to be used in case the
     *                         constructor requires boolean values
     * @return a Constructor type MethodSpec.Builder object
     */
    public static MethodSpec.Builder createDummyConstructorWithSuper(
            Constructor<?> superConstructor, boolean booleanParameter) {

        MethodSpec.Builder resultSpec = MethodSpec.constructorBuilder();
        //get all parameter types of super
        Class<?>[] parameterTypes = superConstructor.getParameterTypes();

        //loop over the parameters
        String callToSuperStatement = "super(";
        callToSuperStatement += MemberReflection
                .getStringForDummyParameters(parameterTypes, booleanParameter);
        callToSuperStatement += ")";
        resultSpec.addStatement(callToSuperStatement);

        return resultSpec;
    }

    public static Iterable<ParameterSpec> getConstructorSpecIterable(
            Constructor<?> constructor) {
        Class<?>[] parameters = constructor.getParameterTypes();
        ArrayList<ParameterSpec> parameterSpecs =
                new ArrayList<ParameterSpec>();
        for (int i = 0; i < parameters.length; i++) {
            Class<?> currentParameter = parameters[i];
            ParameterSpec currentParameterSpec =
                    ParameterSpec
                            .builder(
                                    currentParameter,
                                    "arg" + i).build();
            parameterSpecs.add(currentParameterSpec);
        }
        return parameterSpecs;
    }

    public static MethodSpec.Builder getConstructorMethodSpec(
            String superTypeWithPackageName, boolean constructorBoolean)
            throws ClassNotFoundException {
        //find the constructor of minimal parameter size
        Constructor<?> constructor = ClassReflection
                .getSmallestConstructorWithParameter(superTypeWithPackageName);
        //create a dummy super statement around it
        return createDummyConstructorWithSuper(constructor, constructorBoolean);
    }

    public static MethodSpec.Builder addBogusLoopInOverridingMethodSpec(
            Method baseMethod,
            MethodSpec.Builder methodSpecBuilder,
            Iterable<ParameterSpec> parameterSpecs,
            boolean returnValueBoolean) {
        return methodSpecBuilder;
    }

    public static void addBogusConditionsInOverridingMethodSpec
            (
                    Method baseMethod,
                    MethodSpec.Builder methodSpecBuilder,
                    boolean returnValueBoolean,
                    boolean booleanInCondition) {

        String conditionStatement = "\nif(!(" + booleanInCondition;
        for (int i = 0; i < methodSpecBuilder.parameters.size(); i++) {
            ParameterSpec currentParameterSpec = methodSpecBuilder.parameters
                    .get(i);
            System.out.println(
                    currentParameterSpec.name + ", " + currentParameterSpec.type
                            .toString());
            conditionStatement += "||"
                    + currentParameterSpec.name
                    + MemberReflection
                    .bogusConditionForType(currentParameterSpec.type);

        }
        conditionStatement += ")){\n";
        if (MemberReflection.hasThrows(baseMethod)) {
            conditionStatement += "\tthrow new " + baseMethod
                    .getExceptionTypes()[0].getName() + "();\n";
        }
        conditionStatement += "}\n";
        methodSpecBuilder.addCode(conditionStatement);

    }

    public static void addSpecificConditionsInOverridingMethodSpec
            (
                    MethodSpec.Builder methodSpecBuilder,
                    String specificCondition) {
        // bare minimum way to check and add specific conditions
        if (methodSpecBuilder.parameters.size() > 1) {
            methodSpecBuilder.addCode(specificCondition + "}\n");
        }
    }

    public static void addBogusConditionsInOverridingMethodSpec
            (
                    Method baseMethod,
                    MethodSpec.Builder methodSpecBuilder,
                    boolean returnValueBoolean) {
        addBogusConditionsInOverridingMethodSpec(baseMethod, methodSpecBuilder,
                returnValueBoolean, true);
    }

    public static MethodSpec.Builder addBogusConditionInBogusLoopInOverridingMethodSpec(
            Method baseMethod,
            MethodSpec.Builder methodSpecBuilder,
            boolean returnValueBoolean) {
        return methodSpecBuilder;
    }
}
