/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.manager;

import android.util.Log;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public class RequestTracker {
    private static final String TAG = "RequestTracker";
    private boolean isPaused;
    private final List<Request> pendingRequests = new ArrayList<Request>();
    private final Set<Request> requests = Collections.newSetFromMap(new WeakHashMap());

    void addRequest(Request request) {
        this.requests.add(request);
    }

    public boolean clearAndRemove(Request request) {
        boolean bl = true;
        if (request == null) {
            return true;
        }
        boolean bl2 = this.requests.remove(request);
        boolean bl3 = bl;
        if (!this.pendingRequests.remove(request)) {
            bl3 = bl2 ? bl : false;
        }
        if (bl3) {
            request.clear();
        }
        return bl3;
    }

    public void clearRequests() {
        Iterator<Request> iterator = Util.getSnapshot(this.requests).iterator();
        while (iterator.hasNext()) {
            this.clearAndRemove(iterator.next());
        }
        this.pendingRequests.clear();
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    public void pauseAllRequests() {
        this.isPaused = true;
        for (Request request : Util.getSnapshot(this.requests)) {
            if (!request.isRunning() && !request.isComplete()) continue;
            request.clear();
            this.pendingRequests.add(request);
        }
    }

    public void pauseRequests() {
        this.isPaused = true;
        for (Request request : Util.getSnapshot(this.requests)) {
            if (!request.isRunning()) continue;
            request.pause();
            this.pendingRequests.add(request);
        }
    }

    public void restartRequests() {
        for (Request request : Util.getSnapshot(this.requests)) {
            if (request.isComplete() || request.isCleared()) continue;
            request.clear();
            if (!this.isPaused) {
                request.begin();
                continue;
            }
            this.pendingRequests.add(request);
        }
    }

    public void resumeRequests() {
        this.isPaused = false;
        for (Request request : Util.getSnapshot(this.requests)) {
            if (request.isComplete() || request.isRunning()) continue;
            request.begin();
        }
        this.pendingRequests.clear();
    }

    public void runRequest(Request request) {
        this.requests.add(request);
        if (!this.isPaused) {
            request.begin();
            return;
        }
        request.clear();
        if (Log.isLoggable((String)"RequestTracker", (int)2)) {
            Log.v((String)"RequestTracker", (String)"Paused, delaying request");
        }
        this.pendingRequests.add(request);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("{numRequests=");
        stringBuilder.append(this.requests.size());
        stringBuilder.append(", isPaused=");
        stringBuilder.append(this.isPaused);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

