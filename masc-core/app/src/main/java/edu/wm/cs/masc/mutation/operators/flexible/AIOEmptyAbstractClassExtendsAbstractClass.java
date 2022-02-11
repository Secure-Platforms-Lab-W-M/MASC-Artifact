package edu.wm.cs.masc.mutation.operators.flexible;

import edu.wm.cs.masc.mutation.properties.FlexibleOperatorProperties;
import edu.wm.cs.masc.mutation.reflection.MemberReflection;

import java.lang.reflect.Method;

public class AIOEmptyAbstractClassExtendsAbstractClass
        extends AFlexibleOperator {

    public AIOEmptyAbstractClassExtendsAbstractClass(
            FlexibleOperatorProperties p) {
        super(p);
    }

    @Override
    public String mutation() {
        StringBuilder content = new StringBuilder("\nnew ");
        content.append(this.otherClassName).append("(){\n");
        MemberReflection member = null;
        try {
            member = new MemberReflection(
                    this.apiName);
            for (Method method : member.getEligibleMethods()) {
                String methodSignature = MemberReflection
                        .getGeneratedMethod(method, this.booleanReturn);
                content.append(methodSignature).append("\n");
            }
            content.append("\n};");
            return content.toString();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
