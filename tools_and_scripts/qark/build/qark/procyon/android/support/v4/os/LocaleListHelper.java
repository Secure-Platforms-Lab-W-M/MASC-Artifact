// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.os;

import java.util.Arrays;
import android.support.annotation.IntRange;
import android.os.Build$VERSION;
import android.support.annotation.Size;
import android.support.annotation.Nullable;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import android.support.annotation.NonNull;
import android.support.annotation.GuardedBy;
import java.util.Locale;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
final class LocaleListHelper
{
    private static final Locale EN_LATN;
    private static final Locale LOCALE_AR_XB;
    private static final Locale LOCALE_EN_XA;
    private static final int NUM_PSEUDO_LOCALES = 2;
    private static final String STRING_AR_XB = "ar-XB";
    private static final String STRING_EN_XA = "en-XA";
    @GuardedBy("sLock")
    private static LocaleListHelper sDefaultAdjustedLocaleList;
    @GuardedBy("sLock")
    private static LocaleListHelper sDefaultLocaleList;
    private static final Locale[] sEmptyList;
    private static final LocaleListHelper sEmptyLocaleList;
    @GuardedBy("sLock")
    private static Locale sLastDefaultLocale;
    @GuardedBy("sLock")
    private static LocaleListHelper sLastExplicitlySetLocaleList;
    private static final Object sLock;
    private final Locale[] mList;
    @NonNull
    private final String mStringRepresentation;
    
    static {
        sEmptyList = new Locale[0];
        sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
        LOCALE_EN_XA = new Locale("en", "XA");
        LOCALE_AR_XB = new Locale("ar", "XB");
        EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
        sLock = new Object();
        LocaleListHelper.sLastExplicitlySetLocaleList = null;
        LocaleListHelper.sDefaultLocaleList = null;
        LocaleListHelper.sDefaultAdjustedLocaleList = null;
        LocaleListHelper.sLastDefaultLocale = null;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    LocaleListHelper(@NonNull final Locale locale, final LocaleListHelper localeListHelper) {
        if (locale != null) {
            int length;
            if (localeListHelper == null) {
                length = 0;
            }
            else {
                length = localeListHelper.mList.length;
            }
            final int n = -1;
            int n2 = 0;
            int n3;
            while (true) {
                n3 = n;
                if (n2 >= length) {
                    break;
                }
                if (locale.equals(localeListHelper.mList[n2])) {
                    n3 = n2;
                    break;
                }
                ++n2;
            }
            int n4;
            if (n3 == -1) {
                n4 = 1;
            }
            else {
                n4 = 0;
            }
            final int n5 = n4 + length;
            final Locale[] mList = new Locale[n5];
            mList[0] = (Locale)locale.clone();
            if (n3 == -1) {
                for (int i = 0; i < length; ++i) {
                    mList[i + 1] = (Locale)localeListHelper.mList[i].clone();
                }
            }
            else {
                for (int j = 0; j < n3; ++j) {
                    mList[j + 1] = (Locale)localeListHelper.mList[j].clone();
                }
                for (int k = n3 + 1; k < length; ++k) {
                    mList[k] = (Locale)localeListHelper.mList[k].clone();
                }
            }
            final StringBuilder sb = new StringBuilder();
            for (int l = 0; l < n5; ++l) {
                sb.append(LocaleHelper.toLanguageTag(mList[l]));
                if (l < n5 - 1) {
                    sb.append(',');
                }
            }
            this.mList = mList;
            this.mStringRepresentation = sb.toString();
            return;
        }
        throw new NullPointerException("topLocale is null");
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    LocaleListHelper(@NonNull final Locale... array) {
        if (array.length == 0) {
            this.mList = LocaleListHelper.sEmptyList;
            this.mStringRepresentation = "";
            return;
        }
        final Locale[] mList = new Locale[array.length];
        final HashSet<Locale> set = new HashSet<Locale>();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            final Locale locale = array[i];
            if (locale == null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("list[");
                sb2.append(i);
                sb2.append("] is null");
                throw new NullPointerException(sb2.toString());
            }
            if (set.contains(locale)) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("list[");
                sb3.append(i);
                sb3.append("] is a repetition");
                throw new IllegalArgumentException(sb3.toString());
            }
            final Locale locale2 = (Locale)locale.clone();
            mList[i] = locale2;
            sb.append(LocaleHelper.toLanguageTag(locale2));
            if (i < array.length - 1) {
                sb.append(',');
            }
            set.add(locale2);
        }
        this.mList = mList;
        this.mStringRepresentation = sb.toString();
    }
    
    private Locale computeFirstMatch(final Collection<String> collection, final boolean b) {
        final int computeFirstMatchIndex = this.computeFirstMatchIndex(collection, b);
        if (computeFirstMatchIndex == -1) {
            return null;
        }
        return this.mList[computeFirstMatchIndex];
    }
    
    private int computeFirstMatchIndex(final Collection<String> collection, final boolean b) {
        final Locale[] mList = this.mList;
        if (mList.length == 1) {
            return 0;
        }
        if (mList.length == 0) {
            return -1;
        }
        int n = Integer.MAX_VALUE;
        if (b) {
            final int firstMatchIndex = this.findFirstMatchIndex(LocaleListHelper.EN_LATN);
            if (firstMatchIndex == 0) {
                return 0;
            }
            if (firstMatchIndex < Integer.MAX_VALUE) {
                n = firstMatchIndex;
            }
        }
        final Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            final int firstMatchIndex2 = this.findFirstMatchIndex(LocaleHelper.forLanguageTag(iterator.next()));
            if (firstMatchIndex2 == 0) {
                return 0;
            }
            if (firstMatchIndex2 >= n) {
                continue;
            }
            n = firstMatchIndex2;
        }
        if (n == Integer.MAX_VALUE) {
            return 0;
        }
        return n;
    }
    
    private int findFirstMatchIndex(final Locale locale) {
        int n = 0;
        while (true) {
            final Locale[] mList = this.mList;
            if (n >= mList.length) {
                return Integer.MAX_VALUE;
            }
            if (matchScore(locale, mList[n]) > 0) {
                return n;
            }
            ++n;
        }
    }
    
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static LocaleListHelper forLanguageTags(@Nullable final String s) {
        if (s != null && !s.isEmpty()) {
            final String[] split = s.split(",");
            final Locale[] array = new Locale[split.length];
            for (int i = 0; i < array.length; ++i) {
                array[i] = LocaleHelper.forLanguageTag(split[i]);
            }
            return new LocaleListHelper(array);
        }
        return getEmptyLocaleList();
    }
    
    @NonNull
    @Size(min = 1L)
    static LocaleListHelper getAdjustedDefault() {
        getDefault();
        synchronized (LocaleListHelper.sLock) {
            return LocaleListHelper.sDefaultAdjustedLocaleList;
        }
    }
    
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Size(min = 1L)
    static LocaleListHelper getDefault() {
    Label_0075_Outer:
        while (true) {
            final Locale default1 = Locale.getDefault();
            while (true) {
                Label_0094: {
                    while (true) {
                        Label_0091: {
                            Label_0088: {
                                synchronized (LocaleListHelper.sLock) {
                                    if (default1.equals(LocaleListHelper.sLastDefaultLocale)) {
                                        break Label_0094;
                                    }
                                    LocaleListHelper.sLastDefaultLocale = default1;
                                    if (LocaleListHelper.sDefaultLocaleList == null) {
                                        break Label_0091;
                                    }
                                    if (default1.equals(LocaleListHelper.sDefaultLocaleList.get(0))) {
                                        return LocaleListHelper.sDefaultLocaleList;
                                    }
                                    break Label_0088;
                                    final Locale locale;
                                    LocaleListHelper.sDefaultLocaleList = new LocaleListHelper(locale, LocaleListHelper.sLastExplicitlySetLocaleList);
                                    LocaleListHelper.sDefaultAdjustedLocaleList = LocaleListHelper.sDefaultLocaleList;
                                    return LocaleListHelper.sDefaultLocaleList;
                                }
                            }
                            continue Label_0075_Outer;
                        }
                        continue Label_0075_Outer;
                    }
                }
                continue;
            }
        }
    }
    
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static LocaleListHelper getEmptyLocaleList() {
        return LocaleListHelper.sEmptyLocaleList;
    }
    
    private static String getLikelyScript(final Locale locale) {
        if (Build$VERSION.SDK_INT < 21) {
            return "";
        }
        final String script = locale.getScript();
        if (!script.isEmpty()) {
            return script;
        }
        return "";
    }
    
    private static boolean isPseudoLocale(final String s) {
        return "en-XA".equals(s) || "ar-XB".equals(s);
    }
    
    private static boolean isPseudoLocale(final Locale locale) {
        return LocaleListHelper.LOCALE_EN_XA.equals(locale) || LocaleListHelper.LOCALE_AR_XB.equals(locale);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static boolean isPseudoLocalesOnly(@Nullable final String[] array) {
        if (array == null) {
            return true;
        }
        if (array.length > 3) {
            return false;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            final String s = array[i];
            if (!s.isEmpty() && !isPseudoLocale(s)) {
                return false;
            }
        }
        return true;
    }
    
    @IntRange(from = 0L, to = 1L)
    private static int matchScore(final Locale locale, final Locale locale2) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static void setDefault(@NonNull @Size(min = 1L) final LocaleListHelper localeListHelper) {
        setDefault(localeListHelper, 0);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static void setDefault(@NonNull @Size(min = 1L) final LocaleListHelper localeListHelper, final int n) {
        if (localeListHelper != null) {
            if (!localeListHelper.isEmpty()) {
                synchronized (LocaleListHelper.sLock) {
                    Locale.setDefault(LocaleListHelper.sLastDefaultLocale = localeListHelper.get(n));
                    LocaleListHelper.sLastExplicitlySetLocaleList = localeListHelper;
                    LocaleListHelper.sDefaultLocaleList = localeListHelper;
                    if (n == 0) {
                        LocaleListHelper.sDefaultAdjustedLocaleList = LocaleListHelper.sDefaultLocaleList;
                    }
                    else {
                        LocaleListHelper.sDefaultAdjustedLocaleList = new LocaleListHelper(LocaleListHelper.sLastDefaultLocale, LocaleListHelper.sDefaultLocaleList);
                    }
                    return;
                }
            }
            throw new IllegalArgumentException("locales is empty");
        }
        throw new NullPointerException("locales is null");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LocaleListHelper)) {
            return false;
        }
        final Locale[] mList = ((LocaleListHelper)o).mList;
        if (this.mList.length != mList.length) {
            return false;
        }
        int n = 0;
        while (true) {
            final Locale[] mList2 = this.mList;
            if (n >= mList2.length) {
                return true;
            }
            if (!mList2[n].equals(mList[n])) {
                return false;
            }
            ++n;
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    Locale get(final int n) {
        if (n >= 0) {
            final Locale[] mList = this.mList;
            if (n < mList.length) {
                return mList[n];
            }
        }
        return null;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    Locale getFirstMatch(final String[] array) {
        return this.computeFirstMatch(Arrays.asList(array), false);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getFirstMatchIndex(final String[] array) {
        return this.computeFirstMatchIndex(Arrays.asList(array), false);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getFirstMatchIndexWithEnglishSupported(final Collection<String> collection) {
        return this.computeFirstMatchIndex(collection, true);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getFirstMatchIndexWithEnglishSupported(final String[] array) {
        return this.getFirstMatchIndexWithEnglishSupported(Arrays.asList(array));
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    Locale getFirstMatchWithEnglishSupported(final String[] array) {
        return this.computeFirstMatch(Arrays.asList(array), true);
    }
    
    @Override
    public int hashCode() {
        int n = 1;
        int n2 = 0;
        while (true) {
            final Locale[] mList = this.mList;
            if (n2 >= mList.length) {
                break;
            }
            n = n * 31 + mList[n2].hashCode();
            ++n2;
        }
        return n;
    }
    
    @IntRange(from = -1L)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int indexOf(final Locale locale) {
        int n = 0;
        while (true) {
            final Locale[] mList = this.mList;
            if (n >= mList.length) {
                return -1;
            }
            if (mList[n].equals(locale)) {
                return n;
            }
            ++n;
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    boolean isEmpty() {
        return this.mList.length == 0;
    }
    
    @IntRange(from = 0L)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int size() {
        return this.mList.length;
    }
    
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    String toLanguageTags() {
        return this.mStringRepresentation;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        int n = 0;
        while (true) {
            final Locale[] mList = this.mList;
            if (n >= mList.length) {
                break;
            }
            sb.append(mList[n]);
            if (n < this.mList.length - 1) {
                sb.append(',');
            }
            ++n;
        }
        sb.append("]");
        return sb.toString();
    }
}
