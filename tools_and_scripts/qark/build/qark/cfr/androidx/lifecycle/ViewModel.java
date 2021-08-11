/*
 * Decompiled with CFR 0_124.
 */
package androidx.lifecycle;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class ViewModel {
    private final Map<String, Object> mBagOfTags = new HashMap<String, Object>();
    private volatile boolean mCleared = false;

    private static void closeWithRuntimeException(Object object) {
        if (object instanceof Closeable) {
            try {
                ((Closeable)object).close();
                return;
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    final void clear() {
        this.mCleared = true;
        Map<String, Object> map = this.mBagOfTags;
        if (map != null) {
            synchronized (map) {
                Iterator<Object> iterator = this.mBagOfTags.values().iterator();
                while (iterator.hasNext()) {
                    ViewModel.closeWithRuntimeException(iterator.next());
                }
            }
        }
        this.onCleared();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    <T> T getTag(String object) {
        Map<String, Object> map = this.mBagOfTags;
        if (map == null) {
            return null;
        }
        synchronized (map) {
            object = this.mBagOfTags.get(object);
            return (T)object;
        }
    }

    protected void onCleared() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    <T> T setTagIfAbsent(String object, T t) {
        Object object2;
        Map<String, Object> map = this.mBagOfTags;
        synchronized (map) {
            object2 = this.mBagOfTags.get(object);
            if (object2 == null) {
                this.mBagOfTags.put((String)object, t);
            }
        }
        object = object2 == null ? t : object2;
        if (this.mCleared) {
            ViewModel.closeWithRuntimeException(object);
        }
        return (T)object;
    }
}

