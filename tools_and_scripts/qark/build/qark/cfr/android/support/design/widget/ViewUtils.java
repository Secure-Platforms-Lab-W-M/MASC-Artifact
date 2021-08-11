/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 */
package android.support.design.widget;

import android.graphics.PorterDuff;

class ViewUtils {
    ViewUtils() {
    }

    static PorterDuff.Mode parseTintMode(int n, PorterDuff.Mode mode) {
        if (n != 3) {
            if (n != 5) {
                if (n != 9) {
                    switch (n) {
                        default: {
                            return mode;
                        }
                        case 15: {
                            return PorterDuff.Mode.SCREEN;
                        }
                        case 14: 
                    }
                    return PorterDuff.Mode.MULTIPLY;
                }
                return PorterDuff.Mode.SRC_ATOP;
            }
            return PorterDuff.Mode.SRC_IN;
        }
        return PorterDuff.Mode.SRC_OVER;
    }
}

