package android.support.v4.widget;

import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class PopupWindowCompat {
   static final PopupWindowCompat.PopupWindowCompatBaseImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 23) {
         IMPL = new PopupWindowCompat.PopupWindowCompatApi23Impl();
      } else if (VERSION.SDK_INT >= 21) {
         IMPL = new PopupWindowCompat.PopupWindowCompatApi21Impl();
      } else if (VERSION.SDK_INT >= 19) {
         IMPL = new PopupWindowCompat.PopupWindowCompatApi19Impl();
      } else {
         IMPL = new PopupWindowCompat.PopupWindowCompatBaseImpl();
      }
   }

   private PopupWindowCompat() {
   }

   public static boolean getOverlapAnchor(PopupWindow var0) {
      return IMPL.getOverlapAnchor(var0);
   }

   public static int getWindowLayoutType(PopupWindow var0) {
      return IMPL.getWindowLayoutType(var0);
   }

   public static void setOverlapAnchor(PopupWindow var0, boolean var1) {
      IMPL.setOverlapAnchor(var0, var1);
   }

   public static void setWindowLayoutType(PopupWindow var0, int var1) {
      IMPL.setWindowLayoutType(var0, var1);
   }

   public static void showAsDropDown(PopupWindow var0, View var1, int var2, int var3, int var4) {
      IMPL.showAsDropDown(var0, var1, var2, var3, var4);
   }

   @RequiresApi(19)
   static class PopupWindowCompatApi19Impl extends PopupWindowCompat.PopupWindowCompatBaseImpl {
      public void showAsDropDown(PopupWindow var1, View var2, int var3, int var4, int var5) {
         var1.showAsDropDown(var2, var3, var4, var5);
      }
   }

   @RequiresApi(21)
   static class PopupWindowCompatApi21Impl extends PopupWindowCompat.PopupWindowCompatApi19Impl {
      private static final String TAG = "PopupWindowCompatApi21";
      private static Field sOverlapAnchorField;

      static {
         try {
            sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor");
            sOverlapAnchorField.setAccessible(true);
         } catch (NoSuchFieldException var1) {
            Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", var1);
         }
      }

      public boolean getOverlapAnchor(PopupWindow var1) {
         Field var3 = sOverlapAnchorField;
         if (var3 != null) {
            try {
               boolean var2 = (Boolean)var3.get(var1);
               return var2;
            } catch (IllegalAccessException var4) {
               Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", var4);
            }
         }

         return false;
      }

      public void setOverlapAnchor(PopupWindow var1, boolean var2) {
         Field var3 = sOverlapAnchorField;
         if (var3 != null) {
            try {
               var3.set(var1, var2);
               return;
            } catch (IllegalAccessException var4) {
               Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", var4);
            }
         }

      }
   }

   @RequiresApi(23)
   static class PopupWindowCompatApi23Impl extends PopupWindowCompat.PopupWindowCompatApi21Impl {
      public boolean getOverlapAnchor(PopupWindow var1) {
         return var1.getOverlapAnchor();
      }

      public int getWindowLayoutType(PopupWindow var1) {
         return var1.getWindowLayoutType();
      }

      public void setOverlapAnchor(PopupWindow var1, boolean var2) {
         var1.setOverlapAnchor(var2);
      }

      public void setWindowLayoutType(PopupWindow var1, int var2) {
         var1.setWindowLayoutType(var2);
      }
   }

   static class PopupWindowCompatBaseImpl {
      private static Method sGetWindowLayoutTypeMethod;
      private static boolean sGetWindowLayoutTypeMethodAttempted;
      private static Method sSetWindowLayoutTypeMethod;
      private static boolean sSetWindowLayoutTypeMethodAttempted;

      public boolean getOverlapAnchor(PopupWindow var1) {
         return false;
      }

      public int getWindowLayoutType(PopupWindow var1) {
         if (!sGetWindowLayoutTypeMethodAttempted) {
            try {
               sGetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("getWindowLayoutType");
               sGetWindowLayoutTypeMethod.setAccessible(true);
            } catch (Exception var4) {
            }

            sGetWindowLayoutTypeMethodAttempted = true;
         }

         Method var3 = sGetWindowLayoutTypeMethod;
         if (var3 != null) {
            try {
               int var2 = (Integer)var3.invoke(var1);
               return var2;
            } catch (Exception var5) {
            }
         }

         return 0;
      }

      public void setOverlapAnchor(PopupWindow var1, boolean var2) {
      }

      public void setWindowLayoutType(PopupWindow var1, int var2) {
         if (!sSetWindowLayoutTypeMethodAttempted) {
            try {
               sSetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", Integer.TYPE);
               sSetWindowLayoutTypeMethod.setAccessible(true);
            } catch (Exception var4) {
            }

            sSetWindowLayoutTypeMethodAttempted = true;
         }

         Method var3 = sSetWindowLayoutTypeMethod;
         if (var3 != null) {
            try {
               var3.invoke(var1, var2);
               return;
            } catch (Exception var5) {
            }
         }

      }

      public void showAsDropDown(PopupWindow var1, View var2, int var3, int var4, int var5) {
         int var6 = var3;
         if ((GravityCompat.getAbsoluteGravity(var5, ViewCompat.getLayoutDirection(var2)) & 7) == 5) {
            var6 = var3 - (var1.getWidth() - var2.getWidth());
         }

         var1.showAsDropDown(var2, var6, var4);
      }
   }
}
