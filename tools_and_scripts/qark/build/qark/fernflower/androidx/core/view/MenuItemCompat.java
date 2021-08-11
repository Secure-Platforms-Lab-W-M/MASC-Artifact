package androidx.core.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.core.internal.view.SupportMenuItem;

public final class MenuItemCompat {
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
   public static boolean collapseActionView(MenuItem var0) {
      return var0.collapseActionView();
   }

   @Deprecated
   public static boolean expandActionView(MenuItem var0) {
      return var0.expandActionView();
   }

   public static ActionProvider getActionProvider(MenuItem var0) {
      if (var0 instanceof SupportMenuItem) {
         return ((SupportMenuItem)var0).getSupportActionProvider();
      } else {
         Log.w("MenuItemCompat", "getActionProvider: item does not implement SupportMenuItem; returning null");
         return null;
      }
   }

   @Deprecated
   public static View getActionView(MenuItem var0) {
      return var0.getActionView();
   }

   public static int getAlphabeticModifiers(MenuItem var0) {
      if (var0 instanceof SupportMenuItem) {
         return ((SupportMenuItem)var0).getAlphabeticModifiers();
      } else {
         return VERSION.SDK_INT >= 26 ? var0.getAlphabeticModifiers() : 0;
      }
   }

   public static CharSequence getContentDescription(MenuItem var0) {
      if (var0 instanceof SupportMenuItem) {
         return ((SupportMenuItem)var0).getContentDescription();
      } else {
         return VERSION.SDK_INT >= 26 ? var0.getContentDescription() : null;
      }
   }

   public static ColorStateList getIconTintList(MenuItem var0) {
      if (var0 instanceof SupportMenuItem) {
         return ((SupportMenuItem)var0).getIconTintList();
      } else {
         return VERSION.SDK_INT >= 26 ? var0.getIconTintList() : null;
      }
   }

   public static Mode getIconTintMode(MenuItem var0) {
      if (var0 instanceof SupportMenuItem) {
         return ((SupportMenuItem)var0).getIconTintMode();
      } else {
         return VERSION.SDK_INT >= 26 ? var0.getIconTintMode() : null;
      }
   }

   public static int getNumericModifiers(MenuItem var0) {
      if (var0 instanceof SupportMenuItem) {
         return ((SupportMenuItem)var0).getNumericModifiers();
      } else {
         return VERSION.SDK_INT >= 26 ? var0.getNumericModifiers() : 0;
      }
   }

   public static CharSequence getTooltipText(MenuItem var0) {
      if (var0 instanceof SupportMenuItem) {
         return ((SupportMenuItem)var0).getTooltipText();
      } else {
         return VERSION.SDK_INT >= 26 ? var0.getTooltipText() : null;
      }
   }

   @Deprecated
   public static boolean isActionViewExpanded(MenuItem var0) {
      return var0.isActionViewExpanded();
   }

   public static MenuItem setActionProvider(MenuItem var0, ActionProvider var1) {
      if (var0 instanceof SupportMenuItem) {
         return ((SupportMenuItem)var0).setSupportActionProvider(var1);
      } else {
         Log.w("MenuItemCompat", "setActionProvider: item does not implement SupportMenuItem; ignoring");
         return var0;
      }
   }

   @Deprecated
   public static MenuItem setActionView(MenuItem var0, int var1) {
      return var0.setActionView(var1);
   }

   @Deprecated
   public static MenuItem setActionView(MenuItem var0, View var1) {
      return var0.setActionView(var1);
   }

   public static void setAlphabeticShortcut(MenuItem var0, char var1, int var2) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setAlphabeticShortcut(var1, var2);
      } else {
         if (VERSION.SDK_INT >= 26) {
            var0.setAlphabeticShortcut(var1, var2);
         }

      }
   }

   public static void setContentDescription(MenuItem var0, CharSequence var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setContentDescription(var1);
      } else {
         if (VERSION.SDK_INT >= 26) {
            var0.setContentDescription(var1);
         }

      }
   }

   public static void setIconTintList(MenuItem var0, ColorStateList var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setIconTintList(var1);
      } else {
         if (VERSION.SDK_INT >= 26) {
            var0.setIconTintList(var1);
         }

      }
   }

   public static void setIconTintMode(MenuItem var0, Mode var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setIconTintMode(var1);
      } else {
         if (VERSION.SDK_INT >= 26) {
            var0.setIconTintMode(var1);
         }

      }
   }

   public static void setNumericShortcut(MenuItem var0, char var1, int var2) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setNumericShortcut(var1, var2);
      } else {
         if (VERSION.SDK_INT >= 26) {
            var0.setNumericShortcut(var1, var2);
         }

      }
   }

   @Deprecated
   public static MenuItem setOnActionExpandListener(MenuItem var0, final MenuItemCompat.OnActionExpandListener var1) {
      return var0.setOnActionExpandListener(new android.view.MenuItem.OnActionExpandListener() {
         public boolean onMenuItemActionCollapse(MenuItem var1x) {
            return var1.onMenuItemActionCollapse(var1x);
         }

         public boolean onMenuItemActionExpand(MenuItem var1x) {
            return var1.onMenuItemActionExpand(var1x);
         }
      });
   }

   public static void setShortcut(MenuItem var0, char var1, char var2, int var3, int var4) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setShortcut(var1, var2, var3, var4);
      } else {
         if (VERSION.SDK_INT >= 26) {
            var0.setShortcut(var1, var2, var3, var4);
         }

      }
   }

   @Deprecated
   public static void setShowAsAction(MenuItem var0, int var1) {
      var0.setShowAsAction(var1);
   }

   public static void setTooltipText(MenuItem var0, CharSequence var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setTooltipText(var1);
      } else {
         if (VERSION.SDK_INT >= 26) {
            var0.setTooltipText(var1);
         }

      }
   }

   @Deprecated
   public interface OnActionExpandListener {
      boolean onMenuItemActionCollapse(MenuItem var1);

      boolean onMenuItemActionExpand(MenuItem var1);
   }
}
