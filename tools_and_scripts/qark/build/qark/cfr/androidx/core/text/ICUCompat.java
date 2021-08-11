/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.icu.util.ULocale
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package androidx.core.text;

import android.icu.util.ULocale;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class ICUCompat {
    private static final String TAG = "ICUCompat";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static {
        if (Build.VERSION.SDK_INT < 21) {
            Class class_ = Class.forName("libcore.icu.ICU");
            if (class_ == null) return;
            try {
                sGetScriptMethod = class_.getMethod("getScript", String.class);
                sAddLikelySubtagsMethod = class_.getMethod("addLikelySubtags", String.class);
                return;
            }
            catch (Exception exception) {
                sGetScriptMethod = null;
                sAddLikelySubtagsMethod = null;
                Log.w((String)"ICUCompat", (Throwable)exception);
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= 24) return;
        try {
            sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
            return;
        }
        catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    private ICUCompat() {
    }

    private static String addLikelySubtags(Locale object) {
        object = object.toString();
        try {
            if (sAddLikelySubtagsMethod != null) {
                String string2 = (String)sAddLikelySubtagsMethod.invoke(null, object);
                return string2;
            }
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.w((String)"ICUCompat", (Throwable)invocationTargetException);
            return object;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.w((String)"ICUCompat", (Throwable)illegalAccessException);
        }
        return object;
    }

    private static String getScript(String string2) {
        try {
            if (sGetScriptMethod != null) {
                string2 = (String)sGetScriptMethod.invoke(null, string2);
                return string2;
            }
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.w((String)"ICUCompat", (Throwable)invocationTargetException);
            return null;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.w((String)"ICUCompat", (Throwable)illegalAccessException);
        }
        return null;
    }

    public static String maximizeAndGetScript(Locale object) {
        if (Build.VERSION.SDK_INT >= 24) {
            return ULocale.addLikelySubtags((ULocale)ULocale.forLocale((Locale)object)).getScript();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                String string2 = ((Locale)sAddLikelySubtagsMethod.invoke(null, object)).getScript();
                return string2;
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.w((String)"ICUCompat", (Throwable)illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.w((String)"ICUCompat", (Throwable)invocationTargetException);
            }
            return object.getScript();
        }
        if ((object = ICUCompat.addLikelySubtags((Locale)object)) != null) {
            return ICUCompat.getScript((String)object);
        }
        return null;
    }
}

