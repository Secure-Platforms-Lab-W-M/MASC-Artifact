/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 */
package android.support.v4.view;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParent2;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;

public final class ViewParentCompat {
    static final ViewParentCompatBaseImpl IMPL = Build.VERSION.SDK_INT >= 21 ? new ViewParentCompatApi21Impl() : (Build.VERSION.SDK_INT >= 19 ? new ViewParentCompatApi19Impl() : new ViewParentCompatBaseImpl());
    private static final String TAG = "ViewParentCompat";

    private ViewParentCompat() {
    }

    public static void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int n) {
        IMPL.notifySubtreeAccessibilityStateChanged(viewParent, view, view2, n);
    }

    public static boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean bl) {
        return IMPL.onNestedFling(viewParent, view, f, f2, bl);
    }

    public static boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
        return IMPL.onNestedPreFling(viewParent, view, f, f2);
    }

    public static void onNestedPreScroll(ViewParent viewParent, View view, int n, int n2, int[] arrn) {
        ViewParentCompat.onNestedPreScroll(viewParent, view, n, n2, arrn, 0);
    }

    public static void onNestedPreScroll(ViewParent viewParent, View view, int n, int n2, int[] arrn, int n3) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onNestedPreScroll(view, n, n2, arrn, n3);
            return;
        }
        if (n3 == 0) {
            IMPL.onNestedPreScroll(viewParent, view, n, n2, arrn);
            return;
        }
    }

    public static void onNestedScroll(ViewParent viewParent, View view, int n, int n2, int n3, int n4) {
        ViewParentCompat.onNestedScroll(viewParent, view, n, n2, n3, n4, 0);
    }

    public static void onNestedScroll(ViewParent viewParent, View view, int n, int n2, int n3, int n4, int n5) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onNestedScroll(view, n, n2, n3, n4, n5);
            return;
        }
        if (n5 == 0) {
            IMPL.onNestedScroll(viewParent, view, n, n2, n3, n4);
            return;
        }
    }

    public static void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int n) {
        ViewParentCompat.onNestedScrollAccepted(viewParent, view, view2, n, 0);
    }

    public static void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int n, int n2) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onNestedScrollAccepted(view, view2, n, n2);
            return;
        }
        if (n2 == 0) {
            IMPL.onNestedScrollAccepted(viewParent, view, view2, n);
            return;
        }
    }

    public static boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int n) {
        return ViewParentCompat.onStartNestedScroll(viewParent, view, view2, n, 0);
    }

    public static boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int n, int n2) {
        if (viewParent instanceof NestedScrollingParent2) {
            return ((NestedScrollingParent2)viewParent).onStartNestedScroll(view, view2, n, n2);
        }
        if (n2 == 0) {
            return IMPL.onStartNestedScroll(viewParent, view, view2, n);
        }
        return false;
    }

    public static void onStopNestedScroll(ViewParent viewParent, View view) {
        ViewParentCompat.onStopNestedScroll(viewParent, view, 0);
    }

    public static void onStopNestedScroll(ViewParent viewParent, View view, int n) {
        if (viewParent instanceof NestedScrollingParent2) {
            ((NestedScrollingParent2)viewParent).onStopNestedScroll(view, n);
            return;
        }
        if (n == 0) {
            IMPL.onStopNestedScroll(viewParent, view);
            return;
        }
    }

    @Deprecated
    public static boolean requestSendAccessibilityEvent(ViewParent viewParent, View view, AccessibilityEvent accessibilityEvent) {
        return viewParent.requestSendAccessibilityEvent(view, accessibilityEvent);
    }

    @RequiresApi(value=19)
    static class ViewParentCompatApi19Impl
    extends ViewParentCompatBaseImpl {
        ViewParentCompatApi19Impl() {
        }

        @Override
        public void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int n) {
            viewParent.notifySubtreeAccessibilityStateChanged(view, view2, n);
        }
    }

    @RequiresApi(value=21)
    static class ViewParentCompatApi21Impl
    extends ViewParentCompatApi19Impl {
        ViewParentCompatApi21Impl() {
        }

        @Override
        public boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean bl) {
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

        @Override
        public boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
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

        @Override
        public void onNestedPreScroll(ViewParent viewParent, View view, int n, int n2, int[] object) {
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

        @Override
        public void onNestedScroll(ViewParent viewParent, View view, int n, int n2, int n3, int n4) {
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

        @Override
        public void onNestedScrollAccepted(ViewParent viewParent, View view, View object, int n) {
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

        @Override
        public boolean onStartNestedScroll(ViewParent viewParent, View view, View object, int n) {
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

        @Override
        public void onStopNestedScroll(ViewParent viewParent, View view) {
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

    static class ViewParentCompatBaseImpl {
        ViewParentCompatBaseImpl() {
        }

        public void notifySubtreeAccessibilityStateChanged(ViewParent viewParent, View view, View view2, int n) {
        }

        public boolean onNestedFling(ViewParent viewParent, View view, float f, float f2, boolean bl) {
            if (viewParent instanceof NestedScrollingParent) {
                return ((NestedScrollingParent)viewParent).onNestedFling(view, f, f2, bl);
            }
            return false;
        }

        public boolean onNestedPreFling(ViewParent viewParent, View view, float f, float f2) {
            if (viewParent instanceof NestedScrollingParent) {
                return ((NestedScrollingParent)viewParent).onNestedPreFling(view, f, f2);
            }
            return false;
        }

        public void onNestedPreScroll(ViewParent viewParent, View view, int n, int n2, int[] arrn) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedPreScroll(view, n, n2, arrn);
                return;
            }
        }

        public void onNestedScroll(ViewParent viewParent, View view, int n, int n2, int n3, int n4) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedScroll(view, n, n2, n3, n4);
                return;
            }
        }

        public void onNestedScrollAccepted(ViewParent viewParent, View view, View view2, int n) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onNestedScrollAccepted(view, view2, n);
                return;
            }
        }

        public boolean onStartNestedScroll(ViewParent viewParent, View view, View view2, int n) {
            if (viewParent instanceof NestedScrollingParent) {
                return ((NestedScrollingParent)viewParent).onStartNestedScroll(view, view2, n);
            }
            return false;
        }

        public void onStopNestedScroll(ViewParent viewParent, View view) {
            if (viewParent instanceof NestedScrollingParent) {
                ((NestedScrollingParent)viewParent).onStopNestedScroll(view);
                return;
            }
        }
    }

}

