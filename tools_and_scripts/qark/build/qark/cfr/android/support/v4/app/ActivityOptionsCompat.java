/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.ActivityOptions
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Rect
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.util.Pair
 *  android.view.View
 */
package android.support.v4.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Pair;
import android.view.View;

public class ActivityOptionsCompat {
    public static final String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";
    public static final String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";

    protected ActivityOptionsCompat() {
    }

    @RequiresApi(value=16)
    private static ActivityOptionsCompat createImpl(ActivityOptions activityOptions) {
        if (Build.VERSION.SDK_INT >= 24) {
            return new ActivityOptionsCompatApi24Impl(activityOptions);
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return new ActivityOptionsCompatApi23Impl(activityOptions);
        }
        return new ActivityOptionsCompatApi16Impl(activityOptions);
    }

    public static ActivityOptionsCompat makeBasic() {
        if (Build.VERSION.SDK_INT >= 23) {
            return ActivityOptionsCompat.createImpl(ActivityOptions.makeBasic());
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeClipRevealAnimation(View view, int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT >= 23) {
            return ActivityOptionsCompat.createImpl(ActivityOptions.makeClipRevealAnimation((View)view, (int)n, (int)n2, (int)n3, (int)n4));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeCustomAnimation(Context context, int n, int n2) {
        if (Build.VERSION.SDK_INT >= 16) {
            return ActivityOptionsCompat.createImpl(ActivityOptions.makeCustomAnimation((Context)context, (int)n, (int)n2));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeScaleUpAnimation(View view, int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT >= 16) {
            return ActivityOptionsCompat.createImpl(ActivityOptions.makeScaleUpAnimation((View)view, (int)n, (int)n2, (int)n3, (int)n4));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, View view, String string2) {
        if (Build.VERSION.SDK_INT >= 21) {
            return ActivityOptionsCompat.createImpl(ActivityOptions.makeSceneTransitionAnimation((Activity)activity, (View)view, (String)string2));
        }
        return new ActivityOptionsCompat();
    }

    public static /* varargs */ ActivityOptionsCompat makeSceneTransitionAnimation(Activity activity, android.support.v4.util.Pair<View, String> ... arrpair) {
        if (Build.VERSION.SDK_INT >= 21) {
            Pair[] arrpair2 = null;
            if (arrpair != null) {
                Pair[] arrpair3 = new Pair[arrpair.length];
                int n = 0;
                do {
                    arrpair2 = arrpair3;
                    if (n >= arrpair.length) break;
                    arrpair3[n] = Pair.create(arrpair[n].first, arrpair[n].second);
                    ++n;
                } while (true);
            }
            return ActivityOptionsCompat.createImpl(ActivityOptions.makeSceneTransitionAnimation((Activity)activity, (Pair[])arrpair2));
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeTaskLaunchBehind() {
        if (Build.VERSION.SDK_INT >= 21) {
            return ActivityOptionsCompat.createImpl(ActivityOptions.makeTaskLaunchBehind());
        }
        return new ActivityOptionsCompat();
    }

    public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View view, Bitmap bitmap, int n, int n2) {
        if (Build.VERSION.SDK_INT >= 16) {
            return ActivityOptionsCompat.createImpl(ActivityOptions.makeThumbnailScaleUpAnimation((View)view, (Bitmap)bitmap, (int)n, (int)n2));
        }
        return new ActivityOptionsCompat();
    }

    @Nullable
    public Rect getLaunchBounds() {
        return null;
    }

    public void requestUsageTimeReport(PendingIntent pendingIntent) {
    }

    public ActivityOptionsCompat setLaunchBounds(@Nullable Rect rect) {
        return null;
    }

    public Bundle toBundle() {
        return null;
    }

    public void update(ActivityOptionsCompat activityOptionsCompat) {
    }

    @RequiresApi(value=16)
    private static class ActivityOptionsCompatApi16Impl
    extends ActivityOptionsCompat {
        protected final ActivityOptions mActivityOptions;

        ActivityOptionsCompatApi16Impl(ActivityOptions activityOptions) {
            this.mActivityOptions = activityOptions;
        }

        @Override
        public Bundle toBundle() {
            return this.mActivityOptions.toBundle();
        }

        @Override
        public void update(ActivityOptionsCompat activityOptionsCompat) {
            if (activityOptionsCompat instanceof ActivityOptionsCompatApi16Impl) {
                activityOptionsCompat = (ActivityOptionsCompatApi16Impl)activityOptionsCompat;
                this.mActivityOptions.update(activityOptionsCompat.mActivityOptions);
            }
        }
    }

    @RequiresApi(value=23)
    private static class ActivityOptionsCompatApi23Impl
    extends ActivityOptionsCompatApi16Impl {
        ActivityOptionsCompatApi23Impl(ActivityOptions activityOptions) {
            super(activityOptions);
        }

        @Override
        public void requestUsageTimeReport(PendingIntent pendingIntent) {
            this.mActivityOptions.requestUsageTimeReport(pendingIntent);
        }
    }

    @RequiresApi(value=24)
    private static class ActivityOptionsCompatApi24Impl
    extends ActivityOptionsCompatApi23Impl {
        ActivityOptionsCompatApi24Impl(ActivityOptions activityOptions) {
            super(activityOptions);
        }

        @Override
        public Rect getLaunchBounds() {
            return this.mActivityOptions.getLaunchBounds();
        }

        @Override
        public ActivityOptionsCompat setLaunchBounds(@Nullable Rect rect) {
            return new ActivityOptionsCompatApi24Impl(this.mActivityOptions.setLaunchBounds(rect));
        }
    }

}

