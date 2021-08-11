/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.load.model;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

public interface ModelLoaderFactory<T, Y> {
    public ModelLoader<T, Y> build(MultiModelLoaderFactory var1);

    public void teardown();
}

