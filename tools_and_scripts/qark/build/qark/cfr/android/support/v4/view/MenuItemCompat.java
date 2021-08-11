/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.MenuItem
 *  android.view.MenuItem$OnActionExpandListener
 *  android.view.View
 */
package android.support.v4.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ActionProvider;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public final class MenuItemCompat {
    static final MenuVersionImpl IMPL = Build.VERSION.SDK_INT >= 26 ? new MenuItemCompatApi26Impl() : new MenuItemCompatBaseImpl();
    @Deprecated
    public static final int SHOW_AS_ACTION_ALWAYS = 2;
    @Deprecated
    public static final int SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW = 8;
    @Deprecated
    public static final int SHOW_AS_ACTION_IF_ROOM = 1;
    @Deprecated
    public static final int SHOW_AS_ACTION_NEVER = 0;
    @Deprecated
    public static final int SHOW_AS_ACTION_WITH_TEXT = 4;
    private static final String TAG = "MenuItemCompat";

    private MenuItemCompat() {
    }

    @Deprecated
    public static boolean collapseActionView(MenuItem menuItem) {
        return menuItem.collapseActionView();
    }

    @Deprecated
    public static boolean expandActionView(MenuItem menuItem) {
        return menuItem.expandActionView();
    }

    public static ActionProvider getActionProvider(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getSupportActionProvider();
        }
        Log.w((String)"MenuItemCompat", (String)"getActionProvider: item does not implement SupportMenuItem; returning null");
        return null;
    }

    @Deprecated
    public static View getActionView(MenuItem menuItem) {
        return menuItem.getActionView();
    }

    public static int getAlphabeticModifiers(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getAlphabeticModifiers();
        }
        return IMPL.getAlphabeticModifiers(menuItem);
    }

    public static CharSequence getContentDescription(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getContentDescription();
        }
        return IMPL.getContentDescription(menuItem);
    }

    public static ColorStateList getIconTintList(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getIconTintList();
        }
        return IMPL.getIconTintList(menuItem);
    }

    public static PorterDuff.Mode getIconTintMode(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getIconTintMode();
        }
        return IMPL.getIconTintMode(menuItem);
    }

    public static int getNumericModifiers(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getNumericModifiers();
        }
        return IMPL.getNumericModifiers(menuItem);
    }

    public static CharSequence getTooltipText(MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getTooltipText();
        }
        return IMPL.getTooltipText(menuItem);
    }

    @Deprecated
    public static boolean isActionViewExpanded(MenuItem menuItem) {
        return menuItem.isActionViewExpanded();
    }

    public static MenuItem setActionProvider(MenuItem menuItem, ActionProvider actionProvider) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).setSupportActionProvider(actionProvider);
        }
        Log.w((String)"MenuItemCompat", (String)"setActionProvider: item does not implement SupportMenuItem; ignoring");
        return menuItem;
    }

    @Deprecated
    public static MenuItem setActionView(MenuItem menuItem, int n) {
        return menuItem.setActionView(n);
    }

    @Deprecated
    public static MenuItem setActionView(MenuItem menuItem, View view) {
        return menuItem.setActionView(view);
    }

    public static void setAlphabeticShortcut(MenuItem menuItem, char c, int n) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setAlphabeticShortcut(c, n);
            return;
        }
        IMPL.setAlphabeticShortcut(menuItem, c, n);
    }

    public static void setContentDescription(MenuItem menuItem, CharSequence charSequence) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setContentDescription(charSequence);
            return;
        }
        IMPL.setContentDescription(menuItem, charSequence);
    }

    public static void setIconTintList(MenuItem menuItem, ColorStateList colorStateList) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setIconTintList(colorStateList);
            return;
        }
        IMPL.setIconTintList(menuItem, colorStateList);
    }

    public static void setIconTintMode(MenuItem menuItem, PorterDuff.Mode mode) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setIconTintMode(mode);
            return;
        }
        IMPL.setIconTintMode(menuItem, mode);
    }

    public static void setNumericShortcut(MenuItem menuItem, char c, int n) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setNumericShortcut(c, n);
            return;
        }
        IMPL.setNumericShortcut(menuItem, c, n);
    }

    @Deprecated
    public static MenuItem setOnActionExpandListener(MenuItem menuItem, final OnActionExpandListener onActionExpandListener) {
        return menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener(){

            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                return onActionExpandListener.onMenuItemActionCollapse(menuItem);
            }

            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return onActionExpandListener.onMenuItemActionExpand(menuItem);
            }
        });
    }

    public static void setShortcut(MenuItem menuItem, char c, char c2, int n, int n2) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setShortcut(c, c2, n, n2);
            return;
        }
        IMPL.setShortcut(menuItem, c, c2, n, n2);
    }

    @Deprecated
    public static void setShowAsAction(MenuItem menuItem, int n) {
        menuItem.setShowAsAction(n);
    }

    public static void setTooltipText(MenuItem menuItem, CharSequence charSequence) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setTooltipText(charSequence);
            return;
        }
        IMPL.setTooltipText(menuItem, charSequence);
    }

    @RequiresApi(value=26)
    static class MenuItemCompatApi26Impl
    extends MenuItemCompatBaseImpl {
        MenuItemCompatApi26Impl() {
        }

        @Override
        public int getAlphabeticModifiers(MenuItem menuItem) {
            return menuItem.getAlphabeticModifiers();
        }

        @Override
        public CharSequence getContentDescription(MenuItem menuItem) {
            return menuItem.getContentDescription();
        }

        @Override
        public ColorStateList getIconTintList(MenuItem menuItem) {
            return menuItem.getIconTintList();
        }

        @Override
        public PorterDuff.Mode getIconTintMode(MenuItem menuItem) {
            return menuItem.getIconTintMode();
        }

        @Override
        public int getNumericModifiers(MenuItem menuItem) {
            return menuItem.getNumericModifiers();
        }

        @Override
        public CharSequence getTooltipText(MenuItem menuItem) {
            return menuItem.getTooltipText();
        }

        @Override
        public void setAlphabeticShortcut(MenuItem menuItem, char c, int n) {
            menuItem.setAlphabeticShortcut(c, n);
        }

        @Override
        public void setContentDescription(MenuItem menuItem, CharSequence charSequence) {
            menuItem.setContentDescription(charSequence);
        }

        @Override
        public void setIconTintList(MenuItem menuItem, ColorStateList colorStateList) {
            menuItem.setIconTintList(colorStateList);
        }

        @Override
        public void setIconTintMode(MenuItem menuItem, PorterDuff.Mode mode) {
            menuItem.setIconTintMode(mode);
        }

        @Override
        public void setNumericShortcut(MenuItem menuItem, char c, int n) {
            menuItem.setNumericShortcut(c, n);
        }

        @Override
        public void setShortcut(MenuItem menuItem, char c, char c2, int n, int n2) {
            menuItem.setShortcut(c, c2, n, n2);
        }

        @Override
        public void setTooltipText(MenuItem menuItem, CharSequence charSequence) {
            menuItem.setTooltipText(charSequence);
        }
    }

    static class MenuItemCompatBaseImpl
    implements MenuVersionImpl {
        MenuItemCompatBaseImpl() {
        }

        @Override
        public int getAlphabeticModifiers(MenuItem menuItem) {
            return 0;
        }

        @Override
        public CharSequence getContentDescription(MenuItem menuItem) {
            return null;
        }

        @Override
        public ColorStateList getIconTintList(MenuItem menuItem) {
            return null;
        }

        @Override
        public PorterDuff.Mode getIconTintMode(MenuItem menuItem) {
            return null;
        }

        @Override
        public int getNumericModifiers(MenuItem menuItem) {
            return 0;
        }

        @Override
        public CharSequence getTooltipText(MenuItem menuItem) {
            return null;
        }

        @Override
        public void setAlphabeticShortcut(MenuItem menuItem, char c, int n) {
        }

        @Override
        public void setContentDescription(MenuItem menuItem, CharSequence charSequence) {
        }

        @Override
        public void setIconTintList(MenuItem menuItem, ColorStateList colorStateList) {
        }

        @Override
        public void setIconTintMode(MenuItem menuItem, PorterDuff.Mode mode) {
        }

        @Override
        public void setNumericShortcut(MenuItem menuItem, char c, int n) {
        }

        @Override
        public void setShortcut(MenuItem menuItem, char c, char c2, int n, int n2) {
        }

        @Override
        public void setTooltipText(MenuItem menuItem, CharSequence charSequence) {
        }
    }

    static interface MenuVersionImpl {
        public int getAlphabeticModifiers(MenuItem var1);

        public CharSequence getContentDescription(MenuItem var1);

        public ColorStateList getIconTintList(MenuItem var1);

        public PorterDuff.Mode getIconTintMode(MenuItem var1);

        public int getNumericModifiers(MenuItem var1);

        public CharSequence getTooltipText(MenuItem var1);

        public void setAlphabeticShortcut(MenuItem var1, char var2, int var3);

        public void setContentDescription(MenuItem var1, CharSequence var2);

        public void setIconTintList(MenuItem var1, ColorStateList var2);

        public void setIconTintMode(MenuItem var1, PorterDuff.Mode var2);

        public void setNumericShortcut(MenuItem var1, char var2, int var3);

        public void setShortcut(MenuItem var1, char var2, char var3, int var4, int var5);

        public void setTooltipText(MenuItem var1, CharSequence var2);
    }

    @Deprecated
    public static interface OnActionExpandListener {
        public boolean onMenuItemActionCollapse(MenuItem var1);

        public boolean onMenuItemActionExpand(MenuItem var1);
    }

}

