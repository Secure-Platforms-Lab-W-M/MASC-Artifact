/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.MenuItem
 *  android.view.SubMenu
 */
package android.support.v7.view.menu;

import android.content.Context;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.support.v4.util.ArrayMap;
import android.support.v7.view.menu.BaseWrapper;
import android.support.v7.view.menu.MenuWrapperFactory;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

abstract class BaseMenuWrapper<T>
extends BaseWrapper<T> {
    final Context mContext;
    private Map<SupportMenuItem, MenuItem> mMenuItems;
    private Map<SupportSubMenu, SubMenu> mSubMenus;

    BaseMenuWrapper(Context context, T t) {
        super(t);
        this.mContext = context;
    }

    final MenuItem getMenuItemWrapper(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            SupportMenuItem supportMenuItem = (SupportMenuItem)menuItem;
            if (this.mMenuItems == null) {
                this.mMenuItems = new ArrayMap<SupportMenuItem, MenuItem>();
            }
            if ((menuItem = this.mMenuItems.get((Object)menuItem)) == null) {
                menuItem = MenuWrapperFactory.wrapSupportMenuItem(this.mContext, supportMenuItem);
                this.mMenuItems.put(supportMenuItem, menuItem);
                return menuItem;
            }
            return menuItem;
        }
        return menuItem;
    }

    final SubMenu getSubMenuWrapper(SubMenu subMenu) {
        if (subMenu instanceof SupportSubMenu) {
            SubMenu subMenu2;
            subMenu = (SupportSubMenu)subMenu;
            if (this.mSubMenus == null) {
                this.mSubMenus = new ArrayMap<SupportSubMenu, SubMenu>();
            }
            if ((subMenu2 = this.mSubMenus.get((Object)subMenu)) == null) {
                subMenu2 = MenuWrapperFactory.wrapSupportSubMenu(this.mContext, (SupportSubMenu)subMenu);
                this.mSubMenus.put((SupportSubMenu)subMenu, subMenu2);
                return subMenu2;
            }
            return subMenu2;
        }
        return subMenu;
    }

    final void internalClear() {
        Map map = this.mMenuItems;
        if (map != null) {
            map.clear();
        }
        if ((map = this.mSubMenus) != null) {
            map.clear();
            return;
        }
    }

    final void internalRemoveGroup(int n) {
        Map<SupportMenuItem, MenuItem> map = this.mMenuItems;
        if (map == null) {
            return;
        }
        map = map.keySet().iterator();
        while (map.hasNext()) {
            if (n != ((MenuItem)map.next()).getGroupId()) continue;
            map.remove();
        }
    }

    final void internalRemoveItem(int n) {
        Map<SupportMenuItem, MenuItem> map = this.mMenuItems;
        if (map == null) {
            return;
        }
        map = map.keySet().iterator();
        while (map.hasNext()) {
            if (n != ((MenuItem)map.next()).getItemId()) continue;
            map.remove();
            return;
        }
    }
}

