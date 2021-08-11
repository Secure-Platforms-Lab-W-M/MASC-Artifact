/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.net.Uri
 *  android.os.CancellationSignal
 */
package android.support.v4.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.graphics.TypefaceCompat;
import android.support.v4.graphics.TypefaceCompatUtil;
import android.support.v4.provider.FontsContractCompat;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RequiresApi(value=14)
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
class TypefaceCompatBaseImpl
implements TypefaceCompat.TypefaceCompatImpl {
    private static final String CACHE_FILE_PREFIX = "cached_font_";
    private static final String TAG = "TypefaceCompatBaseImpl";

    TypefaceCompatBaseImpl() {
    }

    private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, int n) {
        return TypefaceCompatBaseImpl.findBestFont(fontFamilyFilesResourceEntry.getEntries(), n, new StyleExtractor<FontResourcesParserCompat.FontFileResourceEntry>(){

            @Override
            public int getWeight(FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.getWeight();
            }

            @Override
            public boolean isItalic(FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.isItalic();
            }
        });
    }

    private static <T> T findBestFont(T[] arrT, int n, StyleExtractor<T> styleExtractor) {
        int n2 = (n & 1) == 0 ? 400 : 700;
        boolean bl = (n & 2) != 0;
        int n3 = arrT.length;
        int n4 = Integer.MAX_VALUE;
        T t = null;
        for (n = 0; n < n3; ++n) {
            T t2 = arrT[n];
            int n5 = Math.abs(styleExtractor.getWeight(t2) - n2);
            int n6 = styleExtractor.isItalic(t2) == bl ? 0 : 1;
            n6 = n5 * 2 + n6;
            if (t != null && n4 <= n6) continue;
            t = t2;
            n4 = n6;
        }
        return t;
    }

    @Nullable
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry object, Resources resources, int n) {
        if ((object = this.findBestEntry((FontResourcesParserCompat.FontFamilyFilesResourceEntry)object, n)) == null) {
            return null;
        }
        return TypefaceCompat.createFromResourcesFontFile(context, resources, object.getResourceId(), object.getFileName(), n);
    }

    @Override
    public Typeface createFromFontInfo(Context context, @Nullable CancellationSignal object, @NonNull FontsContractCompat.FontInfo[] object2, int n) {
        if (object2.length < 1) {
            return null;
        }
        Object object3 = this.findBestInfo((FontsContractCompat.FontInfo[])object2, n);
        object2 = null;
        object = null;
        object = object3 = context.getContentResolver().openInputStream(object3.getUri());
        object2 = object3;
        try {
            context = this.createFromInputStream(context, (InputStream)object3);
        }
        catch (Throwable throwable) {
            TypefaceCompatUtil.closeQuietly((Closeable)object);
            throw throwable;
        }
        catch (IOException iOException) {
            TypefaceCompatUtil.closeQuietly((Closeable)object2);
            return null;
        }
        TypefaceCompatUtil.closeQuietly((Closeable)object3);
        return context;
    }

    protected Typeface createFromInputStream(Context object, InputStream inputStream) {
        block6 : {
            if ((object = TypefaceCompatUtil.getTempFile((Context)object)) == null) {
                return null;
            }
            boolean bl = TypefaceCompatUtil.copyToFile((File)object, inputStream);
            if (bl) break block6;
            object.delete();
            return null;
        }
        try {
            inputStream = Typeface.createFromFile((String)object.getPath());
            return inputStream;
        }
        catch (Throwable throwable) {
            throw throwable;
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
        finally {
            object.delete();
        }
    }

    @Nullable
    @Override
    public Typeface createFromResourcesFontFile(Context object, Resources resources, int n, String string2, int n2) {
        block6 : {
            if ((object = TypefaceCompatUtil.getTempFile((Context)object)) == null) {
                return null;
            }
            boolean bl = TypefaceCompatUtil.copyToFile((File)object, resources, n);
            if (bl) break block6;
            object.delete();
            return null;
        }
        try {
            resources = Typeface.createFromFile((String)object.getPath());
            return resources;
        }
        catch (Throwable throwable) {
            throw throwable;
        }
        catch (RuntimeException runtimeException) {
            return null;
        }
        finally {
            object.delete();
        }
    }

    protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] arrfontInfo, int n) {
        return TypefaceCompatBaseImpl.findBestFont(arrfontInfo, n, new StyleExtractor<FontsContractCompat.FontInfo>(){

            @Override
            public int getWeight(FontsContractCompat.FontInfo fontInfo) {
                return fontInfo.getWeight();
            }

            @Override
            public boolean isItalic(FontsContractCompat.FontInfo fontInfo) {
                return fontInfo.isItalic();
            }
        });
    }

    private static interface StyleExtractor<T> {
        public int getWeight(T var1);

        public boolean isItalic(T var1);
    }

}

