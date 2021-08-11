package com.bumptech.glide.manager;

import android.content.Context;

public interface ConnectivityMonitorFactory {
   ConnectivityMonitor build(Context var1, ConnectivityMonitor.ConnectivityListener var2);
}
