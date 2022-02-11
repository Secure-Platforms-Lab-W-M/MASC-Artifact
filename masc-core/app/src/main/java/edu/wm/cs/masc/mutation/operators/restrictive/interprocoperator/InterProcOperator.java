package edu.wm.cs.masc.mutation.operators.restrictive.interprocoperator;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;

public class InterProcOperator implements IOperator {

    private final InterprocProperties p;

    public InterProcOperator(InterprocProperties p) {
        this.p = p;
    }

    public String insecure_call() {
        return String
                .format("%1$s %2$s = " +
                                "%1$s.%3$s(new %4$s().A().B().get%5$s());" +
                                "\n",
                        p.getApiName(),
                        p.getVariableName(),
                        p.getInvocation(),
                        p.getOtherClassName(),
                        p.getPropertyName());
    }

    public String insecure_call_trycatch() {
        return "try {\n" +
                this.insecure_call() +
                "System.out.println(" + p.getVariableName() +
                ".getAlgorithm());\n" +
                "} catch (Exception e) {\n" +
                "System.out.println(\"Error\");\n" +
                "}";
    }

    @Override
    public String mutation() {
        if (p.getTry_catch()) {
            return insecure_call_trycatch();
        } else
            return insecure_call();
    }
}
