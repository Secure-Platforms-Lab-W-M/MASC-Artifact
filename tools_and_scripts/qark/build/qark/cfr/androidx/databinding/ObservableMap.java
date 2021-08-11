/*
 * Decompiled with CFR 0_124.
 */
package androidx.databinding;

import java.util.Map;

public interface ObservableMap<K, V>
extends Map<K, V> {
    public void addOnMapChangedCallback(OnMapChangedCallback<? extends ObservableMap<K, V>, K, V> var1);

    public void removeOnMapChangedCallback(OnMapChangedCallback<? extends ObservableMap<K, V>, K, V> var1);

    public static abstract class OnMapChangedCallback<T extends ObservableMap<K, V>, K, V> {
        public abstract void onMapChanged(T var1, K var2);
    }

}

