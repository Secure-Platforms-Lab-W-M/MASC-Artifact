package android.support.v4.widget;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.PopupWindow;
import java.lang.reflect.Field;

@TargetApi(21)
@RequiresApi(21)
class PopupWindowCompatApi21 {
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

   static boolean getOverlapAnchor(PopupWindow var0) {
      if (sOverlapAnchorField != null) {
         try {
            boolean var1 = (Boolean)sOverlapAnchorField.get(var0);
            return var1;
         } catch (IllegalAccessException var2) {
            Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", var2);
         }
      }

      return false;
   }

   static void setOverlapAnchor(PopupWindow var0, boolean var1) {
      if (sOverlapAnchorField != null) {
         try {
            sOverlapAnchorField.set(var0, var1);
         } catch (IllegalAccessException var2) {
            Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", var2);
            return;
         }
      }

   }
}
