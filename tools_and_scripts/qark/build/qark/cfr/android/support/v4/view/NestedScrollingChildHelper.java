/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewParent
 */
package android.support.v4.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.view.View;
import android.view.ViewParent;

public class NestedScrollingChildHelper {
    private boolean mIsNestedScrollingEnabled;
    private ViewParent mNestedScrollingParentNonTouch;
    private ViewParent mNestedScrollingParentTouch;
    private int[] mTempNestedScrollConsumed;
    private final View mView;

    public NestedScrollingChildHelper(@NonNull View view) {
        this.mView = view;
    }

    private ViewParent getNestedScrollingParentForType(int n) {
        switch (n) {
            default: {
                return null;
            }
            case 1: {
                return this.mNestedScrollingParentNonTouch;
            }
            case 0: 
        }
        return this.mNestedScrollingParentTouch;
    }

    private void setNestedScrollingParentForType(int n, ViewParent viewParent) {
        switch (n) {
            default: {
                return;
            }
            case 1: {
                this.mNestedScrollingParentNonTouch = viewParent;
                return;
            }
            case 0: 
        }
        this.mNestedScrollingParentTouch = viewParent;
    }

    public boolean dispatchNestedFling(float f, float f2, boolean bl) {
        if (this.isNestedScrollingEnabled()) {
            ViewParent viewParent = this.getNestedScrollingParentForType(0);
            if (viewParent != null) {
                return ViewParentCompat.onNestedFling(viewParent, this.mView, f, f2, bl);
            }
            return false;
        }
        return false;
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        if (this.isNestedScrollingEnabled()) {
            ViewParent viewParent = this.getNestedScrollingParentForType(0);
            if (viewParent != null) {
                return ViewParentCompat.onNestedPreFling(viewParent, this.mView, f, f2);
            }
            return false;
        }
        return false;
    }

    public boolean dispatchNestedPreScroll(int n, int n2, @Nullable int[] arrn, @Nullable int[] arrn2) {
        return this.dispatchNestedPreScroll(n, n2, arrn, arrn2, 0);
    }

    public boolean dispatchNestedPreScroll(int n, int n2, @Nullable int[] arrn, @Nullable int[] arrn2, int n3) {
        if (this.isNestedScrollingEnabled()) {
            int n4;
            int n5;
            ViewParent viewParent = this.getNestedScrollingParentForType(n3);
            if (viewParent == null) {
                return false;
            }
            boolean bl = true;
            if (n == 0 && n2 == 0) {
                if (arrn2 != null) {
                    arrn2[0] = 0;
                    arrn2[1] = 0;
                    return false;
                }
                return false;
            }
            if (arrn2 != null) {
                this.mView.getLocationInWindow(arrn2);
                n5 = arrn2[0];
                n4 = arrn2[1];
            } else {
                n5 = 0;
                n4 = 0;
            }
            if (arrn == null) {
                if (this.mTempNestedScrollConsumed == null) {
                    this.mTempNestedScrollConsumed = new int[2];
                }
                arrn = this.mTempNestedScrollConsumed;
            }
            arrn[0] = 0;
            arrn[1] = 0;
            ViewParentCompat.onNestedPreScroll(viewParent, this.mView, n, n2, arrn, n3);
            if (arrn2 != null) {
                this.mView.getLocationInWindow(arrn2);
                arrn2[0] = arrn2[0] - n5;
                arrn2[1] = arrn2[1] - n4;
            }
            if (arrn[0] == 0) {
                if (arrn[1] != 0) {
                    return true;
                }
                bl = false;
            }
            return bl;
        }
        return false;
    }

    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, @Nullable int[] arrn) {
        return this.dispatchNestedScroll(n, n2, n3, n4, arrn, 0);
    }

    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, @Nullable int[] arrn, int n5) {
        if (this.isNestedScrollingEnabled()) {
            int n6;
            int n7;
            ViewParent viewParent = this.getNestedScrollingParentForType(n5);
            if (viewParent == null) {
                return false;
            }
            if (n == 0 && n2 == 0 && n3 == 0 && n4 == 0) {
                if (arrn != null) {
                    arrn[0] = 0;
                    arrn[1] = 0;
                    return false;
                }
                return false;
            }
            if (arrn != null) {
                this.mView.getLocationInWindow(arrn);
                n7 = arrn[0];
                n6 = arrn[1];
            } else {
                n7 = 0;
                n6 = 0;
            }
            ViewParentCompat.onNestedScroll(viewParent, this.mView, n, n2, n3, n4, n5);
            if (arrn != null) {
                this.mView.getLocationInWindow(arrn);
                arrn[0] = arrn[0] - n7;
                arrn[1] = arrn[1] - n6;
                return true;
            }
            return true;
        }
        return false;
    }

    public boolean hasNestedScrollingParent() {
        return this.hasNestedScrollingParent(0);
    }

    public boolean hasNestedScrollingParent(int n) {
        if (this.getNestedScrollingParentForType(n) != null) {
            return true;
        }
        return false;
    }

    public boolean isNestedScrollingEnabled() {
        return this.mIsNestedScrollingEnabled;
    }

    public void onDetachedFromWindow() {
        ViewCompat.stopNestedScroll(this.mView);
    }

    public void onStopNestedScroll(@NonNull View view) {
        ViewCompat.stopNestedScroll(this.mView);
    }

    public void setNestedScrollingEnabled(boolean bl) {
        if (this.mIsNestedScrollingEnabled) {
            ViewCompat.stopNestedScroll(this.mView);
        }
        this.mIsNestedScrollingEnabled = bl;
    }

    public boolean startNestedScroll(int n) {
        return this.startNestedScroll(n, 0);
    }

    public boolean startNestedScroll(int n, int n2) {
        if (this.hasNestedScrollingParent(n2)) {
            return true;
        }
        if (this.isNestedScrollingEnabled()) {
            View view = this.mView;
            for (ViewParent viewParent = this.mView.getParent(); viewParent != null; viewParent = viewParent.getParent()) {
                if (ViewParentCompat.onStartNestedScroll(viewParent, view, this.mView, n, n2)) {
                    this.setNestedScrollingParentForType(n2, viewParent);
                    ViewParentCompat.onNestedScrollAccepted(viewParent, view, this.mView, n, n2);
                    return true;
                }
                if (!(viewParent instanceof View)) continue;
                view = (View)viewParent;
            }
        }
        return false;
    }

    public void stopNestedScroll() {
        this.stopNestedScroll(0);
    }

    public void stopNestedScroll(int n) {
        ViewParent viewParent = this.getNestedScrollingParentForType(n);
        if (viewParent != null) {
            ViewParentCompat.onStopNestedScroll(viewParent, this.mView, n);
            this.setNestedScrollingParentForType(n, null);
            return;
        }
    }
}

