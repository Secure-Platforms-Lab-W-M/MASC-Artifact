/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.view.MenuItem
 *  android.view.SubMenu
 *  android.view.View
 */
package android.support.v7.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportSubMenu;
import android.support.v7.view.menu.MenuWrapperICS;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

@RequiresApi(value=14)
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
class SubMenuWrapperICS
extends MenuWrapperICS
implements SubMenu {
    SubMenuWrapperICS(Context context, SupportSubMenu supportSubMenu) {
        super(context, supportSubMenu);
    }

    public void clearHeader() {
        this.getWrappedObject().clearHeader();
    }

    public MenuItem getItem() {
        return this.getMenuItemWrapper(this.getWrappedObject().getItem());
    }

    @Override
    public SupportSubMenu getWrappedObject() {
        return (SupportSubMenu)this.mWrappedObject;
    }

    public SubMenu setHeaderIcon(int n) {
        this.getWrappedObject().setHeaderIcon(n);
        return this;
    }

    public SubMenu setHeaderIcon(Drawable drawable2) {
        this.getWrappedObject().setHeaderIcon(drawable2);
        return this;
    }

    public SubMenu setHeaderTitle(int n) {
        this.getWrappedObject().setHeaderTitle(n);
        return this;
    }

    public SubMenu setHeaderTitle(CharSequence charSequence) {
        this.getWrappedObject().setHeaderTitle(charSequence);
        return this;
    }

    public SubMenu setHeaderView(View view) {
        this.getWrappedObject().setHeaderView(view);
        return this;
    }

    public SubMenu setIcon(int n) {
        this.getWrappedObject().setIcon(n);
        return this;
    }

    public SubMenu setIcon(Drawable drawable2) {
        this.getWrappedObject().setIcon(drawable2);
        return this;
    }
}

