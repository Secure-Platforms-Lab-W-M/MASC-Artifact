// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.location;

import android.os.Build$VERSION;
import android.location.LocationManager;

public final class LocationManagerCompat
{
    private LocationManagerCompat() {
    }
    
    public static boolean isLocationEnabled(final LocationManager locationManager) {
        if (Build$VERSION.SDK_INT >= 28) {
            return locationManager.isLocationEnabled();
        }
        return locationManager.isProviderEnabled("network") || locationManager.isProviderEnabled("gps");
    }
}
