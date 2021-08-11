/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.widget.Adapter
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.FrameLayout
 *  android.widget.HeaderViewListAdapter
 *  android.widget.ListAdapter
 *  android.widget.PopupWindow
 *  android.widget.PopupWindow$OnDismissListener
 */
package android.support.v7.view.menu;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.ShowableListMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

abstract class MenuPopup
implements ShowableListMenu,
MenuPresenter,
AdapterView.OnItemClickListener {
    private Rect mEpicenterBounds;

    MenuPopup() {
    }

    protected static int measureIndividualMenuWidth(ListAdapter listAdapter, ViewGroup viewGroup, Context context, int n) {
        int n2 = 0;
        Object var12_5 = null;
        int n3 = 0;
        int n4 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
        int n5 = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
        int n6 = listAdapter.getCount();
        ViewGroup viewGroup2 = viewGroup;
        viewGroup = var12_5;
        for (int i = 0; i < n6; ++i) {
            int n7 = listAdapter.getItemViewType(i);
            if (n7 != n3) {
                n3 = n7;
                viewGroup = null;
            }
            if (viewGroup2 == null) {
                viewGroup2 = new FrameLayout(context);
            }
            viewGroup = listAdapter.getView(i, (View)viewGroup, viewGroup2);
            viewGroup.measure(n4, n5);
            n7 = viewGroup.getMeasuredWidth();
            if (n7 >= n) {
                return n;
            }
            if (n7 <= n2) continue;
            n2 = n7;
        }
        return n2;
    }

    protected static boolean shouldPreserveIconSpacing(MenuBuilder menuBuilder) {
        int n = menuBuilder.size();
        for (int i = 0; i < n; ++i) {
            MenuItem menuItem = menuBuilder.getItem(i);
            if (!menuItem.isVisible() || menuItem.getIcon() == null) continue;
            return true;
        }
        return false;
    }

    protected static MenuAdapter toMenuAdapter(ListAdapter listAdapter) {
        if (listAdapter instanceof HeaderViewListAdapter) {
            return (MenuAdapter)((HeaderViewListAdapter)listAdapter).getWrappedAdapter();
        }
        return (MenuAdapter)listAdapter;
    }

    public abstract void addMenu(MenuBuilder var1);

    protected boolean closeMenuOnSubMenuOpened() {
        return true;
    }

    @Override
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public Rect getEpicenterBounds() {
        return this.mEpicenterBounds;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public MenuView getMenuView(ViewGroup viewGroup) {
        throw new UnsupportedOperationException("MenuPopups manage their own views");
    }

    @Override
    public void initForMenu(@NonNull Context context, @Nullable MenuBuilder menuBuilder) {
    }

    public void onItemClick(AdapterView<?> object, View view, int n, long l) {
        view = (ListAdapter)object.getAdapter();
        object = MenuPopup.toMenuAdapter((ListAdapter)view).mAdapterMenu;
        view = (MenuItem)view.getItem(n);
        n = this.closeMenuOnSubMenuOpened() ? 0 : 4;
        object.performItemAction((MenuItem)view, this, n);
    }

    public abstract void setAnchorView(View var1);

    public void setEpicenterBounds(Rect rect) {
        this.mEpicenterBounds = rect;
    }

    public abstract void setForceShowIcon(boolean var1);

    public abstract void setGravity(int var1);

    public abstract void setHorizontalOffset(int var1);

    public abstract void setOnDismissListener(PopupWindow.OnDismissListener var1);

    public abstract void setShowTitle(boolean var1);

    public abstract void setVerticalOffset(int var1);
}

