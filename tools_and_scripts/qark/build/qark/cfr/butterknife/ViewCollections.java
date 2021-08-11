/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Property
 *  android.view.View
 */
package butterknife;

import android.util.Property;
import android.view.View;
import butterknife.Action;
import butterknife.Setter;
import java.util.List;

public final class ViewCollections {
    private ViewCollections() {
    }

    public static <T extends View> void run(T t, Action<? super T> action) {
        action.apply(t, 0);
    }

    @SafeVarargs
    public static /* varargs */ <T extends View> void run(T t, Action<? super T> ... arraction) {
        int n = arraction.length;
        for (int i = 0; i < n; ++i) {
            arraction[i].apply(t, 0);
        }
    }

    public static <T extends View> void run(List<T> list, Action<? super T> action) {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            action.apply((View)list.get(i), i);
        }
    }

    @SafeVarargs
    public static /* varargs */ <T extends View> void run(List<T> list, Action<? super T> ... arraction) {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            int n2 = arraction.length;
            for (int j = 0; j < n2; ++j) {
                arraction[j].apply((View)list.get(i), i);
            }
        }
    }

    public static <T extends View> void run(T[] arrT, Action<? super T> action) {
        int n = arrT.length;
        for (int i = 0; i < n; ++i) {
            action.apply(arrT[i], i);
        }
    }

    @SafeVarargs
    public static /* varargs */ <T extends View> void run(T[] arrT, Action<? super T> ... arraction) {
        int n = arrT.length;
        for (int i = 0; i < n; ++i) {
            int n2 = arraction.length;
            for (int j = 0; j < n2; ++j) {
                arraction[j].apply(arrT[i], i);
            }
        }
    }

    public static <T extends View, V> void set(T t, Property<? super T, V> property, V v) {
        property.set(t, v);
    }

    public static <T extends View, V> void set(T t, Setter<? super T, V> setter, V v) {
        setter.set(t, v, 0);
    }

    public static <T extends View, V> void set(List<T> list, Property<? super T, V> property, V v) {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            property.set(list.get(i), v);
        }
    }

    public static <T extends View, V> void set(List<T> list, Setter<? super T, V> setter, V v) {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            setter.set((View)list.get(i), v, i);
        }
    }

    public static <T extends View, V> void set(T[] arrT, Property<? super T, V> property, V v) {
        int n = arrT.length;
        for (int i = 0; i < n; ++i) {
            property.set(arrT[i], v);
        }
    }

    public static <T extends View, V> void set(T[] arrT, Setter<? super T, V> setter, V v) {
        int n = arrT.length;
        for (int i = 0; i < n; ++i) {
            setter.set(arrT[i], v, i);
        }
    }
}

