package edu.wm.cs.masc.mutation.operators.custom;

/**
 * Users can extend this class if they wish to write their own custom operators other than the five predefined operator types,
 * @author: Yusuf Ahmed
 */

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.properties.CustomOperatorProperties;

public abstract class ACustomGenericOperator implements IOperator {
    CustomOperatorProperties properties;

    public ACustomGenericOperator(CustomOperatorProperties p){
        this.properties = p;
    }

    /**
     * Fetches the corresponding value for a key from properties file or from in-memory cache
     * by delegating call to {@link CustomOperatorProperties#getAttribute(String)}
     * @param key the key whose value is to be retrieved
     * @return the corresponding value for the key
     */
    public String getAttribute(String key){
        return properties.getAttribute(key);
    }
}