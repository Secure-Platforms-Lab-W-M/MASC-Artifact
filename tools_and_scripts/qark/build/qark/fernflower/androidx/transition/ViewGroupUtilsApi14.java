package androidx.transition;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.util.Log;
import android.view.ViewGroup;
import androidx.transition.R.id;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ViewGroupUtilsApi14 {
   private static final int LAYOUT_TRANSITION_CHANGING = 4;
   private static final String TAG = "ViewGroupUtilsApi14";
   private static Method sCancelMethod;
   private static boolean sCancelMethodFetched;
   private static LayoutTransition sEmptyLayoutTransition;
   private static Field sLayoutSuppressedField;
   private static boolean sLayoutSuppressedFieldFetched;

   private ViewGroupUtilsApi14() {
   }

   private static void cancelLayoutTransition(LayoutTransition var0) {
      Method var1;
      if (!sCancelMethodFetched) {
         try {
            var1 = LayoutTransition.class.getDeclaredMethod("cancel");
            sCancelMethod = var1;
            var1.setAccessible(true);
         } catch (NoSuchMethodException var2) {
            Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
         }

         sCancelMethodFetched = true;
      }

      var1 = sCancelMethod;
      if (var1 != null) {
         try {
            var1.invoke(var0);
            return;
         } catch (IllegalAccessException var3) {
            Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
         } catch (InvocationTargetException var4) {
            Log.i("ViewGroupUtilsApi14", "Failed to invoke cancel method by reflection");
            return;
         }
      }

   }

   static void suppressLayout(ViewGroup var0, boolean var1) {
      LayoutTransition var3;
      if (sEmptyLayoutTransition == null) {
         var3 = new LayoutTransition() {
            public boolean isChangingLayout() {
               return true;
            }
         };
         sEmptyLayoutTransition = var3;
         var3.setAnimator(2, (Animator)null);
         sEmptyLayoutTransition.setAnimator(0, (Animator)null);
         sEmptyLayoutTransition.setAnimator(1, (Animator)null);
         sEmptyLayoutTransition.setAnimator(3, (Animator)null);
         sEmptyLayoutTransition.setAnimator(4, (Animator)null);
      }

      if (var1) {
         var3 = var0.getLayoutTransition();
         if (var3 != null) {
            if (var3.isRunning()) {
               cancelLayoutTransition(var3);
            }

            if (var3 != sEmptyLayoutTransition) {
               var0.setTag(id.transition_layout_save, var3);
            }
         }

         var0.setLayoutTransition(sEmptyLayoutTransition);
      } else {
         var0.setLayoutTransition((LayoutTransition)null);
         Field var7;
         if (!sLayoutSuppressedFieldFetched) {
            try {
               var7 = ViewGroup.class.getDeclaredField("mLayoutSuppressed");
               sLayoutSuppressedField = var7;
               var7.setAccessible(true);
            } catch (NoSuchFieldException var4) {
               Log.i("ViewGroupUtilsApi14", "Failed to access mLayoutSuppressed field by reflection");
            }

            sLayoutSuppressedFieldFetched = true;
         }

         var1 = false;
         boolean var2 = false;
         var7 = sLayoutSuppressedField;
         if (var7 != null) {
            label70: {
               var1 = var2;

               label60: {
                  label71: {
                     boolean var10001;
                     try {
                        var2 = var7.getBoolean(var0);
                     } catch (IllegalAccessException var6) {
                        var10001 = false;
                        break label71;
                     }

                     if (!var2) {
                        break label60;
                     }

                     var1 = var2;

                     try {
                        sLayoutSuppressedField.setBoolean(var0, false);
                        break label60;
                     } catch (IllegalAccessException var5) {
                        var10001 = false;
                     }
                  }

                  Log.i("ViewGroupUtilsApi14", "Failed to get mLayoutSuppressed field by reflection");
                  break label70;
               }

               var1 = var2;
            }
         }

         if (var1) {
            var0.requestLayout();
         }

         var3 = (LayoutTransition)var0.getTag(id.transition_layout_save);
         if (var3 != null) {
            var0.setTag(id.transition_layout_save, (Object)null);
            var0.setLayoutTransition(var3);
         }

      }
   }
}
