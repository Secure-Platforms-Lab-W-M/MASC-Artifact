package android.support.design.widget;

import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer.DrawableContainerState;
import android.util.Log;
import java.lang.reflect.Method;

class DrawableUtils {
   private static final String LOG_TAG = "DrawableUtils";
   private static Method sSetConstantStateMethod;
   private static boolean sSetConstantStateMethodFetched;

   private DrawableUtils() {
   }

   static boolean setContainerConstantState(DrawableContainer var0, ConstantState var1) {
      return setContainerConstantStateV9(var0, var1);
   }

   private static boolean setContainerConstantStateV9(DrawableContainer var0, ConstantState var1) {
      if (!sSetConstantStateMethodFetched) {
         try {
            sSetConstantStateMethod = DrawableContainer.class.getDeclaredMethod("setConstantState", DrawableContainerState.class);
            sSetConstantStateMethod.setAccessible(true);
         } catch (NoSuchMethodException var4) {
            Log.e("DrawableUtils", "Could not fetch setConstantState(). Oh well.");
         }

         sSetConstantStateMethodFetched = true;
      }

      Method var2 = sSetConstantStateMethod;
      if (var2 != null) {
         try {
            var2.invoke(var0, var1);
            return true;
         } catch (Exception var3) {
            Log.e("DrawableUtils", "Could not invoke setConstantState(). Oh well.");
            return false;
         }
      } else {
         return false;
      }
   }
}
