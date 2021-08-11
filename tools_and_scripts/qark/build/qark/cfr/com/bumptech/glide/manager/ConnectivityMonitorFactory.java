/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.bumptech.glide.manager;

import android.content.Context;
import com.bumptech.glide.manager.ConnectivityMonitor;

public interface ConnectivityMonitorFactory {
    public ConnectivityMonitor build(Context var1, ConnectivityMonitor.ConnectivityListener var2);
}

