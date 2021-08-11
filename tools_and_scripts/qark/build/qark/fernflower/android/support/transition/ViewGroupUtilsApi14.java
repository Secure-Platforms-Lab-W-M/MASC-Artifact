package android.support.transition;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.ViewGroup;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiresApi(14)
class ViewGroupUtilsApi14 implements ViewGroupUtilsImpl {
   private static final int LAYOUT_TRANSITION_CHANGING = 4;
   private static final String TAG = "ViewGroupUtilsApi14";
   private static Method sCancelMethod;
   private static boolean sCancelMethodFetched;
   private static LayoutTransition sEmptyLayoutTransition;
   private static Field sLayoutSuppressedField;
   private static boolean sLayoutSuppressedFieldFetched;

   private static void cancelLayoutTransition(LayoutTransition var0) {
      if (!sCancelMethodFetched) {
         try {
            sCancelMethod = LayoutTransition.class.getDeclaredMethod("cancel");
            sCancelMethod.setAccessible(true);
         } catch (NoSuchMethodException var4) {
            Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
         }

         sCancelMethodFetched = true;
      }

      Method var1 = sCancelMethod;
      if (var1 != null) {
         try {
            var1.invoke(var0);
         } catch (IllegalAccessException var2) {
            Log.i("ViewGroupUtilsApi14", "Failed to access cancel method by reflection");
         } catch (InvocationTargetException var3) {
            Log.i("ViewGroupUtilsApi14", "Failed to invoke cancel method by reflection");
            return;
         }

      }
   }

   public ViewGroupOverlayImpl getOverlay(@NonNull ViewGroup var1) {
      return ViewGroupOverlayApi14.createFrom(var1);
   }

   public void suppressLayout(@NonNull ViewGroup var1, boolean var2) {
      if (sEmptyLayoutTransition == null) {
         sEmptyLayoutTransition = new LayoutTransition() {
            public boolean isChangingLayout() {
               return true;
            }
         };
         sEmptyLayoutTransition.setAnimator(2, (Animator)null);
         sEmptyLayoutTransition.setAnimator(0, (Animator)null);
         sEmptyLayoutTransition.setAnimator(1, (Animator)null);
         sEmptyLayoutTransition.setAnimator(3, (Animator)null);
         sEmptyLayoutTransition.setAnimator(4, (Animator)null);
      }

      LayoutTransition var8;
      if (var2) {
         var8 = var1.getLayoutTransition();
         if (var8 != null) {
            if (var8.isRunning()) {
               cancelLayoutTransition(var8);
            }

            if (var8 != sEmptyLayoutTransition) {
               var1.setTag(R$id.transition_layout_save, var8);
            }
         }

         var1.setLayoutTransition(sEmptyLayoutTransition);
      } else {
         var1.setLayoutTransition((LayoutTransition)null);
         if (!sLayoutSuppressedFieldFetched) {
            try {
               sLayoutSuppressedField = ViewGroup.class.getDeclaredField("mLayoutSuppressed");
               sLayoutSuppressedField.setAccessible(true);
            } catch (NoSuchFieldException var5) {
               Log.i("ViewGroupUtilsApi14", "Failed to access mLayoutSuppressed field by reflection");
            }

            sLayoutSuppressedFieldFetched = true;
         }

         boolean var3 = false;
         var2 = false;
         Field var4 = sLayoutSuppressedField;
         if (var4 != null) {
            label63: {
               label62: {
                  label71: {
                     boolean var10001;
                     try {
                        var3 = var4.getBoolean(var1);
                     } catch (IllegalAccessException var7) {
                        var10001 = false;
                        break label71;
                     }

                     if (!var3) {
                        break label62;
                     }

                     var2 = var3;

                     try {
                        sLayoutSuppressedField.setBoolean(var1, false);
                        break label62;
                     } catch (IllegalAccessException var6) {
                        var10001 = false;
                     }
                  }

                  Log.i("ViewGroupUtilsApi14", "Failed to get mLayoutSuppressed field by reflection");
                  break label63;
               }

               var2 = var3;
            }
         } else {
            var2 = var3;
         }

         if (var2) {
            var1.requestLayout();
         }

         var8 = (LayoutTransition)var1.getTag(R$id.transition_layout_save);
         if (var8 != null) {
            var1.setTag(R$id.transition_layout_save, (Object)null);
            var1.setLayoutTransition(var8);
         }
      }
   }
}
