package edu.wm.cs.masc.mutation.properties;
/**
 * Operator properties class for operators written by users
 * @author: Yusuf Ahmed
 */

import org.apache.commons.configuration2.ex.ConfigurationException;

public class CustomOperatorProperties extends AOperatorProperties{
    public CustomOperatorProperties(String path) throws ConfigurationException {
        super(path);
    }

    /**
     * Fetches the corresponding value for a key from properties file or from in-memory cache
     * by delegating call to {@link #reader#getAttribute(String)}
     * @param key the key whose value is to be retrieved
     * @return the corresponding value for the key
     */
    public String getAttribute(String key){
        return super.reader.getValueForAKey(key);
    }
}