/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 */
package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class ConnectivityManagerCompatHoneycombMR2 {
    ConnectivityManagerCompatHoneycombMR2() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
        if ((connectivityManager = connectivityManager.getActiveNetworkInfo()) == null) {
            return true;
        }
        switch (connectivityManager.getType()) {
            default: {
                return true;
            }
            case 1: 
            case 7: 
            case 9: {
                return false;
            }
            case 0: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
        }
        return true;
    }
}

