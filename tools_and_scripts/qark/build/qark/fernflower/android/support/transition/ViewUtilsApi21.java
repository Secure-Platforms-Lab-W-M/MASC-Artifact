package android.support.transition;

import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(21)
class ViewUtilsApi21 extends ViewUtilsApi19 {
   private static final String TAG = "ViewUtilsApi21";
   private static Method sSetAnimationMatrixMethod;
   private static boolean sSetAnimationMatrixMethodFetched;
   private static Method sTransformMatrixToGlobalMethod;
   private static boolean sTransformMatrixToGlobalMethodFetched;
   private static Method sTransformMatrixToLocalMethod;
   private static boolean sTransformMatrixToLocalMethodFetched;

   private void fetchSetAnimationMatrix() {
      if (!sSetAnimationMatrixMethodFetched) {
         try {
            sSetAnimationMatrixMethod = View.class.getDeclaredMethod("setAnimationMatrix", Matrix.class);
            sSetAnimationMatrixMethod.setAccessible(true);
         } catch (NoSuchMethodException var2) {
            Log.i("ViewUtilsApi21", "Failed to retrieve setAnimationMatrix method", var2);
         }

         sSetAnimationMatrixMethodFetched = true;
      }
   }

   private void fetchTransformMatrixToGlobalMethod() {
      if (!sTransformMatrixToGlobalMethodFetched) {
         try {
            sTransformMatrixToGlobalMethod = View.class.getDeclaredMethod("transformMatrixToGlobal", Matrix.class);
            sTransformMatrixToGlobalMethod.setAccessible(true);
         } catch (NoSuchMethodException var2) {
            Log.i("ViewUtilsApi21", "Failed to retrieve transformMatrixToGlobal method", var2);
         }

         sTransformMatrixToGlobalMethodFetched = true;
      }
   }

   private void fetchTransformMatrixToLocalMethod() {
      if (!sTransformMatrixToLocalMethodFetched) {
         try {
            sTransformMatrixToLocalMethod = View.class.getDeclaredMethod("transformMatrixToLocal", Matrix.class);
            sTransformMatrixToLocalMethod.setAccessible(true);
         } catch (NoSuchMethodException var2) {
            Log.i("ViewUtilsApi21", "Failed to retrieve transformMatrixToLocal method", var2);
         }

         sTransformMatrixToLocalMethodFetched = true;
      }
   }

   public void setAnimationMatrix(@NonNull View var1, Matrix var2) {
      this.fetchSetAnimationMatrix();
      Method var3 = sSetAnimationMatrixMethod;
      if (var3 != null) {
         try {
            var3.invoke(var1, var2);
         } catch (InvocationTargetException var4) {
         } catch (IllegalAccessException var5) {
            throw new RuntimeException(var5.getCause());
         }

      }
   }

   public void transformMatrixToGlobal(@NonNull View var1, @NonNull Matrix var2) {
      this.fetchTransformMatrixToGlobalMethod();
      Method var3 = sTransformMatrixToGlobalMethod;
      if (var3 != null) {
         try {
            var3.invoke(var1, var2);
         } catch (IllegalAccessException var4) {
         } catch (InvocationTargetException var5) {
            throw new RuntimeException(var5.getCause());
         }

      }
   }

   public void transformMatrixToLocal(@NonNull View var1, @NonNull Matrix var2) {
      this.fetchTransformMatrixToLocalMethod();
      Method var3 = sTransformMatrixToLocalMethod;
      if (var3 != null) {
         try {
            var3.invoke(var1, var2);
         } catch (IllegalAccessException var4) {
         } catch (InvocationTargetException var5) {
            throw new RuntimeException(var5.getCause());
         }

      }
   }
}
