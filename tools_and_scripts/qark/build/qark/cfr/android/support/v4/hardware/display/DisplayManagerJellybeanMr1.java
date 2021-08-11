/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.hardware.display.DisplayManager
 *  android.view.Display
 */
package android.support.v4.hardware.display;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;

final class DisplayManagerJellybeanMr1 {
    DisplayManagerJellybeanMr1() {
    }

    public static Display getDisplay(Object object, int n) {
        return ((DisplayManager)object).getDisplay(n);
    }

    public static Object getDisplayManager(Context context) {
        return context.getSystemService("display");
    }

    public static Display[] getDisplays(Object object) {
        return ((DisplayManager)object).getDisplays();
    }

    public static Display[] getDisplays(Object object, String string) {
        return ((DisplayManager)object).getDisplays(string);
    }
}

