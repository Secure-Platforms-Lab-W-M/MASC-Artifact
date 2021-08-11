// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.content.res;

import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.content.res.Resources;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import androidx.core.content.res.ColorStateListInflaterCompat;
import androidx.appcompat.widget.ResourceManagerInternal;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.os.Build$VERSION;
import android.content.res.ColorStateList;
import android.util.SparseArray;
import android.content.Context;
import java.util.WeakHashMap;
import android.util.TypedValue;

public final class AppCompatResources
{
    private static final String LOG_TAG = "AppCompatResources";
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE;
    private static final Object sColorStateCacheLock;
    private static final WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> sColorStateCaches;
    
    static {
        TL_TYPED_VALUE = new ThreadLocal<TypedValue>();
        sColorStateCaches = new WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>>(0);
        sColorStateCacheLock = new Object();
    }
    
    private AppCompatResources() {
    }
    
    private static void addColorStateListToCache(final Context context, final int n, final ColorStateList list) {
        synchronized (AppCompatResources.sColorStateCacheLock) {
            SparseArray sparseArray;
            if ((sparseArray = AppCompatResources.sColorStateCaches.get(context)) == null) {
                sparseArray = new SparseArray();
                AppCompatResources.sColorStateCaches.put(context, (SparseArray<ColorStateListCacheEntry>)sparseArray);
            }
            sparseArray.append(n, (Object)new ColorStateListCacheEntry(list, context.getResources().getConfiguration()));
        }
    }
    
    private static ColorStateList getCachedColorStateList(final Context context, final int n) {
        synchronized (AppCompatResources.sColorStateCacheLock) {
            final SparseArray<ColorStateListCacheEntry> sparseArray = AppCompatResources.sColorStateCaches.get(context);
            if (sparseArray != null && sparseArray.size() > 0) {
                final ColorStateListCacheEntry colorStateListCacheEntry = (ColorStateListCacheEntry)sparseArray.get(n);
                if (colorStateListCacheEntry != null) {
                    if (colorStateListCacheEntry.configuration.equals(context.getResources().getConfiguration())) {
                        return colorStateListCacheEntry.value;
                    }
                    sparseArray.remove(n);
                }
            }
            return null;
        }
    }
    
    public static ColorStateList getColorStateList(final Context context, final int n) {
        if (Build$VERSION.SDK_INT >= 23) {
            return context.getColorStateList(n);
        }
        final ColorStateList cachedColorStateList = getCachedColorStateList(context, n);
        if (cachedColorStateList != null) {
            return cachedColorStateList;
        }
        final ColorStateList inflateColorStateList = inflateColorStateList(context, n);
        if (inflateColorStateList != null) {
            addColorStateListToCache(context, n, inflateColorStateList);
            return inflateColorStateList;
        }
        return ContextCompat.getColorStateList(context, n);
    }
    
    public static Drawable getDrawable(final Context context, final int n) {
        return ResourceManagerInternal.get().getDrawable(context, n);
    }
    
    private static TypedValue getTypedValue() {
        TypedValue typedValue;
        if ((typedValue = AppCompatResources.TL_TYPED_VALUE.get()) == null) {
            typedValue = new TypedValue();
            AppCompatResources.TL_TYPED_VALUE.set(typedValue);
        }
        return typedValue;
    }
    
    private static ColorStateList inflateColorStateList(final Context context, final int n) {
        if (isColorInt(context, n)) {
            return null;
        }
        final Resources resources = context.getResources();
        final XmlResourceParser xml = resources.getXml(n);
        try {
            return ColorStateListInflaterCompat.createFromXml(resources, (XmlPullParser)xml, context.getTheme());
        }
        catch (Exception ex) {
            Log.e("AppCompatResources", "Failed to inflate ColorStateList, leaving it to the framework", (Throwable)ex);
            return null;
        }
    }
    
    private static boolean isColorInt(final Context context, final int n) {
        final Resources resources = context.getResources();
        final TypedValue typedValue = getTypedValue();
        resources.getValue(n, typedValue, true);
        return typedValue.type >= 28 && typedValue.type <= 31;
    }
    
    private static class ColorStateListCacheEntry
    {
        final Configuration configuration;
        final ColorStateList value;
        
        ColorStateListCacheEntry(final ColorStateList value, final Configuration configuration) {
            this.value = value;
            this.configuration = configuration;
        }
    }
}
