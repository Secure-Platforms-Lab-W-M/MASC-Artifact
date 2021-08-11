// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.content.pm;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.os.Build$VERSION;
import android.content.pm.PermissionInfo;

public final class PermissionInfoCompat
{
    private PermissionInfoCompat() {
    }
    
    public static int getProtection(final PermissionInfo permissionInfo) {
        if (Build$VERSION.SDK_INT >= 28) {
            return permissionInfo.getProtection();
        }
        return permissionInfo.protectionLevel & 0xF;
    }
    
    public static int getProtectionFlags(final PermissionInfo permissionInfo) {
        if (Build$VERSION.SDK_INT >= 28) {
            return permissionInfo.getProtectionFlags();
        }
        return permissionInfo.protectionLevel & 0xFFFFFFF0;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface Protection {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProtectionFlags {
    }
}
