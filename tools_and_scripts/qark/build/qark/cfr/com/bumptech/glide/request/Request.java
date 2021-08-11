/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.request;

public interface Request {
    public void begin();

    public void clear();

    public boolean isAnyResourceSet();

    public boolean isCleared();

    public boolean isComplete();

    public boolean isEquivalentTo(Request var1);

    public boolean isRunning();

    public void pause();
}

