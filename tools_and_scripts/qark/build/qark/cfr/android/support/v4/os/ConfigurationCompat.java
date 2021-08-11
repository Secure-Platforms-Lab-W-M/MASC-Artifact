/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.Configuration
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.LocaleList
 */
package android.support.v4.os;

import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;
import android.support.v4.os.LocaleListCompat;
import java.util.Locale;

public final class ConfigurationCompat {
    private ConfigurationCompat() {
    }

    public static LocaleListCompat getLocales(Configuration configuration) {
        if (Build.VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap((Object)configuration.getLocales());
        }
        return LocaleListCompat.create(configuration.locale);
    }
}

