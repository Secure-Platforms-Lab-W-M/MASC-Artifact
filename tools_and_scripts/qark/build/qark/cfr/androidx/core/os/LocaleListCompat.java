/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.LocaleList
 */
package androidx.core.os;

import android.os.Build;
import android.os.LocaleList;
import androidx.core.os.LocaleListCompatWrapper;
import androidx.core.os.LocaleListInterface;
import androidx.core.os.LocaleListPlatformWrapper;
import java.util.Locale;

public final class LocaleListCompat {
    private static final LocaleListCompat sEmptyLocaleList = LocaleListCompat.create(new Locale[0]);
    private LocaleListInterface mImpl;

    private LocaleListCompat(LocaleListInterface localeListInterface) {
        this.mImpl = localeListInterface;
    }

    public static /* varargs */ LocaleListCompat create(Locale ... arrlocale) {
        if (Build.VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap(new LocaleList(arrlocale));
        }
        return new LocaleListCompat(new LocaleListCompatWrapper(arrlocale));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    static Locale forLanguageTagCompat(String string2) {
        String[] arrstring;
        if (string2.contains("-")) {
            arrstring = string2.split("-", -1);
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
            arrstring = string2.split("_", -1);
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

    public static LocaleListCompat forLanguageTags(String object) {
        if (object != null && !object.isEmpty()) {
            String[] arrstring = object.split(",", -1);
            Locale[] arrlocale = new Locale[arrstring.length];
            for (int i = 0; i < arrlocale.length; ++i) {
                object = Build.VERSION.SDK_INT >= 21 ? Locale.forLanguageTag(arrstring[i]) : LocaleListCompat.forLanguageTagCompat(arrstring[i]);
                arrlocale[i] = object;
            }
            return LocaleListCompat.create(arrlocale);
        }
        return LocaleListCompat.getEmptyLocaleList();
    }

    public static LocaleListCompat getAdjustedDefault() {
        if (Build.VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap(LocaleList.getAdjustedDefault());
        }
        return LocaleListCompat.create(Locale.getDefault());
    }

    public static LocaleListCompat getDefault() {
        if (Build.VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap(LocaleList.getDefault());
        }
        return LocaleListCompat.create(Locale.getDefault());
    }

    public static LocaleListCompat getEmptyLocaleList() {
        return sEmptyLocaleList;
    }

    public static LocaleListCompat wrap(LocaleList localeList) {
        return new LocaleListCompat(new LocaleListPlatformWrapper(localeList));
    }

    @Deprecated
    public static LocaleListCompat wrap(Object object) {
        return LocaleListCompat.wrap((LocaleList)object);
    }

    public boolean equals(Object object) {
        if (object instanceof LocaleListCompat && this.mImpl.equals(((LocaleListCompat)object).mImpl)) {
            return true;
        }
        return false;
    }

    public Locale get(int n) {
        return this.mImpl.get(n);
    }

    public Locale getFirstMatch(String[] arrstring) {
        return this.mImpl.getFirstMatch(arrstring);
    }

    public int hashCode() {
        return this.mImpl.hashCode();
    }

    public int indexOf(Locale locale) {
        return this.mImpl.indexOf(locale);
    }

    public boolean isEmpty() {
        return this.mImpl.isEmpty();
    }

    public int size() {
        return this.mImpl.size();
    }

    public String toLanguageTags() {
        return this.mImpl.toLanguageTags();
    }

    public String toString() {
        return this.mImpl.toString();
    }

    public Object unwrap() {
        return this.mImpl.getLocaleList();
    }
}

