// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.runtime;

import java.util.regex.Pattern;

public class WildcardMatcher
{
    private final Pattern pattern;
    
    public WildcardMatcher(final String s) {
        final String[] split = s.split("\\:");
        final StringBuilder sb = new StringBuilder(s.length() * 2);
        int n = 0;
        for (int length = split.length, i = 0; i < length; ++i) {
            final String s2 = split[i];
            if (n != 0) {
                sb.append('|');
            }
            sb.append('(');
            sb.append(toRegex(s2));
            sb.append(')');
            n = 1;
        }
        this.pattern = Pattern.compile(sb.toString());
    }
    
    private static CharSequence toRegex(final String s) {
        final StringBuilder sb = new StringBuilder(s.length() * 2);
        final char[] charArray = s.toCharArray();
        for (int length = charArray.length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            if (c != '*') {
                if (c != '?') {
                    sb.append(Pattern.quote(String.valueOf(c)));
                }
                else {
                    sb.append(".?");
                }
            }
            else {
                sb.append(".*");
            }
        }
        return sb;
    }
    
    public boolean matches(final String s) {
        return this.pattern.matcher(s).matches();
    }
}
