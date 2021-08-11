// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.net;

import android.net.NetworkInfo;
import android.net.ConnectivityManager;

class ConnectivityManagerCompatHoneycombMR2
{
    public static boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            switch (activeNetworkInfo.getType()) {
                case 0:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6: {
                    break;
                }
                default: {
                    return true;
                }
                case 1:
                case 7:
                case 9: {
                    return false;
                }
            }
        }
        return true;
    }
}
