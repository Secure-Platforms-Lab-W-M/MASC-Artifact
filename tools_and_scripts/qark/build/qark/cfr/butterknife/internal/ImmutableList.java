/*
 * Decompiled with CFR 0_124.
 */
package butterknife.internal;

import java.util.AbstractList;
import java.util.RandomAccess;

final class ImmutableList<T>
extends AbstractList<T>
implements RandomAccess {
    private final T[] views;

    ImmutableList(T[] arrT) {
        this.views = arrT;
    }

    @Override
    public boolean contains(Object object) {
        T[] arrT = this.views;
        int n = arrT.length;
        for (int i = 0; i < n; ++i) {
            if (arrT[i] != object) continue;
            return true;
        }
        return false;
    }

    @Override
    public T get(int n) {
        return this.views[n];
    }

    @Override
    public int size() {
        return this.views.length;
    }
}

