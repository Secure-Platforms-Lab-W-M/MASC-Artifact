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
package android.support.v7.widget;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.RestrictTo;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.DropDownListView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.ListView;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
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
        this.mSrc = view;
        view.setLongClickable(true);
        view.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
        this.mScaledTouchSlop = ViewConfiguration.get((Context)view.getContext()).getScaledTouchSlop();
        this.mTapTimeout = ViewConfiguration.getTapTimeout();
        this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
    }

    private void clearCallbacks() {
        Runnable runnable = this.mTriggerLongPress;
        if (runnable != null) {
            this.mSrc.removeCallbacks(runnable);
        }
        if ((runnable = this.mDisallowIntercept) != null) {
            this.mSrc.removeCallbacks(runnable);
            return;
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

    private boolean onTouchObserved(MotionEvent motionEvent) {
        View view = this.mSrc;
        if (!view.isEnabled()) {
            return false;
        }
        switch (motionEvent.getActionMasked()) {
            default: {
                return false;
            }
            case 2: {
                int n = motionEvent.findPointerIndex(this.mActivePointerId);
                if (n >= 0) {
                    if (!ForwardingListener.pointInView(view, motionEvent.getX(n), motionEvent.getY(n), this.mScaledTouchSlop)) {
                        this.clearCallbacks();
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            case 1: 
            case 3: {
                this.clearCallbacks();
                return false;
            }
            case 0: 
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
        boolean bl2 = this.mForwarding;
        boolean bl3 = true;
        if (bl2) {
            bl = this.onTouchForwarded(motionEvent) || !this.onForwardingStopped();
        } else {
            bl = this.onTouchObserved(motionEvent) && this.onForwardingStarted();
            if (bl) {
                long l = SystemClock.uptimeMillis();
                view = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
                this.mSrc.onTouchEvent((MotionEvent)view);
                view.recycle();
            }
        }
        this.mForwarding = bl;
        if (!bl) {
            if (bl2) {
                return true;
            }
            bl3 = false;
        }
        return bl3;
    }

    public void onViewAttachedToWindow(View view) {
    }

    public void onViewDetachedFromWindow(View object) {
        this.mForwarding = false;
        this.mActivePointerId = -1;
        object = this.mDisallowIntercept;
        if (object != null) {
            this.mSrc.removeCallbacks((Runnable)object);
            return;
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
                return;
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

