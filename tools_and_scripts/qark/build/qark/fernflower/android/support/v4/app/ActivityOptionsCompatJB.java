package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(16)
@RequiresApi(16)
class ActivityOptionsCompatJB {
   private final ActivityOptions mActivityOptions;

   private ActivityOptionsCompatJB(ActivityOptions var1) {
      this.mActivityOptions = var1;
   }

   public static ActivityOptionsCompatJB makeCustomAnimation(Context var0, int var1, int var2) {
      return new ActivityOptionsCompatJB(ActivityOptions.makeCustomAnimation(var0, var1, var2));
   }

   public static ActivityOptionsCompatJB makeScaleUpAnimation(View var0, int var1, int var2, int var3, int var4) {
      return new ActivityOptionsCompatJB(ActivityOptions.makeScaleUpAnimation(var0, var1, var2, var3, var4));
   }

   public static ActivityOptionsCompatJB makeThumbnailScaleUpAnimation(View var0, Bitmap var1, int var2, int var3) {
      return new ActivityOptionsCompatJB(ActivityOptions.makeThumbnailScaleUpAnimation(var0, var1, var2, var3));
   }

   public Bundle toBundle() {
      return this.mActivityOptions.toBundle();
   }

   public void update(ActivityOptionsCompatJB var1) {
      this.mActivityOptions.update(var1.mActivityOptions);
   }
}
