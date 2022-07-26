package edu.wm.cs.masc.mutation.operators.custom;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.properties.CustomOperatorProperties;

public abstract class ACustomGenericOperator implements IOperator {
    CustomOperatorProperties properties;

    public ACustomGenericOperator(CustomOperatorProperties p){
        this.properties = p;
    }

    public String getAttribute(String key){
        return properties.getAttribute(key);
    }
}
