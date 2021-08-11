/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.MenuItem
 */
package android.support.v4.view;

import android.view.MenuItem;

public final class MenuCompat {
    private MenuCompat() {
    }

    @Deprecated
    public static void setShowAsAction(MenuItem menuItem, int n) {
        menuItem.setShowAsAction(n);
    }
}

