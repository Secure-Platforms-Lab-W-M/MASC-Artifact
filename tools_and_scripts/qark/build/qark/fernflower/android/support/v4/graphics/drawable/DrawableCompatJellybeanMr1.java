package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.Method;

@TargetApi(17)
@RequiresApi(17)
class DrawableCompatJellybeanMr1 {
   private static final String TAG = "DrawableCompatJellybeanMr1";
   private static Method sGetLayoutDirectionMethod;
   private static boolean sGetLayoutDirectionMethodFetched;
   private static Method sSetLayoutDirectionMethod;
   private static boolean sSetLayoutDirectionMethodFetched;

   public static int getLayoutDirection(Drawable var0) {
      if (!sGetLayoutDirectionMethodFetched) {
         try {
            sGetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("getLayoutDirection");
            sGetLayoutDirectionMethod.setAccessible(true);
         } catch (NoSuchMethodException var3) {
            Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve getLayoutDirection() method", var3);
         }

         sGetLayoutDirectionMethodFetched = true;
      }

      if (sGetLayoutDirectionMethod != null) {
         try {
            int var1 = (Integer)sGetLayoutDirectionMethod.invoke(var0);
            return var1;
         } catch (Exception var4) {
            Log.i("DrawableCompatJellybeanMr1", "Failed to invoke getLayoutDirection() via reflection", var4);
            sGetLayoutDirectionMethod = null;
         }
      }

      return -1;
   }

   public static boolean setLayoutDirection(Drawable var0, int var1) {
      if (!sSetLayoutDirectionMethodFetched) {
         try {
            sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", Integer.TYPE);
            sSetLayoutDirectionMethod.setAccessible(true);
         } catch (NoSuchMethodException var3) {
            Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve setLayoutDirection(int) method", var3);
         }

         sSetLayoutDirectionMethodFetched = true;
      }

      if (sSetLayoutDirectionMethod != null) {
         try {
            sSetLayoutDirectionMethod.invoke(var0, var1);
            return true;
         } catch (Exception var4) {
            Log.i("DrawableCompatJellybeanMr1", "Failed to invoke setLayoutDirection(int) via reflection", var4);
            sSetLayoutDirectionMethod = null;
         }
      }

      return false;
   }
}
