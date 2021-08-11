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
 *  android.util.Log
 */
package androidx.core.graphics;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.provider.FontsContractCompat;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

class TypefaceCompatBaseImpl {
    private static final int INVALID_KEY = 0;
    private static final String TAG = "TypefaceCompatBaseImpl";
    private ConcurrentHashMap<Long, FontResourcesParserCompat.FontFamilyFilesResourceEntry> mFontFamilies = new ConcurrentHashMap();

    TypefaceCompatBaseImpl() {
    }

    private void addFontFamily(Typeface typeface, FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry) {
        long l = TypefaceCompatBaseImpl.getUniqueKey(typeface);
        if (l != 0L) {
            this.mFontFamilies.put(l, fontFamilyFilesResourceEntry);
        }
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
        T t = null;
        int n3 = Integer.MAX_VALUE;
        for (T t2 : arrT) {
            int n4;
            block4 : {
                int n5;
                block3 : {
                    n5 = Math.abs(styleExtractor.getWeight(t2) - n2);
                    n4 = styleExtractor.isItalic(t2) == bl ? 0 : 1;
                    n5 = n5 * 2 + n4;
                    if (t == null) break block3;
                    n4 = n3;
                    if (n3 <= n5) break block4;
                }
                t = t2;
                n4 = n5;
            }
            n3 = n4;
        }
        return t;
    }

    private static long getUniqueKey(Typeface typeface) {
        if (typeface == null) {
            return 0L;
        }
        try {
            Field field = Typeface.class.getDeclaredField("native_instance");
            field.setAccessible(true);
            long l = ((Number)field.get((Object)typeface)).longValue();
            return l;
        }
        catch (IllegalAccessException illegalAccessException) {
            Log.e((String)"TypefaceCompatBaseImpl", (String)"Could not retrieve font from family.", (Throwable)illegalAccessException);
            return 0L;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            Log.e((String)"TypefaceCompatBaseImpl", (String)"Could not retrieve font from family.", (Throwable)noSuchFieldException);
            return 0L;
        }
    }

    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int n) {
        FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry = this.findBestEntry(fontFamilyFilesResourceEntry, n);
        if (fontFileResourceEntry == null) {
            return null;
        }
        context = TypefaceCompat.createFromResourcesFontFile(context, resources, fontFileResourceEntry.getResourceId(), fontFileResourceEntry.getFileName(), n);
        this.addFontFamily((Typeface)context, fontFamilyFilesResourceEntry);
        return context;
    }

    public Typeface createFromFontInfo(Context context, CancellationSignal object, FontsContractCompat.FontInfo[] object2, int n) {
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

    FontResourcesParserCompat.FontFamilyFilesResourceEntry getFontFamily(Typeface typeface) {
        long l = TypefaceCompatBaseImpl.getUniqueKey(typeface);
        if (l == 0L) {
            return null;
        }
        return this.mFontFamilies.get(l);
    }

    private static interface StyleExtractor<T> {
        public int getWeight(T var1);

        public boolean isItalic(T var1);
    }

}

