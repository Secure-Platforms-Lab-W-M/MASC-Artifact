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
 *  android.os.Handler
 */
package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CancellationSignal;
import android.os.Handler;
import androidx.collection.LruCache;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.TypefaceCompatApi21Impl;
import androidx.core.graphics.TypefaceCompatApi24Impl;
import androidx.core.graphics.TypefaceCompatApi26Impl;
import androidx.core.graphics.TypefaceCompatApi28Impl;
import androidx.core.graphics.TypefaceCompatBaseImpl;
import androidx.core.provider.FontRequest;
import androidx.core.provider.FontsContractCompat;

public class TypefaceCompat {
    private static final LruCache<String, Typeface> sTypefaceCache;
    private static final TypefaceCompatBaseImpl sTypefaceCompatImpl;

    static {
        sTypefaceCompatImpl = Build.VERSION.SDK_INT >= 28 ? new TypefaceCompatApi28Impl() : (Build.VERSION.SDK_INT >= 26 ? new TypefaceCompatApi26Impl() : (Build.VERSION.SDK_INT >= 24 && TypefaceCompatApi24Impl.isUsable() ? new TypefaceCompatApi24Impl() : (Build.VERSION.SDK_INT >= 21 ? new TypefaceCompatApi21Impl() : new TypefaceCompatBaseImpl())));
        sTypefaceCache = new LruCache(16);
    }

    private TypefaceCompat() {
    }

    public static Typeface create(Context context, Typeface typeface, int n) {
        if (context != null) {
            if (Build.VERSION.SDK_INT < 21 && (context = TypefaceCompat.getBestFontFromFamily(context, typeface, n)) != null) {
                return context;
            }
            return Typeface.create((Typeface)typeface, (int)n);
        }
        throw new IllegalArgumentException("Context cannot be null");
    }

    public static Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, FontsContractCompat.FontInfo[] arrfontInfo, int n) {
        return sTypefaceCompatImpl.createFromFontInfo(context, cancellationSignal, arrfontInfo, n);
    }

    public static Typeface createFromResourcesFamilyXml(Context object, FontResourcesParserCompat.FamilyResourceEntry familyResourceEntry, Resources resources, int n, int n2, ResourcesCompat.FontCallback fontCallback, Handler handler, boolean bl) {
        if (familyResourceEntry instanceof FontResourcesParserCompat.ProviderResourceEntry) {
            familyResourceEntry = (FontResourcesParserCompat.ProviderResourceEntry)familyResourceEntry;
            boolean bl2 = bl ? familyResourceEntry.getFetchStrategy() == 0 : fontCallback == null;
            int n3 = bl ? familyResourceEntry.getTimeout() : -1;
            object = FontsContractCompat.getFontSync((Context)object, familyResourceEntry.getRequest(), fontCallback, handler, bl2, n3, n2);
        } else {
            familyResourceEntry = sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry((Context)object, (FontResourcesParserCompat.FontFamilyFilesResourceEntry)familyResourceEntry, resources, n2);
            object = familyResourceEntry;
            if (fontCallback != null) {
                if (familyResourceEntry != null) {
                    fontCallback.callbackSuccessAsync((Typeface)familyResourceEntry, handler);
                    object = familyResourceEntry;
                } else {
                    fontCallback.callbackFailAsync(-3, handler);
                    object = familyResourceEntry;
                }
            }
        }
        if (object != null) {
            sTypefaceCache.put(TypefaceCompat.createResourceUid(resources, n, n2), (Typeface)object);
        }
        return object;
    }

    public static Typeface createFromResourcesFontFile(Context context, Resources object, int n, String string2, int n2) {
        if ((context = sTypefaceCompatImpl.createFromResourcesFontFile(context, (Resources)object, n, string2, n2)) != null) {
            object = TypefaceCompat.createResourceUid((Resources)object, n, n2);
            sTypefaceCache.put((String)object, (Typeface)context);
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

    private static Typeface getBestFontFromFamily(Context context, Typeface object, int n) {
        if ((object = sTypefaceCompatImpl.getFontFamily((Typeface)object)) == null) {
            return null;
        }
        return sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(context, (FontResourcesParserCompat.FontFamilyFilesResourceEntry)object, context.getResources(), n);
    }
}

