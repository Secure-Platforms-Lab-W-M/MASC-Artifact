package edu.wm.cs.masc.utils.config;

import org.apache.commons.configuration2.Configuration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class ConfigurationProxy {
    Configuration configuration;
    static HashMap<String, String> map = new HashMap<>();
    Scanner scanner = new Scanner(System.in);

    public ConfigurationProxy(Configuration configuration) {
        this.configuration = configuration;
    }

    public Iterator<String> getKeys() {
        return configuration.getKeys();
    }

    public String getString(String key) {
        String value =  configuration.getString(key);
        if(value == null) {
            if(map.containsKey(key))
                return map.get(key);

            System.out.println("Key '" + key + "' not found in properties file. Enter value:");
            value = scanner.nextLine();
            map.put(key, value);
        }

        return value;
    }

    public String[] getStringArray(String key) {
        return configuration.getStringArray(key);
    }
}
