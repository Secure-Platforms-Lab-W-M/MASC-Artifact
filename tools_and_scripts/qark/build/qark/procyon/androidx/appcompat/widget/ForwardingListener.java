// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.view.ViewParent;
import android.os.SystemClock;
import androidx.appcompat.view.menu.ShowableListMenu;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.View;
import android.view.View$OnAttachStateChangeListener;
import android.view.View$OnTouchListener;

public abstract class ForwardingListener implements View$OnTouchListener, View$OnAttachStateChangeListener
{
    private int mActivePointerId;
    private Runnable mDisallowIntercept;
    private boolean mForwarding;
    private final int mLongPressTimeout;
    private final float mScaledTouchSlop;
    final View mSrc;
    private final int mTapTimeout;
    private final int[] mTmpLocation;
    private Runnable mTriggerLongPress;
    
    public ForwardingListener(final View mSrc) {
        this.mTmpLocation = new int[2];
        (this.mSrc = mSrc).setLongClickable(true);
        mSrc.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
        this.mScaledTouchSlop = (float)ViewConfiguration.get(mSrc.getContext()).getScaledTouchSlop();
        final int tapTimeout = ViewConfiguration.getTapTimeout();
        this.mTapTimeout = tapTimeout;
        this.mLongPressTimeout = (tapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
    }
    
    private void clearCallbacks() {
        final Runnable mTriggerLongPress = this.mTriggerLongPress;
        if (mTriggerLongPress != null) {
            this.mSrc.removeCallbacks(mTriggerLongPress);
        }
        final Runnable mDisallowIntercept = this.mDisallowIntercept;
        if (mDisallowIntercept != null) {
            this.mSrc.removeCallbacks(mDisallowIntercept);
        }
    }
    
    private boolean onTouchForwarded(final MotionEvent motionEvent) {
        final View mSrc = this.mSrc;
        final ShowableListMenu popup = this.getPopup();
        final boolean b = false;
        if (popup == null) {
            return false;
        }
        if (!popup.isShowing()) {
            return false;
        }
        final DropDownListView dropDownListView = (DropDownListView)popup.getListView();
        if (dropDownListView == null) {
            return false;
        }
        if (!dropDownListView.isShown()) {
            return false;
        }
        final MotionEvent obtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
        this.toGlobalMotionEvent(mSrc, obtainNoHistory);
        this.toLocalMotionEvent((View)dropDownListView, obtainNoHistory);
        final boolean onForwardedEvent = dropDownListView.onForwardedEvent(obtainNoHistory, this.mActivePointerId);
        obtainNoHistory.recycle();
        final int actionMasked = motionEvent.getActionMasked();
        final boolean b2 = actionMasked != 1 && actionMasked != 3;
        boolean b3 = b;
        if (onForwardedEvent) {
            b3 = b;
            if (b2) {
                b3 = true;
            }
        }
        return b3;
    }
    
    private boolean onTouchObserved(final MotionEvent motionEvent) {
        final View mSrc = this.mSrc;
        if (!mSrc.isEnabled()) {
            return false;
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        return false;
                    }
                }
                else {
                    final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex < 0) {
                        return false;
                    }
                    if (!pointInView(mSrc, motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex), this.mScaledTouchSlop)) {
                        this.clearCallbacks();
                        mSrc.getParent().requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                    return false;
                }
            }
            this.clearCallbacks();
            return false;
        }
        this.mActivePointerId = motionEvent.getPointerId(0);
        if (this.mDisallowIntercept == null) {
            this.mDisallowIntercept = new DisallowIntercept();
        }
        mSrc.postDelayed(this.mDisallowIntercept, (long)this.mTapTimeout);
        if (this.mTriggerLongPress == null) {
            this.mTriggerLongPress = new TriggerLongPress();
        }
        mSrc.postDelayed(this.mTriggerLongPress, (long)this.mLongPressTimeout);
        return false;
    }
    
    private static boolean pointInView(final View view, final float n, final float n2, final float n3) {
        return n >= -n3 && n2 >= -n3 && n < view.getRight() - view.getLeft() + n3 && n2 < view.getBottom() - view.getTop() + n3;
    }
    
    private boolean toGlobalMotionEvent(final View view, final MotionEvent motionEvent) {
        final int[] mTmpLocation = this.mTmpLocation;
        view.getLocationOnScreen(mTmpLocation);
        motionEvent.offsetLocation((float)mTmpLocation[0], (float)mTmpLocation[1]);
        return true;
    }
    
    private boolean toLocalMotionEvent(final View view, final MotionEvent motionEvent) {
        final int[] mTmpLocation = this.mTmpLocation;
        view.getLocationOnScreen(mTmpLocation);
        motionEvent.offsetLocation((float)(-mTmpLocation[0]), (float)(-mTmpLocation[1]));
        return true;
    }
    
    public abstract ShowableListMenu getPopup();
    
    protected boolean onForwardingStarted() {
        final ShowableListMenu popup = this.getPopup();
        if (popup != null && !popup.isShowing()) {
            popup.show();
        }
        return true;
    }
    
    protected boolean onForwardingStopped() {
        final ShowableListMenu popup = this.getPopup();
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
        return true;
    }
    
    void onLongPress() {
        this.clearCallbacks();
        final View mSrc = this.mSrc;
        if (!mSrc.isEnabled()) {
            return;
        }
        if (mSrc.isLongClickable()) {
            return;
        }
        if (!this.onForwardingStarted()) {
            return;
        }
        mSrc.getParent().requestDisallowInterceptTouchEvent(true);
        final long uptimeMillis = SystemClock.uptimeMillis();
        final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
        mSrc.onTouchEvent(obtain);
        obtain.recycle();
        this.mForwarding = true;
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final boolean mForwarding = this.mForwarding;
        final boolean b = true;
        boolean mForwarding2;
        if (mForwarding) {
            mForwarding2 = (this.onTouchForwarded(motionEvent) || !this.onForwardingStopped());
        }
        else {
            final boolean b2 = mForwarding2 = (this.onTouchObserved(motionEvent) && this.onForwardingStarted());
            if (b2) {
                final long uptimeMillis = SystemClock.uptimeMillis();
                final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                this.mSrc.onTouchEvent(obtain);
                obtain.recycle();
                mForwarding2 = b2;
            }
        }
        this.mForwarding = mForwarding2;
        boolean b3 = b;
        if (!mForwarding2) {
            if (mForwarding) {
                return true;
            }
            b3 = false;
        }
        return b3;
    }
    
    public void onViewAttachedToWindow(final View view) {
    }
    
    public void onViewDetachedFromWindow(final View view) {
        this.mForwarding = false;
        this.mActivePointerId = -1;
        final Runnable mDisallowIntercept = this.mDisallowIntercept;
        if (mDisallowIntercept != null) {
            this.mSrc.removeCallbacks(mDisallowIntercept);
        }
    }
    
    private class DisallowIntercept implements Runnable
    {
        DisallowIntercept() {
        }
        
        @Override
        public void run() {
            final ViewParent parent = ForwardingListener.this.mSrc.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }
    
    private class TriggerLongPress implements Runnable
    {
        TriggerLongPress() {
        }
        
        @Override
        public void run() {
            ForwardingListener.this.onLongPress();
        }
    }
}
