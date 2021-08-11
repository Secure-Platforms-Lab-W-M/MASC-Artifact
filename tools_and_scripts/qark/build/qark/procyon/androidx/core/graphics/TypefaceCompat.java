// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.graphics;

import android.os.Handler;
import androidx.core.content.res.ResourcesCompat;
import android.content.res.Resources;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.Context;
import android.os.Build$VERSION;
import android.graphics.Typeface;
import androidx.collection.LruCache;

public class TypefaceCompat
{
    private static final LruCache<String, Typeface> sTypefaceCache;
    private static final TypefaceCompatBaseImpl sTypefaceCompatImpl;
    
    static {
        if (Build$VERSION.SDK_INT >= 28) {
            sTypefaceCompatImpl = new TypefaceCompatApi28Impl();
        }
        else if (Build$VERSION.SDK_INT >= 26) {
            sTypefaceCompatImpl = new TypefaceCompatApi26Impl();
        }
        else if (Build$VERSION.SDK_INT >= 24 && TypefaceCompatApi24Impl.isUsable()) {
            sTypefaceCompatImpl = new TypefaceCompatApi24Impl();
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            sTypefaceCompatImpl = new TypefaceCompatApi21Impl();
        }
        else {
            sTypefaceCompatImpl = new TypefaceCompatBaseImpl();
        }
        sTypefaceCache = new LruCache<String, Typeface>(16);
    }
    
    private TypefaceCompat() {
    }
    
    public static Typeface create(final Context context, final Typeface typeface, final int n) {
        if (context != null) {
            if (Build$VERSION.SDK_INT < 21) {
                final Typeface bestFontFromFamily = getBestFontFromFamily(context, typeface, n);
                if (bestFontFromFamily != null) {
                    return bestFontFromFamily;
                }
            }
            return Typeface.create(typeface, n);
        }
        throw new IllegalArgumentException("Context cannot be null");
    }
    
    public static Typeface createFromFontInfo(final Context context, final CancellationSignal cancellationSignal, final FontsContractCompat.FontInfo[] array, final int n) {
        return TypefaceCompat.sTypefaceCompatImpl.createFromFontInfo(context, cancellationSignal, array, n);
    }
    
    public static Typeface createFromResourcesFamilyXml(final Context context, final FontResourcesParserCompat.FamilyResourceEntry familyResourceEntry, final Resources resources, final int n, final int n2, final ResourcesCompat.FontCallback fontCallback, final Handler handler, final boolean b) {
        Typeface typeface;
        if (familyResourceEntry instanceof FontResourcesParserCompat.ProviderResourceEntry) {
            final FontResourcesParserCompat.ProviderResourceEntry providerResourceEntry = (FontResourcesParserCompat.ProviderResourceEntry)familyResourceEntry;
            final boolean b2 = b ? (providerResourceEntry.getFetchStrategy() == 0) : (fontCallback == null);
            int timeout;
            if (b) {
                timeout = providerResourceEntry.getTimeout();
            }
            else {
                timeout = -1;
            }
            typeface = FontsContractCompat.getFontSync(context, providerResourceEntry.getRequest(), fontCallback, handler, b2, timeout, n2);
        }
        else {
            final Typeface typeface2 = typeface = TypefaceCompat.sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(context, (FontResourcesParserCompat.FontFamilyFilesResourceEntry)familyResourceEntry, resources, n2);
            if (fontCallback != null) {
                if (typeface2 != null) {
                    fontCallback.callbackSuccessAsync(typeface2, handler);
                    typeface = typeface2;
                }
                else {
                    fontCallback.callbackFailAsync(-3, handler);
                    typeface = typeface2;
                }
            }
        }
        if (typeface != null) {
            TypefaceCompat.sTypefaceCache.put(createResourceUid(resources, n, n2), typeface);
        }
        return typeface;
    }
    
    public static Typeface createFromResourcesFontFile(final Context context, final Resources resources, final int n, final String s, final int n2) {
        final Typeface fromResourcesFontFile = TypefaceCompat.sTypefaceCompatImpl.createFromResourcesFontFile(context, resources, n, s, n2);
        if (fromResourcesFontFile != null) {
            TypefaceCompat.sTypefaceCache.put(createResourceUid(resources, n, n2), fromResourcesFontFile);
        }
        return fromResourcesFontFile;
    }
    
    private static String createResourceUid(final Resources resources, final int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(resources.getResourcePackageName(n));
        sb.append("-");
        sb.append(n);
        sb.append("-");
        sb.append(n2);
        return sb.toString();
    }
    
    public static Typeface findFromCache(final Resources resources, final int n, final int n2) {
        return TypefaceCompat.sTypefaceCache.get(createResourceUid(resources, n, n2));
    }
    
    private static Typeface getBestFontFromFamily(final Context context, final Typeface typeface, final int n) {
        final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamily = TypefaceCompat.sTypefaceCompatImpl.getFontFamily(typeface);
        if (fontFamily == null) {
            return null;
        }
        return TypefaceCompat.sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(context, fontFamily, context.getResources(), n);
    }
}
