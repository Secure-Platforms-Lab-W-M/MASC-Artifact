package edu.wm.cs.masc.mutation.operators.custom;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.properties.CustomGenericOperatorProperties;

public abstract class ACustomGenericOperator implements IOperator {
    CustomGenericOperatorProperties properties;

    public ACustomGenericOperator(CustomGenericOperatorProperties p){
        this.properties = p;
    }

    public String getAttribute(String key){
        return properties.getAttribute(key);
    }
}
