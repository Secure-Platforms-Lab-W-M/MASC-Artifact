package masc.edu.wm.cs.masc.barebone.mutationmakers;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainClass;
import masc.edu.wm.cs.masc.builders.generic.BuilderMainMethod;
import masc.edu.wm.cs.masc.builders.restrictive.BuilderInterprocClass;
import masc.edu.wm.cs.masc.operator.OperatorType;
import masc.edu.wm.cs.masc.operator.restrictive.interprocoperator.InterProcOperator;
import masc.edu.wm.cs.masc.properties.InterprocProperties;
import masc.edu.wm.cs.masc.utility.FilePack;

import java.util.ArrayList;

public class InterprocMutationMaker extends AMultiClassMutationMaker {
    InterprocProperties p;

    public InterprocMutationMaker(InterprocProperties p) {
        this.p = p;
        String otherClass = p.getOtherClassName();
        FilePack filePack = new FilePack(otherClass, p.getOutputDir(),
                BuilderInterprocClass.getInterprocClassString(p));
        ArrayList<FilePack> filePacks = new ArrayList<>();
        filePacks.add(filePack);
        this.setFilepacks(filePacks);
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

    @Override
    public void populateOperators() {
        operators.put(OperatorType.Interproc, new InterProcOperator(p));
    }

}
