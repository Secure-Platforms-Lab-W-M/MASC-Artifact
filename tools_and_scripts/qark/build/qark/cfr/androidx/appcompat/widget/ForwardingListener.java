/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.SystemClock
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewConfiguration
 *  android.view.ViewParent
 *  android.widget.ListView
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.ListView;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.appcompat.widget.DropDownListView;

public abstract class ForwardingListener
implements View.OnTouchListener,
View.OnAttachStateChangeListener {
    private int mActivePointerId;
    private Runnable mDisallowIntercept;
    private boolean mForwarding;
    private final int mLongPressTimeout;
    private final float mScaledTouchSlop;
    final View mSrc;
    private final int mTapTimeout;
    private final int[] mTmpLocation = new int[2];
    private Runnable mTriggerLongPress;

    public ForwardingListener(View view) {
        int n;
        this.mSrc = view;
        view.setLongClickable(true);
        view.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
        this.mScaledTouchSlop = ViewConfiguration.get((Context)view.getContext()).getScaledTouchSlop();
        this.mTapTimeout = n = ViewConfiguration.getTapTimeout();
        this.mLongPressTimeout = (n + ViewConfiguration.getLongPressTimeout()) / 2;
    }

    private void clearCallbacks() {
        Runnable runnable = this.mTriggerLongPress;
        if (runnable != null) {
            this.mSrc.removeCallbacks(runnable);
        }
        if ((runnable = this.mDisallowIntercept) != null) {
            this.mSrc.removeCallbacks(runnable);
        }
    }

    private boolean onTouchForwarded(MotionEvent motionEvent) {
        View view = this.mSrc;
        Object object = this.getPopup();
        boolean bl = false;
        if (object != null) {
            if (!object.isShowing()) {
                return false;
            }
            if ((object = (DropDownListView)object.getListView()) != null) {
                if (!object.isShown()) {
                    return false;
                }
                MotionEvent motionEvent2 = MotionEvent.obtainNoHistory((MotionEvent)motionEvent);
                this.toGlobalMotionEvent(view, motionEvent2);
                this.toLocalMotionEvent((View)object, motionEvent2);
                boolean bl2 = object.onForwardedEvent(motionEvent2, this.mActivePointerId);
                motionEvent2.recycle();
                int n = motionEvent.getActionMasked();
                n = n != 1 && n != 3 ? 1 : 0;
                boolean bl3 = bl;
                if (bl2) {
                    bl3 = bl;
                    if (n != 0) {
                        bl3 = true;
                    }
                }
                return bl3;
            }
            return false;
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean onTouchObserved(MotionEvent motionEvent) {
        View view = this.mSrc;
        if (!view.isEnabled()) {
            return false;
        }
        int n = motionEvent.getActionMasked();
        if (n != 0) {
            if (n != 1) {
                if (n != 2) {
                    if (n != 3) {
                        return false;
                    }
                } else {
                    n = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (n < 0) return false;
                    if (ForwardingListener.pointInView(view, motionEvent.getX(n), motionEvent.getY(n), this.mScaledTouchSlop)) return false;
                    this.clearCallbacks();
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
            }
            this.clearCallbacks();
            return false;
        }
        this.mActivePointerId = motionEvent.getPointerId(0);
        if (this.mDisallowIntercept == null) {
            this.mDisallowIntercept = new DisallowIntercept();
        }
        view.postDelayed(this.mDisallowIntercept, (long)this.mTapTimeout);
        if (this.mTriggerLongPress == null) {
            this.mTriggerLongPress = new TriggerLongPress();
        }
        view.postDelayed(this.mTriggerLongPress, (long)this.mLongPressTimeout);
        return false;
    }

    private static boolean pointInView(View view, float f, float f2, float f3) {
        if (f >= - f3 && f2 >= - f3 && f < (float)(view.getRight() - view.getLeft()) + f3 && f2 < (float)(view.getBottom() - view.getTop()) + f3) {
            return true;
        }
        return false;
    }

    private boolean toGlobalMotionEvent(View view, MotionEvent motionEvent) {
        int[] arrn = this.mTmpLocation;
        view.getLocationOnScreen(arrn);
        motionEvent.offsetLocation((float)arrn[0], (float)arrn[1]);
        return true;
    }

    private boolean toLocalMotionEvent(View view, MotionEvent motionEvent) {
        int[] arrn = this.mTmpLocation;
        view.getLocationOnScreen(arrn);
        motionEvent.offsetLocation((float)(- arrn[0]), (float)(- arrn[1]));
        return true;
    }

    public abstract ShowableListMenu getPopup();

    protected boolean onForwardingStarted() {
        ShowableListMenu showableListMenu = this.getPopup();
        if (showableListMenu != null && !showableListMenu.isShowing()) {
            showableListMenu.show();
        }
        return true;
    }

    protected boolean onForwardingStopped() {
        ShowableListMenu showableListMenu = this.getPopup();
        if (showableListMenu != null && showableListMenu.isShowing()) {
            showableListMenu.dismiss();
        }
        return true;
    }

    void onLongPress() {
        this.clearCallbacks();
        View view = this.mSrc;
        if (view.isEnabled()) {
            if (view.isLongClickable()) {
                return;
            }
            if (!this.onForwardingStarted()) {
                return;
            }
            view.getParent().requestDisallowInterceptTouchEvent(true);
            long l = SystemClock.uptimeMillis();
            MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
            view.onTouchEvent(motionEvent);
            motionEvent.recycle();
            this.mForwarding = true;
            return;
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean bl;
        boolean bl2;
        boolean bl3 = this.mForwarding;
        boolean bl4 = true;
        if (bl3) {
            bl = this.onTouchForwarded(motionEvent) || !this.onForwardingStopped();
            bl2 = bl;
        } else {
            bl = this.onTouchObserved(motionEvent) && this.onForwardingStarted();
            bl2 = bl;
            if (bl) {
                long l = SystemClock.uptimeMillis();
                view = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
                this.mSrc.onTouchEvent((MotionEvent)view);
                view.recycle();
                bl2 = bl;
            }
        }
        this.mForwarding = bl2;
        bl = bl4;
        if (!bl2) {
            if (bl3) {
                return true;
            }
            bl = false;
        }
        return bl;
    }

    public void onViewAttachedToWindow(View view) {
    }

    public void onViewDetachedFromWindow(View object) {
        this.mForwarding = false;
        this.mActivePointerId = -1;
        object = this.mDisallowIntercept;
        if (object != null) {
            this.mSrc.removeCallbacks((Runnable)object);
        }
    }

    private class DisallowIntercept
    implements Runnable {
        DisallowIntercept() {
        }

        @Override
        public void run() {
            ViewParent viewParent = ForwardingListener.this.mSrc.getParent();
            if (viewParent != null) {
                viewParent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    private class TriggerLongPress
    implements Runnable {
        TriggerLongPress() {
        }

        @Override
        public void run() {
            ForwardingListener.this.onLongPress();
        }
    }

}

