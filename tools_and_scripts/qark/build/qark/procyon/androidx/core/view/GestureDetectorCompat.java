// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.view;

import android.view.GestureDetector;
import android.os.Message;
import android.view.ViewConfiguration;
import android.view.VelocityTracker;
import android.view.GestureDetector$OnDoubleTapListener;
import android.view.MotionEvent;
import android.os.Build$VERSION;
import android.os.Handler;
import android.view.GestureDetector$OnGestureListener;
import android.content.Context;

public final class GestureDetectorCompat
{
    private final GestureDetectorCompatImpl mImpl;
    
    public GestureDetectorCompat(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener) {
        this(context, gestureDetector$OnGestureListener, null);
    }
    
    public GestureDetectorCompat(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener, final Handler handler) {
        if (Build$VERSION.SDK_INT > 17) {
            this.mImpl = (GestureDetectorCompatImpl)new GestureDetectorCompatImplJellybeanMr2(context, gestureDetector$OnGestureListener, handler);
            return;
        }
        this.mImpl = (GestureDetectorCompatImpl)new GestureDetectorCompatImplBase(context, gestureDetector$OnGestureListener, handler);
    }
    
    public boolean isLongpressEnabled() {
        return this.mImpl.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return this.mImpl.onTouchEvent(motionEvent);
    }
    
    public void setIsLongpressEnabled(final boolean isLongpressEnabled) {
        this.mImpl.setIsLongpressEnabled(isLongpressEnabled);
    }
    
    public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener onDoubleTapListener) {
        this.mImpl.setOnDoubleTapListener(onDoubleTapListener);
    }
    
    interface GestureDetectorCompatImpl
    {
        boolean isLongpressEnabled();
        
        boolean onTouchEvent(final MotionEvent p0);
        
        void setIsLongpressEnabled(final boolean p0);
        
        void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener p0);
    }
    
    static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl
    {
        private static final int DOUBLE_TAP_TIMEOUT;
        private static final int LONGPRESS_TIMEOUT;
        private static final int LONG_PRESS = 2;
        private static final int SHOW_PRESS = 1;
        private static final int TAP = 3;
        private static final int TAP_TIMEOUT;
        private boolean mAlwaysInBiggerTapRegion;
        private boolean mAlwaysInTapRegion;
        MotionEvent mCurrentDownEvent;
        boolean mDeferConfirmSingleTap;
        GestureDetector$OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        final GestureDetector$OnGestureListener mListener;
        private int mMaximumFlingVelocity;
        private int mMinimumFlingVelocity;
        private MotionEvent mPreviousUpEvent;
        boolean mStillDown;
        private int mTouchSlopSquare;
        private VelocityTracker mVelocityTracker;
        
        static {
            LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
            TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
            DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
        }
        
        GestureDetectorCompatImplBase(final Context context, final GestureDetector$OnGestureListener mListener, final Handler handler) {
            if (handler != null) {
                this.mHandler = new GestureHandler(handler);
            }
            else {
                this.mHandler = new GestureHandler();
            }
            this.mListener = mListener;
            if (mListener instanceof GestureDetector$OnDoubleTapListener) {
                this.setOnDoubleTapListener((GestureDetector$OnDoubleTapListener)mListener);
            }
            this.init(context);
        }
        
        private void cancel() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            this.mIsDoubleTapping = false;
            this.mStillDown = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }
        
        private void cancelTaps() {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mIsDoubleTapping = false;
            this.mAlwaysInTapRegion = false;
            this.mAlwaysInBiggerTapRegion = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mInLongPress) {
                this.mInLongPress = false;
            }
        }
        
        private void init(final Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null");
            }
            if (this.mListener != null) {
                this.mIsLongpressEnabled = true;
                final ViewConfiguration value = ViewConfiguration.get(context);
                final int scaledTouchSlop = value.getScaledTouchSlop();
                final int scaledDoubleTapSlop = value.getScaledDoubleTapSlop();
                this.mMinimumFlingVelocity = value.getScaledMinimumFlingVelocity();
                this.mMaximumFlingVelocity = value.getScaledMaximumFlingVelocity();
                this.mTouchSlopSquare = scaledTouchSlop * scaledTouchSlop;
                this.mDoubleTapSlopSquare = scaledDoubleTapSlop * scaledDoubleTapSlop;
                return;
            }
            throw new IllegalArgumentException("OnGestureListener must not be null");
        }
        
        private boolean isConsideredDoubleTap(final MotionEvent motionEvent, final MotionEvent motionEvent2, final MotionEvent motionEvent3) {
            final boolean mAlwaysInBiggerTapRegion = this.mAlwaysInBiggerTapRegion;
            boolean b = false;
            if (!mAlwaysInBiggerTapRegion) {
                return false;
            }
            if (motionEvent3.getEventTime() - motionEvent2.getEventTime() > GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT) {
                return false;
            }
            final int n = (int)motionEvent.getX() - (int)motionEvent3.getX();
            final int n2 = (int)motionEvent.getY() - (int)motionEvent3.getY();
            if (n * n + n2 * n2 < this.mDoubleTapSlopSquare) {
                b = true;
            }
            return b;
        }
        
        void dispatchLongPress() {
            this.mHandler.removeMessages(3);
            this.mDeferConfirmSingleTap = false;
            this.mInLongPress = true;
            this.mListener.onLongPress(this.mCurrentDownEvent);
        }
        
        @Override
        public boolean isLongpressEnabled() {
            return this.mIsLongpressEnabled;
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent mPreviousUpEvent) {
            final int action = mPreviousUpEvent.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(mPreviousUpEvent);
            final boolean b = (action & 0xFF) == 0x6;
            int actionIndex;
            if (b) {
                actionIndex = mPreviousUpEvent.getActionIndex();
            }
            else {
                actionIndex = -1;
            }
            float n = 0.0f;
            float n2 = 0.0f;
            final int pointerCount = mPreviousUpEvent.getPointerCount();
            for (int i = 0; i < pointerCount; ++i) {
                if (actionIndex != i) {
                    n += mPreviousUpEvent.getX(i);
                    n2 += mPreviousUpEvent.getY(i);
                }
            }
            int n3;
            if (b) {
                n3 = pointerCount - 1;
            }
            else {
                n3 = pointerCount;
            }
            final float n4 = n / n3;
            final float n5 = n2 / n3;
            final boolean b2 = false;
            final boolean b3 = false;
            boolean onScroll = false;
            final int n6 = action & 0xFF;
            if (n6 == 0) {
                boolean b4 = b3;
                Label_1024: {
                    if (this.mDoubleTapListener != null) {
                        final boolean hasMessages = this.mHandler.hasMessages(3);
                        if (hasMessages) {
                            this.mHandler.removeMessages(3);
                        }
                        final MotionEvent mCurrentDownEvent = this.mCurrentDownEvent;
                        if (mCurrentDownEvent != null) {
                            final MotionEvent mPreviousUpEvent2 = this.mPreviousUpEvent;
                            if (mPreviousUpEvent2 != null && hasMessages && this.isConsideredDoubleTap(mCurrentDownEvent, mPreviousUpEvent2, mPreviousUpEvent)) {
                                this.mIsDoubleTapping = true;
                                b4 = (this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false | this.mDoubleTapListener.onDoubleTapEvent(mPreviousUpEvent));
                                break Label_1024;
                            }
                        }
                        this.mHandler.sendEmptyMessageDelayed(3, (long)GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT);
                        b4 = b3;
                    }
                }
                this.mLastFocusX = n4;
                this.mDownFocusX = n4;
                this.mLastFocusY = n5;
                this.mDownFocusY = n5;
                final MotionEvent mCurrentDownEvent2 = this.mCurrentDownEvent;
                if (mCurrentDownEvent2 != null) {
                    mCurrentDownEvent2.recycle();
                }
                this.mCurrentDownEvent = MotionEvent.obtain(mPreviousUpEvent);
                this.mAlwaysInTapRegion = true;
                this.mAlwaysInBiggerTapRegion = true;
                this.mStillDown = true;
                this.mInLongPress = false;
                this.mDeferConfirmSingleTap = false;
                if (this.mIsLongpressEnabled) {
                    this.mHandler.removeMessages(2);
                    this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + GestureDetectorCompatImplBase.TAP_TIMEOUT + GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT);
                }
                this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + GestureDetectorCompatImplBase.TAP_TIMEOUT);
                return b4 | this.mListener.onDown(mPreviousUpEvent);
            }
            if (n6 != 1) {
                if (n6 != 2) {
                    if (n6 != 3) {
                        if (n6 != 5) {
                            if (n6 == 6) {
                                this.mLastFocusX = n4;
                                this.mDownFocusX = n4;
                                this.mLastFocusY = n5;
                                this.mDownFocusY = n5;
                                this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                                final int actionIndex2 = mPreviousUpEvent.getActionIndex();
                                final int pointerId = mPreviousUpEvent.getPointerId(actionIndex2);
                                final float xVelocity = this.mVelocityTracker.getXVelocity(pointerId);
                                final float yVelocity = this.mVelocityTracker.getYVelocity(pointerId);
                                for (int j = 0; j < pointerCount; ++j) {
                                    if (j != actionIndex2) {
                                        final int pointerId2 = mPreviousUpEvent.getPointerId(j);
                                        if (this.mVelocityTracker.getXVelocity(pointerId2) * xVelocity + this.mVelocityTracker.getYVelocity(pointerId2) * yVelocity < 0.0f) {
                                            this.mVelocityTracker.clear();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            this.mLastFocusX = n4;
                            this.mDownFocusX = n4;
                            this.mLastFocusY = n5;
                            this.mDownFocusY = n5;
                            this.cancelTaps();
                        }
                    }
                    else {
                        this.cancel();
                    }
                }
                else if (!this.mInLongPress) {
                    final float n7 = this.mLastFocusX - n4;
                    final float n8 = this.mLastFocusY - n5;
                    if (this.mIsDoubleTapping) {
                        return false | this.mDoubleTapListener.onDoubleTapEvent(mPreviousUpEvent);
                    }
                    if (this.mAlwaysInTapRegion) {
                        final int n9 = (int)(n4 - this.mDownFocusX);
                        final int n10 = (int)(n5 - this.mDownFocusY);
                        final int n11 = n9 * n9 + n10 * n10;
                        if (n11 > this.mTouchSlopSquare) {
                            onScroll = this.mListener.onScroll(this.mCurrentDownEvent, mPreviousUpEvent, n7, n8);
                            this.mLastFocusX = n4;
                            this.mLastFocusY = n5;
                            this.mAlwaysInTapRegion = false;
                            this.mHandler.removeMessages(3);
                            this.mHandler.removeMessages(1);
                            this.mHandler.removeMessages(2);
                        }
                        if (n11 > this.mTouchSlopSquare) {
                            this.mAlwaysInBiggerTapRegion = false;
                        }
                        return onScroll;
                    }
                    if (Math.abs(n7) >= 1.0f || Math.abs(n8) >= 1.0f) {
                        final boolean onScroll2 = this.mListener.onScroll(this.mCurrentDownEvent, mPreviousUpEvent, n7, n8);
                        this.mLastFocusX = n4;
                        this.mLastFocusY = n5;
                        return onScroll2;
                    }
                }
                return false;
            }
            this.mStillDown = false;
            final MotionEvent obtain = MotionEvent.obtain(mPreviousUpEvent);
            boolean b5 = false;
            Label_0832: {
                if (this.mIsDoubleTapping) {
                    b5 = (false | this.mDoubleTapListener.onDoubleTapEvent(mPreviousUpEvent));
                }
                else if (this.mInLongPress) {
                    this.mHandler.removeMessages(3);
                    this.mInLongPress = false;
                    b5 = b2;
                }
                else if (this.mAlwaysInTapRegion) {
                    final boolean b6 = b5 = this.mListener.onSingleTapUp(mPreviousUpEvent);
                    if (this.mDeferConfirmSingleTap) {
                        final GestureDetector$OnDoubleTapListener mDoubleTapListener = this.mDoubleTapListener;
                        b5 = b6;
                        if (mDoubleTapListener != null) {
                            mDoubleTapListener.onSingleTapConfirmed(mPreviousUpEvent);
                            b5 = b6;
                        }
                    }
                }
                else {
                    final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                    final int pointerId3 = mPreviousUpEvent.getPointerId(0);
                    mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                    final float yVelocity2 = mVelocityTracker.getYVelocity(pointerId3);
                    final float xVelocity2 = mVelocityTracker.getXVelocity(pointerId3);
                    if (Math.abs(yVelocity2) <= this.mMinimumFlingVelocity) {
                        b5 = b2;
                        if (Math.abs(xVelocity2) <= this.mMinimumFlingVelocity) {
                            break Label_0832;
                        }
                    }
                    b5 = this.mListener.onFling(this.mCurrentDownEvent, mPreviousUpEvent, xVelocity2, yVelocity2);
                }
            }
            mPreviousUpEvent = this.mPreviousUpEvent;
            if (mPreviousUpEvent != null) {
                mPreviousUpEvent.recycle();
            }
            this.mPreviousUpEvent = obtain;
            final VelocityTracker mVelocityTracker2 = this.mVelocityTracker;
            if (mVelocityTracker2 != null) {
                mVelocityTracker2.recycle();
                this.mVelocityTracker = null;
            }
            this.mIsDoubleTapping = false;
            this.mDeferConfirmSingleTap = false;
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            return b5;
        }
        
        @Override
        public void setIsLongpressEnabled(final boolean mIsLongpressEnabled) {
            this.mIsLongpressEnabled = mIsLongpressEnabled;
        }
        
        @Override
        public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener mDoubleTapListener) {
            this.mDoubleTapListener = mDoubleTapListener;
        }
        
        private class GestureHandler extends Handler
        {
            GestureHandler() {
            }
            
            GestureHandler(final Handler handler) {
                super(handler.getLooper());
            }
            
            public void handleMessage(final Message message) {
                final int what = message.what;
                if (what != 1) {
                    if (what == 2) {
                        GestureDetectorCompatImplBase.this.dispatchLongPress();
                        return;
                    }
                    if (what != 3) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown message ");
                        sb.append(message);
                        throw new RuntimeException(sb.toString());
                    }
                    if (GestureDetectorCompatImplBase.this.mDoubleTapListener != null) {
                        if (!GestureDetectorCompatImplBase.this.mStillDown) {
                            GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                            return;
                        }
                        GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
                    }
                }
                else {
                    GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                }
            }
        }
    }
    
    static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl
    {
        private final GestureDetector mDetector;
        
        GestureDetectorCompatImplJellybeanMr2(final Context context, final GestureDetector$OnGestureListener gestureDetector$OnGestureListener, final Handler handler) {
            this.mDetector = new GestureDetector(context, gestureDetector$OnGestureListener, handler);
        }
        
        @Override
        public boolean isLongpressEnabled() {
            return this.mDetector.isLongpressEnabled();
        }
        
        @Override
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            return this.mDetector.onTouchEvent(motionEvent);
        }
        
        @Override
        public void setIsLongpressEnabled(final boolean isLongpressEnabled) {
            this.mDetector.setIsLongpressEnabled(isLongpressEnabled);
        }
        
        @Override
        public void setOnDoubleTapListener(final GestureDetector$OnDoubleTapListener onDoubleTapListener) {
            this.mDetector.setOnDoubleTapListener(onDoubleTapListener);
        }
    }
}
