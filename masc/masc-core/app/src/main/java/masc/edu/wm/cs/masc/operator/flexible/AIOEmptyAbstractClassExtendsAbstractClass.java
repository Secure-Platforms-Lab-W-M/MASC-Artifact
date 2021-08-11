package masc.edu.wm.cs.masc.operator.flexible;

import masc.edu.wm.cs.masc.properties.FlexibleOperatorProperties;
import masc.edu.wm.cs.masc.reflection.MemberReflection;

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
