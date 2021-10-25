package masc.edu.wm.cs.masc.barebone.mutationmakers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainClass;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainMethod;
import masc.edu.wm.cs.masc.operator.OperatorType;
import masc.edu.wm.cs.masc.operator.restrictive.stringoperator.*;
import masc.edu.wm.cs.masc.properties.StringOperatorProperties;

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
