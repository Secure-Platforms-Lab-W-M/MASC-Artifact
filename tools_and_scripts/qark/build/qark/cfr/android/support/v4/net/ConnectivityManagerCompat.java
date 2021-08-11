/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcelable
 */
package android.support.v4.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.annotation.RestrictTo;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ConnectivityManagerCompat {
    private static final ConnectivityManagerCompatImpl IMPL = Build.VERSION.SDK_INT >= 24 ? new ConnectivityManagerCompatApi24Impl() : (Build.VERSION.SDK_INT >= 16 ? new ConnectivityManagerCompatApi16Impl() : new ConnectivityManagerCompatBaseImpl());
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;

    private ConnectivityManagerCompat() {
    }

    public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager connectivityManager, Intent intent) {
        if ((intent = (NetworkInfo)intent.getParcelableExtra("networkInfo")) != null) {
            return connectivityManager.getNetworkInfo(intent.getType());
        }
        return null;
    }

    public static int getRestrictBackgroundStatus(ConnectivityManager connectivityManager) {
        return IMPL.getRestrictBackgroundStatus(connectivityManager);
    }

    @RequiresPermission(value="android.permission.ACCESS_NETWORK_STATE")
    public static boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
        return IMPL.isActiveNetworkMetered(connectivityManager);
    }

    @RequiresApi(value=16)
    static class ConnectivityManagerCompatApi16Impl
    extends ConnectivityManagerCompatBaseImpl {
        ConnectivityManagerCompatApi16Impl() {
        }

        @Override
        public boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
            return connectivityManager.isActiveNetworkMetered();
        }
    }

    @RequiresApi(value=24)
    static class ConnectivityManagerCompatApi24Impl
    extends ConnectivityManagerCompatApi16Impl {
        ConnectivityManagerCompatApi24Impl() {
        }

        @Override
        public int getRestrictBackgroundStatus(ConnectivityManager connectivityManager) {
            return connectivityManager.getRestrictBackgroundStatus();
        }
    }

    static class ConnectivityManagerCompatBaseImpl
    implements ConnectivityManagerCompatImpl {
        ConnectivityManagerCompatBaseImpl() {
        }

        @Override
        public int getRestrictBackgroundStatus(ConnectivityManager connectivityManager) {
            return 3;
        }

        @Override
        public boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
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

    static interface ConnectivityManagerCompatImpl {
        public int getRestrictBackgroundStatus(ConnectivityManager var1);

        public boolean isActiveNetworkMetered(ConnectivityManager var1);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface RestrictBackgroundStatus {
    }

}

