package edu.wm.cs.masc.mainScope.mutationmakers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import edu.wm.cs.masc.mutation.operators.restrictive.intoperator.*;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainClass;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainMethod;
import edu.wm.cs.masc.mutation.operators.OperatorType;
//import masc.edu.wm.cs.mascDeprecated.operator.restrictive.intoperator.*;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;

public class IntMutationMaker extends AMutationMaker{

    IntOperatorProperties p;

    public IntMutationMaker(IntOperatorProperties p) {
        this.p = p;
    }

    @Override
    public void populateOperators() {
        operators.put(OperatorType.IntValueInVariable, new ValueInVariable(p));
        operators.put(OperatorType.IntArithmetic, new Arithmetic(p));
        operators.put(OperatorType.IntValueInVariableArithmetic, new ValueInVariableArithmetic(p));
        operators.put(OperatorType.IntIterationMultipleCall, new IterationMultipleCall(p));
        operators.put(OperatorType.IntWhileLoopAccumulation, new WhileLoopAccumulation(p));
        operators.put(OperatorType.IntRoundValue, new RoundValue(p));
        operators.put(OperatorType.IntAbsoluteValue, new AbsoluteValue(p));
        operators.put(OperatorType.IntNestedClass, new NestedClass(p));
        operators.put(OperatorType.IntFromString, new FromString(p));
        operators.put(OperatorType.Overflow, new Overflow(p));
    }

    @Override
    public String getContent(OperatorType operatorType) {
        TypeSpec.Builder builder = BuilderMainClass
                .getClassBody(p.getClassName());
        System.out.println("Processing: " + operatorType.name());
        MethodSpec.Builder mainMethod = BuilderMainMethod
                .getMethodSpecWithException();
        mainMethod.addCode(operators.get(operatorType).mutation());
        builder.addMethod(mainMethod.build());
        return builder.build().toString();
    }
}
