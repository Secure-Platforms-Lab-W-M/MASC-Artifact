/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package android.support.v4.text;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

class ICUCompatApi23 {
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;

    static {
        try {
            sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
            return;
        }
        catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

    ICUCompatApi23() {
    }

    public static String maximizeAndGetScript(Locale locale) {
        try {
            String string = ((Locale)sAddLikelySubtagsMethod.invoke(null, locale)).getScript();
            return string;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.w((String)"ICUCompatIcs", (Throwable)illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.w((String)"ICUCompatIcs", (Throwable)invocationTargetException);
        }
        return locale.getScript();
    }
}

