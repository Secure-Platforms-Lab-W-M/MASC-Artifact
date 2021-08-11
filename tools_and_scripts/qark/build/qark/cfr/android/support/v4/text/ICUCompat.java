/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.text;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.text.ICUCompatApi21;
import android.support.v4.text.ICUCompatIcs;
import java.util.Locale;

public final class ICUCompat {
    private static final ICUCompatBaseImpl IMPL = Build.VERSION.SDK_INT >= 21 ? new ICUCompatApi21Impl() : new ICUCompatBaseImpl();

    private ICUCompat() {
    }

    @Nullable
    public static String maximizeAndGetScript(Locale locale) {
        return IMPL.maximizeAndGetScript(locale);
    }

    @RequiresApi(value=21)
    static class ICUCompatApi21Impl
    extends ICUCompatBaseImpl {
        ICUCompatApi21Impl() {
        }

        @Override
        public String maximizeAndGetScript(Locale locale) {
            return ICUCompatApi21.maximizeAndGetScript(locale);
        }
    }

    static class ICUCompatBaseImpl {
        ICUCompatBaseImpl() {
        }

        public String maximizeAndGetScript(Locale locale) {
            return ICUCompatIcs.maximizeAndGetScript(locale);
        }
    }

}

