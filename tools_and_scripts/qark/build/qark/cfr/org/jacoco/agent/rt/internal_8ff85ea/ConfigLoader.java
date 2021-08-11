/*
 * Decompiled with CFR 0_124.
 */
package org.jacoco.agent.rt.internal_8ff85ea;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jacoco.agent.rt.internal_8ff85ea.Offline;

final class ConfigLoader {
    private static final Pattern SUBST_PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");
    private static final String SYS_PREFIX = "jacoco-agent.";

    private ConfigLoader() {
    }

    static Properties load(String string2, Properties properties) {
        Properties properties2 = new Properties();
        ConfigLoader.loadResource(string2, properties2);
        ConfigLoader.loadSystemProperties(properties, properties2);
        ConfigLoader.substSystemProperties(properties2, properties);
        return properties2;
    }

    private static void loadResource(String object, Properties properties) {
        if ((object = Offline.class.getResourceAsStream((String)object)) != null) {
            try {
                properties.load((InputStream)object);
                return;
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }
    }

    private static void loadSystemProperties(Properties object, Properties properties) {
        for (Map.Entry entry : object.entrySet()) {
            String string2 = entry.getKey().toString();
            if (!string2.startsWith("jacoco-agent.")) continue;
            properties.put(string2.substring("jacoco-agent.".length()), entry.getValue());
        }
    }

    private static void substSystemProperties(Properties object, Properties properties) {
        for (Map.Entry entry : object.entrySet()) {
            String string2 = (String)entry.getValue();
            StringBuilder stringBuilder = new StringBuilder();
            Matcher matcher = SUBST_PATTERN.matcher(string2);
            int n = 0;
            while (matcher.find()) {
                stringBuilder.append(string2.substring(n, matcher.start()));
                object = properties.getProperty(matcher.group(1));
                if (object == null) {
                    object = matcher.group(0);
                }
                stringBuilder.append((String)object);
                n = matcher.end();
            }
            stringBuilder.append(string2.substring(n));
            entry.setValue(stringBuilder.toString());
        }
    }
}

