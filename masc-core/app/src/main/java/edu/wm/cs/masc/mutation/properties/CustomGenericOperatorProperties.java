package edu.wm.cs.masc.mutation.properties;

import org.apache.commons.configuration2.ex.ConfigurationException;

public class CustomGenericOperatorProperties extends AOperatorProperties{
    public CustomGenericOperatorProperties(String path) throws ConfigurationException {
        super(path);
    }

    public String getAttribute(String key){
        return super.reader.getValueForAKey(key);
    }
}
