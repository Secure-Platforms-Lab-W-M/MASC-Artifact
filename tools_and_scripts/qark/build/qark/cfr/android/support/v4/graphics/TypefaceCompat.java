/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.CancellationSignal
 *  android.widget.TextView
 */
package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.graphics.TypefaceCompatApi21Impl;
import android.support.v4.graphics.TypefaceCompatApi24Impl;
import android.support.v4.graphics.TypefaceCompatApi26Impl;
import android.support.v4.graphics.TypefaceCompatBaseImpl;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.util.LruCache;
import android.widget.TextView;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompat {
    private static final String TAG = "TypefaceCompat";
    private static final LruCache<String, Typeface> sTypefaceCache;
    private static final TypefaceCompatImpl sTypefaceCompatImpl;

    static {
        sTypefaceCompatImpl = Build.VERSION.SDK_INT >= 26 ? new TypefaceCompatApi26Impl() : (Build.VERSION.SDK_INT >= 24 && TypefaceCompatApi24Impl.isUsable() ? new TypefaceCompatApi24Impl() : (Build.VERSION.SDK_INT >= 21 ? new TypefaceCompatApi21Impl() : new TypefaceCompatBaseImpl()));
        sTypefaceCache = new LruCache(16);
    }

    private TypefaceCompat() {
    }

    public static Typeface createFromFontInfo(Context context, @Nullable CancellationSignal cancellationSignal, @NonNull FontsContractCompat.FontInfo[] arrfontInfo, int n) {
        return sTypefaceCompatImpl.createFromFontInfo(context, cancellationSignal, arrfontInfo, n);
    }

    public static Typeface createFromResourcesFamilyXml(Context context, FontResourcesParserCompat.FamilyResourceEntry familyResourceEntry, Resources resources, int n, int n2, @Nullable TextView textView) {
        if (familyResourceEntry instanceof FontResourcesParserCompat.ProviderResourceEntry) {
            familyResourceEntry = (FontResourcesParserCompat.ProviderResourceEntry)familyResourceEntry;
            context = FontsContractCompat.getFontSync(context, familyResourceEntry.getRequest(), textView, familyResourceEntry.getFetchStrategy(), familyResourceEntry.getTimeout(), n2);
        } else {
            context = sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(context, (FontResourcesParserCompat.FontFamilyFilesResourceEntry)familyResourceEntry, resources, n2);
        }
        if (context != null) {
            sTypefaceCache.put(TypefaceCompat.createResourceUid(resources, n, n2), (Typeface)context);
            return context;
        }
        return context;
    }

    @Nullable
    public static Typeface createFromResourcesFontFile(Context context, Resources resources, int n, String string2, int n2) {
        if ((context = sTypefaceCompatImpl.createFromResourcesFontFile(context, resources, n, string2, n2)) != null) {
            sTypefaceCache.put(TypefaceCompat.createResourceUid(resources, n, n2), (Typeface)context);
            return context;
        }
        return context;
    }

    private static String createResourceUid(Resources resources, int n, int n2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(resources.getResourcePackageName(n));
        stringBuilder.append("-");
        stringBuilder.append(n);
        stringBuilder.append("-");
        stringBuilder.append(n2);
        return stringBuilder.toString();
    }

    public static Typeface findFromCache(Resources resources, int n, int n2) {
        return sTypefaceCache.get(TypefaceCompat.createResourceUid(resources, n, n2));
    }

    static interface TypefaceCompatImpl {
        public Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4);

        public Typeface createFromFontInfo(Context var1, @Nullable CancellationSignal var2, @NonNull FontsContractCompat.FontInfo[] var3, int var4);

        public Typeface createFromResourcesFontFile(Context var1, Resources var2, int var3, String var4, int var5);
    }

}

