// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

import android.support.annotation.Nullable;
import android.arch.core.internal.SafeIterableMap;
import java.util.Iterator;
import java.util.Map;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import android.arch.core.internal.FastSafeIterableMap;

public class LifecycleRegistry extends Lifecycle
{
    private int mAddingObserverCounter;
    private boolean mHandlingEvent;
    private final LifecycleOwner mLifecycleOwner;
    private boolean mNewEventOccurred;
    private FastSafeIterableMap<LifecycleObserver, ObserverWithState> mObserverMap;
    private ArrayList<State> mParentStates;
    private State mState;
    
    public LifecycleRegistry(@NonNull final LifecycleOwner mLifecycleOwner) {
        this.mObserverMap = new FastSafeIterableMap<LifecycleObserver, ObserverWithState>();
        this.mAddingObserverCounter = 0;
        this.mHandlingEvent = false;
        this.mNewEventOccurred = false;
        this.mParentStates = new ArrayList<State>();
        this.mLifecycleOwner = mLifecycleOwner;
        this.mState = State.INITIALIZED;
    }
    
    private void backwardPass() {
        final Iterator<Map.Entry<LifecycleObserver, ObserverWithState>> descendingIterator = (Iterator<Map.Entry<LifecycleObserver, ObserverWithState>>)this.mObserverMap.descendingIterator();
        while (descendingIterator.hasNext() && !this.mNewEventOccurred) {
            final Map.Entry<LifecycleObserver, ObserverWithState> entry = descendingIterator.next();
            final ObserverWithState observerWithState = entry.getValue();
            while (observerWithState.mState.compareTo(this.mState) > 0 && !this.mNewEventOccurred && this.mObserverMap.contains(entry.getKey())) {
                final Event downEvent = downEvent(observerWithState.mState);
                this.pushParentState(getStateAfter(downEvent));
                observerWithState.dispatchEvent(this.mLifecycleOwner, downEvent);
                this.popParentState();
            }
        }
    }
    
    private State calculateTargetState(final LifecycleObserver lifecycleObserver) {
        final Map.Entry<LifecycleObserver, ObserverWithState> ceil = this.mObserverMap.ceil(lifecycleObserver);
        State state = null;
        State mState;
        if (ceil != null) {
            mState = ceil.getValue().mState;
        }
        else {
            mState = null;
        }
        if (!this.mParentStates.isEmpty()) {
            final ArrayList<State> mParentStates = this.mParentStates;
            state = mParentStates.get(mParentStates.size() - 1);
        }
        return min(min(this.mState, mState), state);
    }
    
    private static Event downEvent(final State state) {
        switch (state) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected state value ");
                sb.append(state);
                throw new IllegalArgumentException(sb.toString());
            }
            case DESTROYED: {
                throw new IllegalArgumentException();
            }
            case RESUMED: {
                return Event.ON_PAUSE;
            }
            case STARTED: {
                return Event.ON_STOP;
            }
            case CREATED: {
                return Event.ON_DESTROY;
            }
            case INITIALIZED: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    private void forwardPass() {
        final SafeIterableMap.IteratorWithAdditions iteratorWithAdditions = this.mObserverMap.iteratorWithAdditions();
        while (iteratorWithAdditions.hasNext() && !this.mNewEventOccurred) {
            final Map.Entry<K, ObserverWithState> entry = ((Iterator<Map.Entry<K, ObserverWithState>>)iteratorWithAdditions).next();
            final ObserverWithState observerWithState = entry.getValue();
            while (observerWithState.mState.compareTo(this.mState) < 0 && !this.mNewEventOccurred && this.mObserverMap.contains((LifecycleObserver)entry.getKey())) {
                this.pushParentState(observerWithState.mState);
                observerWithState.dispatchEvent(this.mLifecycleOwner, upEvent(observerWithState.mState));
                this.popParentState();
            }
        }
    }
    
    static State getStateAfter(final Event event) {
        switch (event) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected event value ");
                sb.append(event);
                throw new IllegalArgumentException(sb.toString());
            }
            case ON_DESTROY: {
                return State.DESTROYED;
            }
            case ON_RESUME: {
                return State.RESUMED;
            }
            case ON_START:
            case ON_PAUSE: {
                return State.STARTED;
            }
            case ON_CREATE:
            case ON_STOP: {
                return State.CREATED;
            }
        }
    }
    
    private boolean isSynced() {
        if (this.mObserverMap.size() == 0) {
            return true;
        }
        final State mState = this.mObserverMap.eldest().getValue().mState;
        final State mState2 = this.mObserverMap.newest().getValue().mState;
        return mState == mState2 && this.mState == mState2;
    }
    
    static State min(@NonNull final State state, @Nullable final State state2) {
        if (state2 != null && state2.compareTo(state) < 0) {
            return state2;
        }
        return state;
    }
    
    private void popParentState() {
        final ArrayList<State> mParentStates = this.mParentStates;
        mParentStates.remove(mParentStates.size() - 1);
    }
    
    private void pushParentState(final State state) {
        this.mParentStates.add(state);
    }
    
    private void sync() {
        while (!this.isSynced()) {
            this.mNewEventOccurred = false;
            if (this.mState.compareTo(this.mObserverMap.eldest().getValue().mState) < 0) {
                this.backwardPass();
            }
            final Map.Entry<LifecycleObserver, ObserverWithState> newest = this.mObserverMap.newest();
            if (!this.mNewEventOccurred && newest != null) {
                if (this.mState.compareTo(newest.getValue().mState) <= 0) {
                    continue;
                }
                this.forwardPass();
            }
        }
        this.mNewEventOccurred = false;
    }
    
    private static Event upEvent(final State state) {
        switch (state) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected state value ");
                sb.append(state);
                throw new IllegalArgumentException(sb.toString());
            }
            case RESUMED: {
                throw new IllegalArgumentException();
            }
            case STARTED: {
                return Event.ON_RESUME;
            }
            case CREATED: {
                return Event.ON_START;
            }
            case INITIALIZED:
            case DESTROYED: {
                return Event.ON_CREATE;
            }
        }
    }
    
    @Override
    public void addObserver(final LifecycleObserver lifecycleObserver) {
        State state;
        if (this.mState == State.DESTROYED) {
            state = State.DESTROYED;
        }
        else {
            state = State.INITIALIZED;
        }
        final ObserverWithState observerWithState = new ObserverWithState(lifecycleObserver, state);
        if (this.mObserverMap.putIfAbsent(lifecycleObserver, observerWithState) != null) {
            return;
        }
        final boolean b = this.mAddingObserverCounter != 0 || this.mHandlingEvent;
        State state2 = this.calculateTargetState(lifecycleObserver);
        ++this.mAddingObserverCounter;
        while (observerWithState.mState.compareTo(state2) < 0 && this.mObserverMap.contains(lifecycleObserver)) {
            this.pushParentState(observerWithState.mState);
            observerWithState.dispatchEvent(this.mLifecycleOwner, upEvent(observerWithState.mState));
            this.popParentState();
            state2 = this.calculateTargetState(lifecycleObserver);
        }
        if (!b) {
            this.sync();
        }
        --this.mAddingObserverCounter;
    }
    
    @Override
    public State getCurrentState() {
        return this.mState;
    }
    
    public int getObserverCount() {
        return this.mObserverMap.size();
    }
    
    public void handleLifecycleEvent(final Event event) {
        this.mState = getStateAfter(event);
        if (!this.mHandlingEvent && this.mAddingObserverCounter == 0) {
            this.mHandlingEvent = true;
            this.sync();
            this.mHandlingEvent = false;
            return;
        }
        this.mNewEventOccurred = true;
    }
    
    public void markState(final State mState) {
        this.mState = mState;
    }
    
    @Override
    public void removeObserver(final LifecycleObserver lifecycleObserver) {
        this.mObserverMap.remove(lifecycleObserver);
    }
    
    static class ObserverWithState
    {
        GenericLifecycleObserver mLifecycleObserver;
        State mState;
        
        ObserverWithState(final LifecycleObserver lifecycleObserver, final State mState) {
            this.mLifecycleObserver = Lifecycling.getCallback(lifecycleObserver);
            this.mState = mState;
        }
        
        void dispatchEvent(final LifecycleOwner lifecycleOwner, final Event event) {
            final State stateAfter = LifecycleRegistry.getStateAfter(event);
            this.mState = LifecycleRegistry.min(this.mState, stateAfter);
            this.mLifecycleObserver.onStateChanged(lifecycleOwner, event);
            this.mState = stateAfter;
        }
    }
}
