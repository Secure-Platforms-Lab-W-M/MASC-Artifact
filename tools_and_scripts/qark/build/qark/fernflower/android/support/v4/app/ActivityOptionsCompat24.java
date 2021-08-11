package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Pair;
import android.view.View;

@TargetApi(24)
@RequiresApi(24)
class ActivityOptionsCompat24 {
   private final ActivityOptions mActivityOptions;

   private ActivityOptionsCompat24(ActivityOptions var1) {
      this.mActivityOptions = var1;
   }

   public static ActivityOptionsCompat24 makeBasic() {
      return new ActivityOptionsCompat24(ActivityOptions.makeBasic());
   }

   public static ActivityOptionsCompat24 makeClipRevealAnimation(View var0, int var1, int var2, int var3, int var4) {
      return new ActivityOptionsCompat24(ActivityOptions.makeClipRevealAnimation(var0, var1, var2, var3, var4));
   }

   public static ActivityOptionsCompat24 makeCustomAnimation(Context var0, int var1, int var2) {
      return new ActivityOptionsCompat24(ActivityOptions.makeCustomAnimation(var0, var1, var2));
   }

   public static ActivityOptionsCompat24 makeScaleUpAnimation(View var0, int var1, int var2, int var3, int var4) {
      return new ActivityOptionsCompat24(ActivityOptions.makeScaleUpAnimation(var0, var1, var2, var3, var4));
   }

   public static ActivityOptionsCompat24 makeSceneTransitionAnimation(Activity var0, View var1, String var2) {
      return new ActivityOptionsCompat24(ActivityOptions.makeSceneTransitionAnimation(var0, var1, var2));
   }

   public static ActivityOptionsCompat24 makeSceneTransitionAnimation(Activity var0, View[] var1, String[] var2) {
      Pair[] var4 = null;
      if (var1 != null) {
         Pair[] var5 = new Pair[var1.length];
         int var3 = 0;

         while(true) {
            var4 = var5;
            if (var3 >= var5.length) {
               break;
            }

            var5[var3] = Pair.create(var1[var3], var2[var3]);
            ++var3;
         }
      }

      return new ActivityOptionsCompat24(ActivityOptions.makeSceneTransitionAnimation(var0, var4));
   }

   public static ActivityOptionsCompat24 makeTaskLaunchBehind() {
      return new ActivityOptionsCompat24(ActivityOptions.makeTaskLaunchBehind());
   }

   public static ActivityOptionsCompat24 makeThumbnailScaleUpAnimation(View var0, Bitmap var1, int var2, int var3) {
      return new ActivityOptionsCompat24(ActivityOptions.makeThumbnailScaleUpAnimation(var0, var1, var2, var3));
   }

   public Rect getLaunchBounds() {
      return this.mActivityOptions.getLaunchBounds();
   }

   public void requestUsageTimeReport(PendingIntent var1) {
      this.mActivityOptions.requestUsageTimeReport(var1);
   }

   public ActivityOptionsCompat24 setLaunchBounds(@Nullable Rect var1) {
      return new ActivityOptionsCompat24(this.mActivityOptions.setLaunchBounds(var1));
   }

   public Bundle toBundle() {
      return this.mActivityOptions.toBundle();
   }

   public void update(ActivityOptionsCompat24 var1) {
      this.mActivityOptions.update(var1.mActivityOptions);
   }
}
