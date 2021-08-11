/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewParent
 */
package androidx.core.view;

import android.view.View;
import android.view.ViewParent;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewParentCompat;

public class NestedScrollingChildHelper {
    private boolean mIsNestedScrollingEnabled;
    private ViewParent mNestedScrollingParentNonTouch;
    private ViewParent mNestedScrollingParentTouch;
    private int[] mTempNestedScrollConsumed;
    private final View mView;

    public NestedScrollingChildHelper(View view) {
        this.mView = view;
    }

    private boolean dispatchNestedScrollInternal(int n, int n2, int n3, int n4, int[] arrn, int n5, int[] arrn2) {
        if (this.isNestedScrollingEnabled()) {
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
            } else {
                int n6;
                int n7;
                if (arrn != null) {
                    this.mView.getLocationInWindow(arrn);
                    n6 = arrn[0];
                    n7 = arrn[1];
                } else {
                    n6 = 0;
                    n7 = 0;
                }
                if (arrn2 == null) {
                    arrn2 = this.getTempNestedScrollConsumed();
                    arrn2[0] = 0;
                    arrn2[1] = 0;
                }
                ViewParentCompat.onNestedScroll(viewParent, this.mView, n, n2, n3, n4, n5, arrn2);
                if (arrn != null) {
                    this.mView.getLocationInWindow(arrn);
                    arrn[0] = arrn[0] - n6;
                    arrn[1] = arrn[1] - n7;
                }
                return true;
            }
        }
        return false;
    }

    private ViewParent getNestedScrollingParentForType(int n) {
        if (n != 0) {
            if (n != 1) {
                return null;
            }
            return this.mNestedScrollingParentNonTouch;
        }
        return this.mNestedScrollingParentTouch;
    }

    private int[] getTempNestedScrollConsumed() {
        if (this.mTempNestedScrollConsumed == null) {
            this.mTempNestedScrollConsumed = new int[2];
        }
        return this.mTempNestedScrollConsumed;
    }

    private void setNestedScrollingParentForType(int n, ViewParent viewParent) {
        if (n != 0) {
            if (n != 1) {
                return;
            }
            this.mNestedScrollingParentNonTouch = viewParent;
            return;
        }
        this.mNestedScrollingParentTouch = viewParent;
    }

    public boolean dispatchNestedFling(float f, float f2, boolean bl) {
        ViewParent viewParent;
        if (this.isNestedScrollingEnabled() && (viewParent = this.getNestedScrollingParentForType(0)) != null) {
            return ViewParentCompat.onNestedFling(viewParent, this.mView, f, f2, bl);
        }
        return false;
    }

    public boolean dispatchNestedPreFling(float f, float f2) {
        ViewParent viewParent;
        if (this.isNestedScrollingEnabled() && (viewParent = this.getNestedScrollingParentForType(0)) != null) {
            return ViewParentCompat.onNestedPreFling(viewParent, this.mView, f, f2);
        }
        return false;
    }

    public boolean dispatchNestedPreScroll(int n, int n2, int[] arrn, int[] arrn2) {
        return this.dispatchNestedPreScroll(n, n2, arrn, arrn2, 0);
    }

    public boolean dispatchNestedPreScroll(int n, int n2, int[] arrn, int[] arrn2, int n3) {
        boolean bl = this.isNestedScrollingEnabled();
        boolean bl2 = false;
        if (bl) {
            ViewParent viewParent = this.getNestedScrollingParentForType(n3);
            if (viewParent == null) {
                return false;
            }
            if (n == 0 && n2 == 0) {
                if (arrn2 != null) {
                    arrn2[0] = 0;
                    arrn2[1] = 0;
                    return false;
                }
            } else {
                int n4;
                int n5;
                if (arrn2 != null) {
                    this.mView.getLocationInWindow(arrn2);
                    n5 = arrn2[0];
                    n4 = arrn2[1];
                } else {
                    n5 = 0;
                    n4 = 0;
                }
                if (arrn == null) {
                    arrn = this.getTempNestedScrollConsumed();
                }
                arrn[0] = 0;
                arrn[1] = 0;
                ViewParentCompat.onNestedPreScroll(viewParent, this.mView, n, n2, arrn, n3);
                if (arrn2 != null) {
                    this.mView.getLocationInWindow(arrn2);
                    arrn2[0] = arrn2[0] - n5;
                    arrn2[1] = arrn2[1] - n4;
                }
                if (arrn[0] != 0 || arrn[1] != 0) {
                    bl2 = true;
                }
                return bl2;
            }
        }
        return false;
    }

    public void dispatchNestedScroll(int n, int n2, int n3, int n4, int[] arrn, int n5, int[] arrn2) {
        this.dispatchNestedScrollInternal(n, n2, n3, n4, arrn, n5, arrn2);
    }

    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, int[] arrn) {
        return this.dispatchNestedScrollInternal(n, n2, n3, n4, arrn, 0, null);
    }

    public boolean dispatchNestedScroll(int n, int n2, int n3, int n4, int[] arrn, int n5) {
        return this.dispatchNestedScrollInternal(n, n2, n3, n4, arrn, n5, null);
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

    public void onStopNestedScroll(View view) {
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
        }
    }
}

