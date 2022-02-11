package edu.wm.cs.masc.mutation.operators.flexible;

import edu.wm.cs.masc.mutation.builders.generic.BuilderTypeSpec;
import edu.wm.cs.masc.mutation.properties.FlexibleOperatorProperties;
import edu.wm.cs.masc.mutation.reflection.MemberReflection;

import java.lang.reflect.Method;

public class AIOSpecific extends AFlexibleOperator {

    public AIOSpecific(
            FlexibleOperatorProperties p) {
        super(p);
        this.setClassNameForClass(p.getOtherClassName());
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
                content.append(BuilderTypeSpec
                        .overriddenMethodBuilderSpecificCondition(
                                p.getBooleanReturn(),
                                p.getSpecificCondition(),
                                method
                        ).build()
                        .toString());
            }
            content.append("\n};");
            return content.toString();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
