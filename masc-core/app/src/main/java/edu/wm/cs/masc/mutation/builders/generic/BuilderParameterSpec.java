package edu.wm.cs.masc.mutation.builders.generic;

import com.squareup.javapoet.ParameterSpec;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class BuilderParameterSpec {
    public static Iterable<ParameterSpec> getParameterSpecIterator(Method method) {
        Class<?>[] parameters = method.getParameterTypes();
        ArrayList<ParameterSpec> parameterSpecs = new ArrayList<ParameterSpec>();
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
}
