package android.support.v4.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.util.Pair;
import android.view.View;

public class ActivityOptionsCompat {
   public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
   public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";

   protected ActivityOptionsCompat() {
   }

   @RequiresApi(16)
   private static ActivityOptionsCompat createImpl(ActivityOptions var0) {
      if (VERSION.SDK_INT >= 24) {
         return new ActivityOptionsCompat.ActivityOptionsCompatApi24Impl(var0);
      } else {
         return (ActivityOptionsCompat)(VERSION.SDK_INT >= 23 ? new ActivityOptionsCompat.ActivityOptionsCompatApi23Impl(var0) : new ActivityOptionsCompat.ActivityOptionsCompatApi16Impl(var0));
      }
   }

   public static ActivityOptionsCompat makeBasic() {
      return VERSION.SDK_INT >= 23 ? createImpl(ActivityOptions.makeBasic()) : new ActivityOptionsCompat();
   }

   public static ActivityOptionsCompat makeClipRevealAnimation(View var0, int var1, int var2, int var3, int var4) {
      return VERSION.SDK_INT >= 23 ? createImpl(ActivityOptions.makeClipRevealAnimation(var0, var1, var2, var3, var4)) : new ActivityOptionsCompat();
   }

   public static ActivityOptionsCompat makeCustomAnimation(Context var0, int var1, int var2) {
      return VERSION.SDK_INT >= 16 ? createImpl(ActivityOptions.makeCustomAnimation(var0, var1, var2)) : new ActivityOptionsCompat();
   }

   public static ActivityOptionsCompat makeScaleUpAnimation(View var0, int var1, int var2, int var3, int var4) {
      return VERSION.SDK_INT >= 16 ? createImpl(ActivityOptions.makeScaleUpAnimation(var0, var1, var2, var3, var4)) : new ActivityOptionsCompat();
   }

   public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity var0, View var1, String var2) {
      return VERSION.SDK_INT >= 21 ? createImpl(ActivityOptions.makeSceneTransitionAnimation(var0, var1, var2)) : new ActivityOptionsCompat();
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

         return createImpl(ActivityOptions.makeSceneTransitionAnimation(var0, var3));
      }
   }

   public static ActivityOptionsCompat makeTaskLaunchBehind() {
      return VERSION.SDK_INT >= 21 ? createImpl(ActivityOptions.makeTaskLaunchBehind()) : new ActivityOptionsCompat();
   }

   public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View var0, Bitmap var1, int var2, int var3) {
      return VERSION.SDK_INT >= 16 ? createImpl(ActivityOptions.makeThumbnailScaleUpAnimation(var0, var1, var2, var3)) : new ActivityOptionsCompat();
   }

   @Nullable
   public Rect getLaunchBounds() {
      return null;
   }

   public void requestUsageTimeReport(PendingIntent var1) {
   }

   public ActivityOptionsCompat setLaunchBounds(@Nullable Rect var1) {
      return null;
   }

   public Bundle toBundle() {
      return null;
   }

   public void update(ActivityOptionsCompat var1) {
   }

   @RequiresApi(16)
   private static class ActivityOptionsCompatApi16Impl extends ActivityOptionsCompat {
      protected final ActivityOptions mActivityOptions;

      ActivityOptionsCompatApi16Impl(ActivityOptions var1) {
         this.mActivityOptions = var1;
      }

      public Bundle toBundle() {
         return this.mActivityOptions.toBundle();
      }

      public void update(ActivityOptionsCompat var1) {
         if (var1 instanceof ActivityOptionsCompat.ActivityOptionsCompatApi16Impl) {
            ActivityOptionsCompat.ActivityOptionsCompatApi16Impl var2 = (ActivityOptionsCompat.ActivityOptionsCompatApi16Impl)var1;
            this.mActivityOptions.update(var2.mActivityOptions);
         }

      }
   }

   @RequiresApi(23)
   private static class ActivityOptionsCompatApi23Impl extends ActivityOptionsCompat.ActivityOptionsCompatApi16Impl {
      ActivityOptionsCompatApi23Impl(ActivityOptions var1) {
         super(var1);
      }

      public void requestUsageTimeReport(PendingIntent var1) {
         this.mActivityOptions.requestUsageTimeReport(var1);
      }
   }

   @RequiresApi(24)
   private static class ActivityOptionsCompatApi24Impl extends ActivityOptionsCompat.ActivityOptionsCompatApi23Impl {
      ActivityOptionsCompatApi24Impl(ActivityOptions var1) {
         super(var1);
      }

      public Rect getLaunchBounds() {
         return this.mActivityOptions.getLaunchBounds();
      }

      public ActivityOptionsCompat setLaunchBounds(@Nullable Rect var1) {
         return new ActivityOptionsCompat.ActivityOptionsCompatApi24Impl(this.mActivityOptions.setLaunchBounds(var1));
      }
   }
}
