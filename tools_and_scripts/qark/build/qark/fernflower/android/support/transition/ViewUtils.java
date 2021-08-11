package android.support.transition;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.Property;
import android.view.View;
import java.lang.reflect.Field;

class ViewUtils {
   static final Property CLIP_BOUNDS;
   private static final ViewUtilsImpl IMPL;
   private static final String TAG = "ViewUtils";
   static final Property TRANSITION_ALPHA;
   private static final int VISIBILITY_MASK = 12;
   private static Field sViewFlagsField;
   private static boolean sViewFlagsFieldFetched;

   static {
      if (VERSION.SDK_INT >= 22) {
         IMPL = new ViewUtilsApi22();
      } else if (VERSION.SDK_INT >= 21) {
         IMPL = new ViewUtilsApi21();
      } else if (VERSION.SDK_INT >= 19) {
         IMPL = new ViewUtilsApi19();
      } else if (VERSION.SDK_INT >= 18) {
         IMPL = new ViewUtilsApi18();
      } else {
         IMPL = new ViewUtilsApi14();
      }

      TRANSITION_ALPHA = new Property(Float.class, "translationAlpha") {
         public Float get(View var1) {
            return ViewUtils.getTransitionAlpha(var1);
         }

         public void set(View var1, Float var2) {
            ViewUtils.setTransitionAlpha(var1, var2);
         }
      };
      CLIP_BOUNDS = new Property(Rect.class, "clipBounds") {
         public Rect get(View var1) {
            return ViewCompat.getClipBounds(var1);
         }

         public void set(View var1, Rect var2) {
            ViewCompat.setClipBounds(var1, var2);
         }
      };
   }

   static void clearNonTransitionAlpha(@NonNull View var0) {
      IMPL.clearNonTransitionAlpha(var0);
   }

   private static void fetchViewFlagsField() {
      if (!sViewFlagsFieldFetched) {
         try {
            sViewFlagsField = View.class.getDeclaredField("mViewFlags");
            sViewFlagsField.setAccessible(true);
         } catch (NoSuchFieldException var1) {
            Log.i("ViewUtils", "fetchViewFlagsField: ");
         }

         sViewFlagsFieldFetched = true;
      }
   }

   static ViewOverlayImpl getOverlay(@NonNull View var0) {
      return IMPL.getOverlay(var0);
   }

   static float getTransitionAlpha(@NonNull View var0) {
      return IMPL.getTransitionAlpha(var0);
   }

   static WindowIdImpl getWindowId(@NonNull View var0) {
      return IMPL.getWindowId(var0);
   }

   static void saveNonTransitionAlpha(@NonNull View var0) {
      IMPL.saveNonTransitionAlpha(var0);
   }

   static void setAnimationMatrix(@NonNull View var0, @Nullable Matrix var1) {
      IMPL.setAnimationMatrix(var0, var1);
   }

   static void setLeftTopRightBottom(@NonNull View var0, int var1, int var2, int var3, int var4) {
      IMPL.setLeftTopRightBottom(var0, var1, var2, var3, var4);
   }

   static void setTransitionAlpha(@NonNull View var0, float var1) {
      IMPL.setTransitionAlpha(var0, var1);
   }

   static void setTransitionVisibility(@NonNull View var0, int var1) {
      fetchViewFlagsField();
      Field var3 = sViewFlagsField;
      if (var3 != null) {
         try {
            int var2 = var3.getInt(var0);
            sViewFlagsField.setInt(var0, var2 & -13 | var1);
         } catch (IllegalAccessException var4) {
         }
      }
   }

   static void transformMatrixToGlobal(@NonNull View var0, @NonNull Matrix var1) {
      IMPL.transformMatrixToGlobal(var0, var1);
   }

   static void transformMatrixToLocal(@NonNull View var0, @NonNull Matrix var1) {
      IMPL.transformMatrixToLocal(var0, var1);
   }
}
