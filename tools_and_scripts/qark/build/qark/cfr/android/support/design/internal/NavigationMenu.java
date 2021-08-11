/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.MenuItem
 *  android.view.SubMenu
 */
package android.support.design.internal;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.design.internal.NavigationSubMenu;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.SubMenuBuilder;
import android.view.MenuItem;
import android.view.SubMenu;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class NavigationMenu
extends MenuBuilder {
    public NavigationMenu(Context context) {
        super(context);
    }

    @Override
    public SubMenu addSubMenu(int n, int n2, int n3, CharSequence object) {
        object = (MenuItemImpl)this.addInternal(n, n2, n3, (CharSequence)object);
        NavigationSubMenu navigationSubMenu = new NavigationSubMenu(this.getContext(), this, (MenuItemImpl)object);
        object.setSubMenu(navigationSubMenu);
        return navigationSubMenu;
    }
}

