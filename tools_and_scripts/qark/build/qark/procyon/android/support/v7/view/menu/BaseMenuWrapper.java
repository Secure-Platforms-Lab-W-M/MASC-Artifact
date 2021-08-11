// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.view.menu;

import java.util.Iterator;
import android.support.v4.util.ArrayMap;
import android.view.SubMenu;
import android.support.v4.internal.view.SupportSubMenu;
import android.view.MenuItem;
import android.support.v4.internal.view.SupportMenuItem;
import java.util.Map;
import android.content.Context;

abstract class BaseMenuWrapper<T> extends BaseWrapper<T>
{
    final Context mContext;
    private Map<SupportMenuItem, MenuItem> mMenuItems;
    private Map<SupportSubMenu, SubMenu> mSubMenus;
    
    BaseMenuWrapper(final Context mContext, final T t) {
        super(t);
        this.mContext = mContext;
    }
    
    final MenuItem getMenuItemWrapper(MenuItem wrapSupportMenuItem) {
        if (!(wrapSupportMenuItem instanceof SupportMenuItem)) {
            return wrapSupportMenuItem;
        }
        final SupportMenuItem supportMenuItem = (SupportMenuItem)wrapSupportMenuItem;
        if (this.mMenuItems == null) {
            this.mMenuItems = new ArrayMap<SupportMenuItem, MenuItem>();
        }
        wrapSupportMenuItem = this.mMenuItems.get(wrapSupportMenuItem);
        if (wrapSupportMenuItem == null) {
            wrapSupportMenuItem = MenuWrapperFactory.wrapSupportMenuItem(this.mContext, supportMenuItem);
            this.mMenuItems.put(supportMenuItem, wrapSupportMenuItem);
            return wrapSupportMenuItem;
        }
        return wrapSupportMenuItem;
    }
    
    final SubMenu getSubMenuWrapper(final SubMenu subMenu) {
        if (!(subMenu instanceof SupportSubMenu)) {
            return subMenu;
        }
        final SupportSubMenu supportSubMenu = (SupportSubMenu)subMenu;
        if (this.mSubMenus == null) {
            this.mSubMenus = new ArrayMap<SupportSubMenu, SubMenu>();
        }
        final SubMenu subMenu2 = this.mSubMenus.get(supportSubMenu);
        if (subMenu2 == null) {
            final SubMenu wrapSupportSubMenu = MenuWrapperFactory.wrapSupportSubMenu(this.mContext, supportSubMenu);
            this.mSubMenus.put(supportSubMenu, wrapSupportSubMenu);
            return wrapSupportSubMenu;
        }
        return subMenu2;
    }
    
    final void internalClear() {
        final Map<SupportMenuItem, MenuItem> mMenuItems = this.mMenuItems;
        if (mMenuItems != null) {
            mMenuItems.clear();
        }
        final Map<SupportSubMenu, SubMenu> mSubMenus = this.mSubMenus;
        if (mSubMenus != null) {
            mSubMenus.clear();
        }
    }
    
    final void internalRemoveGroup(final int n) {
        final Map<SupportMenuItem, MenuItem> mMenuItems = this.mMenuItems;
        if (mMenuItems == null) {
            return;
        }
        final Iterator<SupportMenuItem> iterator = mMenuItems.keySet().iterator();
        while (iterator.hasNext()) {
            if (n == ((MenuItem)iterator.next()).getGroupId()) {
                iterator.remove();
            }
        }
    }
    
    final void internalRemoveItem(final int n) {
        final Map<SupportMenuItem, MenuItem> mMenuItems = this.mMenuItems;
        if (mMenuItems == null) {
            return;
        }
        final Iterator<SupportMenuItem> iterator = mMenuItems.keySet().iterator();
        while (iterator.hasNext()) {
            if (n == ((MenuItem)iterator.next()).getItemId()) {
                iterator.remove();
            }
        }
    }
}
