// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.google.gson.internal.bind.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601Utils
{
    private static final TimeZone TIMEZONE_UTC;
    private static final String UTC_ID = "UTC";
    
    static {
        TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
    }
    
    private static boolean checkOffset(final String s, final int n, final char c) {
        return n < s.length() && s.charAt(n) == c;
    }
    
    public static String format(final Date date) {
        return format(date, false, ISO8601Utils.TIMEZONE_UTC);
    }
    
    public static String format(final Date date, final boolean b) {
        return format(date, b, ISO8601Utils.TIMEZONE_UTC);
    }
    
    public static String format(final Date time, final boolean b, final TimeZone timeZone) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, Locale.US);
        gregorianCalendar.setTime(time);
        final int length = "yyyy-MM-ddThh:mm:ss".length();
        int length2;
        if (b) {
            length2 = ".sss".length();
        }
        else {
            length2 = 0;
        }
        int n;
        if (timeZone.getRawOffset() == 0) {
            n = "Z".length();
        }
        else {
            n = "+hh:mm".length();
        }
        final StringBuilder sb = new StringBuilder(length + length2 + n);
        padInt(sb, gregorianCalendar.get(1), "yyyy".length());
        sb.append('-');
        padInt(sb, gregorianCalendar.get(2) + 1, "MM".length());
        sb.append('-');
        padInt(sb, gregorianCalendar.get(5), "dd".length());
        sb.append('T');
        padInt(sb, gregorianCalendar.get(11), "hh".length());
        sb.append(':');
        padInt(sb, gregorianCalendar.get(12), "mm".length());
        sb.append(':');
        padInt(sb, gregorianCalendar.get(13), "ss".length());
        if (b) {
            sb.append('.');
            padInt(sb, gregorianCalendar.get(14), "sss".length());
        }
        final int offset = timeZone.getOffset(gregorianCalendar.getTimeInMillis());
        if (offset != 0) {
            final int abs = Math.abs(offset / 60000 / 60);
            final int abs2 = Math.abs(offset / 60000 % 60);
            char c;
            if (offset < 0) {
                c = '-';
            }
            else {
                c = '+';
            }
            sb.append(c);
            padInt(sb, abs, "hh".length());
            sb.append(':');
            padInt(sb, abs2, "mm".length());
        }
        else {
            sb.append('Z');
        }
        return sb.toString();
    }
    
    private static int indexOfNonDigit(final String s, int i) {
        while (i < s.length()) {
            final char char1 = s.charAt(i);
            if (char1 < '0' || char1 > '9') {
                return i;
            }
            ++i;
        }
        return s.length();
    }
    
    private static void padInt(final StringBuilder sb, int i, final int n) {
        final String string = Integer.toString(i);
        for (i = n - string.length(); i > 0; --i) {
            sb.append('0');
        }
        sb.append(string);
    }
    
    public static Date parse(final String s, final ParsePosition parsePosition) throws ParseException {
        try {
            final int index = parsePosition.getIndex();
            final int n = index + 4;
            final int int1 = parseInt(s, index, n);
            int n2 = n;
            if (checkOffset(s, n, '-')) {
                n2 = n + 1;
            }
            final int n3 = n2 + 2;
            final int int2 = parseInt(s, n2, n3);
            if (!checkOffset(s, n3, '-')) {
                goto Label_0945;
            }
            final int n4 = n3 + 1;
            final int index2 = n4 + 2;
            final int int3 = parseInt(s, n4, index2);
            final boolean checkOffset = checkOffset(s, index2, 'T');
            if (!checkOffset && s.length() <= index2) {
                final GregorianCalendar gregorianCalendar = new GregorianCalendar(int1, int2 - 1, int3);
                parsePosition.setIndex(index2);
                return gregorianCalendar.getTime();
            }
            int n5 = index2;
            if (checkOffset) {
                final int n6 = index2 + 1;
                final int n7 = n6 + 2;
                parseInt(s, n6, n7);
                int n8 = n7;
                if (checkOffset(s, n7, ':')) {
                    n8 = n7 + 1;
                }
                final int n9 = n8 + 2;
                parseInt(s, n8, n9);
                if (!checkOffset(s, n9, ':')) {
                    goto Label_0939;
                }
                n5 = n9 + 1;
                if (s.length() <= n5) {
                    goto Label_0920;
                }
                final char char1 = s.charAt(n5);
                if (char1 == 'Z' || char1 == '+' || char1 == '-') {
                    goto Label_0920;
                }
                final int n10 = n5 + 2;
                int int4;
                final int n11 = int4 = parseInt(s, n5, n10);
                if (n11 > 59 && (int4 = n11) < 63) {}
                n5 = n10;
                if (checkOffset(s, n10, '.')) {
                    final int n12 = n10 + 1;
                    final int min = Math.min(indexOfNonDigit(s, n12 + 1), n12 + 3);
                    parseInt(s, n12, min);
                    switch (min - n12) {
                        case 2: {
                            goto Label_0529;
                            goto Label_0529;
                        }
                        case 1: {
                            goto Label_0537;
                            goto Label_0537;
                        }
                        default: {
                            goto Label_0951;
                        }
                    }
                }
            }
            if (s.length() <= n5) {
                throw new IllegalArgumentException("No time zone indicator");
            }
            goto Label_0545;
        }
        catch (IndexOutOfBoundsException ex) {}
        catch (IllegalArgumentException gregorianCalendar) {
            goto Label_0416;
        }
        catch (NumberFormatException gregorianCalendar) {
            goto Label_0416;
        }
    }
    
    private static int parseInt(final String s, final int n, final int n2) throws NumberFormatException {
        if (n < 0 || n2 > s.length() || n > n2) {
            throw new NumberFormatException(s);
        }
        int n3 = 0;
        int i;
        if (n < n2) {
            i = n + 1;
            final int digit = Character.digit(s.charAt(n), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + s.substring(n, n2));
            }
            n3 = -digit;
        }
        else {
            i = n;
        }
        while (i < n2) {
            final int digit2 = Character.digit(s.charAt(i), 10);
            if (digit2 < 0) {
                throw new NumberFormatException("Invalid number: " + s.substring(n, n2));
            }
            n3 = n3 * 10 - digit2;
            ++i;
        }
        return -n3;
    }
}
