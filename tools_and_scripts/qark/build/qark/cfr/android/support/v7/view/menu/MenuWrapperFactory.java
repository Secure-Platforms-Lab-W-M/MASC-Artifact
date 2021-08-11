/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.SubMenu
 */
package android.support.v7.view.menu;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.support.v7.view.menu.MenuItemWrapperICS;
import android.support.v7.view.menu.MenuItemWrapperJB;
import android.support.v7.view.menu.MenuWrapperICS;
import android.support.v7.view.menu.SubMenuWrapperICS;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public final class MenuWrapperFactory {
    private MenuWrapperFactory() {
    }

    public static Menu wrapSupportMenu(Context context, SupportMenu supportMenu) {
        return new MenuWrapperICS(context, supportMenu);
    }

    public static MenuItem wrapSupportMenuItem(Context context, SupportMenuItem supportMenuItem) {
        if (Build.VERSION.SDK_INT >= 16) {
            return new MenuItemWrapperJB(context, supportMenuItem);
        }
        return new MenuItemWrapperICS(context, supportMenuItem);
    }

    public static SubMenu wrapSupportSubMenu(Context context, SupportSubMenu supportSubMenu) {
        return new SubMenuWrapperICS(context, supportSubMenu);
    }
}

