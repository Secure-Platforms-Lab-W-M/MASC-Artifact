// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.activity;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleEventObserver;
import java.util.Iterator;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayDeque;

public final class OnBackPressedDispatcher
{
    private final Runnable mFallbackOnBackPressed;
    final ArrayDeque<OnBackPressedCallback> mOnBackPressedCallbacks;
    
    public OnBackPressedDispatcher() {
        this(null);
    }
    
    public OnBackPressedDispatcher(final Runnable mFallbackOnBackPressed) {
        this.mOnBackPressedCallbacks = new ArrayDeque<OnBackPressedCallback>();
        this.mFallbackOnBackPressed = mFallbackOnBackPressed;
    }
    
    public void addCallback(final OnBackPressedCallback onBackPressedCallback) {
        this.addCancellableCallback(onBackPressedCallback);
    }
    
    public void addCallback(final LifecycleOwner lifecycleOwner, final OnBackPressedCallback onBackPressedCallback) {
        final Lifecycle lifecycle = lifecycleOwner.getLifecycle();
        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        onBackPressedCallback.addCancellable(new LifecycleOnBackPressedCancellable(lifecycle, onBackPressedCallback));
    }
    
    Cancellable addCancellableCallback(final OnBackPressedCallback onBackPressedCallback) {
        this.mOnBackPressedCallbacks.add(onBackPressedCallback);
        final OnBackPressedCancellable onBackPressedCancellable = new OnBackPressedCancellable(onBackPressedCallback);
        onBackPressedCallback.addCancellable(onBackPressedCancellable);
        return onBackPressedCancellable;
    }
    
    public boolean hasEnabledCallbacks() {
        final Iterator<OnBackPressedCallback> descendingIterator = this.mOnBackPressedCallbacks.descendingIterator();
        while (descendingIterator.hasNext()) {
            if (descendingIterator.next().isEnabled()) {
                return true;
            }
        }
        return false;
    }
    
    public void onBackPressed() {
        final Iterator<OnBackPressedCallback> descendingIterator = this.mOnBackPressedCallbacks.descendingIterator();
        while (descendingIterator.hasNext()) {
            final OnBackPressedCallback onBackPressedCallback = descendingIterator.next();
            if (onBackPressedCallback.isEnabled()) {
                onBackPressedCallback.handleOnBackPressed();
                return;
            }
        }
        final Runnable mFallbackOnBackPressed = this.mFallbackOnBackPressed;
        if (mFallbackOnBackPressed != null) {
            mFallbackOnBackPressed.run();
        }
    }
    
    private class LifecycleOnBackPressedCancellable implements LifecycleEventObserver, Cancellable
    {
        private Cancellable mCurrentCancellable;
        private final Lifecycle mLifecycle;
        private final OnBackPressedCallback mOnBackPressedCallback;
        
        LifecycleOnBackPressedCancellable(final Lifecycle mLifecycle, final OnBackPressedCallback mOnBackPressedCallback) {
            this.mLifecycle = mLifecycle;
            this.mOnBackPressedCallback = mOnBackPressedCallback;
            mLifecycle.addObserver(this);
        }
        
        @Override
        public void cancel() {
            this.mLifecycle.removeObserver(this);
            this.mOnBackPressedCallback.removeCancellable(this);
            final Cancellable mCurrentCancellable = this.mCurrentCancellable;
            if (mCurrentCancellable != null) {
                mCurrentCancellable.cancel();
                this.mCurrentCancellable = null;
            }
        }
        
        @Override
        public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_START) {
                this.mCurrentCancellable = OnBackPressedDispatcher.this.addCancellableCallback(this.mOnBackPressedCallback);
                return;
            }
            if (event == Lifecycle.Event.ON_STOP) {
                final Cancellable mCurrentCancellable = this.mCurrentCancellable;
                if (mCurrentCancellable != null) {
                    mCurrentCancellable.cancel();
                }
            }
            else if (event == Lifecycle.Event.ON_DESTROY) {
                this.cancel();
            }
        }
    }
    
    private class OnBackPressedCancellable implements Cancellable
    {
        private final OnBackPressedCallback mOnBackPressedCallback;
        
        OnBackPressedCancellable(final OnBackPressedCallback mOnBackPressedCallback) {
            this.mOnBackPressedCallback = mOnBackPressedCallback;
        }
        
        @Override
        public void cancel() {
            OnBackPressedDispatcher.this.mOnBackPressedCallbacks.remove(this.mOnBackPressedCallback);
            this.mOnBackPressedCallback.removeCancellable(this);
        }
    }
}
