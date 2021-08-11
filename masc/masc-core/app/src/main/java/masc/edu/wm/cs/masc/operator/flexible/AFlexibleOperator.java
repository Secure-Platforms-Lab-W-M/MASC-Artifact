package masc.edu.wm.cs.masc.operator.flexible;

import masc.edu.wm.cs.masc.operator.IOperator;
import masc.edu.wm.cs.masc.properties.FlexibleOperatorProperties;
import masc.edu.wm.cs.masc.reflection.MemberReflection;

import java.lang.reflect.Method;

public abstract class AFlexibleOperator implements IOperator {
    protected final String apiName;
    protected final String className;
    protected boolean booleanReturn;
    protected final String otherClassName;
    protected String classNameForClass;
    FlexibleOperatorProperties p;
    protected String specificCondition;

    public AFlexibleOperator(FlexibleOperatorProperties p) {
        this.apiName = p.getApiName();
        this.className = p.getClassName();
        this.booleanReturn = p.getBooleanReturn();
        this.otherClassName = p.getOtherClassName();
        this.p = p;
        this.specificCondition = p.getSpecificCondition();
    }

    protected void setClassNameForClass(String className) {
        this.classNameForClass = className;
    }

    @Override
    public String mutation() {
        StringBuilder content = new StringBuilder("\nnew ");
        if (classNameForClass == null) {
            throw new IllegalStateException(
                    "You must call setClassNameForClass to assign the " +
                            "classname to be used for creating the mutation " +
                            "for " + p.getType());
        }
        content.append(this.classNameForClass).append("(){\n");
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
