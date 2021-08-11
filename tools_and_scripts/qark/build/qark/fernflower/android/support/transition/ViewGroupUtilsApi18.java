package android.support.transition;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(18)
class ViewGroupUtilsApi18 extends ViewGroupUtilsApi14 {
   private static final String TAG = "ViewUtilsApi18";
   private static Method sSuppressLayoutMethod;
   private static boolean sSuppressLayoutMethodFetched;

   private void fetchSuppressLayoutMethod() {
      if (!sSuppressLayoutMethodFetched) {
         try {
            sSuppressLayoutMethod = ViewGroup.class.getDeclaredMethod("suppressLayout", Boolean.TYPE);
            sSuppressLayoutMethod.setAccessible(true);
         } catch (NoSuchMethodException var2) {
            Log.i("ViewUtilsApi18", "Failed to retrieve suppressLayout method", var2);
         }

         sSuppressLayoutMethodFetched = true;
      }
   }

   public ViewGroupOverlayImpl getOverlay(@NonNull ViewGroup var1) {
      return new ViewGroupOverlayApi18(var1);
   }

   public void suppressLayout(@NonNull ViewGroup var1, boolean var2) {
      this.fetchSuppressLayoutMethod();
      Method var3 = sSuppressLayoutMethod;
      if (var3 != null) {
         try {
            var3.invoke(var1, var2);
         } catch (IllegalAccessException var4) {
            Log.i("ViewUtilsApi18", "Failed to invoke suppressLayout method", var4);
         } catch (InvocationTargetException var5) {
            Log.i("ViewUtilsApi18", "Error invoking suppressLayout method", var5);
            return;
         }

      }
   }
}
