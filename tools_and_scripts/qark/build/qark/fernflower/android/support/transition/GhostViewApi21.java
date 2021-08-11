package android.support.transition;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(21)
class GhostViewApi21 implements GhostViewImpl {
   private static final String TAG = "GhostViewApi21";
   private static Method sAddGhostMethod;
   private static boolean sAddGhostMethodFetched;
   private static Class sGhostViewClass;
   private static boolean sGhostViewClassFetched;
   private static Method sRemoveGhostMethod;
   private static boolean sRemoveGhostMethodFetched;
   private final View mGhostView;

   private GhostViewApi21(@NonNull View var1) {
      this.mGhostView = var1;
   }

   // $FF: synthetic method
   GhostViewApi21(View var1, Object var2) {
      this(var1);
   }

   private static void fetchAddGhostMethod() {
      if (!sAddGhostMethodFetched) {
         try {
            fetchGhostViewClass();
            sAddGhostMethod = sGhostViewClass.getDeclaredMethod("addGhost", View.class, ViewGroup.class, Matrix.class);
            sAddGhostMethod.setAccessible(true);
         } catch (NoSuchMethodException var1) {
            Log.i("GhostViewApi21", "Failed to retrieve addGhost method", var1);
         }

         sAddGhostMethodFetched = true;
      }
   }

   private static void fetchGhostViewClass() {
      if (!sGhostViewClassFetched) {
         try {
            sGhostViewClass = Class.forName("android.view.GhostView");
         } catch (ClassNotFoundException var1) {
            Log.i("GhostViewApi21", "Failed to retrieve GhostView class", var1);
         }

         sGhostViewClassFetched = true;
      }
   }

   private static void fetchRemoveGhostMethod() {
      if (!sRemoveGhostMethodFetched) {
         try {
            fetchGhostViewClass();
            sRemoveGhostMethod = sGhostViewClass.getDeclaredMethod("removeGhost", View.class);
            sRemoveGhostMethod.setAccessible(true);
         } catch (NoSuchMethodException var1) {
            Log.i("GhostViewApi21", "Failed to retrieve removeGhost method", var1);
         }

         sRemoveGhostMethodFetched = true;
      }
   }

   public void reserveEndViewTransition(ViewGroup var1, View var2) {
   }

   public void setVisibility(int var1) {
      this.mGhostView.setVisibility(var1);
   }

   static class Creator implements GhostViewImpl.Creator {
      public GhostViewImpl addGhost(View var1, ViewGroup var2, Matrix var3) {
         GhostViewApi21.fetchAddGhostMethod();
         if (GhostViewApi21.sAddGhostMethod != null) {
            try {
               GhostViewApi21 var6 = new GhostViewApi21((View)GhostViewApi21.sAddGhostMethod.invoke((Object)null, var1, var2, var3));
               return var6;
            } catch (IllegalAccessException var4) {
               return null;
            } catch (InvocationTargetException var5) {
               throw new RuntimeException(var5.getCause());
            }
         } else {
            return null;
         }
      }

      public void removeGhost(View var1) {
         GhostViewApi21.fetchRemoveGhostMethod();
         if (GhostViewApi21.sRemoveGhostMethod != null) {
            try {
               GhostViewApi21.sRemoveGhostMethod.invoke((Object)null, var1);
            } catch (IllegalAccessException var2) {
            } catch (InvocationTargetException var3) {
               throw new RuntimeException(var3.getCause());
            }

         }
      }
   }
}
