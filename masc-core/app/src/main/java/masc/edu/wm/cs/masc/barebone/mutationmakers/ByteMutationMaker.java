package masc.edu.wm.cs.masc.barebone.mutationmakers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainClass;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainMethod;
import masc.edu.wm.cs.masc.operator.OperatorType;
import masc.edu.wm.cs.masc.operator.restrictive.byteoperator.CurrentTime;
import masc.edu.wm.cs.masc.properties.ByteOperatorProperties;

public class ByteMutationMaker extends AMutationMaker {

    ByteOperatorProperties p;

    public ByteMutationMaker(ByteOperatorProperties p) {
        this.p = p;
    }

    @Override
    public String getContent(OperatorType operatorType) {
        TypeSpec.Builder builder = BuilderMainClass
                .getClassBody(p.getClassName());
        System.out.println("Processing: " + operatorType.name());
        MethodSpec.Builder mainMethod = BuilderMainMethod
                .getMethodSpec();
        mainMethod.addCode(operators.get(operatorType).mutation());
        builder.addMethod(mainMethod.build());
        return builder.build().toString();
    }

    @Override
    public void populateOperators() {
        operators.put(OperatorType.ByteCurrentTime, new CurrentTime(p));
        operators.put(OperatorType.ByteLoop, new CurrentTime(p));
    }
}
