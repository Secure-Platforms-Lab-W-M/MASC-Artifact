// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

public final class Utf8
{
    private Utf8() {
    }
    
    public static long size(final String s) {
        return size(s, 0, s.length());
    }
    
    public static long size(final String s, int i, final int n) {
        if (s == null) {
            throw new IllegalArgumentException("string == null");
        }
        if (i < 0) {
            throw new IllegalArgumentException("beginIndex < 0: " + i);
        }
        if (n < i) {
            throw new IllegalArgumentException("endIndex < beginIndex: " + n + " < " + i);
        }
        if (n > s.length()) {
            throw new IllegalArgumentException("endIndex > string.length: " + n + " > " + s.length());
        }
        long n2 = 0L;
        while (i < n) {
            final char char1 = s.charAt(i);
            if (char1 < '\u0080') {
                ++n2;
                ++i;
            }
            else if (char1 < '\u0800') {
                n2 += 2L;
                ++i;
            }
            else if (char1 < '\ud800' || char1 > '\udfff') {
                n2 += 3L;
                ++i;
            }
            else {
                char char2;
                if (i + 1 < n) {
                    char2 = s.charAt(i + 1);
                }
                else {
                    char2 = '\0';
                }
                if (char1 > '\udbff' || char2 < '\udc00' || char2 > '\udfff') {
                    ++n2;
                    ++i;
                }
                else {
                    n2 += 4L;
                    i += 2;
                }
            }
        }
        return n2;
    }
}
