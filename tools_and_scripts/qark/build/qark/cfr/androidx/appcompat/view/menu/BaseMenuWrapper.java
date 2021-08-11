/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.MenuItem
 *  android.view.SubMenu
 */
package androidx.appcompat.view.menu;

import android.content.Context;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.appcompat.view.menu.MenuItemWrapperICS;
import androidx.appcompat.view.menu.SubMenuWrapperICS;
import androidx.collection.ArrayMap;
import androidx.core.internal.view.SupportMenuItem;
import androidx.core.internal.view.SupportSubMenu;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

abstract class BaseMenuWrapper {
    final Context mContext;
    private Map<SupportMenuItem, MenuItem> mMenuItems;
    private Map<SupportSubMenu, SubMenu> mSubMenus;

    BaseMenuWrapper(Context context) {
        this.mContext = context;
    }

    final MenuItem getMenuItemWrapper(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            MenuItem menuItem2;
            SupportMenuItem supportMenuItem = (SupportMenuItem)menuItem;
            if (this.mMenuItems == null) {
                this.mMenuItems = new ArrayMap<SupportMenuItem, MenuItem>();
            }
            menuItem = menuItem2 = this.mMenuItems.get((Object)menuItem);
            if (menuItem2 == null) {
                menuItem = new MenuItemWrapperICS(this.mContext, supportMenuItem);
                this.mMenuItems.put(supportMenuItem, menuItem);
            }
            return menuItem;
        }
        return menuItem;
    }

    final SubMenu getSubMenuWrapper(SubMenu subMenu) {
        if (subMenu instanceof SupportSubMenu) {
            SubMenu subMenu2;
            SupportSubMenu supportSubMenu = (SupportSubMenu)subMenu;
            if (this.mSubMenus == null) {
                this.mSubMenus = new ArrayMap<SupportSubMenu, SubMenu>();
            }
            subMenu = subMenu2 = this.mSubMenus.get(supportSubMenu);
            if (subMenu2 == null) {
                subMenu = new SubMenuWrapperICS(this.mContext, supportSubMenu);
                this.mSubMenus.put(supportSubMenu, subMenu);
            }
            return subMenu;
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
            break;
        }
    }
}

