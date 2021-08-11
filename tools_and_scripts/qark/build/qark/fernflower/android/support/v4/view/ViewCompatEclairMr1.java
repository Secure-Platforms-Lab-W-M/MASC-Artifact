package android.support.v4.view;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewCompatEclairMr1 {
   public static final String TAG = "ViewCompat";
   private static Method sChildrenDrawingOrderMethod;

   public static boolean isOpaque(View var0) {
      return var0.isOpaque();
   }

   public static void setChildrenDrawingOrderEnabled(ViewGroup var0, boolean var1) {
      if (sChildrenDrawingOrderMethod == null) {
         try {
            sChildrenDrawingOrderMethod = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", Boolean.TYPE);
         } catch (NoSuchMethodException var6) {
            Log.e("ViewCompat", "Unable to find childrenDrawingOrderEnabled", var6);
         }

         sChildrenDrawingOrderMethod.setAccessible(true);
      }

      try {
         sChildrenDrawingOrderMethod.invoke(var0, var1);
      } catch (IllegalAccessException var3) {
         Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", var3);
      } catch (IllegalArgumentException var4) {
         Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", var4);
      } catch (InvocationTargetException var5) {
         Log.e("ViewCompat", "Unable to invoke childrenDrawingOrderEnabled", var5);
         return;
      }

   }
}
