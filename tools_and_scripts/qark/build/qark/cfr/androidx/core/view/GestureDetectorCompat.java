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
package androidx.core.view;

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

        GestureDetectorCompatImplBase(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
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

        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean bl;
            float f;
            float f2;
            int n;
            int n2;
            MotionEvent motionEvent2;
            block35 : {
                MotionEvent motionEvent3;
                block40 : {
                    block43 : {
                        boolean bl2;
                        block42 : {
                            block41 : {
                                block39 : {
                                    block36 : {
                                        float f3;
                                        float f4;
                                        block38 : {
                                            block34 : {
                                                block37 : {
                                                    int n3;
                                                    int n4 = motionEvent.getAction();
                                                    if (this.mVelocityTracker == null) {
                                                        this.mVelocityTracker = VelocityTracker.obtain();
                                                    }
                                                    this.mVelocityTracker.addMovement(motionEvent);
                                                    n2 = (n4 & 255) == 6 ? 1 : 0;
                                                    n = n2 != 0 ? motionEvent.getActionIndex() : -1;
                                                    f = 0.0f;
                                                    f2 = 0.0f;
                                                    int n5 = motionEvent.getPointerCount();
                                                    for (n3 = 0; n3 < n5; ++n3) {
                                                        if (n == n3) continue;
                                                        f += motionEvent.getX(n3);
                                                        f2 += motionEvent.getY(n3);
                                                    }
                                                    n = n2 != 0 ? n5 - 1 : n5;
                                                    f /= (float)n;
                                                    f2 /= (float)n;
                                                    bl2 = false;
                                                    n = 0;
                                                    bl = false;
                                                    n3 = n4 & 255;
                                                    if (n3 == 0) break block35;
                                                    if (n3 == 1) break block36;
                                                    if (n3 == 2) break block37;
                                                    if (n3 != 3) {
                                                        if (n3 != 5) {
                                                            if (n3 == 6) {
                                                                this.mLastFocusX = f;
                                                                this.mDownFocusX = f;
                                                                this.mLastFocusY = f2;
                                                                this.mDownFocusY = f2;
                                                                this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                                                                n3 = motionEvent.getActionIndex();
                                                                n = motionEvent.getPointerId(n3);
                                                                f2 = this.mVelocityTracker.getXVelocity(n);
                                                                f = this.mVelocityTracker.getYVelocity(n);
                                                                for (n4 = 0; n4 < n5; ++n4) {
                                                                    int n6;
                                                                    if (n4 == n3 || this.mVelocityTracker.getXVelocity(n6 = motionEvent.getPointerId(n4)) * f2 + this.mVelocityTracker.getYVelocity(n6) * f >= 0.0f) continue;
                                                                    this.mVelocityTracker.clear();
                                                                    break block34;
                                                                }
                                                            }
                                                        } else {
                                                            this.mLastFocusX = f;
                                                            this.mDownFocusX = f;
                                                            this.mLastFocusY = f2;
                                                            this.mDownFocusY = f2;
                                                            this.cancelTaps();
                                                        }
                                                    } else {
                                                        this.cancel();
                                                    }
                                                    break block34;
                                                }
                                                if (this.mInLongPress) break block34;
                                                f3 = this.mLastFocusX - f;
                                                f4 = this.mLastFocusY - f2;
                                                if (this.mIsDoubleTapping) {
                                                    return false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                                                }
                                                if (this.mAlwaysInTapRegion) {
                                                    n2 = (int)(f - this.mDownFocusX);
                                                    n = (int)(f2 - this.mDownFocusY);
                                                    if ((n2 = n2 * n2 + n * n) > this.mTouchSlopSquare) {
                                                        bl = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, f3, f4);
                                                        this.mLastFocusX = f;
                                                        this.mLastFocusY = f2;
                                                        this.mAlwaysInTapRegion = false;
                                                        this.mHandler.removeMessages(3);
                                                        this.mHandler.removeMessages(1);
                                                        this.mHandler.removeMessages(2);
                                                    }
                                                    if (n2 > this.mTouchSlopSquare) {
                                                        this.mAlwaysInBiggerTapRegion = false;
                                                    }
                                                    return bl;
                                                }
                                                if (Math.abs(f3) >= 1.0f || Math.abs(f4) >= 1.0f) break block38;
                                            }
                                            return false;
                                        }
                                        bl = this.mListener.onScroll(this.mCurrentDownEvent, motionEvent, f3, f4);
                                        this.mLastFocusX = f;
                                        this.mLastFocusY = f2;
                                        return bl;
                                    }
                                    this.mStillDown = false;
                                    motionEvent3 = MotionEvent.obtain((MotionEvent)motionEvent);
                                    if (!this.mIsDoubleTapping) break block39;
                                    bl = false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                                    break block40;
                                }
                                if (!this.mInLongPress) break block41;
                                this.mHandler.removeMessages(3);
                                this.mInLongPress = false;
                                bl = bl2;
                                break block40;
                            }
                            if (!this.mAlwaysInTapRegion) break block42;
                            bl = bl2 = this.mListener.onSingleTapUp(motionEvent);
                            if (this.mDeferConfirmSingleTap) {
                                GestureDetector.OnDoubleTapListener onDoubleTapListener = this.mDoubleTapListener;
                                bl = bl2;
                                if (onDoubleTapListener != null) {
                                    onDoubleTapListener.onSingleTapConfirmed(motionEvent);
                                    bl = bl2;
                                }
                            }
                            break block40;
                        }
                        VelocityTracker velocityTracker = this.mVelocityTracker;
                        n2 = motionEvent.getPointerId(0);
                        velocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumFlingVelocity);
                        f2 = velocityTracker.getYVelocity(n2);
                        f = velocityTracker.getXVelocity(n2);
                        if (Math.abs(f2) > (float)this.mMinimumFlingVelocity) break block43;
                        bl = bl2;
                        if (Math.abs(f) <= (float)this.mMinimumFlingVelocity) break block40;
                    }
                    bl = this.mListener.onFling(this.mCurrentDownEvent, motionEvent, f, f2);
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
                return bl;
            }
            n2 = n;
            if (this.mDoubleTapListener != null) {
                MotionEvent motionEvent4;
                bl = this.mHandler.hasMessages(3);
                if (bl) {
                    this.mHandler.removeMessages(3);
                }
                if ((motionEvent2 = this.mCurrentDownEvent) != null && (motionEvent4 = this.mPreviousUpEvent) != null && bl && this.isConsideredDoubleTap(motionEvent2, motionEvent4, motionEvent)) {
                    this.mIsDoubleTapping = true;
                    n2 = this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | false | this.mDoubleTapListener.onDoubleTapEvent(motionEvent);
                } else {
                    this.mHandler.sendEmptyMessageDelayed(3, (long)DOUBLE_TAP_TIMEOUT);
                    n2 = n;
                }
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
            return (boolean)(n2 | this.mListener.onDown(motionEvent));
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

            /*
             * Enabled force condition propagation
             * Lifted jumps to return sites
             */
            public void handleMessage(Message message) {
                int n = message.what;
                if (n != 1) {
                    if (n != 2) {
                        if (n == 3) {
                            if (this.this$0.mDoubleTapListener == null) return;
                            if (!this.this$0.mStillDown) {
                                this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
                                return;
                            }
                            this.this$0.mDeferConfirmSingleTap = true;
                            return;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown message ");
                        stringBuilder.append((Object)message);
                        throw new RuntimeException(stringBuilder.toString());
                    }
                    this.this$0.dispatchLongPress();
                    return;
                }
                this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
            }
        }

    }

    static class GestureDetectorCompatImplJellybeanMr2
    implements GestureDetectorCompatImpl {
        private final GestureDetector mDetector;

        GestureDetectorCompatImplJellybeanMr2(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
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

