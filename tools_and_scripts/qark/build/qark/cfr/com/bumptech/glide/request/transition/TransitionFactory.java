/*
 * Decompiled with CFR 0_124.
 */
package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.Transition;

public interface TransitionFactory<R> {
    public Transition<R> build(DataSource var1, boolean var2);
}

