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

@RequiresApi(value=21)
class ICUCompatApi21 {
    private static final String TAG = "ICUCompatApi21";
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

    ICUCompatApi21() {
    }

    public static String maximizeAndGetScript(Locale locale) {
        try {
            String string2 = ((Locale)sAddLikelySubtagsMethod.invoke(null, locale)).getScript();
            return string2;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.w((String)"ICUCompatApi21", (Throwable)illegalAccessException);
        }
        catch (InvocationTargetException invocationTargetException) {
            Log.w((String)"ICUCompatApi21", (Throwable)invocationTargetException);
        }
        return locale.getScript();
    }
}

