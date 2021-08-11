/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Parcelable
 *  android.view.ViewGroup
 */
package androidx.appcompat.view.menu;

import android.content.Context;
import android.os.Parcelable;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.view.menu.SubMenuBuilder;

public interface MenuPresenter {
    public boolean collapseItemActionView(MenuBuilder var1, MenuItemImpl var2);

    public boolean expandItemActionView(MenuBuilder var1, MenuItemImpl var2);

    public boolean flagActionItems();

    public int getId();

    public MenuView getMenuView(ViewGroup var1);

    public void initForMenu(Context var1, MenuBuilder var2);

    public void onCloseMenu(MenuBuilder var1, boolean var2);

    public void onRestoreInstanceState(Parcelable var1);

    public Parcelable onSaveInstanceState();

    public boolean onSubMenuSelected(SubMenuBuilder var1);

    public void setCallback(Callback var1);

    public void updateMenuView(boolean var1);

    public static interface Callback {
        public void onCloseMenu(MenuBuilder var1, boolean var2);

        public boolean onOpenSubMenu(MenuBuilder var1);
    }

}

