/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.os;

import android.support.annotation.RestrictTo;
import java.util.Locale;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
final class LocaleHelper {
    LocaleHelper() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static Locale forLanguageTag(String string2) {
        String[] arrstring;
        if (string2.contains("-")) {
            arrstring = string2.split("-");
            if (arrstring.length > 2) {
                return new Locale(arrstring[0], arrstring[1], arrstring[2]);
            }
            if (arrstring.length > 1) {
                return new Locale(arrstring[0], arrstring[1]);
            }
            if (arrstring.length == 1) {
                return new Locale(arrstring[0]);
            }
        } else {
            if (!string2.contains("_")) return new Locale(string2);
            arrstring = string2.split("_");
            if (arrstring.length > 2) {
                return new Locale(arrstring[0], arrstring[1], (String)arrstring[2]);
            }
            if (arrstring.length > 1) {
                return new Locale(arrstring[0], (String)arrstring[1]);
            }
            if (arrstring.length == 1) {
                return new Locale((String)arrstring[0]);
            }
        }
        arrstring = new StringBuilder();
        arrstring.append("Can not parse language tag: [");
        arrstring.append(string2);
        arrstring.append("]");
        throw new IllegalArgumentException(arrstring.toString());
    }

    static String toLanguageTag(Locale locale) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(locale.getLanguage());
        String string2 = locale.getCountry();
        if (string2 != null && !string2.isEmpty()) {
            stringBuilder.append("-");
            stringBuilder.append(locale.getCountry());
        }
        return stringBuilder.toString();
    }
}

