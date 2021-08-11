/*
 * Decompiled with CFR 0_124.
 */
package androidx.lifecycle;

import androidx.lifecycle.LifecycleObserver;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Lifecycle {
    AtomicReference<Object> mInternalScopeRef = new AtomicReference();

    public abstract void addObserver(LifecycleObserver var1);

    public abstract State getCurrentState();

    public abstract void removeObserver(LifecycleObserver var1);

    public static final class Event
    extends Enum<Event> {
        private static final /* synthetic */ Event[] $VALUES;
        public static final /* enum */ Event ON_ANY;
        public static final /* enum */ Event ON_CREATE;
        public static final /* enum */ Event ON_DESTROY;
        public static final /* enum */ Event ON_PAUSE;
        public static final /* enum */ Event ON_RESUME;
        public static final /* enum */ Event ON_START;
        public static final /* enum */ Event ON_STOP;

        static {
            Event event;
            ON_CREATE = new Event();
            ON_START = new Event();
            ON_RESUME = new Event();
            ON_PAUSE = new Event();
            ON_STOP = new Event();
            ON_DESTROY = new Event();
            ON_ANY = event = new Event();
            $VALUES = new Event[]{ON_CREATE, ON_START, ON_RESUME, ON_PAUSE, ON_STOP, ON_DESTROY, event};
        }

        private Event() {
        }

        public static Event valueOf(String string2) {
            return Enum.valueOf(Event.class, string2);
        }

        public static Event[] values() {
            return (Event[])$VALUES.clone();
        }
    }

    public static final class State
    extends Enum<State> {
        private static final /* synthetic */ State[] $VALUES;
        public static final /* enum */ State CREATED;
        public static final /* enum */ State DESTROYED;
        public static final /* enum */ State INITIALIZED;
        public static final /* enum */ State RESUMED;
        public static final /* enum */ State STARTED;

        static {
            State state;
            DESTROYED = new State();
            INITIALIZED = new State();
            CREATED = new State();
            STARTED = new State();
            RESUMED = state = new State();
            $VALUES = new State[]{DESTROYED, INITIALIZED, CREATED, STARTED, state};
        }

        private State() {
        }

        public static State valueOf(String string2) {
            return Enum.valueOf(State.class, string2);
        }

        public static State[] values() {
            return (State[])$VALUES.clone();
        }

        public boolean isAtLeast(State state) {
            if (this.compareTo(state) >= 0) {
                return true;
            }
            return false;
        }
    }

}

