// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson;

import java.util.Locale;
import java.lang.reflect.Field;

public enum FieldNamingPolicy implements FieldNamingStrategy
{
    IDENTITY {
        @Override
        public String translateName(final Field field) {
            return field.getName();
        }
    }, 
    LOWER_CASE_WITH_DASHES {
        @Override
        public String translateName(final Field field) {
            return FieldNamingPolicy.separateCamelCase(field.getName(), "-").toLowerCase(Locale.ENGLISH);
        }
    }, 
    LOWER_CASE_WITH_UNDERSCORES {
        @Override
        public String translateName(final Field field) {
            return FieldNamingPolicy.separateCamelCase(field.getName(), "_").toLowerCase(Locale.ENGLISH);
        }
    }, 
    UPPER_CAMEL_CASE {
        @Override
        public String translateName(final Field field) {
            return FieldNamingPolicy.upperCaseFirstLetter(field.getName());
        }
    }, 
    UPPER_CAMEL_CASE_WITH_SPACES {
        @Override
        public String translateName(final Field field) {
            return FieldNamingPolicy.upperCaseFirstLetter(FieldNamingPolicy.separateCamelCase(field.getName(), " "));
        }
    };
    
    private static String modifyString(final char c, final String s, final int n) {
        if (n < s.length()) {
            return c + s.substring(n);
        }
        return String.valueOf(c);
    }
    
    static String separateCamelCase(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (Character.isUpperCase(char1) && sb.length() != 0) {
                sb.append(s2);
            }
            sb.append(char1);
        }
        return sb.toString();
    }
    
    static String upperCaseFirstLetter(final String s) {
        final StringBuilder sb = new StringBuilder();
        int n;
        char c;
        for (n = 0, c = s.charAt(0); n < s.length() - 1 && !Character.isLetter(c); ++n, c = s.charAt(n)) {
            sb.append(c);
        }
        String string = s;
        if (!Character.isUpperCase(c)) {
            string = sb.append(modifyString(Character.toUpperCase(c), s, n + 1)).toString();
        }
        return string;
    }
}
