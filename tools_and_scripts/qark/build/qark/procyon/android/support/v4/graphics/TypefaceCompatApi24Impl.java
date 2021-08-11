// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics;

import android.net.Uri;
import android.support.v4.util.SimpleArrayMap;
import android.support.annotation.NonNull;
import android.support.v4.provider.FontsContractCompat;
import android.support.annotation.Nullable;
import android.os.CancellationSignal;
import android.content.res.Resources;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.content.Context;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import java.lang.reflect.Array;
import android.graphics.Typeface;
import java.util.List;
import java.nio.ByteBuffer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
class TypefaceCompatApi24Impl extends TypefaceCompatBaseImpl
{
    private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String TAG = "TypefaceCompatApi24Impl";
    private static final Method sAddFontWeightStyle;
    private static final Method sCreateFromFamiliesWithDefault;
    private static final Class sFontFamily;
    private static final Constructor sFontFamilyCtor;
    
    static {
        NoSuchMethodException ex = null;
        try {
            final Class<?> forName = Class.forName("android.graphics.FontFamily");
            try {
                final Constructor<?> constructor = forName.getConstructor((Class<?>[])new Class[0]);
                try {
                    final Method method = forName.getMethod("addFontWeightStyle", ByteBuffer.class, Integer.TYPE, List.class, Integer.TYPE, Boolean.TYPE);
                    try {
                        final Method method2 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(forName, 1).getClass());
                    }
                    catch (ClassNotFoundException | NoSuchMethodException ex5) {
                        final NoSuchMethodException ex2;
                        ex = ex2;
                    }
                }
                catch (ClassNotFoundException | NoSuchMethodException ex6) {
                    final NoSuchMethodException ex3;
                    ex = ex3;
                }
            }
            catch (ClassNotFoundException | NoSuchMethodException ex7) {
                final NoSuchMethodException ex4;
                ex = ex4;
            }
        }
        catch (ClassNotFoundException ex8) {}
        catch (NoSuchMethodException ex9) {}
        Log.e("TypefaceCompatApi24Impl", ex.getClass().getName(), (Throwable)ex);
        final Class<?> forName = null;
        final Constructor<?> constructor = null;
        final Method method = null;
        final Method method2 = null;
        sFontFamilyCtor = constructor;
        sFontFamily = forName;
        sAddFontWeightStyle = method;
        sCreateFromFamiliesWithDefault = method2;
    }
    
    private static boolean addFontWeightStyle(final Object o, final ByteBuffer byteBuffer, final int n, final int n2, final boolean b) {
        try {
            return (boolean)TypefaceCompatApi24Impl.sAddFontWeightStyle.invoke(o, byteBuffer, n, null, n2, b);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object o2;
            throw new RuntimeException((Throwable)o2);
        }
    }
    
    private static Typeface createFromFamiliesWithDefault(final Object o) {
        try {
            final Object instance = Array.newInstance(TypefaceCompatApi24Impl.sFontFamily, 1);
            Array.set(instance, 0, o);
            return (Typeface)TypefaceCompatApi24Impl.sCreateFromFamiliesWithDefault.invoke(null, instance);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Object o2;
            throw new RuntimeException((Throwable)o2);
        }
    }
    
    public static boolean isUsable() {
        if (TypefaceCompatApi24Impl.sAddFontWeightStyle == null) {
            Log.w("TypefaceCompatApi24Impl", "Unable to collect necessary private methods.Fallback to legacy implementation.");
        }
        return TypefaceCompatApi24Impl.sAddFontWeightStyle != null;
    }
    
    private static Object newFamily() {
        try {
            return TypefaceCompatApi24Impl.sFontFamilyCtor.newInstance(new Object[0]);
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException ex) {
            final Object o;
            throw new RuntimeException((Throwable)o);
        }
    }
    
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final Resources resources, int i) {
        final Object family = newFamily();
        final FontResourcesParserCompat.FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
        int length;
        FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry;
        for (length = entries.length, i = 0; i < length; ++i) {
            fontFileResourceEntry = entries[i];
            if (!addFontWeightStyle(family, TypefaceCompatUtil.copyToDirectBuffer(context, resources, fontFileResourceEntry.getResourceId()), 0, fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic())) {
                return null;
            }
        }
        return createFromFamiliesWithDefault(family);
    }
    
    @Override
    public Typeface createFromFontInfo(final Context context, @Nullable final CancellationSignal cancellationSignal, @NonNull final FontsContractCompat.FontInfo[] array, int i) {
        final Object family = newFamily();
        final SimpleArrayMap<Uri, ByteBuffer> simpleArrayMap = new SimpleArrayMap<Uri, ByteBuffer>();
        int length;
        FontsContractCompat.FontInfo fontInfo;
        Uri uri;
        ByteBuffer mmap;
        for (length = array.length, i = 0; i < length; ++i) {
            fontInfo = array[i];
            uri = fontInfo.getUri();
            mmap = simpleArrayMap.get(uri);
            if (mmap == null) {
                mmap = TypefaceCompatUtil.mmap(context, cancellationSignal, uri);
                simpleArrayMap.put(uri, mmap);
            }
            if (!addFontWeightStyle(family, mmap, fontInfo.getTtcIndex(), fontInfo.getWeight(), fontInfo.isItalic())) {
                return null;
            }
        }
        return createFromFamiliesWithDefault(family);
    }
}
