/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.ActionBar
 *  android.app.Activity
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.widget.ImageView
 */
package android.support.v4.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import java.lang.reflect.Method;

@RequiresApi(value=14)
class ActionBarDrawerToggleIcs {
    private static final String TAG = "ActionBarDrawerToggleHoneycomb";
    private static final int[] THEME_ATTRS = new int[]{16843531};

    ActionBarDrawerToggleIcs() {
    }

    public static Drawable getThemeUpIndicator(Activity activity) {
        activity = activity.obtainStyledAttributes(THEME_ATTRS);
        Drawable drawable2 = activity.getDrawable(0);
        activity.recycle();
        return drawable2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Object setActionBarDescription(Object object, Activity activity, int n) {
        Object object2 = object;
        if (object == null) {
            object2 = new SetIndicatorInfo(activity);
        }
        object = (SetIndicatorInfo)object2;
        if (object.setHomeAsUpIndicator == null) return object2;
        try {
            activity = activity.getActionBar();
            object.setHomeActionContentDescription.invoke((Object)activity, n);
            if (Build.VERSION.SDK_INT > 19) return object2;
            activity.setSubtitle(activity.getSubtitle());
        }
        catch (Exception exception) {
            Log.w((String)"ActionBarDrawerToggleHoneycomb", (String)"Couldn't set content description via JB-MR2 API", (Throwable)exception);
            return object2;
        }
        return object2;
    }

    public static Object setActionBarUpIndicator(Object object, Activity activity, Drawable drawable2, int n) {
        Object object2 = object;
        if (object == null) {
            object2 = new SetIndicatorInfo(activity);
        }
        object = (SetIndicatorInfo)object2;
        if (object.setHomeAsUpIndicator != null) {
            try {
                activity = activity.getActionBar();
                object.setHomeAsUpIndicator.invoke((Object)activity, new Object[]{drawable2});
                object.setHomeActionContentDescription.invoke((Object)activity, n);
                return object2;
            }
            catch (Exception exception) {
                Log.w((String)"ActionBarDrawerToggleHoneycomb", (String)"Couldn't set home-as-up indicator via JB-MR2 API", (Throwable)exception);
                return object2;
            }
        }
        if (object.upIndicatorView != null) {
            object.upIndicatorView.setImageDrawable(drawable2);
            return object2;
        }
        Log.w((String)"ActionBarDrawerToggleHoneycomb", (String)"Couldn't set home-as-up indicator");
        return object2;
    }

    private static class SetIndicatorInfo {
        public Method setHomeActionContentDescription;
        public Method setHomeAsUpIndicator;
        public ImageView upIndicatorView;

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        SetIndicatorInfo(Activity activity) {
            try {
                this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
                this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
                return;
            }
            catch (NoSuchMethodException noSuchMethodException) {
                ViewGroup viewGroup;
                if ((activity = activity.findViewById(16908332)) == null || (viewGroup = (ViewGroup)activity.getParent()).getChildCount() != 2) return;
                activity = viewGroup.getChildAt(0);
                viewGroup = viewGroup.getChildAt(1);
                if (activity.getId() == 16908332) {
                    activity = viewGroup;
                }
                if (!(activity instanceof ImageView)) return;
                this.upIndicatorView = (ImageView)activity;
                return;
            }
        }
    }

}

