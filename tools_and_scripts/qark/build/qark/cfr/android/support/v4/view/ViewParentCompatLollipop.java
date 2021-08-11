/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewParent
 */
package android.support.v4.view;

import android.util.Log;
import android.view.View;
import android.view.ViewParent;

class ViewParentCompatLollipop {
    private static final String TAG = "ViewParentCompat";

    ViewParentCompatLollipop() {
    }

    public static boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean bl) {
        try {
            bl = viewParent.onNestedFling(view, f, f2, bl);
            return bl;
        }
        catch (AbstractMethodError abstractMethodError) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ViewParent ");
            stringBuilder.append((Object)viewParent);
            stringBuilder.append(" does not implement interface ");
            stringBuilder.append("method onNestedFling");
            Log.e((String)"ViewParentCompat", (String)stringBuilder.toString(), (Throwable)abstractMethodError);
            return false;
        }
    }

    public static boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
        try {
            boolean bl = viewParent.onNestedPreFling(view, f, f2);
            return bl;
        }
        catch (AbstractMethodError abstractMethodError) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ViewParent ");
            stringBuilder.append((Object)viewParent);
            stringBuilder.append(" does not implement interface ");
            stringBuilder.append("method onNestedPreFling");
            Log.e((String)"ViewParentCompat", (String)stringBuilder.toString(), (Throwable)abstractMethodError);
            return false;
        }
    }

    public static void onNestedPreScroll(ViewParent viewParent, View view, int n, int n2, int[] object) {
        try {
            viewParent.onNestedPreScroll(view, n, n2, (int[])object);
            return;
        }
        catch (AbstractMethodError abstractMethodError) {
            object = new StringBuilder();
            object.append("ViewParent ");
            object.append((Object)viewParent);
            object.append(" does not implement interface ");
            object.append("method onNestedPreScroll");
            Log.e((String)"ViewParentCompat", (String)object.toString(), (Throwable)abstractMethodError);
            return;
        }
    }

    public static void onNestedScroll(ViewParent viewParent, View view, int n, int n2, int n3, int n4) {
        try {
            viewParent.onNestedScroll(view, n, n2, n3, n4);
            return;
        }
        catch (AbstractMethodError abstractMethodError) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ViewParent ");
            stringBuilder.append((Object)viewParent);
            stringBuilder.append(" does not implement interface ");
            stringBuilder.append("method onNestedScroll");
            Log.e((String)"ViewParentCompat", (String)stringBuilder.toString(), (Throwable)abstractMethodError);
            return;
        }
    }

    public static void onNestedScrollAccepted(ViewParent viewParent, View view, View object, int n) {
        try {
            viewParent.onNestedScrollAccepted(view, (View)object, n);
            return;
        }
        catch (AbstractMethodError abstractMethodError) {
            object = new StringBuilder();
            object.append("ViewParent ");
            object.append((Object)viewParent);
            object.append(" does not implement interface ");
            object.append("method onNestedScrollAccepted");
            Log.e((String)"ViewParentCompat", (String)object.toString(), (Throwable)abstractMethodError);
            return;
        }
    }

    public static boolean onStartNestedScroll(ViewParent viewParent, View view, View object, int n) {
        try {
            boolean bl = viewParent.onStartNestedScroll(view, (View)object, n);
            return bl;
        }
        catch (AbstractMethodError abstractMethodError) {
            object = new StringBuilder();
            object.append("ViewParent ");
            object.append((Object)viewParent);
            object.append(" does not implement interface ");
            object.append("method onStartNestedScroll");
            Log.e((String)"ViewParentCompat", (String)object.toString(), (Throwable)abstractMethodError);
            return false;
        }
    }

    public static void onStopNestedScroll(ViewParent viewParent, View view) {
        try {
            viewParent.onStopNestedScroll(view);
            return;
        }
        catch (AbstractMethodError abstractMethodError) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ViewParent ");
            stringBuilder.append((Object)viewParent);
            stringBuilder.append(" does not implement interface ");
            stringBuilder.append("method onStopNestedScroll");
            Log.e((String)"ViewParentCompat", (String)stringBuilder.toString(), (Throwable)abstractMethodError);
            return;
        }
    }
}

