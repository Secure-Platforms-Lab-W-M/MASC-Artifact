/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Message
 */
package androidx.core.os;

import android.os.Build;
import android.os.Message;

public final class MessageCompat {
    private static boolean sTryIsAsynchronous;
    private static boolean sTrySetAsynchronous;

    static {
        sTrySetAsynchronous = true;
        sTryIsAsynchronous = true;
    }

    private MessageCompat() {
    }

    public static boolean isAsynchronous(Message message) {
        if (Build.VERSION.SDK_INT >= 22) {
            return message.isAsynchronous();
        }
        if (sTryIsAsynchronous && Build.VERSION.SDK_INT >= 16) {
            try {
                boolean bl = message.isAsynchronous();
                return bl;
            }
            catch (NoSuchMethodError noSuchMethodError) {
                sTryIsAsynchronous = false;
            }
        }
        return false;
    }

    public static void setAsynchronous(Message message, boolean bl) {
        if (Build.VERSION.SDK_INT >= 22) {
            message.setAsynchronous(bl);
            return;
        }
        if (sTrySetAsynchronous && Build.VERSION.SDK_INT >= 16) {
            try {
                message.setAsynchronous(bl);
                return;
            }
            catch (NoSuchMethodError noSuchMethodError) {
                sTrySetAsynchronous = false;
            }
        }
    }
}

