/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.XmlResourceParser
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.util.SparseArray
 *  android.util.TypedValue
 *  org.xmlpull.v1.XmlPullParser
 */
package android.support.v7.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatColorStateListInflater;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;

public final class AppCompatResources {
    private static final String LOG_TAG = "AppCompatResources";
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal();
    private static final Object sColorStateCacheLock;
    private static final WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> sColorStateCaches;

    static {
        sColorStateCaches = new WeakHashMap(0);
        sColorStateCacheLock = new Object();
    }

    private AppCompatResources() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void addColorStateListToCache(@NonNull Context context, @ColorRes int n, @NonNull ColorStateList colorStateList) {
        Object object = sColorStateCacheLock;
        synchronized (object) {
            SparseArray sparseArray = sColorStateCaches.get((Object)context);
            if (sparseArray == null) {
                sparseArray = new SparseArray();
                sColorStateCaches.put(context, (SparseArray)sparseArray);
            }
            sparseArray.append(n, (Object)new ColorStateListCacheEntry(colorStateList, context.getResources().getConfiguration()));
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Nullable
    private static ColorStateList getCachedColorStateList(@NonNull Context context, @ColorRes int n) {
        Object object = sColorStateCacheLock;
        synchronized (object) {
            SparseArray<ColorStateListCacheEntry> sparseArray = sColorStateCaches.get((Object)context);
            if (sparseArray == null) return null;
            if (sparseArray.size() <= 0) return null;
            ColorStateListCacheEntry colorStateListCacheEntry = (ColorStateListCacheEntry)sparseArray.get(n);
            if (colorStateListCacheEntry == null) return null;
            if (colorStateListCacheEntry.configuration.equals(context.getResources().getConfiguration())) {
                return colorStateListCacheEntry.value;
            }
            sparseArray.remove(n);
            return null;
        }
    }

    public static ColorStateList getColorStateList(@NonNull Context context, @ColorRes int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColorStateList(n);
        }
        ColorStateList colorStateList = AppCompatResources.getCachedColorStateList(context, n);
        if (colorStateList != null) {
            return colorStateList;
        }
        colorStateList = AppCompatResources.inflateColorStateList(context, n);
        if (colorStateList != null) {
            AppCompatResources.addColorStateListToCache(context, n, colorStateList);
            return colorStateList;
        }
        return ContextCompat.getColorStateList(context, n);
    }

    @Nullable
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int n) {
        return AppCompatDrawableManager.get().getDrawable(context, n);
    }

    @NonNull
    private static TypedValue getTypedValue() {
        TypedValue typedValue = TL_TYPED_VALUE.get();
        if (typedValue == null) {
            typedValue = new TypedValue();
            TL_TYPED_VALUE.set(typedValue);
            return typedValue;
        }
        return typedValue;
    }

    @Nullable
    private static ColorStateList inflateColorStateList(Context context, int n) {
        if (AppCompatResources.isColorInt(context, n)) {
            return null;
        }
        Resources resources = context.getResources();
        XmlResourceParser xmlResourceParser = resources.getXml(n);
        try {
            context = AppCompatColorStateListInflater.createFromXml(resources, (XmlPullParser)xmlResourceParser, context.getTheme());
            return context;
        }
        catch (Exception exception) {
            Log.e((String)"AppCompatResources", (String)"Failed to inflate ColorStateList, leaving it to the framework", (Throwable)exception);
            return null;
        }
    }

    private static boolean isColorInt(@NonNull Context context, @ColorRes int n) {
        context = context.getResources();
        TypedValue typedValue = AppCompatResources.getTypedValue();
        context.getValue(n, typedValue, true);
        if (typedValue.type >= 28 && typedValue.type <= 31) {
            return true;
        }
        return false;
    }

    private static class ColorStateListCacheEntry {
        final Configuration configuration;
        final ColorStateList value;

        ColorStateListCacheEntry(@NonNull ColorStateList colorStateList, @NonNull Configuration configuration) {
            this.value = colorStateList;
            this.configuration = configuration;
        }
    }

}

