/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.engine;

public interface Resource<Z> {
    public Z get();

    public Class<Z> getResourceClass();

    public int getSize();

    public void recycle();
}

