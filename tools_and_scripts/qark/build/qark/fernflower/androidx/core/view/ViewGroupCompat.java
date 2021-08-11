package androidx.core.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.R.id;

public final class ViewGroupCompat {
   public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
   public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;

   private ViewGroupCompat() {
   }

   public static int getLayoutMode(ViewGroup var0) {
      return VERSION.SDK_INT >= 18 ? var0.getLayoutMode() : 0;
   }

   public static int getNestedScrollAxes(ViewGroup var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.getNestedScrollAxes();
      } else {
         return var0 instanceof NestedScrollingParent ? ((NestedScrollingParent)var0).getNestedScrollAxes() : 0;
      }
   }

   public static boolean isTransitionGroup(ViewGroup var0) {
      if (VERSION.SDK_INT >= 21) {
         return var0.isTransitionGroup();
      } else {
         Boolean var1 = (Boolean)var0.getTag(id.tag_transition_group);
         return var1 != null && var1 || var0.getBackground() != null || ViewCompat.getTransitionName(var0) != null;
      }
   }

   @Deprecated
   public static boolean onRequestSendAccessibilityEvent(ViewGroup var0, View var1, AccessibilityEvent var2) {
      return var0.onRequestSendAccessibilityEvent(var1, var2);
   }

   public static void setLayoutMode(ViewGroup var0, int var1) {
      if (VERSION.SDK_INT >= 18) {
         var0.setLayoutMode(var1);
      }

   }

   @Deprecated
   public static void setMotionEventSplittingEnabled(ViewGroup var0, boolean var1) {
      var0.setMotionEventSplittingEnabled(var1);
   }

   public static void setTransitionGroup(ViewGroup var0, boolean var1) {
      if (VERSION.SDK_INT >= 21) {
         var0.setTransitionGroup(var1);
      } else {
         var0.setTag(id.tag_transition_group, var1);
      }
   }
}
