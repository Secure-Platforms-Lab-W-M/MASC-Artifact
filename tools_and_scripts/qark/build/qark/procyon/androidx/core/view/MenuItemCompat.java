// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.view.MenuItem$OnActionExpandListener;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.os.Build$VERSION;
import android.view.View;
import android.util.Log;
import androidx.core.internal.view.SupportMenuItem;
import android.view.MenuItem;

public final class MenuItemCompat
{
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
    public static boolean collapseActionView(final MenuItem menuItem) {
        return menuItem.collapseActionView();
    }
    
    @Deprecated
    public static boolean expandActionView(final MenuItem menuItem) {
        return menuItem.expandActionView();
    }
    
    public static ActionProvider getActionProvider(final MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getSupportActionProvider();
        }
        Log.w("MenuItemCompat", "getActionProvider: item does not implement SupportMenuItem; returning null");
        return null;
    }
    
    @Deprecated
    public static View getActionView(final MenuItem menuItem) {
        return menuItem.getActionView();
    }
    
    public static int getAlphabeticModifiers(final MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getAlphabeticModifiers();
        }
        if (Build$VERSION.SDK_INT >= 26) {
            return menuItem.getAlphabeticModifiers();
        }
        return 0;
    }
    
    public static CharSequence getContentDescription(final MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getContentDescription();
        }
        if (Build$VERSION.SDK_INT >= 26) {
            return menuItem.getContentDescription();
        }
        return null;
    }
    
    public static ColorStateList getIconTintList(final MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getIconTintList();
        }
        if (Build$VERSION.SDK_INT >= 26) {
            return menuItem.getIconTintList();
        }
        return null;
    }
    
    public static PorterDuff$Mode getIconTintMode(final MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getIconTintMode();
        }
        if (Build$VERSION.SDK_INT >= 26) {
            return menuItem.getIconTintMode();
        }
        return null;
    }
    
    public static int getNumericModifiers(final MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getNumericModifiers();
        }
        if (Build$VERSION.SDK_INT >= 26) {
            return menuItem.getNumericModifiers();
        }
        return 0;
    }
    
    public static CharSequence getTooltipText(final MenuItem menuItem) {
        if (menuItem instanceof SupportMenuItem) {
            return ((SupportMenuItem)menuItem).getTooltipText();
        }
        if (Build$VERSION.SDK_INT >= 26) {
            return menuItem.getTooltipText();
        }
        return null;
    }
    
    @Deprecated
    public static boolean isActionViewExpanded(final MenuItem menuItem) {
        return menuItem.isActionViewExpanded();
    }
    
    public static MenuItem setActionProvider(final MenuItem menuItem, final ActionProvider supportActionProvider) {
        if (menuItem instanceof SupportMenuItem) {
            return (MenuItem)((SupportMenuItem)menuItem).setSupportActionProvider(supportActionProvider);
        }
        Log.w("MenuItemCompat", "setActionProvider: item does not implement SupportMenuItem; ignoring");
        return menuItem;
    }
    
    @Deprecated
    public static MenuItem setActionView(final MenuItem menuItem, final int actionView) {
        return menuItem.setActionView(actionView);
    }
    
    @Deprecated
    public static MenuItem setActionView(final MenuItem menuItem, final View actionView) {
        return menuItem.setActionView(actionView);
    }
    
    public static void setAlphabeticShortcut(final MenuItem menuItem, final char c, final int n) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setAlphabeticShortcut(c, n);
            return;
        }
        if (Build$VERSION.SDK_INT >= 26) {
            menuItem.setAlphabeticShortcut(c, n);
        }
    }
    
    public static void setContentDescription(final MenuItem menuItem, final CharSequence charSequence) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setContentDescription(charSequence);
            return;
        }
        if (Build$VERSION.SDK_INT >= 26) {
            menuItem.setContentDescription(charSequence);
        }
    }
    
    public static void setIconTintList(final MenuItem menuItem, final ColorStateList list) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setIconTintList(list);
            return;
        }
        if (Build$VERSION.SDK_INT >= 26) {
            menuItem.setIconTintList(list);
        }
    }
    
    public static void setIconTintMode(final MenuItem menuItem, final PorterDuff$Mode porterDuff$Mode) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setIconTintMode(porterDuff$Mode);
            return;
        }
        if (Build$VERSION.SDK_INT >= 26) {
            menuItem.setIconTintMode(porterDuff$Mode);
        }
    }
    
    public static void setNumericShortcut(final MenuItem menuItem, final char c, final int n) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setNumericShortcut(c, n);
            return;
        }
        if (Build$VERSION.SDK_INT >= 26) {
            menuItem.setNumericShortcut(c, n);
        }
    }
    
    @Deprecated
    public static MenuItem setOnActionExpandListener(final MenuItem menuItem, final OnActionExpandListener onActionExpandListener) {
        return menuItem.setOnActionExpandListener((MenuItem$OnActionExpandListener)new MenuItem$OnActionExpandListener() {
            public boolean onMenuItemActionCollapse(final MenuItem menuItem) {
                return onActionExpandListener.onMenuItemActionCollapse(menuItem);
            }
            
            public boolean onMenuItemActionExpand(final MenuItem menuItem) {
                return onActionExpandListener.onMenuItemActionExpand(menuItem);
            }
        });
    }
    
    public static void setShortcut(final MenuItem menuItem, final char c, final char c2, final int n, final int n2) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setShortcut(c, c2, n, n2);
            return;
        }
        if (Build$VERSION.SDK_INT >= 26) {
            menuItem.setShortcut(c, c2, n, n2);
        }
    }
    
    @Deprecated
    public static void setShowAsAction(final MenuItem menuItem, final int showAsAction) {
        menuItem.setShowAsAction(showAsAction);
    }
    
    public static void setTooltipText(final MenuItem menuItem, final CharSequence charSequence) {
        if (menuItem instanceof SupportMenuItem) {
            ((SupportMenuItem)menuItem).setTooltipText(charSequence);
            return;
        }
        if (Build$VERSION.SDK_INT >= 26) {
            menuItem.setTooltipText(charSequence);
        }
    }
    
    @Deprecated
    public interface OnActionExpandListener
    {
        boolean onMenuItemActionCollapse(final MenuItem p0);
        
        boolean onMenuItemActionExpand(final MenuItem p0);
    }
}
