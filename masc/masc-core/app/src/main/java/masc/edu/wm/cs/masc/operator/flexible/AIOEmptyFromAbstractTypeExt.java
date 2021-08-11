package masc.edu.wm.cs.masc.operator.flexible;//package edu.wm.cs.masc.operator.flexible;
//
//import masc.edu.wm.cs.masc.properties.FlexibleOperatorProperties;
//import masc.edu.wm.cs.masc.reflection.MemberReflection;
//
//import java.lang.reflect.Method;
//
///**
// * Similar to @{@link AIOEmptyFromAbstractType}, but it creates an
// * abstraction over the extension first, and then creates an anonymous inner
// * class object.
// * Abstraction over the extension comes in two ways,
// *
// */
//public class AIOEmptyFromAbstractTypeExt extends AFlexibleOperator {
//    public AIOEmptyFromAbstractTypeExt(FlexibleOperatorProperties p) {
//        super(p);
//    }
//
//    @Override
//    public String mutation() {
//        StringBuilder content = new StringBuilder("\nnew ");
//        content.append(this.otherClassName).append("(){\n");
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
//}
