/*
 * Decompiled with CFR 0_124.
 */
package android.arch.lifecycle;

import android.arch.core.internal.FastSafeIterableMap;
import android.arch.core.internal.SafeIterableMap;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Lifecycling;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class LifecycleRegistry
extends Lifecycle {
    private int mAddingObserverCounter = 0;
    private boolean mHandlingEvent = false;
    private final LifecycleOwner mLifecycleOwner;
    private boolean mNewEventOccurred = false;
    private FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap = new FastSafeIterableMap();
    private ArrayList<Lifecycle.State> mParentStates = new ArrayList();
    private Lifecycle.State mState;

    public LifecycleRegistry(@NonNull LifecycleOwner lifecycleOwner) {
        this.mLifecycleOwner = lifecycleOwner;
        this.mState = Lifecycle.State.INITIALIZED;
    }

    private void backwardPass() {
        Iterator<Map.Entry<LifecycleObserver, ObserverWithState>> iterator = this.mObserverMap.descendingIterator();
        while (iterator.hasNext() && !this.mNewEventOccurred) {
            Map.Entry<LifecycleObserver, ObserverWithState> entry = iterator.next();
            ObserverWithState observerWithState = entry.getValue();
            while (observerWithState.mState.compareTo(this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains(entry.getKey())) {
                Lifecycle.Event event = LifecycleRegistry.downEvent(observerWithState.mState);
                this.pushParentState(LifecycleRegistry.getStateAfter(event));
                observerWithState.dispatchEvent(this.mLifecycleOwner, event);
                this.popParentState();
            }
        }
    }

    private Lifecycle.State calculateTargetState(LifecycleObserver object) {
        object = this.mObserverMap.ceil((LifecycleObserver)object);
        Object object2 = null;
        object = object != null ? ((ObserverWithState)object.getValue()).mState : null;
        if (!this.mParentStates.isEmpty()) {
            object2 = this.mParentStates;
            object2 = object2.get(object2.size() - 1);
        }
        return LifecycleRegistry.min(LifecycleRegistry.min(this.mState, (Lifecycle.State)((Object)object)), object2);
    }

    private static Lifecycle.Event downEvent(Lifecycle.State state) {
        switch (state) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected state value ");
                stringBuilder.append((Object)state);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            case DESTROYED: {
                throw new IllegalArgumentException();
            }
            case RESUMED: {
                return Lifecycle.Event.ON_PAUSE;
            }
            case STARTED: {
                return Lifecycle.Event.ON_STOP;
            }
            case CREATED: {
                return Lifecycle.Event.ON_DESTROY;
            }
            case INITIALIZED: 
        }
        throw new IllegalArgumentException();
    }

    private void forwardPass() {
        SafeIterableMap.IteratorWithAdditions iteratorWithAdditions = this.mObserverMap.iteratorWithAdditions();
        while (iteratorWithAdditions.hasNext() && !this.mNewEventOccurred) {
            Map.Entry entry = (Map.Entry)iteratorWithAdditions.next();
            ObserverWithState observerWithState = (ObserverWithState)entry.getValue();
            while (observerWithState.mState.compareTo(this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains((LifecycleObserver)entry.getKey())) {
                this.pushParentState(observerWithState.mState);
                observerWithState.dispatchEvent(this.mLifecycleOwner, LifecycleRegistry.upEvent(observerWithState.mState));
                this.popParentState();
            }
        }
    }

    static Lifecycle.State getStateAfter(Lifecycle.Event event) {
        switch (event) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected event value ");
                stringBuilder.append((Object)event);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            case ON_DESTROY: {
                return Lifecycle.State.DESTROYED;
            }
            case ON_RESUME: {
                return Lifecycle.State.RESUMED;
            }
            case ON_START: 
            case ON_PAUSE: {
                return Lifecycle.State.STARTED;
            }
            case ON_CREATE: 
            case ON_STOP: 
        }
        return Lifecycle.State.CREATED;
    }

    private boolean isSynced() {
        if (this.mObserverMap.size() == 0) {
            return true;
        }
        Lifecycle.State state = this.mObserverMap.eldest().getValue().mState;
        Lifecycle.State state2 = this.mObserverMap.newest().getValue().mState;
        if (state == state2 && this.mState == state2) {
            return true;
        }
        return false;
    }

    static Lifecycle.State min(@NonNull Lifecycle.State state, @Nullable Lifecycle.State state2) {
        if (state2 != null && state2.compareTo(state) < 0) {
            return state2;
        }
        return state;
    }

    private void popParentState() {
        ArrayList<Lifecycle.State> arrayList = this.mParentStates;
        arrayList.remove(arrayList.size() - 1);
    }

    private void pushParentState(Lifecycle.State state) {
        this.mParentStates.add(state);
    }

    private void sync() {
        while (!this.isSynced()) {
            this.mNewEventOccurred = false;
            if (this.mState.compareTo(this.mObserverMap.eldest().getValue().mState) < 0) {
                this.backwardPass();
            }
            Map.Entry<LifecycleObserver, ObserverWithState> entry = this.mObserverMap.newest();
            if (this.mNewEventOccurred || entry == null || this.mState.compareTo(entry.getValue().mState) <= 0) continue;
            this.forwardPass();
        }
        this.mNewEventOccurred = false;
    }

    private static Lifecycle.Event upEvent(Lifecycle.State state) {
        switch (state) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected state value ");
                stringBuilder.append((Object)state);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            case RESUMED: {
                throw new IllegalArgumentException();
            }
            case STARTED: {
                return Lifecycle.Event.ON_RESUME;
            }
            case CREATED: {
                return Lifecycle.Event.ON_START;
            }
            case INITIALIZED: 
            case DESTROYED: 
        }
        return Lifecycle.Event.ON_CREATE;
    }

    @Override
    public void addObserver(LifecycleObserver lifecycleObserver) {
        Lifecycle.State state = this.mState == Lifecycle.State.DESTROYED ? Lifecycle.State.DESTROYED : Lifecycle.State.INITIALIZED;
        ObserverWithState observerWithState = new ObserverWithState(lifecycleObserver, state);
        if (this.mObserverMap.putIfAbsent(lifecycleObserver, observerWithState) != null) {
            return;
        }
        boolean bl = this.mAddingObserverCounter != 0 || this.mHandlingEvent;
        state = this.calculateTargetState(lifecycleObserver);
        ++this.mAddingObserverCounter;
        while (observerWithState.mState.compareTo(state) < 0 && this.mObserverMap.contains(lifecycleObserver)) {
            this.pushParentState(observerWithState.mState);
            observerWithState.dispatchEvent(this.mLifecycleOwner, LifecycleRegistry.upEvent(observerWithState.mState));
            this.popParentState();
            state = this.calculateTargetState(lifecycleObserver);
        }
        if (!bl) {
            this.sync();
        }
        --this.mAddingObserverCounter;
    }

    @Override
    public Lifecycle.State getCurrentState() {
        return this.mState;
    }

    public int getObserverCount() {
        return this.mObserverMap.size();
    }

    public void handleLifecycleEvent(Lifecycle.Event event) {
        this.mState = LifecycleRegistry.getStateAfter(event);
        if (!this.mHandlingEvent && this.mAddingObserverCounter == 0) {
            this.mHandlingEvent = true;
            this.sync();
            this.mHandlingEvent = false;
            return;
        }
        this.mNewEventOccurred = true;
    }

    public void markState(Lifecycle.State state) {
        this.mState = state;
    }

    @Override
    public void removeObserver(LifecycleObserver lifecycleObserver) {
        this.mObserverMap.remove(lifecycleObserver);
    }

    static class ObserverWithState {
        GenericLifecycleObserver mLifecycleObserver;
        Lifecycle.State mState;

        ObserverWithState(LifecycleObserver lifecycleObserver, Lifecycle.State state) {
            this.mLifecycleObserver = Lifecycling.getCallback(lifecycleObserver);
            this.mState = state;
        }

        void dispatchEvent(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
            Lifecycle.State state = LifecycleRegistry.getStateAfter(event);
            this.mState = LifecycleRegistry.min(this.mState, state);
            this.mLifecycleObserver.onStateChanged(lifecycleOwner, event);
            this.mState = state;
        }
    }

}

