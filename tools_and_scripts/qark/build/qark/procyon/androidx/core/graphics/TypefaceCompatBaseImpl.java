// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.graphics;

import java.io.File;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import androidx.core.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.res.Resources;
import android.content.Context;
import java.lang.reflect.Field;
import android.util.Log;
import android.graphics.Typeface;
import androidx.core.content.res.FontResourcesParserCompat;
import java.util.concurrent.ConcurrentHashMap;

class TypefaceCompatBaseImpl
{
    private static final int INVALID_KEY = 0;
    private static final String TAG = "TypefaceCompatBaseImpl";
    private ConcurrentHashMap<Long, FontResourcesParserCompat.FontFamilyFilesResourceEntry> mFontFamilies;
    
    TypefaceCompatBaseImpl() {
        this.mFontFamilies = new ConcurrentHashMap<Long, FontResourcesParserCompat.FontFamilyFilesResourceEntry>();
    }
    
    private void addFontFamily(final Typeface typeface, final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry) {
        final long uniqueKey = getUniqueKey(typeface);
        if (uniqueKey != 0L) {
            this.mFontFamilies.put(uniqueKey, fontFamilyFilesResourceEntry);
        }
    }
    
    private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final int n) {
        return findBestFont(fontFamilyFilesResourceEntry.getEntries(), n, (StyleExtractor<FontResourcesParserCompat.FontFileResourceEntry>)new StyleExtractor<FontResourcesParserCompat.FontFileResourceEntry>() {
            public int getWeight(final FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.getWeight();
            }
            
            public boolean isItalic(final FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry) {
                return fontFileResourceEntry.isItalic();
            }
        });
    }
    
    private static <T> T findBestFont(final T[] array, int i, final StyleExtractor<T> styleExtractor) {
        int n;
        if ((i & 0x1) == 0x0) {
            n = 400;
        }
        else {
            n = 700;
        }
        final boolean b = (i & 0x2) != 0x0;
        T t = null;
        int n2 = Integer.MAX_VALUE;
        int length;
        T t2;
        int abs;
        int n3;
        int n4;
        int n5;
        for (length = array.length, i = 0; i < length; ++i, n2 = n5) {
            t2 = array[i];
            abs = Math.abs(styleExtractor.getWeight(t2) - n);
            if (styleExtractor.isItalic(t2) == b) {
                n3 = 0;
            }
            else {
                n3 = 1;
            }
            n4 = abs * 2 + n3;
            if (t == null || (n5 = n2) > n4) {
                t = t2;
                n5 = n4;
            }
        }
        return t;
    }
    
    private static long getUniqueKey(final Typeface typeface) {
        if (typeface == null) {
            return 0L;
        }
        try {
            final Field declaredField = Typeface.class.getDeclaredField("native_instance");
            declaredField.setAccessible(true);
            return ((Number)declaredField.get(typeface)).longValue();
        }
        catch (IllegalAccessException ex) {
            Log.e("TypefaceCompatBaseImpl", "Could not retrieve font from family.", (Throwable)ex);
            return 0L;
        }
        catch (NoSuchFieldException ex2) {
            Log.e("TypefaceCompatBaseImpl", "Could not retrieve font from family.", (Throwable)ex2);
            return 0L;
        }
    }
    
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final Resources resources, final int n) {
        final FontResourcesParserCompat.FontFileResourceEntry bestEntry = this.findBestEntry(fontFamilyFilesResourceEntry, n);
        if (bestEntry == null) {
            return null;
        }
        final Typeface fromResourcesFontFile = TypefaceCompat.createFromResourcesFontFile(context, resources, bestEntry.getResourceId(), bestEntry.getFileName(), n);
        this.addFontFamily(fromResourcesFontFile, fontFamilyFilesResourceEntry);
        return fromResourcesFontFile;
    }
    
    public Typeface createFromFontInfo(final Context context, CancellationSignal openInputStream, FontsContractCompat.FontInfo[] array, final int n) {
        if (array.length < 1) {
            return null;
        }
        final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(array, n);
        array = null;
        openInputStream = null;
        try {
            return this.createFromInputStream(context, (InputStream)(Object)(array = (FontsContractCompat.FontInfo[])(Object)(openInputStream = (CancellationSignal)context.getContentResolver().openInputStream(bestInfo.getUri()))));
        }
        catch (IOException ex) {
            return null;
        }
        finally {
            TypefaceCompatUtil.closeQuietly((Closeable)openInputStream);
        }
    }
    
    protected Typeface createFromInputStream(Context tempFile, final InputStream inputStream) {
        tempFile = (Context)TypefaceCompatUtil.getTempFile(tempFile);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile((File)tempFile, inputStream)) {
                return null;
            }
            return Typeface.createFromFile(((File)tempFile).getPath());
        }
        catch (RuntimeException ex) {
            return null;
        }
        finally {
            ((File)tempFile).delete();
        }
    }
    
    public Typeface createFromResourcesFontFile(Context tempFile, final Resources resources, final int n, final String s, final int n2) {
        tempFile = (Context)TypefaceCompatUtil.getTempFile(tempFile);
        if (tempFile == null) {
            return null;
        }
        try {
            if (!TypefaceCompatUtil.copyToFile((File)tempFile, resources, n)) {
                return null;
            }
            return Typeface.createFromFile(((File)tempFile).getPath());
        }
        catch (RuntimeException ex) {
            return null;
        }
        finally {
            ((File)tempFile).delete();
        }
    }
    
    protected FontsContractCompat.FontInfo findBestInfo(final FontsContractCompat.FontInfo[] array, final int n) {
        return findBestFont(array, n, (StyleExtractor<FontsContractCompat.FontInfo>)new StyleExtractor<FontsContractCompat.FontInfo>() {
            public int getWeight(final FontsContractCompat.FontInfo fontInfo) {
                return fontInfo.getWeight();
            }
            
            public boolean isItalic(final FontsContractCompat.FontInfo fontInfo) {
                return fontInfo.isItalic();
            }
        });
    }
    
    FontResourcesParserCompat.FontFamilyFilesResourceEntry getFontFamily(final Typeface typeface) {
        final long uniqueKey = getUniqueKey(typeface);
        if (uniqueKey == 0L) {
            return null;
        }
        return this.mFontFamilies.get(uniqueKey);
    }
    
    private interface StyleExtractor<T>
    {
        int getWeight(final T p0);
        
        boolean isItalic(final T p0);
    }
}
