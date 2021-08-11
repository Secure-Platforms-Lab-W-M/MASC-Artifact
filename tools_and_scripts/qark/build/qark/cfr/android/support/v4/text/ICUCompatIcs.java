/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package android.support.v4.text;

import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

@RequiresApi(value=14)
class ICUCompatIcs {
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static {
        Class class_;
        try {
            class_ = Class.forName("libcore.icu.ICU");
            if (class_ == null) return;
        }
        catch (Exception exception) {
            sGetScriptMethod = null;
            sAddLikelySubtagsMethod = null;
            Log.w((String)"ICUCompatIcs", (Throwable)exception);
            return;
        }
        sGetScriptMethod = class_.getMethod("getScript", String.class);
        sAddLikelySubtagsMethod = class_.getMethod("addLikelySubtags", String.class);
    }

    ICUCompatIcs() {
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
            Log.w((String)"ICUCompatIcs", (Throwable)invocationTargetException);
            return object;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.w((String)"ICUCompatIcs", (Throwable)illegalAccessException);
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
            Log.w((String)"ICUCompatIcs", (Throwable)invocationTargetException);
            return null;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.w((String)"ICUCompatIcs", (Throwable)illegalAccessException);
        }
        return null;
    }

    public static String maximizeAndGetScript(Locale object) {
        if ((object = ICUCompatIcs.addLikelySubtags((Locale)object)) != null) {
            return ICUCompatIcs.getScript((String)object);
        }
        return null;
    }
}

