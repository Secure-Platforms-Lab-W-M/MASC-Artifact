// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.app;

import android.util.LongSparseArray;
import android.support.annotation.RequiresApi;
import java.util.Map;
import android.util.Log;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.res.Resources;
import java.lang.reflect.Field;

class ResourcesFlusher
{
    private static final String TAG = "ResourcesFlusher";
    private static Field sDrawableCacheField;
    private static boolean sDrawableCacheFieldFetched;
    private static Field sResourcesImplField;
    private static boolean sResourcesImplFieldFetched;
    private static Class sThemedResourceCacheClazz;
    private static boolean sThemedResourceCacheClazzFetched;
    private static Field sThemedResourceCache_mUnthemedEntriesField;
    private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;
    
    static boolean flush(@NonNull final Resources resources) {
        if (Build$VERSION.SDK_INT >= 24) {
            return flushNougats(resources);
        }
        if (Build$VERSION.SDK_INT >= 23) {
            return flushMarshmallows(resources);
        }
        return Build$VERSION.SDK_INT >= 21 && flushLollipops(resources);
    }
    
    @RequiresApi(21)
    private static boolean flushLollipops(@NonNull final Resources resources) {
        if (!ResourcesFlusher.sDrawableCacheFieldFetched) {
            try {
                (ResourcesFlusher.sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", (Throwable)ex);
            }
            ResourcesFlusher.sDrawableCacheFieldFetched = true;
        }
        final Field sDrawableCacheField = ResourcesFlusher.sDrawableCacheField;
        if (sDrawableCacheField != null) {
            final Map map = null;
            Map map2;
            try {
                map2 = (Map)sDrawableCacheField.get(resources);
            }
            catch (IllegalAccessException ex2) {
                Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", (Throwable)ex2);
                map2 = map;
            }
            if (map2 != null) {
                map2.clear();
                return true;
            }
        }
        return false;
    }
    
    @RequiresApi(23)
    private static boolean flushMarshmallows(@NonNull final Resources resources) {
        if (!ResourcesFlusher.sDrawableCacheFieldFetched) {
            try {
                (ResourcesFlusher.sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                Log.e("ResourcesFlusher", "Could not retrieve Resources#mDrawableCache field", (Throwable)ex);
            }
            ResourcesFlusher.sDrawableCacheFieldFetched = true;
        }
        final Object o = null;
        final Field sDrawableCacheField = ResourcesFlusher.sDrawableCacheField;
        Object value;
        if (sDrawableCacheField != null) {
            try {
                value = sDrawableCacheField.get(resources);
            }
            catch (IllegalAccessException ex2) {
                Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mDrawableCache", (Throwable)ex2);
                value = o;
            }
        }
        else {
            value = o;
        }
        return value != null && (value != null && flushThemedResourcesCache(value));
    }
    
    @RequiresApi(24)
    private static boolean flushNougats(@NonNull Resources value) {
        if (!ResourcesFlusher.sResourcesImplFieldFetched) {
            try {
                (ResourcesFlusher.sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                Log.e("ResourcesFlusher", "Could not retrieve Resources#mResourcesImpl field", (Throwable)ex);
            }
            ResourcesFlusher.sResourcesImplFieldFetched = true;
        }
        final Field sResourcesImplField = ResourcesFlusher.sResourcesImplField;
        if (sResourcesImplField == null) {
            return false;
        }
        final Object o = null;
        try {
            value = sResourcesImplField.get(value);
        }
        catch (IllegalAccessException ex2) {
            Log.e("ResourcesFlusher", "Could not retrieve value from Resources#mResourcesImpl", (Throwable)ex2);
            value = o;
        }
        if (value == null) {
            return false;
        }
        if (!ResourcesFlusher.sDrawableCacheFieldFetched) {
            try {
                (ResourcesFlusher.sDrawableCacheField = value.getClass().getDeclaredField("mDrawableCache")).setAccessible(true);
            }
            catch (NoSuchFieldException ex3) {
                Log.e("ResourcesFlusher", "Could not retrieve ResourcesImpl#mDrawableCache field", (Throwable)ex3);
            }
            ResourcesFlusher.sDrawableCacheFieldFetched = true;
        }
        final Object o2 = null;
        final Field sDrawableCacheField = ResourcesFlusher.sDrawableCacheField;
        Object value2;
        if (sDrawableCacheField != null) {
            try {
                value2 = sDrawableCacheField.get(value);
            }
            catch (IllegalAccessException ex4) {
                Log.e("ResourcesFlusher", "Could not retrieve value from ResourcesImpl#mDrawableCache", (Throwable)ex4);
                value2 = o2;
            }
        }
        else {
            value2 = o2;
        }
        return value2 != null && flushThemedResourcesCache(value2);
    }
    
    @RequiresApi(16)
    private static boolean flushThemedResourcesCache(@NonNull final Object o) {
        if (!ResourcesFlusher.sThemedResourceCacheClazzFetched) {
            try {
                ResourcesFlusher.sThemedResourceCacheClazz = Class.forName("android.content.res.ThemedResourceCache");
            }
            catch (ClassNotFoundException ex) {
                Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", (Throwable)ex);
            }
            ResourcesFlusher.sThemedResourceCacheClazzFetched = true;
        }
        final Class sThemedResourceCacheClazz = ResourcesFlusher.sThemedResourceCacheClazz;
        if (sThemedResourceCacheClazz == null) {
            return false;
        }
        if (!ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesFieldFetched) {
            try {
                (ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField = sThemedResourceCacheClazz.getDeclaredField("mUnthemedEntries")).setAccessible(true);
            }
            catch (NoSuchFieldException ex2) {
                Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", (Throwable)ex2);
            }
            ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesFieldFetched = true;
        }
        final Field sThemedResourceCache_mUnthemedEntriesField = ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField;
        if (sThemedResourceCache_mUnthemedEntriesField == null) {
            return false;
        }
        final LongSparseArray longSparseArray = null;
        LongSparseArray longSparseArray2;
        try {
            longSparseArray2 = (LongSparseArray)sThemedResourceCache_mUnthemedEntriesField.get(o);
        }
        catch (IllegalAccessException ex3) {
            Log.e("ResourcesFlusher", "Could not retrieve value from ThemedResourceCache#mUnthemedEntries", (Throwable)ex3);
            longSparseArray2 = longSparseArray;
        }
        if (longSparseArray2 != null) {
            longSparseArray2.clear();
            return true;
        }
        return false;
    }
}
