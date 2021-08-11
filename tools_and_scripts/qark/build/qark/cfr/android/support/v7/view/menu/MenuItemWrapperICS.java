/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.util.Log
 *  android.view.ActionProvider
 *  android.view.CollapsibleActionView
 *  android.view.ContextMenu
 *  android.view.ContextMenu$ContextMenuInfo
 *  android.view.MenuItem
 *  android.view.MenuItem$OnActionExpandListener
 *  android.view.MenuItem$OnMenuItemClickListener
 *  android.view.SubMenu
 *  android.view.View
 *  android.widget.FrameLayout
 */
package android.support.v7.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ActionProvider;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.menu.BaseMenuWrapper;
import android.support.v7.view.menu.BaseWrapper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import java.lang.reflect.Method;

@RequiresApi(value=14)
@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
public class MenuItemWrapperICS
extends BaseMenuWrapper<SupportMenuItem>
implements MenuItem {
    static final String LOG_TAG = "MenuItemWrapper";
    private Method mSetExclusiveCheckableMethod;

    MenuItemWrapperICS(Context context, SupportMenuItem supportMenuItem) {
        super(context, supportMenuItem);
    }

    public boolean collapseActionView() {
        return ((SupportMenuItem)this.mWrappedObject).collapseActionView();
    }

    ActionProviderWrapper createActionProviderWrapper(android.view.ActionProvider actionProvider) {
        return new ActionProviderWrapper(this.mContext, actionProvider);
    }

    public boolean expandActionView() {
        return ((SupportMenuItem)this.mWrappedObject).expandActionView();
    }

    public android.view.ActionProvider getActionProvider() {
        ActionProvider actionProvider = ((SupportMenuItem)this.mWrappedObject).getSupportActionProvider();
        if (actionProvider instanceof ActionProviderWrapper) {
            return ((ActionProviderWrapper)actionProvider).mInner;
        }
        return null;
    }

    public View getActionView() {
        View view = ((SupportMenuItem)this.mWrappedObject).getActionView();
        if (view instanceof CollapsibleActionViewWrapper) {
            return ((CollapsibleActionViewWrapper)view).getWrappedView();
        }
        return view;
    }

    public int getAlphabeticModifiers() {
        return ((SupportMenuItem)this.mWrappedObject).getAlphabeticModifiers();
    }

    public char getAlphabeticShortcut() {
        return ((SupportMenuItem)this.mWrappedObject).getAlphabeticShortcut();
    }

    public CharSequence getContentDescription() {
        return ((SupportMenuItem)this.mWrappedObject).getContentDescription();
    }

    public int getGroupId() {
        return ((SupportMenuItem)this.mWrappedObject).getGroupId();
    }

    public Drawable getIcon() {
        return ((SupportMenuItem)this.mWrappedObject).getIcon();
    }

    public ColorStateList getIconTintList() {
        return ((SupportMenuItem)this.mWrappedObject).getIconTintList();
    }

    public PorterDuff.Mode getIconTintMode() {
        return ((SupportMenuItem)this.mWrappedObject).getIconTintMode();
    }

    public Intent getIntent() {
        return ((SupportMenuItem)this.mWrappedObject).getIntent();
    }

    public int getItemId() {
        return ((SupportMenuItem)this.mWrappedObject).getItemId();
    }

    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return ((SupportMenuItem)this.mWrappedObject).getMenuInfo();
    }

    public int getNumericModifiers() {
        return ((SupportMenuItem)this.mWrappedObject).getNumericModifiers();
    }

    public char getNumericShortcut() {
        return ((SupportMenuItem)this.mWrappedObject).getNumericShortcut();
    }

    public int getOrder() {
        return ((SupportMenuItem)this.mWrappedObject).getOrder();
    }

    public SubMenu getSubMenu() {
        return this.getSubMenuWrapper(((SupportMenuItem)this.mWrappedObject).getSubMenu());
    }

    public CharSequence getTitle() {
        return ((SupportMenuItem)this.mWrappedObject).getTitle();
    }

    public CharSequence getTitleCondensed() {
        return ((SupportMenuItem)this.mWrappedObject).getTitleCondensed();
    }

    public CharSequence getTooltipText() {
        return ((SupportMenuItem)this.mWrappedObject).getTooltipText();
    }

    public boolean hasSubMenu() {
        return ((SupportMenuItem)this.mWrappedObject).hasSubMenu();
    }

    public boolean isActionViewExpanded() {
        return ((SupportMenuItem)this.mWrappedObject).isActionViewExpanded();
    }

    public boolean isCheckable() {
        return ((SupportMenuItem)this.mWrappedObject).isCheckable();
    }

    public boolean isChecked() {
        return ((SupportMenuItem)this.mWrappedObject).isChecked();
    }

    public boolean isEnabled() {
        return ((SupportMenuItem)this.mWrappedObject).isEnabled();
    }

    public boolean isVisible() {
        return ((SupportMenuItem)this.mWrappedObject).isVisible();
    }

    public MenuItem setActionProvider(android.view.ActionProvider object) {
        SupportMenuItem supportMenuItem = (SupportMenuItem)this.mWrappedObject;
        object = object != null ? this.createActionProviderWrapper((android.view.ActionProvider)object) : null;
        supportMenuItem.setSupportActionProvider((ActionProvider)object);
        return this;
    }

    public MenuItem setActionView(int n) {
        ((SupportMenuItem)this.mWrappedObject).setActionView(n);
        View view = ((SupportMenuItem)this.mWrappedObject).getActionView();
        if (view instanceof android.view.CollapsibleActionView) {
            ((SupportMenuItem)this.mWrappedObject).setActionView((View)new CollapsibleActionViewWrapper(view));
            return this;
        }
        return this;
    }

    public MenuItem setActionView(View object) {
        if (object instanceof android.view.CollapsibleActionView) {
            object = new CollapsibleActionViewWrapper((View)object);
        }
        ((SupportMenuItem)this.mWrappedObject).setActionView((View)object);
        return this;
    }

    public MenuItem setAlphabeticShortcut(char c) {
        ((SupportMenuItem)this.mWrappedObject).setAlphabeticShortcut(c);
        return this;
    }

    public MenuItem setAlphabeticShortcut(char c, int n) {
        ((SupportMenuItem)this.mWrappedObject).setAlphabeticShortcut(c, n);
        return this;
    }

    public MenuItem setCheckable(boolean bl) {
        ((SupportMenuItem)this.mWrappedObject).setCheckable(bl);
        return this;
    }

    public MenuItem setChecked(boolean bl) {
        ((SupportMenuItem)this.mWrappedObject).setChecked(bl);
        return this;
    }

    public MenuItem setContentDescription(CharSequence charSequence) {
        ((SupportMenuItem)this.mWrappedObject).setContentDescription(charSequence);
        return this;
    }

    public MenuItem setEnabled(boolean bl) {
        ((SupportMenuItem)this.mWrappedObject).setEnabled(bl);
        return this;
    }

    public void setExclusiveCheckable(boolean bl) {
        try {
            if (this.mSetExclusiveCheckableMethod == null) {
                this.mSetExclusiveCheckableMethod = ((SupportMenuItem)this.mWrappedObject).getClass().getDeclaredMethod("setExclusiveCheckable", Boolean.TYPE);
            }
            this.mSetExclusiveCheckableMethod.invoke(this.mWrappedObject, bl);
            return;
        }
        catch (Exception exception) {
            Log.w((String)"MenuItemWrapper", (String)"Error while calling setExclusiveCheckable", (Throwable)exception);
            return;
        }
    }

    public MenuItem setIcon(int n) {
        ((SupportMenuItem)this.mWrappedObject).setIcon(n);
        return this;
    }

    public MenuItem setIcon(Drawable drawable2) {
        ((SupportMenuItem)this.mWrappedObject).setIcon(drawable2);
        return this;
    }

    public MenuItem setIconTintList(ColorStateList colorStateList) {
        ((SupportMenuItem)this.mWrappedObject).setIconTintList(colorStateList);
        return this;
    }

    public MenuItem setIconTintMode(PorterDuff.Mode mode) {
        ((SupportMenuItem)this.mWrappedObject).setIconTintMode(mode);
        return this;
    }

    public MenuItem setIntent(Intent intent) {
        ((SupportMenuItem)this.mWrappedObject).setIntent(intent);
        return this;
    }

    public MenuItem setNumericShortcut(char c) {
        ((SupportMenuItem)this.mWrappedObject).setNumericShortcut(c);
        return this;
    }

    public MenuItem setNumericShortcut(char c, int n) {
        ((SupportMenuItem)this.mWrappedObject).setNumericShortcut(c, n);
        return this;
    }

    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        SupportMenuItem supportMenuItem = (SupportMenuItem)this.mWrappedObject;
        onActionExpandListener = onActionExpandListener != null ? new OnActionExpandListenerWrapper(onActionExpandListener) : null;
        supportMenuItem.setOnActionExpandListener(onActionExpandListener);
        return this;
    }

    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        SupportMenuItem supportMenuItem = (SupportMenuItem)this.mWrappedObject;
        onMenuItemClickListener = onMenuItemClickListener != null ? new OnMenuItemClickListenerWrapper(onMenuItemClickListener) : null;
        supportMenuItem.setOnMenuItemClickListener(onMenuItemClickListener);
        return this;
    }

    public MenuItem setShortcut(char c, char c2) {
        ((SupportMenuItem)this.mWrappedObject).setShortcut(c, c2);
        return this;
    }

    public MenuItem setShortcut(char c, char c2, int n, int n2) {
        ((SupportMenuItem)this.mWrappedObject).setShortcut(c, c2, n, n2);
        return this;
    }

    public void setShowAsAction(int n) {
        ((SupportMenuItem)this.mWrappedObject).setShowAsAction(n);
    }

    public MenuItem setShowAsActionFlags(int n) {
        ((SupportMenuItem)this.mWrappedObject).setShowAsActionFlags(n);
        return this;
    }

    public MenuItem setTitle(int n) {
        ((SupportMenuItem)this.mWrappedObject).setTitle(n);
        return this;
    }

    public MenuItem setTitle(CharSequence charSequence) {
        ((SupportMenuItem)this.mWrappedObject).setTitle(charSequence);
        return this;
    }

    public MenuItem setTitleCondensed(CharSequence charSequence) {
        ((SupportMenuItem)this.mWrappedObject).setTitleCondensed(charSequence);
        return this;
    }

    public MenuItem setTooltipText(CharSequence charSequence) {
        ((SupportMenuItem)this.mWrappedObject).setTooltipText(charSequence);
        return this;
    }

    public MenuItem setVisible(boolean bl) {
        return ((SupportMenuItem)this.mWrappedObject).setVisible(bl);
    }

    class ActionProviderWrapper
    extends ActionProvider {
        final android.view.ActionProvider mInner;

        public ActionProviderWrapper(Context context, android.view.ActionProvider actionProvider) {
            super(context);
            this.mInner = actionProvider;
        }

        @Override
        public boolean hasSubMenu() {
            return this.mInner.hasSubMenu();
        }

        @Override
        public View onCreateActionView() {
            return this.mInner.onCreateActionView();
        }

        @Override
        public boolean onPerformDefaultAction() {
            return this.mInner.onPerformDefaultAction();
        }

        @Override
        public void onPrepareSubMenu(SubMenu subMenu) {
            this.mInner.onPrepareSubMenu(MenuItemWrapperICS.this.getSubMenuWrapper(subMenu));
        }
    }

    static class CollapsibleActionViewWrapper
    extends FrameLayout
    implements CollapsibleActionView {
        final android.view.CollapsibleActionView mWrappedView;

        CollapsibleActionViewWrapper(View view) {
            super(view.getContext());
            this.mWrappedView = (android.view.CollapsibleActionView)view;
            this.addView(view);
        }

        View getWrappedView() {
            return (View)this.mWrappedView;
        }

        @Override
        public void onActionViewCollapsed() {
            this.mWrappedView.onActionViewCollapsed();
        }

        @Override
        public void onActionViewExpanded() {
            this.mWrappedView.onActionViewExpanded();
        }
    }

    private class OnActionExpandListenerWrapper
    extends BaseWrapper<MenuItem.OnActionExpandListener>
    implements MenuItem.OnActionExpandListener {
        OnActionExpandListenerWrapper(MenuItem.OnActionExpandListener onActionExpandListener) {
            super(onActionExpandListener);
        }

        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            return ((MenuItem.OnActionExpandListener)this.mWrappedObject).onMenuItemActionCollapse(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }

        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            return ((MenuItem.OnActionExpandListener)this.mWrappedObject).onMenuItemActionExpand(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
    }

    private class OnMenuItemClickListenerWrapper
    extends BaseWrapper<MenuItem.OnMenuItemClickListener>
    implements MenuItem.OnMenuItemClickListener {
        OnMenuItemClickListenerWrapper(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
            super(onMenuItemClickListener);
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            return ((MenuItem.OnMenuItemClickListener)this.mWrappedObject).onMenuItemClick(MenuItemWrapperICS.this.getMenuItemWrapper(menuItem));
        }
    }

}

