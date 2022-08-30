package edu.wm.cs.masc.utils.config;

/**
 * This class acts as a proxy for {@link org.apache.commons.configuration2.Configuration}, which has been used here to retrieve values form properties file.
 * The intention for this class is to cache for values not found in properties file. This class maintains the cache.
 * @author: Yusuf Ahmed
 */

import org.apache.commons.configuration2.Configuration;

import java.util.*;

public class ConfigurationProxy {
    Configuration configuration;
    static HashMap<String, String> map = new HashMap<>();
    Scanner scanner = new Scanner(System.in);

    public ConfigurationProxy(Configuration configuration) {
        this.configuration = configuration;
    }

    public Iterator<String> getKeys() {
        // should I do the same here?
        return configuration.getKeys();
    }

    /**
     * Used to fetch the corresponding value for the given key.
     * If they key is found in the properties file, the matching value will be returned.
     * In case the key is not present in the properties file and the key will be searched in the in-memory cache.
     * Else the user will be asked for input which will be cached.
     * @param key the key whose value is to be retrieved
     * @return the corresponding value for the key
     */
    public String getString(String key) {
        String value =  configuration.getString(key);
        if(value == null) {
            if(map.containsKey(key))
                return map.get(key);

            value = inputValueAndUpdateMap(key);
        }

        return value;
    }

    /**
     * Takes input from user and updates the in-memory cache for the given key.
     * @param key the key against which the user input is to be cached in memory.
     * @return value input by the user.
     */
    private String inputValueAndUpdateMap(String key) {
        String value;
        System.out.println("Key '" + key + "' not found in properties file. Enter value:");
        value = scanner.nextLine();
        map.put(key, value);
        return value;
    }

    /**
     * returns an array of values for a corresponding key from properties file or from in-memory cache.
     * @param key the key whose corresponding values are to be retrieved.
     * @return an array of values mapped against key
     */
    public String[] getStringArray(String key) {
        String[] resultArr = configuration.getStringArray(key);
        if(resultArr.length != 0)
            return resultArr;

        if(!map.containsKey(key))
            inputValueAndUpdateMap(key);

        ArrayList<String> resultArrayList = new ArrayList<>(Arrays.asList(resultArr));
        resultArrayList.add(map.get(key));

        return (String[]) resultArrayList.toArray();
    }
}