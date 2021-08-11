// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(18)
@RequiresApi(18)
class AccessibilityServiceInfoCompatJellyBeanMr2
{
    public static int getCapabilities(final AccessibilityServiceInfo accessibilityServiceInfo) {
        return accessibilityServiceInfo.getCapabilities();
    }
}
