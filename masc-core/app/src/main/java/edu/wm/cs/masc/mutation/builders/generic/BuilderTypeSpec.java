package edu.wm.cs.masc.mutation.builders.generic;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import edu.wm.cs.masc.mutation.reflection.MemberReflection;

import java.lang.reflect.Method;

public class BuilderTypeSpec {

    public static void addOverridingMethodsInTypeSpec(
            boolean valueForBooleanReturn,
            TypeSpec.Builder resultSpec,
            MemberReflection member,
            boolean addBogusCondition) {
        //loop over all methods of the abstract super type
        for (Method method : member.getEligibleMethods()) {
            MethodSpec.Builder currentMethodBuilder = overriddenMethodBuilder(
                    valueForBooleanReturn, addBogusCondition, method);
            resultSpec.addMethod(currentMethodBuilder.build());
        }
    }

    public static MethodSpec.Builder overriddenMethodBuilder(
            boolean valueForBooleanReturn, boolean addBogusCondition,
            boolean booleanInCondition,
            Method method) {
        MethodSpec.Builder currentMethodBuilder =
                BuilderMethodSpec.getOverridingPublicAbstractMethod(method);

        // if the method throws exception, add throws statement for
        // overriding method
        BuilderMethodSpec
                .addExceptionsToOverridingMethodSpec(method,
                        currentMethodBuilder);

        Iterable<ParameterSpec> parameterSpecs = BuilderParameterSpec
                .getParameterSpecIterator(method);
        currentMethodBuilder.addParameters(parameterSpecs);
        if (addBogusCondition) {
            BuilderMethodSpec
                    .addBogusConditionsInOverridingMethodSpec(
                            method,
                            currentMethodBuilder,
                            valueForBooleanReturn, booleanInCondition);
        }
        // if the overriding method returns anything other than void, add a
        // return statement
        BuilderMethodSpec
                .addReturnValueToOverridingMethodSpec(
                        valueForBooleanReturn,
                        method,
                        currentMethodBuilder);
        return currentMethodBuilder;
    }

    public static MethodSpec.Builder overriddenMethodBuilderSpecificCondition(
            boolean valueForBooleanReturn,
            String specificCondition,
            Method method) {
        MethodSpec.Builder currentMethodBuilder =
                BuilderMethodSpec.getOverridingPublicAbstractMethod(method);

        // if the method throws exception, add throws statement for
        // overriding method
        BuilderMethodSpec
                .addExceptionsToOverridingMethodSpec(method,
                        currentMethodBuilder);

        Iterable<ParameterSpec> parameterSpecs = BuilderParameterSpec
                .getParameterSpecIterator(method);
        currentMethodBuilder.addParameters(parameterSpecs);
        BuilderMethodSpec.addSpecificConditionsInOverridingMethodSpec(
                currentMethodBuilder,
                specificCondition);
        // if the overriding method returns anything other than void, add a
        // return statement
        BuilderMethodSpec
                .addReturnValueToOverridingMethodSpec(
                        valueForBooleanReturn,
                        method,
                        currentMethodBuilder);
        return currentMethodBuilder;
    }

    public static MethodSpec.Builder overriddenMethodBuilder(
            boolean valueForBooleanReturn, boolean addBogusCondition,
            Method method) {
        //create overriding methods for each method
        return overriddenMethodBuilder(valueForBooleanReturn, addBogusCondition,
                true,
                method);
    }

    public static void addOverridingMethodsInTypeSpec(
            boolean valueForBooleanReturn,
            TypeSpec.Builder resultSpec,
            MemberReflection member) {
        //loop over all methods of the abstract super type
        addOverridingMethodsInTypeSpec(
                valueForBooleanReturn,
                resultSpec,
                member,
                false);
    }
}
