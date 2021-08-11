// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.view.menu;

import java.util.Iterator;
import androidx.collection.ArrayMap;
import android.view.SubMenu;
import androidx.core.internal.view.SupportSubMenu;
import android.view.MenuItem;
import androidx.core.internal.view.SupportMenuItem;
import java.util.Map;
import android.content.Context;

abstract class BaseMenuWrapper
{
    final Context mContext;
    private Map<SupportMenuItem, MenuItem> mMenuItems;
    private Map<SupportSubMenu, SubMenu> mSubMenus;
    
    BaseMenuWrapper(final Context mContext) {
        this.mContext = mContext;
    }
    
    final MenuItem getMenuItemWrapper(MenuItem o) {
        if (o instanceof SupportMenuItem) {
            final SupportMenuItem supportMenuItem = (SupportMenuItem)o;
            if (this.mMenuItems == null) {
                this.mMenuItems = new ArrayMap<SupportMenuItem, MenuItem>();
            }
            if ((o = this.mMenuItems.get(o)) == null) {
                o = new MenuItemWrapperICS(this.mContext, supportMenuItem);
                this.mMenuItems.put(supportMenuItem, (MenuItem)o);
            }
            return (MenuItem)o;
        }
        return (MenuItem)o;
    }
    
    final SubMenu getSubMenuWrapper(SubMenu o) {
        if (o instanceof SupportSubMenu) {
            final SupportSubMenu supportSubMenu = (SupportSubMenu)o;
            if (this.mSubMenus == null) {
                this.mSubMenus = new ArrayMap<SupportSubMenu, SubMenu>();
            }
            if ((o = this.mSubMenus.get(supportSubMenu)) == null) {
                o = new SubMenuWrapperICS(this.mContext, supportSubMenu);
                this.mSubMenus.put(supportSubMenu, (SubMenu)o);
            }
            return (SubMenu)o;
        }
        return (SubMenu)o;
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
                break;
            }
        }
    }
}
