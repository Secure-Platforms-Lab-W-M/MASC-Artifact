// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.net;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.os.Build$VERSION;
import android.net.NetworkInfo;
import android.content.Intent;
import android.net.ConnectivityManager;

public final class ConnectivityManagerCompat
{
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;
    
    private ConnectivityManagerCompat() {
    }
    
    public static NetworkInfo getNetworkInfoFromBroadcast(final ConnectivityManager connectivityManager, final Intent intent) {
        final NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
        if (networkInfo != null) {
            return connectivityManager.getNetworkInfo(networkInfo.getType());
        }
        return null;
    }
    
    public static int getRestrictBackgroundStatus(final ConnectivityManager connectivityManager) {
        if (Build$VERSION.SDK_INT >= 24) {
            return connectivityManager.getRestrictBackgroundStatus();
        }
        return 3;
    }
    
    public static boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
        if (Build$VERSION.SDK_INT >= 16) {
            return connectivityManager.isActiveNetworkMetered();
        }
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return true;
        }
        final int type = activeNetworkInfo.getType();
        return type != 1 && type != 7 && type != 9;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface RestrictBackgroundStatus {
    }
}
