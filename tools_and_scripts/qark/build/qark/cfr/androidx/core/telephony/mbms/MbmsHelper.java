/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.LocaleList
 *  android.telephony.mbms.ServiceInfo
 */
package androidx.core.telephony.mbms;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.telephony.mbms.ServiceInfo;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public final class MbmsHelper {
    private MbmsHelper() {
    }

    public static CharSequence getBestNameForService(Context object, ServiceInfo serviceInfo) {
        if (Build.VERSION.SDK_INT < 28) {
            return null;
        }
        object = object.getResources().getConfiguration().getLocales();
        int n = serviceInfo.getNamedContentLocales().size();
        if (n == 0) {
            return null;
        }
        String[] arrstring = new String[n];
        n = 0;
        Iterator iterator = serviceInfo.getNamedContentLocales().iterator();
        while (iterator.hasNext()) {
            arrstring[n] = ((Locale)iterator.next()).toLanguageTag();
            ++n;
        }
        if ((object = object.getFirstMatch(arrstring)) == null) {
            return null;
        }
        return serviceInfo.getNameForLocale((Locale)object);
    }
}

