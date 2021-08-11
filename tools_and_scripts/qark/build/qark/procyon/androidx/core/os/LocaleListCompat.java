// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.os;

import android.os.LocaleList;
import android.os.Build$VERSION;
import java.util.Locale;

public final class LocaleListCompat
{
    private static final LocaleListCompat sEmptyLocaleList;
    private LocaleListInterface mImpl;
    
    static {
        sEmptyLocaleList = create(new Locale[0]);
    }
    
    private LocaleListCompat(final LocaleListInterface mImpl) {
        this.mImpl = mImpl;
    }
    
    public static LocaleListCompat create(final Locale... array) {
        if (Build$VERSION.SDK_INT >= 24) {
            return wrap(new LocaleList(array));
        }
        return new LocaleListCompat(new LocaleListCompatWrapper(array));
    }
    
    static Locale forLanguageTagCompat(final String s) {
        if (s.contains("-")) {
            final String[] split = s.split("-", -1);
            if (split.length > 2) {
                return new Locale(split[0], split[1], split[2]);
            }
            if (split.length > 1) {
                return new Locale(split[0], split[1]);
            }
            if (split.length == 1) {
                return new Locale(split[0]);
            }
        }
        else {
            if (!s.contains("_")) {
                return new Locale(s);
            }
            final String[] split2 = s.split("_", -1);
            if (split2.length > 2) {
                return new Locale(split2[0], split2[1], split2[2]);
            }
            if (split2.length > 1) {
                return new Locale(split2[0], split2[1]);
            }
            if (split2.length == 1) {
                return new Locale(split2[0]);
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Can not parse language tag: [");
        sb.append(s);
        sb.append("]");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static LocaleListCompat forLanguageTags(final String s) {
        if (s != null && !s.isEmpty()) {
            final String[] split = s.split(",", -1);
            final Locale[] array = new Locale[split.length];
            for (int i = 0; i < array.length; ++i) {
                Locale locale;
                if (Build$VERSION.SDK_INT >= 21) {
                    locale = Locale.forLanguageTag(split[i]);
                }
                else {
                    locale = forLanguageTagCompat(split[i]);
                }
                array[i] = locale;
            }
            return create(array);
        }
        return getEmptyLocaleList();
    }
    
    public static LocaleListCompat getAdjustedDefault() {
        if (Build$VERSION.SDK_INT >= 24) {
            return wrap(LocaleList.getAdjustedDefault());
        }
        return create(Locale.getDefault());
    }
    
    public static LocaleListCompat getDefault() {
        if (Build$VERSION.SDK_INT >= 24) {
            return wrap(LocaleList.getDefault());
        }
        return create(Locale.getDefault());
    }
    
    public static LocaleListCompat getEmptyLocaleList() {
        return LocaleListCompat.sEmptyLocaleList;
    }
    
    public static LocaleListCompat wrap(final LocaleList list) {
        return new LocaleListCompat(new LocaleListPlatformWrapper(list));
    }
    
    @Deprecated
    public static LocaleListCompat wrap(final Object o) {
        return wrap((LocaleList)o);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof LocaleListCompat && this.mImpl.equals(((LocaleListCompat)o).mImpl);
    }
    
    public Locale get(final int n) {
        return this.mImpl.get(n);
    }
    
    public Locale getFirstMatch(final String[] array) {
        return this.mImpl.getFirstMatch(array);
    }
    
    @Override
    public int hashCode() {
        return this.mImpl.hashCode();
    }
    
    public int indexOf(final Locale locale) {
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
    
    @Override
    public String toString() {
        return this.mImpl.toString();
    }
    
    public Object unwrap() {
        return this.mImpl.getLocaleList();
    }
}
