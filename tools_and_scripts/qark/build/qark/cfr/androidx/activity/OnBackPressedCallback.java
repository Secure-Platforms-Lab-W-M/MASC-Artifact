/*
 * Decompiled with CFR 0_124.
 */
package androidx.activity;

import androidx.activity.Cancellable;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class OnBackPressedCallback {
    private CopyOnWriteArrayList<Cancellable> mCancellables = new CopyOnWriteArrayList();
    private boolean mEnabled;

    public OnBackPressedCallback(boolean bl) {
        this.mEnabled = bl;
    }

    void addCancellable(Cancellable cancellable) {
        this.mCancellables.add(cancellable);
    }

    public abstract void handleOnBackPressed();

    public final boolean isEnabled() {
        return this.mEnabled;
    }

    public final void remove() {
        Iterator<Cancellable> iterator = this.mCancellables.iterator();
        while (iterator.hasNext()) {
            iterator.next().cancel();
        }
    }

    void removeCancellable(Cancellable cancellable) {
        this.mCancellables.remove(cancellable);
    }

    public final void setEnabled(boolean bl) {
        this.mEnabled = bl;
    }
}

