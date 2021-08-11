/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 */
package android.support.v7.view.menu;

import android.content.Context;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.ArrayList;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public abstract class BaseMenuPresenter
implements MenuPresenter {
    private MenuPresenter.Callback mCallback;
    protected Context mContext;
    private int mId;
    protected LayoutInflater mInflater;
    private int mItemLayoutRes;
    protected MenuBuilder mMenu;
    private int mMenuLayoutRes;
    protected MenuView mMenuView;
    protected Context mSystemContext;
    protected LayoutInflater mSystemInflater;

    public BaseMenuPresenter(Context context, int n, int n2) {
        this.mSystemContext = context;
        this.mSystemInflater = LayoutInflater.from((Context)context);
        this.mMenuLayoutRes = n;
        this.mItemLayoutRes = n2;
    }

    protected void addItemView(View view, int n) {
        ViewGroup viewGroup = (ViewGroup)view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        ((ViewGroup)this.mMenuView).addView(view, n);
    }

    public abstract void bindItemView(MenuItemImpl var1, MenuView.ItemView var2);

    @Override
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public MenuView.ItemView createItemView(ViewGroup viewGroup) {
        return (MenuView.ItemView)this.mSystemInflater.inflate(this.mItemLayoutRes, viewGroup, false);
    }

    @Override
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    protected boolean filterLeftoverView(ViewGroup viewGroup, int n) {
        viewGroup.removeViewAt(n);
        return true;
    }

    @Override
    public boolean flagActionItems() {
        return false;
    }

    public MenuPresenter.Callback getCallback() {
        return this.mCallback;
    }

    @Override
    public int getId() {
        return this.mId;
    }

    public View getItemView(MenuItemImpl menuItemImpl, View object, ViewGroup viewGroup) {
        object = object instanceof MenuView.ItemView ? (MenuView.ItemView)object : this.createItemView(viewGroup);
        this.bindItemView(menuItemImpl, (MenuView.ItemView)object);
        return (View)object;
    }

    @Override
    public MenuView getMenuView(ViewGroup viewGroup) {
        if (this.mMenuView == null) {
            this.mMenuView = (MenuView)this.mSystemInflater.inflate(this.mMenuLayoutRes, viewGroup, false);
            this.mMenuView.initialize(this.mMenu);
            this.updateMenuView(true);
        }
        return this.mMenuView;
    }

    @Override
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from((Context)this.mContext);
        this.mMenu = menuBuilder;
    }

    @Override
    public void onCloseMenu(MenuBuilder menuBuilder, boolean bl) {
        MenuPresenter.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, bl);
            return;
        }
    }

    @Override
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        MenuPresenter.Callback callback = this.mCallback;
        if (callback != null) {
            return callback.onOpenSubMenu(subMenuBuilder);
        }
        return false;
    }

    @Override
    public void setCallback(MenuPresenter.Callback callback) {
        this.mCallback = callback;
    }

    public void setId(int n) {
        this.mId = n;
    }

    public boolean shouldIncludeItem(int n, MenuItemImpl menuItemImpl) {
        return true;
    }

    @Override
    public void updateMenuView(boolean bl) {
        ViewGroup viewGroup = (ViewGroup)this.mMenuView;
        if (viewGroup == null) {
            return;
        }
        int n = 0;
        int n2 = 0;
        Object object = this.mMenu;
        if (object != null) {
            object.flagActionItems();
            ArrayList<MenuItemImpl> arrayList = this.mMenu.getVisibleItems();
            int n3 = arrayList.size();
            for (n = 0; n < n3; ++n) {
                MenuItemImpl menuItemImpl = arrayList.get(n);
                if (!this.shouldIncludeItem(n2, menuItemImpl)) continue;
                View view = viewGroup.getChildAt(n2);
                object = view instanceof MenuView.ItemView ? ((MenuView.ItemView)view).getItemData() : null;
                View view2 = this.getItemView(menuItemImpl, view, viewGroup);
                if (menuItemImpl != object) {
                    view2.setPressed(false);
                    view2.jumpDrawablesToCurrentState();
                }
                if (view2 != view) {
                    this.addItemView(view2, n2);
                }
                ++n2;
            }
        } else {
            n2 = n;
        }
        while (n2 < viewGroup.getChildCount()) {
            if (this.filterLeftoverView(viewGroup, n2)) continue;
            ++n2;
        }
    }
}

