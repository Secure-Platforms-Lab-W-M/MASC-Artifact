/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.SparseArray
 *  android.view.View
 */
package androidx.databinding.adapters;

import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class ListenerUtil {
    private static final SparseArray<WeakHashMap<View, WeakReference<?>>> sListeners = new SparseArray();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static <T> T getListener(View object, int n) {
        if (Build.VERSION.SDK_INT >= 14) {
            return (T)object.getTag(n);
        }
        SparseArray sparseArray = sListeners;
        synchronized (sparseArray) {
            WeakHashMap weakHashMap = (WeakHashMap)sListeners.get(n);
            if (weakHashMap == null) {
                return null;
            }
            if ((object = (WeakReference)weakHashMap.get(object)) == null) {
                return null;
            }
            object = object.get();
            return (T)object;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static <T> T trackListener(View weakReference, T t, int n) {
        if (Build.VERSION.SDK_INT >= 14) {
            Object object = weakReference.getTag(n);
            weakReference.setTag(n, t);
            return (T)object;
        }
        SparseArray sparseArray = sListeners;
        synchronized (sparseArray) {
            WeakHashMap<View, WeakReference<T>> weakHashMap;
            WeakHashMap<View, WeakReference<T>> weakHashMap2 = weakHashMap = (WeakHashMap<View, WeakReference<T>>)sListeners.get(n);
            if (weakHashMap == null) {
                weakHashMap2 = new WeakHashMap<View, WeakReference<T>>();
                sListeners.put(n, weakHashMap2);
            }
            weakReference = t == null ? (WeakReference)weakHashMap2.remove(weakReference) : weakHashMap2.put((View)weakReference, new WeakReference<T>(t));
            if (weakReference == null) {
                return null;
            }
            weakReference = weakReference.get();
            return (T)weakReference;
        }
    }
}

