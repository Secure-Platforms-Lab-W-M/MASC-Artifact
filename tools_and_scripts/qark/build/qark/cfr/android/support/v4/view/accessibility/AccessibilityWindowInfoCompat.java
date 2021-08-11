/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.accessibility.AccessibilityWindowInfo
 */
package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

public class AccessibilityWindowInfoCompat {
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 4;
    public static final int TYPE_APPLICATION = 1;
    public static final int TYPE_INPUT_METHOD = 2;
    public static final int TYPE_SPLIT_SCREEN_DIVIDER = 5;
    public static final int TYPE_SYSTEM = 3;
    private static final int UNDEFINED = -1;
    private Object mInfo;

    private AccessibilityWindowInfoCompat(Object object) {
        this.mInfo = object;
    }

    public static AccessibilityWindowInfoCompat obtain() {
        if (Build.VERSION.SDK_INT >= 21) {
            return AccessibilityWindowInfoCompat.wrapNonNullInstance((Object)AccessibilityWindowInfo.obtain());
        }
        return null;
    }

    public static AccessibilityWindowInfoCompat obtain(AccessibilityWindowInfoCompat accessibilityWindowInfoCompat) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (accessibilityWindowInfoCompat == null) {
                return null;
            }
            return AccessibilityWindowInfoCompat.wrapNonNullInstance((Object)AccessibilityWindowInfo.obtain((AccessibilityWindowInfo)((AccessibilityWindowInfo)accessibilityWindowInfoCompat.mInfo)));
        }
        return null;
    }

    private static String typeToString(int n) {
        switch (n) {
            default: {
                return "<UNKNOWN>";
            }
            case 4: {
                return "TYPE_ACCESSIBILITY_OVERLAY";
            }
            case 3: {
                return "TYPE_SYSTEM";
            }
            case 2: {
                return "TYPE_INPUT_METHOD";
            }
            case 1: 
        }
        return "TYPE_APPLICATION";
    }

    static AccessibilityWindowInfoCompat wrapNonNullInstance(Object object) {
        if (object != null) {
            return new AccessibilityWindowInfoCompat(object);
        }
        return null;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        object = (AccessibilityWindowInfoCompat)object;
        Object object2 = this.mInfo;
        if (object2 == null) {
            if (object.mInfo != null) {
                return false;
            }
            return true;
        }
        if (!object2.equals(object.mInfo)) {
            return false;
        }
        return true;
    }

    public AccessibilityNodeInfoCompat getAnchor() {
        if (Build.VERSION.SDK_INT >= 24) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance((Object)((AccessibilityWindowInfo)this.mInfo).getAnchor());
        }
        return null;
    }

    public void getBoundsInScreen(Rect rect) {
        if (Build.VERSION.SDK_INT >= 21) {
            ((AccessibilityWindowInfo)this.mInfo).getBoundsInScreen(rect);
            return;
        }
    }

    public AccessibilityWindowInfoCompat getChild(int n) {
        if (Build.VERSION.SDK_INT >= 21) {
            return AccessibilityWindowInfoCompat.wrapNonNullInstance((Object)((AccessibilityWindowInfo)this.mInfo).getChild(n));
        }
        return null;
    }

    public int getChildCount() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getChildCount();
        }
        return 0;
    }

    public int getId() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getId();
        }
        return -1;
    }

    public int getLayer() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getLayer();
        }
        return -1;
    }

    public AccessibilityWindowInfoCompat getParent() {
        if (Build.VERSION.SDK_INT >= 21) {
            return AccessibilityWindowInfoCompat.wrapNonNullInstance((Object)((AccessibilityWindowInfo)this.mInfo).getParent());
        }
        return null;
    }

    public AccessibilityNodeInfoCompat getRoot() {
        if (Build.VERSION.SDK_INT >= 21) {
            return AccessibilityNodeInfoCompat.wrapNonNullInstance((Object)((AccessibilityWindowInfo)this.mInfo).getRoot());
        }
        return null;
    }

    public CharSequence getTitle() {
        if (Build.VERSION.SDK_INT >= 24) {
            return ((AccessibilityWindowInfo)this.mInfo).getTitle();
        }
        return null;
    }

    public int getType() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).getType();
        }
        return -1;
    }

    public int hashCode() {
        Object object = this.mInfo;
        if (object == null) {
            return 0;
        }
        return object.hashCode();
    }

    public boolean isAccessibilityFocused() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).isAccessibilityFocused();
        }
        return true;
    }

    public boolean isActive() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).isActive();
        }
        return true;
    }

    public boolean isFocused() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ((AccessibilityWindowInfo)this.mInfo).isFocused();
        }
        return true;
    }

    public void recycle() {
        if (Build.VERSION.SDK_INT >= 21) {
            ((AccessibilityWindowInfo)this.mInfo).recycle();
            return;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Object object = new Rect();
        this.getBoundsInScreen((Rect)object);
        stringBuilder.append("AccessibilityWindowInfo[");
        stringBuilder.append("id=");
        stringBuilder.append(this.getId());
        stringBuilder.append(", type=");
        stringBuilder.append(AccessibilityWindowInfoCompat.typeToString(this.getType()));
        stringBuilder.append(", layer=");
        stringBuilder.append(this.getLayer());
        stringBuilder.append(", bounds=");
        stringBuilder.append(object);
        stringBuilder.append(", focused=");
        stringBuilder.append(this.isFocused());
        stringBuilder.append(", active=");
        stringBuilder.append(this.isActive());
        stringBuilder.append(", hasParent=");
        object = this.getParent();
        boolean bl = true;
        boolean bl2 = object != null;
        stringBuilder.append(bl2);
        stringBuilder.append(", hasChildren=");
        bl2 = this.getChildCount() > 0 ? bl : false;
        stringBuilder.append(bl2);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}

