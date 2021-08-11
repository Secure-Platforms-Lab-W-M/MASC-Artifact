/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.request;

import com.bumptech.glide.request.Request;

public interface RequestCoordinator {
    public boolean canNotifyCleared(Request var1);

    public boolean canNotifyStatusChanged(Request var1);

    public boolean canSetImage(Request var1);

    public RequestCoordinator getRoot();

    public boolean isAnyResourceSet();

    public void onRequestFailed(Request var1);

    public void onRequestSuccess(Request var1);

    public static final class RequestState
    extends Enum<RequestState> {
        private static final /* synthetic */ RequestState[] $VALUES;
        public static final /* enum */ RequestState CLEARED;
        public static final /* enum */ RequestState FAILED;
        public static final /* enum */ RequestState PAUSED;
        public static final /* enum */ RequestState RUNNING;
        public static final /* enum */ RequestState SUCCESS;
        private final boolean isComplete;

        static {
            RequestState requestState;
            RUNNING = new RequestState(false);
            PAUSED = new RequestState(false);
            CLEARED = new RequestState(false);
            SUCCESS = new RequestState(true);
            FAILED = requestState = new RequestState(true);
            $VALUES = new RequestState[]{RUNNING, PAUSED, CLEARED, SUCCESS, requestState};
        }

        private RequestState(boolean bl) {
            this.isComplete = bl;
        }

        public static RequestState valueOf(String string2) {
            return Enum.valueOf(RequestState.class, string2);
        }

        public static RequestState[] values() {
            return (RequestState[])$VALUES.clone();
        }

        boolean isComplete() {
            return this.isComplete;
        }
    }

}

