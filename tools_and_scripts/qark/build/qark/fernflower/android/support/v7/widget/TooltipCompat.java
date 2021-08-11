package android.support.v7.widget;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class TooltipCompat {
   private static final TooltipCompat.ViewCompatImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 26) {
         IMPL = new TooltipCompat.Api26ViewCompatImpl();
      } else {
         IMPL = new TooltipCompat.BaseViewCompatImpl();
      }
   }

   private TooltipCompat() {
   }

   public static void setTooltipText(@NonNull View var0, @Nullable CharSequence var1) {
      IMPL.setTooltipText(var0, var1);
   }

   @TargetApi(26)
   private static class Api26ViewCompatImpl implements TooltipCompat.ViewCompatImpl {
      private Api26ViewCompatImpl() {
      }

      // $FF: synthetic method
      Api26ViewCompatImpl(Object var1) {
         this();
      }

      public void setTooltipText(@NonNull View var1, @Nullable CharSequence var2) {
         var1.setTooltipText(var2);
      }
   }

   private static class BaseViewCompatImpl implements TooltipCompat.ViewCompatImpl {
      private BaseViewCompatImpl() {
      }

      // $FF: synthetic method
      BaseViewCompatImpl(Object var1) {
         this();
      }

      public void setTooltipText(@NonNull View var1, @Nullable CharSequence var2) {
         TooltipCompatHandler.setTooltipText(var1, var2);
      }
   }

   private interface ViewCompatImpl {
      void setTooltipText(@NonNull View var1, @Nullable CharSequence var2);
   }
}
