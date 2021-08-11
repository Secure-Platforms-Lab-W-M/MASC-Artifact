package android.support.v4.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.v4.internal.view.SupportMenuItem;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public final class MenuItemCompat {
   static final MenuItemCompat.MenuVersionImpl IMPL;
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

   static {
      if (VERSION.SDK_INT >= 26) {
         IMPL = new MenuItemCompat.MenuItemCompatApi26Impl();
      } else {
         IMPL = new MenuItemCompat.MenuItemCompatBaseImpl();
      }
   }

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
      return var0 instanceof SupportMenuItem ? ((SupportMenuItem)var0).getAlphabeticModifiers() : IMPL.getAlphabeticModifiers(var0);
   }

   public static CharSequence getContentDescription(MenuItem var0) {
      return var0 instanceof SupportMenuItem ? ((SupportMenuItem)var0).getContentDescription() : IMPL.getContentDescription(var0);
   }

   public static ColorStateList getIconTintList(MenuItem var0) {
      return var0 instanceof SupportMenuItem ? ((SupportMenuItem)var0).getIconTintList() : IMPL.getIconTintList(var0);
   }

   public static Mode getIconTintMode(MenuItem var0) {
      return var0 instanceof SupportMenuItem ? ((SupportMenuItem)var0).getIconTintMode() : IMPL.getIconTintMode(var0);
   }

   public static int getNumericModifiers(MenuItem var0) {
      return var0 instanceof SupportMenuItem ? ((SupportMenuItem)var0).getNumericModifiers() : IMPL.getNumericModifiers(var0);
   }

   public static CharSequence getTooltipText(MenuItem var0) {
      return var0 instanceof SupportMenuItem ? ((SupportMenuItem)var0).getTooltipText() : IMPL.getTooltipText(var0);
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
         IMPL.setAlphabeticShortcut(var0, var1, var2);
      }
   }

   public static void setContentDescription(MenuItem var0, CharSequence var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setContentDescription(var1);
      } else {
         IMPL.setContentDescription(var0, var1);
      }
   }

   public static void setIconTintList(MenuItem var0, ColorStateList var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setIconTintList(var1);
      } else {
         IMPL.setIconTintList(var0, var1);
      }
   }

   public static void setIconTintMode(MenuItem var0, Mode var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setIconTintMode(var1);
      } else {
         IMPL.setIconTintMode(var0, var1);
      }
   }

   public static void setNumericShortcut(MenuItem var0, char var1, int var2) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setNumericShortcut(var1, var2);
      } else {
         IMPL.setNumericShortcut(var0, var1, var2);
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
         IMPL.setShortcut(var0, var1, var2, var3, var4);
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
         IMPL.setTooltipText(var0, var1);
      }
   }

   @RequiresApi(26)
   static class MenuItemCompatApi26Impl extends MenuItemCompat.MenuItemCompatBaseImpl {
      public int getAlphabeticModifiers(MenuItem var1) {
         return var1.getAlphabeticModifiers();
      }

      public CharSequence getContentDescription(MenuItem var1) {
         return var1.getContentDescription();
      }

      public ColorStateList getIconTintList(MenuItem var1) {
         return var1.getIconTintList();
      }

      public Mode getIconTintMode(MenuItem var1) {
         return var1.getIconTintMode();
      }

      public int getNumericModifiers(MenuItem var1) {
         return var1.getNumericModifiers();
      }

      public CharSequence getTooltipText(MenuItem var1) {
         return var1.getTooltipText();
      }

      public void setAlphabeticShortcut(MenuItem var1, char var2, int var3) {
         var1.setAlphabeticShortcut(var2, var3);
      }

      public void setContentDescription(MenuItem var1, CharSequence var2) {
         var1.setContentDescription(var2);
      }

      public void setIconTintList(MenuItem var1, ColorStateList var2) {
         var1.setIconTintList(var2);
      }

      public void setIconTintMode(MenuItem var1, Mode var2) {
         var1.setIconTintMode(var2);
      }

      public void setNumericShortcut(MenuItem var1, char var2, int var3) {
         var1.setNumericShortcut(var2, var3);
      }

      public void setShortcut(MenuItem var1, char var2, char var3, int var4, int var5) {
         var1.setShortcut(var2, var3, var4, var5);
      }

      public void setTooltipText(MenuItem var1, CharSequence var2) {
         var1.setTooltipText(var2);
      }
   }

   static class MenuItemCompatBaseImpl implements MenuItemCompat.MenuVersionImpl {
      public int getAlphabeticModifiers(MenuItem var1) {
         return 0;
      }

      public CharSequence getContentDescription(MenuItem var1) {
         return null;
      }

      public ColorStateList getIconTintList(MenuItem var1) {
         return null;
      }

      public Mode getIconTintMode(MenuItem var1) {
         return null;
      }

      public int getNumericModifiers(MenuItem var1) {
         return 0;
      }

      public CharSequence getTooltipText(MenuItem var1) {
         return null;
      }

      public void setAlphabeticShortcut(MenuItem var1, char var2, int var3) {
      }

      public void setContentDescription(MenuItem var1, CharSequence var2) {
      }

      public void setIconTintList(MenuItem var1, ColorStateList var2) {
      }

      public void setIconTintMode(MenuItem var1, Mode var2) {
      }

      public void setNumericShortcut(MenuItem var1, char var2, int var3) {
      }

      public void setShortcut(MenuItem var1, char var2, char var3, int var4, int var5) {
      }

      public void setTooltipText(MenuItem var1, CharSequence var2) {
      }
   }

   interface MenuVersionImpl {
      int getAlphabeticModifiers(MenuItem var1);

      CharSequence getContentDescription(MenuItem var1);

      ColorStateList getIconTintList(MenuItem var1);

      Mode getIconTintMode(MenuItem var1);

      int getNumericModifiers(MenuItem var1);

      CharSequence getTooltipText(MenuItem var1);

      void setAlphabeticShortcut(MenuItem var1, char var2, int var3);

      void setContentDescription(MenuItem var1, CharSequence var2);

      void setIconTintList(MenuItem var1, ColorStateList var2);

      void setIconTintMode(MenuItem var1, Mode var2);

      void setNumericShortcut(MenuItem var1, char var2, int var3);

      void setShortcut(MenuItem var1, char var2, char var3, int var4, int var5);

      void setTooltipText(MenuItem var1, CharSequence var2);
   }

   @Deprecated
   public interface OnActionExpandListener {
      boolean onMenuItemActionCollapse(MenuItem var1);

      boolean onMenuItemActionExpand(MenuItem var1);
   }
}
