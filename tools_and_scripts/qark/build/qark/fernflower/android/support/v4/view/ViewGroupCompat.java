package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public final class ViewGroupCompat {
   static final ViewGroupCompat.ViewGroupCompatBaseImpl IMPL;
   public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
   public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;

   static {
      if (VERSION.SDK_INT >= 21) {
         IMPL = new ViewGroupCompat.ViewGroupCompatApi21Impl();
      } else if (VERSION.SDK_INT >= 18) {
         IMPL = new ViewGroupCompat.ViewGroupCompatApi18Impl();
      } else {
         IMPL = new ViewGroupCompat.ViewGroupCompatBaseImpl();
      }
   }

   private ViewGroupCompat() {
   }

   public static int getLayoutMode(ViewGroup var0) {
      return IMPL.getLayoutMode(var0);
   }

   public static int getNestedScrollAxes(@NonNull ViewGroup var0) {
      return IMPL.getNestedScrollAxes(var0);
   }

   public static boolean isTransitionGroup(ViewGroup var0) {
      return IMPL.isTransitionGroup(var0);
   }

   @Deprecated
   public static boolean onRequestSendAccessibilityEvent(ViewGroup var0, View var1, AccessibilityEvent var2) {
      return var0.onRequestSendAccessibilityEvent(var1, var2);
   }

   public static void setLayoutMode(ViewGroup var0, int var1) {
      IMPL.setLayoutMode(var0, var1);
   }

   @Deprecated
   public static void setMotionEventSplittingEnabled(ViewGroup var0, boolean var1) {
      var0.setMotionEventSplittingEnabled(var1);
   }

   public static void setTransitionGroup(ViewGroup var0, boolean var1) {
      IMPL.setTransitionGroup(var0, var1);
   }

   @RequiresApi(18)
   static class ViewGroupCompatApi18Impl extends ViewGroupCompat.ViewGroupCompatBaseImpl {
      public int getLayoutMode(ViewGroup var1) {
         return var1.getLayoutMode();
      }

      public void setLayoutMode(ViewGroup var1, int var2) {
         var1.setLayoutMode(var2);
      }
   }

   @RequiresApi(21)
   static class ViewGroupCompatApi21Impl extends ViewGroupCompat.ViewGroupCompatApi18Impl {
      public int getNestedScrollAxes(ViewGroup var1) {
         return var1.getNestedScrollAxes();
      }

      public boolean isTransitionGroup(ViewGroup var1) {
         return var1.isTransitionGroup();
      }

      public void setTransitionGroup(ViewGroup var1, boolean var2) {
         var1.setTransitionGroup(var2);
      }
   }

   static class ViewGroupCompatBaseImpl {
      public int getLayoutMode(ViewGroup var1) {
         return 0;
      }

      public int getNestedScrollAxes(ViewGroup var1) {
         return var1 instanceof NestedScrollingParent ? ((NestedScrollingParent)var1).getNestedScrollAxes() : 0;
      }

      public boolean isTransitionGroup(ViewGroup var1) {
         return false;
      }

      public void setLayoutMode(ViewGroup var1, int var2) {
      }

      public void setTransitionGroup(ViewGroup var1, boolean var2) {
      }
   }
}
