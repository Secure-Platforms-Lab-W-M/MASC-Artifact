// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.telephony.mbms;

import java.util.Iterator;
import android.os.LocaleList;
import java.util.Locale;
import android.os.Build$VERSION;
import android.telephony.mbms.ServiceInfo;
import android.content.Context;

public final class MbmsHelper
{
    private MbmsHelper() {
    }
    
    public static CharSequence getBestNameForService(final Context context, final ServiceInfo serviceInfo) {
        if (Build$VERSION.SDK_INT < 28) {
            return null;
        }
        final LocaleList locales = context.getResources().getConfiguration().getLocales();
        final int size = serviceInfo.getNamedContentLocales().size();
        if (size == 0) {
            return null;
        }
        final String[] array = new String[size];
        int n = 0;
        final Iterator<Locale> iterator = serviceInfo.getNamedContentLocales().iterator();
        while (iterator.hasNext()) {
            array[n] = iterator.next().toLanguageTag();
            ++n;
        }
        final Locale firstMatch = locales.getFirstMatch(array);
        if (firstMatch == null) {
            return null;
        }
        return serviceInfo.getNameForLocale(firstMatch);
    }
}
