/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.LocaleList
 */
package android.support.v4.os;

import android.os.Build;
import android.os.LocaleList;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import android.support.v4.os.LocaleHelper;
import android.support.v4.os.LocaleListHelper;
import android.support.v4.os.LocaleListInterface;
import java.util.Locale;

public final class LocaleListCompat {
    static final LocaleListInterface IMPL;
    private static final LocaleListCompat sEmptyLocaleList;

    static {
        sEmptyLocaleList = new LocaleListCompat();
        IMPL = Build.VERSION.SDK_INT >= 24 ? new LocaleListCompatApi24Impl() : new LocaleListCompatBaseImpl();
    }

    private LocaleListCompat() {
    }

    public static /* varargs */ LocaleListCompat create(@NonNull Locale ... arrlocale) {
        LocaleListCompat localeListCompat = new LocaleListCompat();
        localeListCompat.setLocaleListArray(arrlocale);
        return localeListCompat;
    }

    @NonNull
    public static LocaleListCompat forLanguageTags(@Nullable String object) {
        if (object != null && !object.isEmpty()) {
            String[] arrstring = object.split(",");
            Locale[] arrlocale = new Locale[arrstring.length];
            for (int i = 0; i < arrlocale.length; ++i) {
                object = Build.VERSION.SDK_INT >= 21 ? Locale.forLanguageTag(arrstring[i]) : LocaleHelper.forLanguageTag(arrstring[i]);
                arrlocale[i] = object;
            }
            object = new LocaleListCompat();
            LocaleListCompat.super.setLocaleListArray(arrlocale);
            return object;
        }
        return LocaleListCompat.getEmptyLocaleList();
    }

    @NonNull
    @Size(min=1L)
    public static LocaleListCompat getAdjustedDefault() {
        if (Build.VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap((Object)LocaleList.getAdjustedDefault());
        }
        return LocaleListCompat.create(Locale.getDefault());
    }

    @NonNull
    @Size(min=1L)
    public static LocaleListCompat getDefault() {
        if (Build.VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap((Object)LocaleList.getDefault());
        }
        return LocaleListCompat.create(Locale.getDefault());
    }

    @NonNull
    public static LocaleListCompat getEmptyLocaleList() {
        return sEmptyLocaleList;
    }

    @RequiresApi(value=24)
    private void setLocaleList(LocaleList localeList) {
        int n = localeList.size();
        if (n > 0) {
            Locale[] arrlocale = new Locale[n];
            for (int i = 0; i < n; ++i) {
                arrlocale[i] = localeList.get(i);
            }
            IMPL.setLocaleList(arrlocale);
            return;
        }
    }

    private /* varargs */ void setLocaleListArray(Locale ... arrlocale) {
        IMPL.setLocaleList(arrlocale);
    }

    @RequiresApi(value=24)
    public static LocaleListCompat wrap(Object object) {
        LocaleListCompat localeListCompat = new LocaleListCompat();
        if (object instanceof LocaleList) {
            localeListCompat.setLocaleList((LocaleList)object);
            return localeListCompat;
        }
        return localeListCompat;
    }

    public boolean equals(Object object) {
        return IMPL.equals(object);
    }

    public Locale get(int n) {
        return IMPL.get(n);
    }

    public Locale getFirstMatch(String[] arrstring) {
        return IMPL.getFirstMatch(arrstring);
    }

    public int hashCode() {
        return IMPL.hashCode();
    }

    @IntRange(from=-1L)
    public int indexOf(Locale locale) {
        return IMPL.indexOf(locale);
    }

    public boolean isEmpty() {
        return IMPL.isEmpty();
    }

    @IntRange(from=0L)
    public int size() {
        return IMPL.size();
    }

    @NonNull
    public String toLanguageTags() {
        return IMPL.toLanguageTags();
    }

    public String toString() {
        return IMPL.toString();
    }

    @Nullable
    public Object unwrap() {
        return IMPL.getLocaleList();
    }

    @RequiresApi(value=24)
    static class LocaleListCompatApi24Impl
    implements LocaleListInterface {
        private LocaleList mLocaleList = new LocaleList(new Locale[0]);

        LocaleListCompatApi24Impl() {
        }

        @Override
        public boolean equals(Object object) {
            return this.mLocaleList.equals(((LocaleListCompat)object).unwrap());
        }

        @Override
        public Locale get(int n) {
            return this.mLocaleList.get(n);
        }

        @Nullable
        @Override
        public Locale getFirstMatch(String[] arrstring) {
            LocaleList localeList = this.mLocaleList;
            if (localeList != null) {
                return localeList.getFirstMatch(arrstring);
            }
            return null;
        }

        @Override
        public Object getLocaleList() {
            return this.mLocaleList;
        }

        @Override
        public int hashCode() {
            return this.mLocaleList.hashCode();
        }

        @IntRange(from=-1L)
        @Override
        public int indexOf(Locale locale) {
            return this.mLocaleList.indexOf(locale);
        }

        @Override
        public boolean isEmpty() {
            return this.mLocaleList.isEmpty();
        }

        @Override
        public /* varargs */ void setLocaleList(@NonNull Locale ... arrlocale) {
            this.mLocaleList = new LocaleList(arrlocale);
        }

        @IntRange(from=0L)
        @Override
        public int size() {
            return this.mLocaleList.size();
        }

        @Override
        public String toLanguageTags() {
            return this.mLocaleList.toLanguageTags();
        }

        @Override
        public String toString() {
            return this.mLocaleList.toString();
        }
    }

    static class LocaleListCompatBaseImpl
    implements LocaleListInterface {
        private LocaleListHelper mLocaleList = new LocaleListHelper(new Locale[0]);

        LocaleListCompatBaseImpl() {
        }

        @Override
        public boolean equals(Object object) {
            return this.mLocaleList.equals(((LocaleListCompat)object).unwrap());
        }

        @Override
        public Locale get(int n) {
            return this.mLocaleList.get(n);
        }

        @Nullable
        @Override
        public Locale getFirstMatch(String[] arrstring) {
            LocaleListHelper localeListHelper = this.mLocaleList;
            if (localeListHelper != null) {
                return localeListHelper.getFirstMatch(arrstring);
            }
            return null;
        }

        @Override
        public Object getLocaleList() {
            return this.mLocaleList;
        }

        @Override
        public int hashCode() {
            return this.mLocaleList.hashCode();
        }

        @IntRange(from=-1L)
        @Override
        public int indexOf(Locale locale) {
            return this.mLocaleList.indexOf(locale);
        }

        @Override
        public boolean isEmpty() {
            return this.mLocaleList.isEmpty();
        }

        @Override
        public /* varargs */ void setLocaleList(@NonNull Locale ... arrlocale) {
            this.mLocaleList = new LocaleListHelper(arrlocale);
        }

        @IntRange(from=0L)
        @Override
        public int size() {
            return this.mLocaleList.size();
        }

        @Override
        public String toLanguageTags() {
            return this.mLocaleList.toLanguageTags();
        }

        @Override
        public String toString() {
            return this.mLocaleList.toString();
        }
    }

}

