/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.MenuItem
 */
package android.support.v7.widget;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.MenuBuilder;
import android.view.MenuItem;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public interface MenuItemHoverListener {
    public void onItemHoverEnter(@NonNull MenuBuilder var1, @NonNull MenuItem var2);

    public void onItemHoverExit(@NonNull MenuBuilder var1, @NonNull MenuItem var2);
}

