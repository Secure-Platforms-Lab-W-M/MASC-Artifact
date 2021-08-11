// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.util;

import java.io.PrintWriter;

public final class TimeUtils
{
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr;
    private static final Object sFormatSync;
    
    static {
        sFormatSync = new Object();
        TimeUtils.sFormatStr = new char[24];
    }
    
    private TimeUtils() {
    }
    
    private static int accumField(final int n, final int n2, final boolean b, final int n3) {
        if (n > 99 || (b && n3 >= 3)) {
            return n2 + 3;
        }
        if (n > 9 || (b && n3 >= 2)) {
            return n2 + 2;
        }
        if (!b && n <= 0) {
            return 0;
        }
        return n2 + 1;
    }
    
    public static void formatDuration(final long n, final long n2, final PrintWriter printWriter) {
        if (n == 0L) {
            printWriter.print("--");
            return;
        }
        formatDuration(n - n2, printWriter, 0);
    }
    
    public static void formatDuration(final long n, final PrintWriter printWriter) {
        formatDuration(n, printWriter, 0);
    }
    
    public static void formatDuration(final long n, final PrintWriter printWriter, int formatDurationLocked) {
        synchronized (TimeUtils.sFormatSync) {
            formatDurationLocked = formatDurationLocked(n, formatDurationLocked);
            printWriter.print(new String(TimeUtils.sFormatStr, 0, formatDurationLocked));
        }
    }
    
    public static void formatDuration(final long n, final StringBuilder sb) {
        synchronized (TimeUtils.sFormatSync) {
            sb.append(TimeUtils.sFormatStr, 0, formatDurationLocked(n, 0));
        }
    }
    
    private static int formatDurationLocked(long n, int printField) {
        if (TimeUtils.sFormatStr.length < printField) {
            TimeUtils.sFormatStr = new char[printField];
        }
        final char[] sFormatStr = TimeUtils.sFormatStr;
        if (n == 0L) {
            while (printField - 1 < 0) {
                sFormatStr[0] = ' ';
            }
            sFormatStr[0] = '0';
            return 0 + 1;
        }
        char c;
        if (n > 0L) {
            c = '+';
        }
        else {
            n = -n;
            c = '-';
        }
        final int n2 = (int)(n % 1000L);
        int n3 = (int)Math.floor((double)(n / 1000L));
        int n4;
        if (n3 > 86400) {
            n4 = n3 / 86400;
            n3 -= 86400 * n4;
        }
        else {
            n4 = 0;
        }
        int n5;
        if (n3 > 3600) {
            n5 = n3 / 3600;
            n3 -= n5 * 3600;
        }
        else {
            n5 = 0;
        }
        int n6;
        int n7;
        if (n3 > 60) {
            n6 = n3 / 60;
            n7 = n3 - n6 * 60;
        }
        else {
            n6 = 0;
            n7 = n3;
        }
        int n8 = 0;
        final int n9 = 0;
        final int n10 = 3;
        boolean b = false;
        if (printField != 0) {
            final int accumField = accumField(n4, 1, false, 0);
            if (accumField > 0) {
                b = true;
            }
            final int n11 = accumField + accumField(n5, 1, b, 2);
            final int n12 = n11 + accumField(n6, 1, n11 > 0, 2);
            final int n13 = n12 + accumField(n7, 1, n12 > 0, 2);
            int n14;
            if (n13 > 0) {
                n14 = 3;
            }
            else {
                n14 = 0;
            }
            int n15 = n13 + (accumField(n2, 2, true, n14) + 1);
            int n16 = n9;
            while (true) {
                n8 = n16;
                if (n15 >= printField) {
                    break;
                }
                sFormatStr[n16] = ' ';
                ++n16;
                ++n15;
            }
        }
        sFormatStr[n8] = c;
        final int n17 = n8 + 1;
        if (printField != 0) {
            printField = 1;
        }
        else {
            printField = 0;
        }
        final boolean b2 = true;
        final int n18 = 2;
        final int printField2 = printField(sFormatStr, n4, 'd', n17, false, 0);
        final boolean b3 = printField2 != n17;
        int n19;
        if (printField != 0) {
            n19 = 2;
        }
        else {
            n19 = 0;
        }
        final int printField3 = printField(sFormatStr, n5, 'h', printField2, b3, n19);
        final boolean b4 = printField3 != n17;
        int n20;
        if (printField != 0) {
            n20 = 2;
        }
        else {
            n20 = 0;
        }
        final int printField4 = printField(sFormatStr, n6, 'm', printField3, b4, n20);
        final boolean b5 = printField4 != n17 && b2;
        int n21;
        if (printField != 0) {
            n21 = n18;
        }
        else {
            n21 = 0;
        }
        final int printField5 = printField(sFormatStr, n7, 's', printField4, b5, n21);
        if (printField != 0 && printField5 != n17) {
            printField = n10;
        }
        else {
            printField = 0;
        }
        printField = printField(sFormatStr, n2, 'm', printField5, true, printField);
        sFormatStr[printField] = 's';
        return printField + 1;
    }
    
    private static int printField(final char[] array, int n, final char c, int n2, final boolean b, int n3) {
        if (!b) {
            final int n4 = n2;
            if (n <= 0) {
                return n4;
            }
        }
        int n5 = 0;
        int n6 = 0;
        Label_0064: {
            if (!b || n3 < 3) {
                n5 = n;
                n6 = n2;
                if (n <= 99) {
                    break Label_0064;
                }
            }
            final int n7 = n / 100;
            array[n2] = (char)(n7 + 48);
            n6 = n2 + 1;
            n5 = n - n7 * 100;
        }
        Label_0124: {
            if ((!b || n3 < 2) && n5 <= 9) {
                n3 = n5;
                if (n2 == (n = n6)) {
                    break Label_0124;
                }
            }
            n2 = n5 / 10;
            array[n6] = (char)(n2 + 48);
            n = n6 + 1;
            n3 = n5 - n2 * 10;
        }
        array[n] = (char)(n3 + 48);
        ++n;
        array[n] = c;
        return n + 1;
    }
}
