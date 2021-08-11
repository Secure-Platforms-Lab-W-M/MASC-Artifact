// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.accessibilityservice;

import android.content.pm.PackageManager;
import android.os.Build$VERSION;
import android.accessibilityservice.AccessibilityServiceInfo;

public final class AccessibilityServiceInfoCompat
{
    public static final int CAPABILITY_CAN_FILTER_KEY_EVENTS = 8;
    public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
    public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
    public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
    public static final int FEEDBACK_ALL_MASK = -1;
    public static final int FEEDBACK_BRAILLE = 32;
    public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
    public static final int FLAG_REPORT_VIEW_IDS = 16;
    public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
    public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
    public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;
    
    private AccessibilityServiceInfoCompat() {
    }
    
    public static String capabilityToString(final int n) {
        if (n == 1) {
            return "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
        }
        if (n == 2) {
            return "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION";
        }
        if (n == 4) {
            return "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
        }
        if (n != 8) {
            return "UNKNOWN";
        }
        return "CAPABILITY_CAN_FILTER_KEY_EVENTS";
    }
    
    public static String feedbackTypeToString(int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (i > 0) {
            final int n = 1 << Integer.numberOfTrailingZeros(i);
            i &= n;
            if (sb.length() > 1) {
                sb.append(", ");
            }
            if (n != 1) {
                if (n != 2) {
                    if (n != 4) {
                        if (n != 8) {
                            if (n != 16) {
                                continue;
                            }
                            sb.append("FEEDBACK_GENERIC");
                        }
                        else {
                            sb.append("FEEDBACK_VISUAL");
                        }
                    }
                    else {
                        sb.append("FEEDBACK_AUDIBLE");
                    }
                }
                else {
                    sb.append("FEEDBACK_HAPTIC");
                }
            }
            else {
                sb.append("FEEDBACK_SPOKEN");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String flagToString(final int n) {
        if (n == 1) {
            return "DEFAULT";
        }
        if (n == 2) {
            return "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
        }
        if (n == 4) {
            return "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
        }
        if (n == 8) {
            return "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
        }
        if (n == 16) {
            return "FLAG_REPORT_VIEW_IDS";
        }
        if (n != 32) {
            return null;
        }
        return "FLAG_REQUEST_FILTER_KEY_EVENTS";
    }
    
    public static int getCapabilities(final AccessibilityServiceInfo accessibilityServiceInfo) {
        if (Build$VERSION.SDK_INT >= 18) {
            return accessibilityServiceInfo.getCapabilities();
        }
        if (accessibilityServiceInfo.getCanRetrieveWindowContent()) {
            return 1;
        }
        return 0;
    }
    
    public static String loadDescription(final AccessibilityServiceInfo accessibilityServiceInfo, final PackageManager packageManager) {
        if (Build$VERSION.SDK_INT >= 16) {
            return accessibilityServiceInfo.loadDescription(packageManager);
        }
        return accessibilityServiceInfo.getDescription();
    }
}
