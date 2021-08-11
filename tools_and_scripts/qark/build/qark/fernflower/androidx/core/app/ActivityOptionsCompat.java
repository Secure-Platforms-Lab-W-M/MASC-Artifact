package androidx.core.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.view.View;
import androidx.core.util.Pair;

public class ActivityOptionsCompat {
   public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
   public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";

   protected ActivityOptionsCompat() {
   }

   public static ActivityOptionsCompat makeBasic() {
      return (ActivityOptionsCompat)(VERSION.SDK_INT >= 23 ? new ActivityOptionsCompat.ActivityOptionsCompatImpl(ActivityOptions.makeBasic()) : new ActivityOptionsCompat());
   }

   public static ActivityOptionsCompat makeClipRevealAnimation(View var0, int var1, int var2, int var3, int var4) {
      return (ActivityOptionsCompat)(VERSION.SDK_INT >= 23 ? new ActivityOptionsCompat.ActivityOptionsCompatImpl(ActivityOptions.makeClipRevealAnimation(var0, var1, var2, var3, var4)) : new ActivityOptionsCompat());
   }

   public static ActivityOptionsCompat makeCustomAnimation(Context var0, int var1, int var2) {
      return (ActivityOptionsCompat)(VERSION.SDK_INT >= 16 ? new ActivityOptionsCompat.ActivityOptionsCompatImpl(ActivityOptions.makeCustomAnimation(var0, var1, var2)) : new ActivityOptionsCompat());
   }

   public static ActivityOptionsCompat makeScaleUpAnimation(View var0, int var1, int var2, int var3, int var4) {
      return (ActivityOptionsCompat)(VERSION.SDK_INT >= 16 ? new ActivityOptionsCompat.ActivityOptionsCompatImpl(ActivityOptions.makeScaleUpAnimation(var0, var1, var2, var3, var4)) : new ActivityOptionsCompat());
   }

   public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity var0, View var1, String var2) {
      return (ActivityOptionsCompat)(VERSION.SDK_INT >= 21 ? new ActivityOptionsCompat.ActivityOptionsCompatImpl(ActivityOptions.makeSceneTransitionAnimation(var0, var1, var2)) : new ActivityOptionsCompat());
   }

   public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity var0, Pair... var1) {
      if (VERSION.SDK_INT < 21) {
         return new ActivityOptionsCompat();
      } else {
         android.util.Pair[] var3 = null;
         if (var1 != null) {
            android.util.Pair[] var4 = new android.util.Pair[var1.length];
            int var2 = 0;

            while(true) {
               var3 = var4;
               if (var2 >= var1.length) {
                  break;
               }

               var4[var2] = android.util.Pair.create(var1[var2].first, var1[var2].second);
               ++var2;
            }
         }

         return new ActivityOptionsCompat.ActivityOptionsCompatImpl(ActivityOptions.makeSceneTransitionAnimation(var0, var3));
      }
   }

   public static ActivityOptionsCompat makeTaskLaunchBehind() {
      return (ActivityOptionsCompat)(VERSION.SDK_INT >= 21 ? new ActivityOptionsCompat.ActivityOptionsCompatImpl(ActivityOptions.makeTaskLaunchBehind()) : new ActivityOptionsCompat());
   }

   public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View var0, Bitmap var1, int var2, int var3) {
      return (ActivityOptionsCompat)(VERSION.SDK_INT >= 16 ? new ActivityOptionsCompat.ActivityOptionsCompatImpl(ActivityOptions.makeThumbnailScaleUpAnimation(var0, var1, var2, var3)) : new ActivityOptionsCompat());
   }

   public Rect getLaunchBounds() {
      return null;
   }

   public void requestUsageTimeReport(PendingIntent var1) {
   }

   public ActivityOptionsCompat setLaunchBounds(Rect var1) {
      return this;
   }

   public Bundle toBundle() {
      return null;
   }

   public void update(ActivityOptionsCompat var1) {
   }

   private static class ActivityOptionsCompatImpl extends ActivityOptionsCompat {
      private final ActivityOptions mActivityOptions;

      ActivityOptionsCompatImpl(ActivityOptions var1) {
         this.mActivityOptions = var1;
      }

      public Rect getLaunchBounds() {
         return VERSION.SDK_INT < 24 ? null : this.mActivityOptions.getLaunchBounds();
      }

      public void requestUsageTimeReport(PendingIntent var1) {
         if (VERSION.SDK_INT >= 23) {
            this.mActivityOptions.requestUsageTimeReport(var1);
         }

      }

      public ActivityOptionsCompat setLaunchBounds(Rect var1) {
         return VERSION.SDK_INT < 24 ? this : new ActivityOptionsCompat.ActivityOptionsCompatImpl(this.mActivityOptions.setLaunchBounds(var1));
      }

      public Bundle toBundle() {
         return this.mActivityOptions.toBundle();
      }

      public void update(ActivityOptionsCompat var1) {
         if (var1 instanceof ActivityOptionsCompat.ActivityOptionsCompatImpl) {
            ActivityOptionsCompat.ActivityOptionsCompatImpl var2 = (ActivityOptionsCompat.ActivityOptionsCompatImpl)var1;
            this.mActivityOptions.update(var2.mActivityOptions);
         }

      }
   }
}
