// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.jacoco.agent.rt.internal_8ff85ea.core.runtime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class CommandLineSupport
{
    private static final char BLANK = ' ';
    private static final int M_ESCAPED = 2;
    private static final int M_PARSEARGUMENT = 1;
    private static final int M_STRIPWHITESPACE = 0;
    private static final char QUOTE = '\"';
    private static final char SLASH = '\\';
    
    private CommandLineSupport() {
    }
    
    private static void addArgument(final List<String> list, final StringBuilder sb) {
        if (sb.length() > 0) {
            list.add(sb.toString());
            sb.setLength(0);
        }
    }
    
    static String quote(final String s) {
        final StringBuilder sb = new StringBuilder();
        final char[] charArray = s.toCharArray();
        for (int length = charArray.length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            if (c == '\"' || c == '\\') {
                sb.append('\\');
            }
            sb.append(c);
        }
        if (s.indexOf(32) != -1 || s.indexOf(34) != -1) {
            sb.insert(0, '\"').append('\"');
        }
        return sb.toString();
    }
    
    static String quote(final List<String> list) {
        final StringBuilder sb = new StringBuilder();
        int n = 0;
        for (final String s : list) {
            if (n != 0) {
                sb.append(' ');
            }
            sb.append(quote(s));
            n = 1;
        }
        return sb.toString();
    }
    
    static List<String> split(final String s) {
        if (s != null && s.length() != 0) {
            final ArrayList<String> list = new ArrayList<String>();
            final StringBuilder sb = new StringBuilder();
            int n = 0;
            char c = ' ';
            final char[] charArray = s.toCharArray();
            for (int length = charArray.length, i = 0; i < length; ++i) {
                final char c2 = charArray[i];
                switch (n) {
                    case 2: {
                        if (c2 != '\"' && c2 != '\\') {
                            if (c2 == c) {
                                addArgument(list, sb);
                            }
                            else {
                                sb.append(c2);
                            }
                        }
                        else {
                            sb.setCharAt(sb.length() - 1, c2);
                        }
                        n = 1;
                        break;
                    }
                    case 1: {
                        if (c2 == c) {
                            addArgument(list, sb);
                            n = 0;
                            break;
                        }
                        if (c2 == '\\') {
                            sb.append('\\');
                            n = 2;
                            break;
                        }
                        sb.append(c2);
                        break;
                    }
                    case 0: {
                        if (!Character.isWhitespace(c2)) {
                            char c3;
                            if (c2 == '\"') {
                                c3 = '\"';
                            }
                            else {
                                sb.append(c2);
                                c3 = ' ';
                            }
                            final boolean b = true;
                            c = c3;
                            n = (b ? 1 : 0);
                            break;
                        }
                        break;
                    }
                }
            }
            addArgument(list, sb);
            return list;
        }
        return new ArrayList<String>();
    }
}
