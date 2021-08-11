/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityManager
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Interpolator
 *  android.widget.FrameLayout
 */
package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SnackbarManager;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.design.widget.ThemeUtils;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>> {
    static final int ANIMATION_DURATION = 250;
    static final int ANIMATION_FADE_DURATION = 180;
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    static final int MSG_DISMISS = 1;
    static final int MSG_SHOW = 0;
    private static final boolean USE_OFFSET_API;
    static final Handler sHandler;
    private final AccessibilityManager mAccessibilityManager;
    private List<BaseCallback<B>> mCallbacks;
    private final ContentViewCallback mContentViewCallback;
    private final Context mContext;
    private int mDuration;
    final SnackbarManager.Callback mManagerCallback;
    private final ViewGroup mTargetParent;
    final SnackbarBaseLayout mView;

    static {
        boolean bl = Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 19;
        USE_OFFSET_API = bl;
        sHandler = new Handler(Looper.getMainLooper(), new Handler.Callback(){

            public boolean handleMessage(Message message) {
                switch (message.what) {
                    default: {
                        return false;
                    }
                    case 1: {
                        ((BaseTransientBottomBar)message.obj).hideView(message.arg1);
                        return true;
                    }
                    case 0: 
                }
                ((BaseTransientBottomBar)message.obj).showView();
                return true;
            }
        });
    }

    protected BaseTransientBottomBar(@NonNull ViewGroup viewGroup, @NonNull View view, @NonNull ContentViewCallback contentViewCallback) {
        this.mManagerCallback = new SnackbarManager.Callback(){

            @Override
            public void dismiss(int n) {
                BaseTransientBottomBar.sHandler.sendMessage(BaseTransientBottomBar.sHandler.obtainMessage(1, n, 0, (Object)BaseTransientBottomBar.this));
            }

            @Override
            public void show() {
                BaseTransientBottomBar.sHandler.sendMessage(BaseTransientBottomBar.sHandler.obtainMessage(0, (Object)BaseTransientBottomBar.this));
            }
        };
        if (viewGroup != null) {
            if (view != null) {
                if (contentViewCallback != null) {
                    this.mTargetParent = viewGroup;
                    this.mContentViewCallback = contentViewCallback;
                    this.mContext = viewGroup.getContext();
                    ThemeUtils.checkAppCompatTheme(this.mContext);
                    this.mView = (SnackbarBaseLayout)LayoutInflater.from((Context)this.mContext).inflate(R.layout.design_layout_snackbar, this.mTargetParent, false);
                    this.mView.addView(view);
                    ViewCompat.setAccessibilityLiveRegion((View)this.mView, 1);
                    ViewCompat.setImportantForAccessibility((View)this.mView, 1);
                    ViewCompat.setFitsSystemWindows((View)this.mView, true);
                    ViewCompat.setOnApplyWindowInsetsListener((View)this.mView, new OnApplyWindowInsetsListener(){

                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                            return windowInsetsCompat;
                        }
                    });
                    this.mAccessibilityManager = (AccessibilityManager)this.mContext.getSystemService("accessibility");
                    return;
                }
                throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
            }
            throw new IllegalArgumentException("Transient bottom bar must have non-null content");
        }
        throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
    }

    private void animateViewOut(final int n) {
        if (Build.VERSION.SDK_INT >= 12) {
            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setIntValues(new int[]{0, this.mView.getHeight()});
            valueAnimator.setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            valueAnimator.setDuration(250L);
            valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                public void onAnimationEnd(Animator animator2) {
                    BaseTransientBottomBar.this.onViewHidden(n);
                }

                public void onAnimationStart(Animator animator2) {
                    BaseTransientBottomBar.this.mContentViewCallback.animateContentOut(0, 180);
                }
            });
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
                private int mPreviousAnimatedIntValue;
                {
                    this.mPreviousAnimatedIntValue = 0;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int n = (Integer)valueAnimator.getAnimatedValue();
                    if (USE_OFFSET_API) {
                        ViewCompat.offsetTopAndBottom((View)BaseTransientBottomBar.this.mView, n - this.mPreviousAnimatedIntValue);
                    } else {
                        BaseTransientBottomBar.this.mView.setTranslationY((float)n);
                    }
                    this.mPreviousAnimatedIntValue = n;
                }
            });
            valueAnimator.start();
            return;
        }
        Animation animation = android.view.animation.AnimationUtils.loadAnimation((Context)this.mView.getContext(), (int)R.anim.design_snackbar_out);
        animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration(250L);
        animation.setAnimationListener(new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
                BaseTransientBottomBar.this.onViewHidden(n);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.mView.startAnimation(animation);
    }

    @NonNull
    public B addCallback(@NonNull BaseCallback<B> baseCallback) {
        if (baseCallback == null) {
            return (B)this;
        }
        if (this.mCallbacks == null) {
            this.mCallbacks = new ArrayList<BaseCallback<B>>();
        }
        this.mCallbacks.add(baseCallback);
        return (B)this;
    }

    void animateViewIn() {
        if (Build.VERSION.SDK_INT >= 12) {
            final int n = this.mView.getHeight();
            if (USE_OFFSET_API) {
                ViewCompat.offsetTopAndBottom((View)this.mView, n);
            } else {
                this.mView.setTranslationY((float)n);
            }
            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setIntValues(new int[]{n, 0});
            valueAnimator.setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            valueAnimator.setDuration(250L);
            valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                public void onAnimationEnd(Animator animator2) {
                    BaseTransientBottomBar.this.onViewShown();
                }

                public void onAnimationStart(Animator animator2) {
                    BaseTransientBottomBar.this.mContentViewCallback.animateContentIn(70, 180);
                }
            });
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
                private int mPreviousAnimatedIntValue;
                {
                    this.mPreviousAnimatedIntValue = n;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int n2 = (Integer)valueAnimator.getAnimatedValue();
                    if (USE_OFFSET_API) {
                        ViewCompat.offsetTopAndBottom((View)BaseTransientBottomBar.this.mView, n2 - this.mPreviousAnimatedIntValue);
                    } else {
                        BaseTransientBottomBar.this.mView.setTranslationY((float)n2);
                    }
                    this.mPreviousAnimatedIntValue = n2;
                }
            });
            valueAnimator.start();
            return;
        }
        Animation animation = android.view.animation.AnimationUtils.loadAnimation((Context)this.mView.getContext(), (int)R.anim.design_snackbar_in);
        animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration(250L);
        animation.setAnimationListener(new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
                BaseTransientBottomBar.this.onViewShown();
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.mView.startAnimation(animation);
    }

    public void dismiss() {
        this.dispatchDismiss(3);
    }

    void dispatchDismiss(int n) {
        SnackbarManager.getInstance().dismiss(this.mManagerCallback, n);
    }

    @NonNull
    public Context getContext() {
        return this.mContext;
    }

    public int getDuration() {
        return this.mDuration;
    }

    @NonNull
    public View getView() {
        return this.mView;
    }

    final void hideView(int n) {
        if (this.shouldAnimate() && this.mView.getVisibility() == 0) {
            this.animateViewOut(n);
            return;
        }
        this.onViewHidden(n);
    }

    public boolean isShown() {
        return SnackbarManager.getInstance().isCurrent(this.mManagerCallback);
    }

    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(this.mManagerCallback);
    }

    void onViewHidden(int n) {
        SnackbarManager.getInstance().onDismissed(this.mManagerCallback);
        ViewParent viewParent = this.mCallbacks;
        if (viewParent != null) {
            for (int i = viewParent.size() - 1; i >= 0; --i) {
                this.mCallbacks.get(i).onDismissed(this, n);
            }
        }
        if (Build.VERSION.SDK_INT < 11) {
            this.mView.setVisibility(8);
        }
        if ((viewParent = this.mView.getParent()) instanceof ViewGroup) {
            ((ViewGroup)viewParent).removeView((View)this.mView);
            return;
        }
    }

    void onViewShown() {
        SnackbarManager.getInstance().onShown(this.mManagerCallback);
        List<BaseCallback<B>> list = this.mCallbacks;
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; --i) {
                this.mCallbacks.get(i).onShown(this);
            }
            return;
        }
    }

    @NonNull
    public B removeCallback(@NonNull BaseCallback<B> baseCallback) {
        if (baseCallback == null) {
            return (B)this;
        }
        List<BaseCallback<B>> list = this.mCallbacks;
        if (list == null) {
            return (B)this;
        }
        list.remove(baseCallback);
        return (B)this;
    }

    @NonNull
    public B setDuration(int n) {
        this.mDuration = n;
        return (B)this;
    }

    boolean shouldAnimate() {
        return this.mAccessibilityManager.isEnabled() ^ true;
    }

    public void show() {
        SnackbarManager.getInstance().show(this.mDuration, this.mManagerCallback);
    }

    final void showView() {
        if (this.mView.getParent() == null) {
            Object object = this.mView.getLayoutParams();
            if (object instanceof CoordinatorLayout.LayoutParams) {
                object = (CoordinatorLayout.LayoutParams)((Object)object);
                Behavior behavior = new Behavior();
                behavior.setStartAlphaSwipeDistance(0.1f);
                behavior.setEndAlphaSwipeDistance(0.6f);
                behavior.setSwipeDirection(0);
                behavior.setListener(new SwipeDismissBehavior.OnDismissListener(){

                    @Override
                    public void onDismiss(View view) {
                        view.setVisibility(8);
                        BaseTransientBottomBar.this.dispatchDismiss(0);
                    }

                    @Override
                    public void onDragStateChanged(int n) {
                        switch (n) {
                            default: {
                                return;
                            }
                            case 1: 
                            case 2: {
                                SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.mManagerCallback);
                                return;
                            }
                            case 0: 
                        }
                        SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.mManagerCallback);
                    }
                });
                object.setBehavior(behavior);
                object.insetEdge = 80;
            }
            this.mTargetParent.addView((View)this.mView);
        }
        this.mView.setOnAttachStateChangeListener(new OnAttachStateChangeListener(){

            @Override
            public void onViewAttachedToWindow(View view) {
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                if (BaseTransientBottomBar.this.isShownOrQueued()) {
                    BaseTransientBottomBar.sHandler.post(new Runnable(){

                        @Override
                        public void run() {
                            BaseTransientBottomBar.this.onViewHidden(3);
                        }
                    });
                    return;
                }
            }

        });
        if (ViewCompat.isLaidOut((View)this.mView)) {
            if (this.shouldAnimate()) {
                this.animateViewIn();
                return;
            }
            this.onViewShown();
            return;
        }
        this.mView.setOnLayoutChangeListener(new OnLayoutChangeListener(){

            @Override
            public void onLayoutChange(View view, int n, int n2, int n3, int n4) {
                BaseTransientBottomBar.this.mView.setOnLayoutChangeListener(null);
                if (BaseTransientBottomBar.this.shouldAnimate()) {
                    BaseTransientBottomBar.this.animateViewIn();
                    return;
                }
                BaseTransientBottomBar.this.onViewShown();
            }
        });
    }

    public static abstract class BaseCallback<B> {
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_TIMEOUT = 2;

        public void onDismissed(B b, int n) {
        }

        public void onShown(B b) {
        }

        @Retention(value=RetentionPolicy.SOURCE)
        @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
        public static @interface DismissEvent {
        }

    }

    final class Behavior
    extends SwipeDismissBehavior<SnackbarBaseLayout> {
        Behavior() {
        }

        @Override
        public boolean canSwipeDismissView(View view) {
            return view instanceof SnackbarBaseLayout;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, SnackbarBaseLayout snackbarBaseLayout, MotionEvent motionEvent) {
            int n = motionEvent.getActionMasked();
            if (n != 3) {
                switch (n) {
                    default: {
                        return super.onInterceptTouchEvent(coordinatorLayout, snackbarBaseLayout, motionEvent);
                    }
                    case 0: {
                        if (!coordinatorLayout.isPointInChildBounds((View)snackbarBaseLayout, (int)motionEvent.getX(), (int)motionEvent.getY())) return super.onInterceptTouchEvent(coordinatorLayout, snackbarBaseLayout, motionEvent);
                        SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.mManagerCallback);
                        return super.onInterceptTouchEvent(coordinatorLayout, snackbarBaseLayout, motionEvent);
                    }
                    case 1: 
                }
            }
            SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.mManagerCallback);
            return super.onInterceptTouchEvent(coordinatorLayout, snackbarBaseLayout, motionEvent);
        }
    }

    public static interface ContentViewCallback {
        public void animateContentIn(int var1, int var2);

        public void animateContentOut(int var1, int var2);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @IntRange(from=1L)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface Duration {
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    static interface OnAttachStateChangeListener {
        public void onViewAttachedToWindow(View var1);

        public void onViewDetachedFromWindow(View var1);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    static interface OnLayoutChangeListener {
        public void onLayoutChange(View var1, int var2, int var3, int var4, int var5);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    static class SnackbarBaseLayout
    extends FrameLayout {
        private OnAttachStateChangeListener mOnAttachStateChangeListener;
        private OnLayoutChangeListener mOnLayoutChangeListener;

        SnackbarBaseLayout(Context context) {
            this(context, null);
        }

        SnackbarBaseLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, R.styleable.SnackbarLayout);
            if (context.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation((View)this, context.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0));
            }
            context.recycle();
            this.setClickable(true);
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            OnAttachStateChangeListener onAttachStateChangeListener = this.mOnAttachStateChangeListener;
            if (onAttachStateChangeListener != null) {
                onAttachStateChangeListener.onViewAttachedToWindow((View)this);
            }
            ViewCompat.requestApplyInsets((View)this);
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            OnAttachStateChangeListener onAttachStateChangeListener = this.mOnAttachStateChangeListener;
            if (onAttachStateChangeListener != null) {
                onAttachStateChangeListener.onViewDetachedFromWindow((View)this);
                return;
            }
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
            super.onLayout(bl, n, n2, n3, n4);
            OnLayoutChangeListener onLayoutChangeListener = this.mOnLayoutChangeListener;
            if (onLayoutChangeListener != null) {
                onLayoutChangeListener.onLayoutChange((View)this, n, n2, n3, n4);
                return;
            }
        }

        void setOnAttachStateChangeListener(OnAttachStateChangeListener onAttachStateChangeListener) {
            this.mOnAttachStateChangeListener = onAttachStateChangeListener;
        }

        void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
            this.mOnLayoutChangeListener = onLayoutChangeListener;
        }
    }

}

