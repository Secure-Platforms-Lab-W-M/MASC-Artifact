// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.os.Bundle;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.util.Pair;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.app.ActivityOptions;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(23)
@RequiresApi(23)
class ActivityOptionsCompat23
{
    private final ActivityOptions mActivityOptions;
    
    private ActivityOptionsCompat23(final ActivityOptions mActivityOptions) {
        this.mActivityOptions = mActivityOptions;
    }
    
    public static ActivityOptionsCompat23 makeBasic() {
        return new ActivityOptionsCompat23(ActivityOptions.makeBasic());
    }
    
    public static ActivityOptionsCompat23 makeClipRevealAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
        return new ActivityOptionsCompat23(ActivityOptions.makeClipRevealAnimation(view, n, n2, n3, n4));
    }
    
    public static ActivityOptionsCompat23 makeCustomAnimation(final Context context, final int n, final int n2) {
        return new ActivityOptionsCompat23(ActivityOptions.makeCustomAnimation(context, n, n2));
    }
    
    public static ActivityOptionsCompat23 makeScaleUpAnimation(final View view, final int n, final int n2, final int n3, final int n4) {
        return new ActivityOptionsCompat23(ActivityOptions.makeScaleUpAnimation(view, n, n2, n3, n4));
    }
    
    public static ActivityOptionsCompat23 makeSceneTransitionAnimation(final Activity activity, final View view, final String s) {
        return new ActivityOptionsCompat23(ActivityOptions.makeSceneTransitionAnimation(activity, view, s));
    }
    
    public static ActivityOptionsCompat23 makeSceneTransitionAnimation(final Activity activity, final View[] array, final String[] array2) {
        Pair[] array3 = null;
        if (array != null) {
            final Pair[] array4 = new Pair[array.length];
            int n = 0;
            while (true) {
                array3 = array4;
                if (n >= array4.length) {
                    break;
                }
                array4[n] = Pair.create((Object)array[n], (Object)array2[n]);
                ++n;
            }
        }
        return new ActivityOptionsCompat23(ActivityOptions.makeSceneTransitionAnimation(activity, array3));
    }
    
    public static ActivityOptionsCompat23 makeTaskLaunchBehind() {
        return new ActivityOptionsCompat23(ActivityOptions.makeTaskLaunchBehind());
    }
    
    public static ActivityOptionsCompat23 makeThumbnailScaleUpAnimation(final View view, final Bitmap bitmap, final int n, final int n2) {
        return new ActivityOptionsCompat23(ActivityOptions.makeThumbnailScaleUpAnimation(view, bitmap, n, n2));
    }
    
    public void requestUsageTimeReport(final PendingIntent pendingIntent) {
        this.mActivityOptions.requestUsageTimeReport(pendingIntent);
    }
    
    public Bundle toBundle() {
        return this.mActivityOptions.toBundle();
    }
    
    public void update(final ActivityOptionsCompat23 activityOptionsCompat23) {
        this.mActivityOptions.update(activityOptionsCompat23.mActivityOptions);
    }
}
