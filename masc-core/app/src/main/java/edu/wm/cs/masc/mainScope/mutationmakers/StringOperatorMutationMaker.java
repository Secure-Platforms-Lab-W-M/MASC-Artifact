package edu.wm.cs.masc.mainScope.mutationmakers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import edu.wm.cs.masc.mutation.operators.restrictive.stringoperator.*;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainClass;
import edu.wm.cs.masc.mutation.builders.generic.BuilderMainMethod;
import edu.wm.cs.masc.mutation.operators.OperatorType;
//import masc.edu.wm.cs.mascDeprecated.operator.restrictive.stringoperator.*;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

public class StringOperatorMutationMaker extends AMutationMaker {

    StringOperatorProperties p;

    @Override
    public void populateOperators() {
        operators.put(OperatorType.StringDifferentCase, new DifferentCase(p));
        operators.put(OperatorType.StringNoiseReplace, new NoiseReplace(p));
        operators.put(OperatorType.StringSafeReplaceWithUnsafe,
                new SafeReplaceWithUnsafe(p));
        operators.put(OperatorType.StringUnsafeReplaceWithUnsafe,
                new UnsafeReplaceWithUnsafe(p));
        operators.put(OperatorType.StringStringCaseTransform,
                new StringCaseTransform(p));
        operators.put(OperatorType.StringValueInVariable,
                new ValueInVariable(p));
    }

    public StringOperatorMutationMaker(StringOperatorProperties p) {
        this.p = p;
    }

}
