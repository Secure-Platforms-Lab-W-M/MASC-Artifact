package masc.edu.wm.cs.masc.operator.flexible;

import masc.edu.wm.cs.masc.properties.FlexibleOperatorProperties;


/**
 * This is empty anonymous inner class object based mutation.
 * Here, the mutation returns the body of the anonymous inner class object,
 * with overridden methods containing empty bodies. Return type depends on
 * configuration value
 */
public class AIOEmptyFromAbstractType extends AFlexibleOperator {
    public AIOEmptyFromAbstractType(FlexibleOperatorProperties p) {
        super(p);
        this.setClassNameForClass(p.getApiName());
    }

//    @Override
//    public String mutation() {
//        StringBuilder content = new StringBuilder("\nnew ");
//        content.append(this.apiName).append("(){\n");
//        MemberReflection member = null;
//        try {
//            member = new MemberReflection(
//                    this.apiName);
//            for (Method method : member.getEligibleMethods()) {
//                String methodSignature = MemberReflection
//                        .getGeneratedMethod(method, this.booleanReturn);
//                content.append(methodSignature).append("\n");
//            }
//            content.append("\n};");
//            return content.toString();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }
}
