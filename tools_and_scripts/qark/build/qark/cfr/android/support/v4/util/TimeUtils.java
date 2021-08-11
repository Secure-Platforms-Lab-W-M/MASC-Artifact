/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.util;

import android.support.annotation.RestrictTo;
import java.io.PrintWriter;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public final class TimeUtils {
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr;
    private static final Object sFormatSync;

    static {
        sFormatSync = new Object();
        sFormatStr = new char[24];
    }

    private TimeUtils() {
    }

    private static int accumField(int n, int n2, boolean bl, int n3) {
        if (!(n > 99 || bl && n3 >= 3)) {
            if (!(n > 9 || bl && n3 >= 2)) {
                if (!bl && n <= 0) {
                    return 0;
                }
                return n2 + 1;
            }
            return n2 + 2;
        }
        return n2 + 3;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void formatDuration(long l, long l2, PrintWriter printWriter) {
        if (l == 0L) {
            printWriter.print("--");
            return;
        }
        TimeUtils.formatDuration(l - l2, printWriter, 0);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void formatDuration(long l, PrintWriter printWriter) {
        TimeUtils.formatDuration(l, printWriter, 0);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void formatDuration(long l, PrintWriter printWriter, int n) {
        Object object = sFormatSync;
        synchronized (object) {
            n = TimeUtils.formatDurationLocked(l, n);
            printWriter.print(new String(sFormatStr, 0, n));
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static void formatDuration(long l, StringBuilder stringBuilder) {
        Object object = sFormatSync;
        synchronized (object) {
            int n = TimeUtils.formatDurationLocked(l, 0);
            stringBuilder.append(sFormatStr, 0, n);
            return;
        }
    }

    private static int formatDurationLocked(long l, int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        if (sFormatStr.length < n) {
            sFormatStr = new char[n];
        }
        char[] arrc = sFormatStr;
        if (l == 0L) {
            while (n - 1 < 0) {
                arrc[0] = 32;
            }
            arrc[0] = 48;
            return 0 + 1;
        }
        if (l > 0L) {
            n6 = 43;
        } else {
            l = - l;
            n6 = 45;
        }
        int n8 = (int)(l % 1000L);
        int n9 = (int)Math.floor(l / 1000L);
        if (n9 > 86400) {
            n5 = n9 / 86400;
            n9 -= 86400 * n5;
        } else {
            n5 = 0;
        }
        if (n9 > 3600) {
            n2 = n9 / 3600;
            n9 -= n2 * 3600;
        } else {
            n2 = 0;
        }
        if (n9 > 60) {
            n7 = n9 / 60;
            n3 = n9 - n7 * 60;
        } else {
            n7 = 0;
            n3 = n9;
        }
        n9 = 0;
        int n10 = 0;
        int n11 = 3;
        boolean bl = false;
        if (n != 0) {
            n9 = TimeUtils.accumField(n5, 1, false, 0);
            if (n9 > 0) {
                bl = true;
            }
            bl = (n9 += TimeUtils.accumField(n2, 1, bl, 2)) > 0;
            bl = (n9 += TimeUtils.accumField(n7, 1, bl, 2)) > 0;
            n4 = n9 + TimeUtils.accumField(n3, 1, bl, 2);
            n9 = n4 > 0 ? 3 : 0;
            n4 += TimeUtils.accumField(n8, 2, true, n9) + 1;
            n9 = n10;
            while (n4 < n) {
                arrc[n9] = 32;
                ++n9;
                ++n4;
            }
        }
        arrc[n9] = n6;
        n10 = n9 + 1;
        n = n != 0 ? 1 : 0;
        boolean bl2 = true;
        n4 = 2;
        n5 = TimeUtils.printField(arrc, n5, 'd', n10, false, 0);
        bl = n5 != n10;
        n9 = n != 0 ? 2 : 0;
        n5 = TimeUtils.printField(arrc, n2, 'h', n5, bl, n9);
        bl = n5 != n10;
        n9 = n != 0 ? 2 : 0;
        n5 = TimeUtils.printField(arrc, n7, 'm', n5, bl, n9);
        bl = n5 != n10 ? bl2 : false;
        n9 = n != 0 ? n4 : 0;
        n9 = TimeUtils.printField(arrc, n3, 's', n5, bl, n9);
        n = n != 0 && n9 != n10 ? n11 : 0;
        n = TimeUtils.printField(arrc, n8, 'm', n9, true, n);
        arrc[n] = 115;
        return n + 1;
    }

    private static int printField(char[] arrc, int n, char c, int n2, boolean bl, int n3) {
        int n4;
        if (!bl && n <= 0) {
            return n2;
        }
        if (bl && n3 >= 3 || n > 99) {
            int n5 = n / 100;
            arrc[n2] = (char)(n5 + 48);
            n4 = n2 + 1;
            n -= n5 * 100;
        } else {
            n4 = n2;
        }
        if (bl && n3 >= 2 || n > 9 || n2 != n4) {
            n2 = n / 10;
            arrc[n4] = (char)(n2 + 48);
            ++n4;
            n -= n2 * 10;
        }
        arrc[n4] = (char)(n + 48);
        n = n4 + 1;
        arrc[n] = c;
        return n + 1;
    }
}

