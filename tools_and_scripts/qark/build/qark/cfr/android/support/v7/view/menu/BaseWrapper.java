/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.view.menu;

class BaseWrapper<T> {
    final T mWrappedObject;

    BaseWrapper(T t) {
        if (t != null) {
            this.mWrappedObject = t;
            return;
        }
        throw new IllegalArgumentException("Wrapped Object can not be null.");
    }

    public T getWrappedObject() {
        return this.mWrappedObject;
    }
}

