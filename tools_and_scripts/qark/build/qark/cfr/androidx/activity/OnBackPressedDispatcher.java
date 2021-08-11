/*
 * Decompiled with CFR 0_124.
 */
package androidx.activity;

import androidx.activity.Cancellable;
import androidx.activity.OnBackPressedCallback;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayDeque;
import java.util.Iterator;

public final class OnBackPressedDispatcher {
    private final Runnable mFallbackOnBackPressed;
    final ArrayDeque<OnBackPressedCallback> mOnBackPressedCallbacks = new ArrayDeque();

    public OnBackPressedDispatcher() {
        this(null);
    }

    public OnBackPressedDispatcher(Runnable runnable) {
        this.mFallbackOnBackPressed = runnable;
    }

    public void addCallback(OnBackPressedCallback onBackPressedCallback) {
        this.addCancellableCallback(onBackPressedCallback);
    }

    public void addCallback(LifecycleOwner object, OnBackPressedCallback onBackPressedCallback) {
        if ((object = object.getLifecycle()).getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        onBackPressedCallback.addCancellable(new LifecycleOnBackPressedCancellable((Lifecycle)object, onBackPressedCallback));
    }

    Cancellable addCancellableCallback(OnBackPressedCallback onBackPressedCallback) {
        this.mOnBackPressedCallbacks.add(onBackPressedCallback);
        OnBackPressedCancellable onBackPressedCancellable = new OnBackPressedCancellable(onBackPressedCallback);
        onBackPressedCallback.addCancellable(onBackPressedCancellable);
        return onBackPressedCancellable;
    }

    public boolean hasEnabledCallbacks() {
        Iterator<OnBackPressedCallback> iterator = this.mOnBackPressedCallbacks.descendingIterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isEnabled()) continue;
            return true;
        }
        return false;
    }

    public void onBackPressed() {
        Object object = this.mOnBackPressedCallbacks.descendingIterator();
        while (object.hasNext()) {
            OnBackPressedCallback onBackPressedCallback = object.next();
            if (!onBackPressedCallback.isEnabled()) continue;
            onBackPressedCallback.handleOnBackPressed();
            return;
        }
        object = this.mFallbackOnBackPressed;
        if (object != null) {
            object.run();
        }
    }

    private class LifecycleOnBackPressedCancellable
    implements LifecycleEventObserver,
    Cancellable {
        private Cancellable mCurrentCancellable;
        private final Lifecycle mLifecycle;
        private final OnBackPressedCallback mOnBackPressedCallback;

        LifecycleOnBackPressedCancellable(Lifecycle lifecycle, OnBackPressedCallback onBackPressedCallback) {
            this.mLifecycle = lifecycle;
            this.mOnBackPressedCallback = onBackPressedCallback;
            lifecycle.addObserver(this);
        }

        @Override
        public void cancel() {
            this.mLifecycle.removeObserver(this);
            this.mOnBackPressedCallback.removeCancellable(this);
            Cancellable cancellable = this.mCurrentCancellable;
            if (cancellable != null) {
                cancellable.cancel();
                this.mCurrentCancellable = null;
            }
        }

        @Override
        public void onStateChanged(LifecycleOwner object, Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_START) {
                this.mCurrentCancellable = OnBackPressedDispatcher.this.addCancellableCallback(this.mOnBackPressedCallback);
                return;
            }
            if (event == Lifecycle.Event.ON_STOP) {
                object = this.mCurrentCancellable;
                if (object != null) {
                    object.cancel();
                    return;
                }
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                this.cancel();
            }
        }
    }

    private class OnBackPressedCancellable
    implements Cancellable {
        private final OnBackPressedCallback mOnBackPressedCallback;

        OnBackPressedCancellable(OnBackPressedCallback onBackPressedCallback) {
            this.mOnBackPressedCallback = onBackPressedCallback;
        }

        @Override
        public void cancel() {
            OnBackPressedDispatcher.this.mOnBackPressedCallbacks.remove(this.mOnBackPressedCallback);
            this.mOnBackPressedCallback.removeCancellable(this);
        }
    }

}

