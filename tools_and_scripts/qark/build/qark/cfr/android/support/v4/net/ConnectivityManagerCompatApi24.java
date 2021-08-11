/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.net.ConnectivityManager
 */
package android.support.v4.net;

import android.net.ConnectivityManager;

class ConnectivityManagerCompatApi24 {
    ConnectivityManagerCompatApi24() {
    }

    public static int getRestrictBackgroundStatus(ConnectivityManager connectivityManager) {
        return connectivityManager.getRestrictBackgroundStatus();
    }
}

