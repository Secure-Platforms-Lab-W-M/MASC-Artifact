/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 *  android.view.GestureDetector
 *  android.view.GestureDetector$OnDoubleTapListener
 *  android.view.GestureDetector$OnGestureListener
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.ViewConfiguration
 */
package android.support.v4.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat {
    private final GestureDetectorCompatImpl mImpl;

    public GestureDetectorCompat(Context context, GestureDetector.OnGestureListener onGestureListener) {
        this(context, onGestureListener, null);
    }

    public GestureDetectorCompat(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
        if (Build.VERSION.SDK_INT > 17) {
            this.mImpl = new GestureDetectorCompatImplJellybeanMr2(context, onGestureListener, handler);
            return;
        }
        this.mImpl = new GestureDetectorCompatImplBase(context, onGestureListener, handler);
    }

    public boolean isLongpressEnabled() {
        return this.mImpl.isLongpressEnabled();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mImpl.onTouchEvent(motionEvent);
    }

    public void setIsLongpressEnabled(boolean bl) {
        this.mImpl.setIsLongpressEnabled(bl);
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        this.mImpl.setOnDoubleTapListener(onDoubleTapListener);
    }

    static interface GestureDetectorCompatImpl {
        public boolean isLongpressEnabled();

        public boolean onTouchEvent(MotionEvent var1);

        public void setIsLongpressEnabled(boolean var1);

        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener var1);
    }

    static class GestureDetectorCompatImplBase
    implements GestureDetectorCompatImpl {
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
        GestureDetector.OnDoubleTapListener mDoubleTapListener;
        private int mDoubleTapSlopSquare;
        private float mDownFocusX;
        private float mDownFocusY;
        private final Handler mHandler;
        private boolean mInLongPress;
        private boolean mIsDoubleTapping;
        private boolean mIsLongpressEnabled;
        private float mLastFocusX;
        private float mLastFocusY;
        final GestureDetector.OnGestureListener mListener;
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

        public GestureDetectorCompatImplBase(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
            this.mHandler = handler != null ? new GestureHandler(this, handler) : new GestureHandler(this);
            this.mListener = onGestureListener;
            if (onGestureListener instanceof GestureDetector.OnDoubleTapListener) {
                this.setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)onGestureListener);
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
                return;
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
                return;
            }
        }

        private void init(Context context) {
            if (context != null) {
                if (this.mListener != null) {
                    this.mIsLongpressEnabled = true;
                    context = ViewConfiguration.get((Context)context);
                    int n = context.getScaledTouchSlop();
                    int n2 = context.getScaledDoubleTapSlop();
                    this.mMinimumFlingVelocity = context.getScaledMinimumFlingVelocity();
                    this.mMaximumFlingVelocity = context.getScaledMaximumFlingVelocity();
                    this.mTouchSlopSquare = n * n;
                    this.mDoubleTapSlopSquare = n2 * n2;
                    return;
                }
                throw new IllegalArgumentException("OnGestureListener must not be null");
            }
            throw new IllegalArgumentException("Context must not be null");
        }

        private boolean isConsideredDoubleTap(MotionEvent motionEvent, MotionEvent motionEvent2, MotionEvent motionEvent3) {
            int n;
            boolean bl = this.mAlwaysInBiggerTapRegion;
            boolean bl2 = false;
            if (!bl) {
                return false;
            }
            if (motionEvent3.getEventTime() - motionEvent2.getEventTime() > (long)DOUBLE_TAP_TIMEOUT) {
                return false;
            }
            int n2 = (int)motionEvent.getX() - (int)motionEvent3.getX();
            if (n2 * n2 + (n = (int)motionEvent.getY() - (int)motionEvent3.getY()) * n < this.mDoubleTapSlopSquare) {
                bl2 = true;
            }
            return bl2;
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

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            int n;
            MotionEvent motionEvent2;
            int n2 = motionEvent.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            int n3 = (n2 & 255) == 6 ? 1 : 0;
            int n4 = n3 != 0 ? motionEvent.getActionIndex() : -1;
            float f = 0.0f;
            float f2 = 0.0f;
            int n5 = motionEvent.getPointerCount();
            for (n = 0; n < n5; ++n) {
                if (n4 == n) continue;
                f += motionEvent.getX(n);
                f2 += motionEvent.getY(n);
            }
            n = n3 != 0 ? n5 - 1 : n5;
            f /= (float)n;
            f2 /= (float)n;
            boolean bl = false;
            boolean bl2 = false;
            n = 0;
            boolean bl3 = false;
            switch (n2 & 255) {
                default: {
                    return false;
                }
                case 6: {
                    this.mLastFocusX = f;
                    this.mDownFocusX = f;
                    this.mLastFocusY = f2;
                    this.mDownFocusY = f2;
                    this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                    n = motionEvent.getActionIndex();
                    n2 = motionEvent.getPointerId(n);
                    f2 = this.mVelocityTracker.getXVelocity(n2);
                    f = this.mVelocityTracker.getYVelocity(n2);
                    n2 = 0;
                    while (n2 < n5) {
                        int n6;
                        if (n2 != n && this.mVelocityTracker.getXVelocity(n6 = motionEvent.getPointerId(n2)) * f2 + this.mVelocityTracker.getYVelocity(n6) * f < 0.0f) {
                            this.mVelocityTracker.clear();
                            return false;
                        }
                        ++n2;
                    }
                    return false;
                }
                case 5: {
                    this.mLastFocusX = f;
                    this.mDownFocusX = f;
                    this.mLastFocusY = f2;
                    this.mDownFocusY = f2;
                    this.cancelTaps();
                    return false;
                }
                case 3: {
                    this.cancel();
                    return false;
                }
                case 2: {
                    if (this.mInLongPress) {
                        return false;
                    }
                    float f3 = this.mLastFocusX - f;
                    float f4 = this.mLastFocusY - f2;
                    if (this.mIsDoubleTapping) {
                        return false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                    }
                    if (this.mAlwaysInTapRegion) {
                        n3 = (int)(f - this.mDownFocusX);
                        n4 = (int)(f2 - this.mDownFocusY);
                        if ((n3 = n3 * n3 + n4 * n4) > this.mTouchSlopSquare) {
                            bl3 = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, f3, f4);
                            this.mLastFocusX = f;
                            this.mLastFocusY = f2;
                            this.mAlwaysInTapRegion = false;
                            this.mHandler.removeMessages(3);
                            this.mHandler.removeMessages(1);
                            this.mHandler.removeMessages(2);
                        }
                        if (n3 <= this.mTouchSlopSquare) return bl3;
                        this.mAlwaysInBiggerTapRegion = false;
                        return bl3;
                    }
                    if (Math.abs(f3) < 1.0f) {
                        bl3 = bl;
                        if (Math.abs(f4) < 1.0f) return bl3;
                    }
                    bl3 = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, f3, f4);
                    this.mLastFocusX = f;
                    this.mLastFocusY = f2;
                    return bl3;
                }
                case 1: {
                    this.mStillDown = false;
                    MotionEvent motionEvent3 = MotionEvent.obtain((MotionEvent)motionEvent);
                    if (this.mIsDoubleTapping) {
                        bl3 = false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                    } else if (this.mInLongPress) {
                        this.mHandler.removeMessages(3);
                        this.mInLongPress = false;
                        bl3 = bl2;
                    } else if (this.mAlwaysInTapRegion) {
                        GestureDetector.OnDoubleTapListener onDoubleTapListener;
                        bl3 = this.mListener.onSingleTapUp(motionEvent);
                        if (this.mDeferConfirmSingleTap && (onDoubleTapListener = this.mDoubleTapListener) != null) {
                            onDoubleTapListener.onSingleTapConfirmed(motionEvent);
                        }
                    } else {
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        n3 = motionEvent.getPointerId(0);
                        velocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                        f2 = velocityTracker.getYVelocity(n3);
                        f = velocityTracker.getXVelocity(n3);
                        bl3 = Math.abs(f2) <= (float)this.mMinimumFlingVelocity && Math.abs(f) <= (float)this.mMinimumFlingVelocity ? bl2 : this.mListener.onFling(this.mCurrentDownEvent, motionEvent, f, f2);
                    }
                    motionEvent = this.mPreviousUpEvent;
                    if (motionEvent != null) {
                        motionEvent.recycle();
                    }
                    this.mPreviousUpEvent = motionEvent3;
                    motionEvent = this.mVelocityTracker;
                    if (motionEvent != null) {
                        motionEvent.recycle();
                        this.mVelocityTracker = null;
                    }
                    this.mIsDoubleTapping = false;
                    this.mDeferConfirmSingleTap = false;
                    this.mHandler.removeMessages(1);
                    this.mHandler.removeMessages(2);
                    return bl3;
                }
                case 0: 
            }
            if (this.mDoubleTapListener != null) {
                MotionEvent motionEvent4;
                bl3 = this.mHandler.hasMessages(3);
                if (bl3) {
                    this.mHandler.removeMessages(3);
                }
                if ((motionEvent2 = this.mCurrentDownEvent) != null && (motionEvent4 = this.mPreviousUpEvent) != null && bl3 && this.isConsideredDoubleTap(motionEvent2, motionEvent4, motionEvent)) {
                    this.mIsDoubleTapping = true;
                    n3 = this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                } else {
                    this.mHandler.sendEmptyMessageDelayed(3, (long)DOUBLE_TAP_TIMEOUT);
                    n3 = n;
                }
            } else {
                n3 = n;
            }
            this.mLastFocusX = f;
            this.mDownFocusX = f;
            this.mLastFocusY = f2;
            this.mDownFocusY = f2;
            motionEvent2 = this.mCurrentDownEvent;
            if (motionEvent2 != null) {
                motionEvent2.recycle();
            }
            this.mCurrentDownEvent = MotionEvent.obtain((MotionEvent)motionEvent);
            this.mAlwaysInTapRegion = true;
            this.mAlwaysInBiggerTapRegion = true;
            this.mStillDown = true;
            this.mInLongPress = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mIsLongpressEnabled) {
                this.mHandler.removeMessages(2);
                this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + (long)TAP_TIMEOUT + (long)LONGPRESS_TIMEOUT);
            }
            this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + (long)TAP_TIMEOUT);
            return (boolean)(n3 | this.mListener.onDown(motionEvent));
        }

        @Override
        public void setIsLongpressEnabled(boolean bl) {
            this.mIsLongpressEnabled = bl;
        }

        @Override
        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
            this.mDoubleTapListener = onDoubleTapListener;
        }

        private class GestureHandler
        extends Handler {
            final /* synthetic */ GestureDetectorCompatImplBase this$0;

            GestureHandler(GestureDetectorCompatImplBase gestureDetectorCompatImplBase) {
                this.this$0 = gestureDetectorCompatImplBase;
            }

            GestureHandler(GestureDetectorCompatImplBase gestureDetectorCompatImplBase, Handler handler) {
                this.this$0 = gestureDetectorCompatImplBase;
                super(handler.getLooper());
            }

            public void handleMessage(Message message) {
                switch (message.what) {
                    default: {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown message ");
                        stringBuilder.append((Object)message);
                        throw new RuntimeException(stringBuilder.toString());
                    }
                    case 3: {
                        if (this.this$0.mDoubleTapListener != null) {
                            if (!this.this$0.mStillDown) {
                                this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
                                return;
                            }
                            this.this$0.mDeferConfirmSingleTap = true;
                            return;
                        }
                        return;
                    }
                    case 2: {
                        this.this$0.dispatchLongPress();
                        return;
                    }
                    case 1: 
                }
                this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
            }
        }

    }

    static class GestureDetectorCompatImplJellybeanMr2
    implements GestureDetectorCompatImpl {
        private final GestureDetector mDetector;

        public GestureDetectorCompatImplJellybeanMr2(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
            this.mDetector = new GestureDetector(context, onGestureListener, handler);
        }

        @Override
        public boolean isLongpressEnabled() {
            return this.mDetector.isLongpressEnabled();
        }

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return this.mDetector.onTouchEvent(motionEvent);
        }

        @Override
        public void setIsLongpressEnabled(boolean bl) {
            this.mDetector.setIsLongpressEnabled(bl);
        }

        @Override
        public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
            this.mDetector.setOnDoubleTapListener(onDoubleTapListener);
        }
    }

}

