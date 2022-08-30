package edu.wm.cs.masc.utils.config;

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

    public String getString(String key) {
        String value =  configuration.getString(key);
        if(value == null) {
            if(map.containsKey(key))
                return map.get(key);

            value = inputValueAndUpdateMap(key);
        }

        return value;
    }

    private String inputValueAndUpdateMap(String key) {
        String value;
        System.out.println("Key '" + key + "' not found in properties file. Enter value:");
        value = scanner.nextLine();
        map.put(key, value);
        return value;
    }

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