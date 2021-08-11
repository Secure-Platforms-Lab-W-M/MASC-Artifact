// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content.pm;

import android.os.Build$VERSION;
import android.content.pm.PackageInfo;

public final class PackageInfoCompat
{
    private PackageInfoCompat() {
    }
    
    public static long getLongVersionCode(final PackageInfo packageInfo) {
        if (Build$VERSION.SDK_INT >= 28) {
            return packageInfo.getLongVersionCode();
        }
        return packageInfo.versionCode;
    }
}
