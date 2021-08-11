// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

final class ConfigLoader
{
    private static final Pattern SUBST_PATTERN;
    private static final String SYS_PREFIX = "jacoco-agent.";
    
    static {
        SUBST_PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}");
    }
    
    private ConfigLoader() {
    }
    
    static Properties load(final String s, final Properties properties) {
        final Properties properties2 = new Properties();
        loadResource(s, properties2);
        loadSystemProperties(properties, properties2);
        substSystemProperties(properties2, properties);
        return properties2;
    }
    
    private static void loadResource(final String s, final Properties properties) {
        final InputStream resourceAsStream = Offline.class.getResourceAsStream(s);
        if (resourceAsStream != null) {
            try {
                properties.load(resourceAsStream);
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    private static void loadSystemProperties(final Properties properties, final Properties properties2) {
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            final String string = entry.getKey().toString();
            if (string.startsWith("jacoco-agent.")) {
                ((Hashtable<String, Object>)properties2).put(string.substring("jacoco-agent.".length()), entry.getValue());
            }
        }
    }
    
    private static void substSystemProperties(final Properties properties, final Properties properties2) {
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            final String s = entry.getValue();
            final StringBuilder sb = new StringBuilder();
            final Matcher matcher = ConfigLoader.SUBST_PATTERN.matcher(s);
            int end = 0;
            while (matcher.find()) {
                sb.append(s.substring(end, matcher.start()));
                String s2 = properties2.getProperty(matcher.group(1));
                if (s2 == null) {
                    s2 = matcher.group(0);
                }
                sb.append(s2);
                end = matcher.end();
            }
            sb.append(s.substring(end));
            entry.setValue(sb.toString());
        }
    }
}
