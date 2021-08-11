/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Insets
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.Display
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.WindowInsets
 *  android.view.WindowManager
 *  android.view.accessibility.AccessibilityManager
 *  android.widget.FrameLayout
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 *  com.google.android.material.R$dimen
 *  com.google.android.material.R$layout
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.snackbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.snackbar.SnackbarContentLayout;
import com.google.android.material.snackbar.SnackbarManager;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>> {
    static final int ANIMATION_DURATION = 250;
    static final int ANIMATION_FADE_DURATION = 180;
    private static final int ANIMATION_FADE_IN_DURATION = 150;
    private static final int ANIMATION_FADE_OUT_DURATION = 75;
    public static final int ANIMATION_MODE_FADE = 1;
    public static final int ANIMATION_MODE_SLIDE = 0;
    private static final float ANIMATION_SCALE_FROM_VALUE = 0.8f;
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    static final int MSG_DISMISS = 1;
    static final int MSG_SHOW = 0;
    private static final int[] SNACKBAR_STYLE_ATTR;
    private static final String TAG;
    private static final boolean USE_OFFSET_API;
    static final Handler handler;
    private final AccessibilityManager accessibilityManager;
    private View anchorView;
    private Behavior behavior;
    private final Runnable bottomMarginGestureInsetRunnable;
    private List<BaseCallback<B>> callbacks;
    private final com.google.android.material.snackbar.ContentViewCallback contentViewCallback;
    private final Context context;
    private int duration;
    private int extraBottomMarginAnchorView;
    private int extraBottomMarginGestureInset;
    private int extraBottomMarginWindowInset;
    private int extraLeftMarginWindowInset;
    private int extraRightMarginWindowInset;
    private boolean gestureInsetBottomIgnored;
    SnackbarManager.Callback managerCallback;
    private Rect originalMargins;
    private final ViewGroup targetParent;
    protected final SnackbarBaseLayout view;

    static {
        boolean bl = Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 19;
        USE_OFFSET_API = bl;
        SNACKBAR_STYLE_ATTR = new int[]{R.attr.snackbarStyle};
        TAG = BaseTransientBottomBar.class.getSimpleName();
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback(){

            public boolean handleMessage(Message message) {
                int n = message.what;
                if (n != 0) {
                    if (n != 1) {
                        return false;
                    }
                    ((BaseTransientBottomBar)message.obj).hideView(message.arg1);
                    return true;
                }
                ((BaseTransientBottomBar)message.obj).showView();
                return true;
            }
        });
    }

    protected BaseTransientBottomBar(ViewGroup object, View view, com.google.android.material.snackbar.ContentViewCallback contentViewCallback) {
        this.bottomMarginGestureInsetRunnable = new Runnable(){

            @Override
            public void run() {
                if (BaseTransientBottomBar.this.view != null) {
                    if (BaseTransientBottomBar.this.context == null) {
                        return;
                    }
                    int n = BaseTransientBottomBar.this.getScreenHeight() - BaseTransientBottomBar.this.getViewAbsoluteBottom() + (int)BaseTransientBottomBar.this.view.getTranslationY();
                    if (n >= BaseTransientBottomBar.this.extraBottomMarginGestureInset) {
                        return;
                    }
                    ViewGroup.LayoutParams layoutParams = BaseTransientBottomBar.this.view.getLayoutParams();
                    if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                        Log.w((String)TAG, (String)"Unable to apply gesture inset because layout params are not MarginLayoutParams");
                        return;
                    }
                    layoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
                    layoutParams.bottomMargin += BaseTransientBottomBar.this.extraBottomMarginGestureInset - n;
                    BaseTransientBottomBar.this.view.requestLayout();
                    return;
                }
            }
        };
        this.managerCallback = new SnackbarManager.Callback(){

            @Override
            public void dismiss(int n) {
                BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(1, n, 0, (Object)BaseTransientBottomBar.this));
            }

            @Override
            public void show() {
                BaseTransientBottomBar.handler.sendMessage(BaseTransientBottomBar.handler.obtainMessage(0, (Object)BaseTransientBottomBar.this));
            }
        };
        if (object != null) {
            if (view != null) {
                if (contentViewCallback != null) {
                    this.targetParent = object;
                    this.contentViewCallback = contentViewCallback;
                    object = object.getContext();
                    this.context = object;
                    ThemeEnforcement.checkAppCompatTheme((Context)object);
                    this.view = object = (SnackbarBaseLayout)LayoutInflater.from((Context)this.context).inflate(this.getSnackbarBaseLayoutResId(), this.targetParent, false);
                    if (object.getBackground() == null) {
                        ViewCompat.setBackground((View)this.view, this.createThemedBackground());
                    }
                    if (view instanceof SnackbarContentLayout) {
                        ((SnackbarContentLayout)view).updateActionTextColorAlphaIfNeeded(this.view.getActionTextColorAlpha());
                    }
                    this.view.addView(view);
                    object = this.view.getLayoutParams();
                    if (object instanceof ViewGroup.MarginLayoutParams) {
                        object = (ViewGroup.MarginLayoutParams)object;
                        this.originalMargins = new Rect(object.leftMargin, object.topMargin, object.rightMargin, object.bottomMargin);
                    }
                    ViewCompat.setAccessibilityLiveRegion((View)this.view, 1);
                    ViewCompat.setImportantForAccessibility((View)this.view, 1);
                    ViewCompat.setFitsSystemWindows((View)this.view, true);
                    ViewCompat.setOnApplyWindowInsetsListener((View)this.view, new OnApplyWindowInsetsListener(){

                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                            BaseTransientBottomBar.this.extraBottomMarginWindowInset = windowInsetsCompat.getSystemWindowInsetBottom();
                            BaseTransientBottomBar.this.extraLeftMarginWindowInset = windowInsetsCompat.getSystemWindowInsetLeft();
                            BaseTransientBottomBar.this.extraRightMarginWindowInset = windowInsetsCompat.getSystemWindowInsetRight();
                            BaseTransientBottomBar.this.updateMargins();
                            return windowInsetsCompat;
                        }
                    });
                    ViewCompat.setAccessibilityDelegate((View)this.view, new AccessibilityDelegateCompat(){

                        @Override
                        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                            accessibilityNodeInfoCompat.addAction(1048576);
                            accessibilityNodeInfoCompat.setDismissable(true);
                        }

                        @Override
                        public boolean performAccessibilityAction(View view, int n, Bundle bundle) {
                            if (n == 1048576) {
                                BaseTransientBottomBar.this.dismiss();
                                return true;
                            }
                            return super.performAccessibilityAction(view, n, bundle);
                        }
                    });
                    this.accessibilityManager = (AccessibilityManager)this.context.getSystemService("accessibility");
                    return;
                }
                throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
            }
            throw new IllegalArgumentException("Transient bottom bar must have non-null content");
        }
        throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
    }

    private void animateViewOut(int n) {
        if (this.view.getAnimationMode() == 1) {
            this.startFadeOutAnimation(n);
            return;
        }
        this.startSlideOutAnimation(n);
    }

    private int calculateBottomMarginForAnchorView() {
        int[] arrn = this.anchorView;
        if (arrn == null) {
            return 0;
        }
        int[] arrn2 = new int[2];
        arrn.getLocationOnScreen(arrn2);
        int n = arrn2[1];
        arrn = new int[2];
        this.targetParent.getLocationOnScreen(arrn);
        return arrn[1] + this.targetParent.getHeight() - n;
    }

    private Drawable createThemedBackground() {
        int n = MaterialColors.layer((View)this.view, R.attr.colorSurface, R.attr.colorOnSurface, this.view.getBackgroundOverlayColorAlpha());
        float f = this.view.getResources().getDimension(R.dimen.mtrl_snackbar_background_corner_radius);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(n);
        gradientDrawable.setCornerRadius(f);
        return gradientDrawable;
    }

    private /* varargs */ ValueAnimator getAlphaAnimator(float ... valueAnimator) {
        valueAnimator = ValueAnimator.ofFloat((float[])valueAnimator);
        valueAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BaseTransientBottomBar.this.view.setAlpha(((Float)valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        return valueAnimator;
    }

    private /* varargs */ ValueAnimator getScaleAnimator(float ... valueAnimator) {
        valueAnimator = ValueAnimator.ofFloat((float[])valueAnimator);
        valueAnimator.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                BaseTransientBottomBar.this.view.setScaleX(f);
                BaseTransientBottomBar.this.view.setScaleY(f);
            }
        });
        return valueAnimator;
    }

    private int getScreenHeight() {
        WindowManager windowManager = (WindowManager)this.context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private int getTranslationYBottom() {
        int n = this.view.getHeight();
        ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
        int n2 = n;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            n2 = n + ((ViewGroup.MarginLayoutParams)layoutParams).bottomMargin;
        }
        return n2;
    }

    private int getViewAbsoluteBottom() {
        int[] arrn = new int[2];
        this.view.getLocationOnScreen(arrn);
        return arrn[1] + this.view.getHeight();
    }

    private boolean isSwipeDismissable() {
        ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams && ((CoordinatorLayout.LayoutParams)layoutParams).getBehavior() instanceof SwipeDismissBehavior) {
            return true;
        }
        return false;
    }

    private void setUpBehavior(CoordinatorLayout.LayoutParams layoutParams) {
        SwipeDismissBehavior swipeDismissBehavior = this.behavior;
        if (swipeDismissBehavior == null) {
            swipeDismissBehavior = this.getNewBehavior();
        }
        if (swipeDismissBehavior instanceof Behavior) {
            ((Behavior)swipeDismissBehavior).setBaseTransientBottomBar(this);
        }
        swipeDismissBehavior.setListener(new SwipeDismissBehavior.OnDismissListener(){

            @Override
            public void onDismiss(View view) {
                view.setVisibility(8);
                BaseTransientBottomBar.this.dispatchDismiss(0);
            }

            @Override
            public void onDragStateChanged(int n) {
                if (n != 0) {
                    if (n != 1 && n != 2) {
                        return;
                    }
                    SnackbarManager.getInstance().pauseTimeout(BaseTransientBottomBar.this.managerCallback);
                    return;
                }
                SnackbarManager.getInstance().restoreTimeoutIfPaused(BaseTransientBottomBar.this.managerCallback);
            }
        });
        layoutParams.setBehavior(swipeDismissBehavior);
        if (this.anchorView == null) {
            layoutParams.insetEdge = 80;
        }
    }

    private boolean shouldUpdateGestureInset() {
        if (this.extraBottomMarginGestureInset > 0 && !this.gestureInsetBottomIgnored && this.isSwipeDismissable()) {
            return true;
        }
        return false;
    }

    private void showViewImpl() {
        if (this.shouldAnimate()) {
            this.animateViewIn();
            return;
        }
        this.view.setVisibility(0);
        this.onViewShown();
    }

    private void startFadeInAnimation() {
        ValueAnimator valueAnimator = this.getAlphaAnimator(0.0f, 1.0f);
        ValueAnimator valueAnimator2 = this.getScaleAnimator(0.8f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{valueAnimator, valueAnimator2});
        animatorSet.setDuration(150L);
        animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animator2) {
                BaseTransientBottomBar.this.onViewShown();
            }
        });
        animatorSet.start();
    }

    private void startFadeOutAnimation(final int n) {
        ValueAnimator valueAnimator = this.getAlphaAnimator(1.0f, 0.0f);
        valueAnimator.setDuration(75L);
        valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animator2) {
                BaseTransientBottomBar.this.onViewHidden(n);
            }
        });
        valueAnimator.start();
    }

    private void startSlideInAnimation() {
        final int n = this.getTranslationYBottom();
        if (USE_OFFSET_API) {
            ViewCompat.offsetTopAndBottom((View)this.view, n);
        } else {
            this.view.setTranslationY((float)n);
        }
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(new int[]{n, 0});
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(250L);
        valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animator2) {
                BaseTransientBottomBar.this.onViewShown();
            }

            public void onAnimationStart(Animator animator2) {
                BaseTransientBottomBar.this.contentViewCallback.animateContentIn(70, 180);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            private int previousAnimatedIntValue;
            {
                this.previousAnimatedIntValue = n;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int n2 = (Integer)valueAnimator.getAnimatedValue();
                if (USE_OFFSET_API) {
                    ViewCompat.offsetTopAndBottom((View)BaseTransientBottomBar.this.view, n2 - this.previousAnimatedIntValue);
                } else {
                    BaseTransientBottomBar.this.view.setTranslationY((float)n2);
                }
                this.previousAnimatedIntValue = n2;
            }
        });
        valueAnimator.start();
    }

    private void startSlideOutAnimation(final int n) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(new int[]{0, this.getTranslationYBottom()});
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(250L);
        valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animator2) {
                BaseTransientBottomBar.this.onViewHidden(n);
            }

            public void onAnimationStart(Animator animator2) {
                BaseTransientBottomBar.this.contentViewCallback.animateContentOut(0, 180);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
            private int previousAnimatedIntValue;
            {
                this.previousAnimatedIntValue = 0;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int n = (Integer)valueAnimator.getAnimatedValue();
                if (USE_OFFSET_API) {
                    ViewCompat.offsetTopAndBottom((View)BaseTransientBottomBar.this.view, n - this.previousAnimatedIntValue);
                } else {
                    BaseTransientBottomBar.this.view.setTranslationY((float)n);
                }
                this.previousAnimatedIntValue = n;
            }
        });
        valueAnimator.start();
    }

    private void updateMargins() {
        ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams && this.originalMargins != null) {
            int n = this.anchorView != null ? this.extraBottomMarginAnchorView : this.extraBottomMarginWindowInset;
            layoutParams = (ViewGroup.MarginLayoutParams)layoutParams;
            layoutParams.bottomMargin = this.originalMargins.bottom + n;
            layoutParams.leftMargin = this.originalMargins.left + this.extraLeftMarginWindowInset;
            layoutParams.rightMargin = this.originalMargins.right + this.extraRightMarginWindowInset;
            this.view.requestLayout();
            if (Build.VERSION.SDK_INT >= 29 && this.shouldUpdateGestureInset()) {
                this.view.removeCallbacks(this.bottomMarginGestureInsetRunnable);
                this.view.post(this.bottomMarginGestureInsetRunnable);
            }
            return;
        }
        Log.w((String)TAG, (String)"Unable to update margins because layout params are not MarginLayoutParams");
    }

    public B addCallback(BaseCallback<B> baseCallback) {
        if (baseCallback == null) {
            return (B)this;
        }
        if (this.callbacks == null) {
            this.callbacks = new ArrayList<BaseCallback<B>>();
        }
        this.callbacks.add(baseCallback);
        return (B)this;
    }

    void animateViewIn() {
        this.view.post(new Runnable(){

            @Override
            public void run() {
                if (BaseTransientBottomBar.this.view == null) {
                    return;
                }
                BaseTransientBottomBar.this.view.setVisibility(0);
                if (BaseTransientBottomBar.this.view.getAnimationMode() == 1) {
                    BaseTransientBottomBar.this.startFadeInAnimation();
                    return;
                }
                BaseTransientBottomBar.this.startSlideInAnimation();
            }
        });
    }

    public void dismiss() {
        this.dispatchDismiss(3);
    }

    protected void dispatchDismiss(int n) {
        SnackbarManager.getInstance().dismiss(this.managerCallback, n);
    }

    public View getAnchorView() {
        return this.anchorView;
    }

    public int getAnimationMode() {
        return this.view.getAnimationMode();
    }

    public Behavior getBehavior() {
        return this.behavior;
    }

    public Context getContext() {
        return this.context;
    }

    public int getDuration() {
        return this.duration;
    }

    protected SwipeDismissBehavior<? extends View> getNewBehavior() {
        return new Behavior();
    }

    protected int getSnackbarBaseLayoutResId() {
        if (this.hasSnackbarStyleAttr()) {
            return R.layout.mtrl_layout_snackbar;
        }
        return R.layout.design_layout_snackbar;
    }

    public View getView() {
        return this.view;
    }

    protected boolean hasSnackbarStyleAttr() {
        TypedArray typedArray = this.context.obtainStyledAttributes(SNACKBAR_STYLE_ATTR);
        boolean bl = false;
        int n = typedArray.getResourceId(0, -1);
        typedArray.recycle();
        if (n != -1) {
            bl = true;
        }
        return bl;
    }

    final void hideView(int n) {
        if (this.shouldAnimate() && this.view.getVisibility() == 0) {
            this.animateViewOut(n);
            return;
        }
        this.onViewHidden(n);
    }

    public boolean isGestureInsetBottomIgnored() {
        return this.gestureInsetBottomIgnored;
    }

    public boolean isShown() {
        return SnackbarManager.getInstance().isCurrent(this.managerCallback);
    }

    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(this.managerCallback);
    }

    void onViewHidden(int n) {
        SnackbarManager.getInstance().onDismissed(this.managerCallback);
        ViewParent viewParent = this.callbacks;
        if (viewParent != null) {
            for (int i = viewParent.size() - 1; i >= 0; --i) {
                this.callbacks.get(i).onDismissed(this, n);
            }
        }
        if ((viewParent = this.view.getParent()) instanceof ViewGroup) {
            ((ViewGroup)viewParent).removeView((View)this.view);
        }
    }

    void onViewShown() {
        SnackbarManager.getInstance().onShown(this.managerCallback);
        List<BaseCallback<B>> list = this.callbacks;
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; --i) {
                this.callbacks.get(i).onShown(this);
            }
        }
    }

    public B removeCallback(BaseCallback<B> baseCallback) {
        if (baseCallback == null) {
            return (B)this;
        }
        List<BaseCallback<B>> list = this.callbacks;
        if (list == null) {
            return (B)this;
        }
        list.remove(baseCallback);
        return (B)this;
    }

    public B setAnchorView(int n) {
        Object object = this.targetParent.findViewById(n);
        this.anchorView = object;
        if (object != null) {
            return (B)this;
        }
        object = new StringBuilder();
        object.append("Unable to find anchor view with id: ");
        object.append(n);
        throw new IllegalArgumentException(object.toString());
    }

    public B setAnchorView(View view) {
        this.anchorView = view;
        return (B)this;
    }

    public B setAnimationMode(int n) {
        this.view.setAnimationMode(n);
        return (B)this;
    }

    public B setBehavior(Behavior behavior) {
        this.behavior = behavior;
        return (B)this;
    }

    public B setDuration(int n) {
        this.duration = n;
        return (B)this;
    }

    public B setGestureInsetBottomIgnored(boolean bl) {
        this.gestureInsetBottomIgnored = bl;
        return (B)this;
    }

    boolean shouldAnimate() {
        List list = this.accessibilityManager.getEnabledAccessibilityServiceList(1);
        if (list != null && list.isEmpty()) {
            return true;
        }
        return false;
    }

    public void show() {
        SnackbarManager.getInstance().show(this.getDuration(), this.managerCallback);
    }

    final void showView() {
        this.view.setOnAttachStateChangeListener(new OnAttachStateChangeListener(){

            @Override
            public void onViewAttachedToWindow(View view) {
                if (Build.VERSION.SDK_INT >= 29 && (view = BaseTransientBottomBar.this.view.getRootWindowInsets()) != null) {
                    BaseTransientBottomBar.this.extraBottomMarginGestureInset = view.getMandatorySystemGestureInsets().bottom;
                    BaseTransientBottomBar.this.updateMargins();
                }
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                if (BaseTransientBottomBar.this.isShownOrQueued()) {
                    BaseTransientBottomBar.handler.post(new Runnable(){

                        @Override
                        public void run() {
                            BaseTransientBottomBar.this.onViewHidden(3);
                        }
                    });
                }
            }

        });
        if (this.view.getParent() == null) {
            ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
            if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
                this.setUpBehavior((CoordinatorLayout.LayoutParams)layoutParams);
            }
            this.extraBottomMarginAnchorView = this.calculateBottomMarginForAnchorView();
            this.updateMargins();
            this.view.setVisibility(4);
            this.targetParent.addView((View)this.view);
        }
        if (ViewCompat.isLaidOut((View)this.view)) {
            this.showViewImpl();
            return;
        }
        this.view.setOnLayoutChangeListener(new OnLayoutChangeListener(){

            @Override
            public void onLayoutChange(View view, int n, int n2, int n3, int n4) {
                BaseTransientBottomBar.this.view.setOnLayoutChangeListener(null);
                BaseTransientBottomBar.this.showViewImpl();
            }
        });
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface AnimationMode {
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
        public static @interface DismissEvent {
        }

    }

    public static class Behavior
    extends SwipeDismissBehavior<View> {
        private final BehaviorDelegate delegate;

        public Behavior() {
            this.delegate = new BehaviorDelegate(this);
        }

        private void setBaseTransientBottomBar(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.delegate.setBaseTransientBottomBar(baseTransientBottomBar);
        }

        @Override
        public boolean canSwipeDismissView(View view) {
            return this.delegate.canSwipeDismissView(view);
        }

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            this.delegate.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
            return super.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
        }
    }

    public static class BehaviorDelegate {
        private SnackbarManager.Callback managerCallback;

        public BehaviorDelegate(SwipeDismissBehavior<?> swipeDismissBehavior) {
            swipeDismissBehavior.setStartAlphaSwipeDistance(0.1f);
            swipeDismissBehavior.setEndAlphaSwipeDistance(0.6f);
            swipeDismissBehavior.setSwipeDirection(0);
        }

        public boolean canSwipeDismissView(View view) {
            return view instanceof SnackbarBaseLayout;
        }

        public void onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            int n = motionEvent.getActionMasked();
            if (n != 0) {
                if (n != 1 && n != 3) {
                    return;
                }
                SnackbarManager.getInstance().restoreTimeoutIfPaused(this.managerCallback);
                return;
            }
            if (coordinatorLayout.isPointInChildBounds(view, (int)motionEvent.getX(), (int)motionEvent.getY())) {
                SnackbarManager.getInstance().pauseTimeout(this.managerCallback);
            }
        }

        public void setBaseTransientBottomBar(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.managerCallback = baseTransientBottomBar.managerCallback;
        }
    }

    @Deprecated
    public static interface ContentViewCallback
    extends com.google.android.material.snackbar.ContentViewCallback {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Duration {
    }

    protected static interface OnAttachStateChangeListener {
        public void onViewAttachedToWindow(View var1);

        public void onViewDetachedFromWindow(View var1);
    }

    protected static interface OnLayoutChangeListener {
        public void onLayoutChange(View var1, int var2, int var3, int var4, int var5);
    }

    protected static class SnackbarBaseLayout
    extends FrameLayout {
        private static final View.OnTouchListener consumeAllTouchListener = new View.OnTouchListener(){

            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        };
        private final float actionTextColorAlpha;
        private int animationMode;
        private final float backgroundOverlayColorAlpha;
        private OnAttachStateChangeListener onAttachStateChangeListener;
        private OnLayoutChangeListener onLayoutChangeListener;

        protected SnackbarBaseLayout(Context context) {
            this(context, null);
        }

        protected SnackbarBaseLayout(Context context, AttributeSet attributeSet) {
            super(ThemeEnforcement.createThemedContext(context, attributeSet, 0, 0), attributeSet);
            context = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.SnackbarLayout);
            if (context.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation((View)this, context.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0));
            }
            this.animationMode = context.getInt(R.styleable.SnackbarLayout_animationMode, 0);
            this.backgroundOverlayColorAlpha = context.getFloat(R.styleable.SnackbarLayout_backgroundOverlayColorAlpha, 1.0f);
            this.actionTextColorAlpha = context.getFloat(R.styleable.SnackbarLayout_actionTextColorAlpha, 1.0f);
            context.recycle();
            this.setOnTouchListener(consumeAllTouchListener);
            this.setFocusable(true);
        }

        float getActionTextColorAlpha() {
            return this.actionTextColorAlpha;
        }

        int getAnimationMode() {
            return this.animationMode;
        }

        float getBackgroundOverlayColorAlpha() {
            return this.backgroundOverlayColorAlpha;
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            OnAttachStateChangeListener onAttachStateChangeListener = this.onAttachStateChangeListener;
            if (onAttachStateChangeListener != null) {
                onAttachStateChangeListener.onViewAttachedToWindow((View)this);
            }
            ViewCompat.requestApplyInsets((View)this);
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            OnAttachStateChangeListener onAttachStateChangeListener = this.onAttachStateChangeListener;
            if (onAttachStateChangeListener != null) {
                onAttachStateChangeListener.onViewDetachedFromWindow((View)this);
            }
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
            super.onLayout(bl, n, n2, n3, n4);
            OnLayoutChangeListener onLayoutChangeListener = this.onLayoutChangeListener;
            if (onLayoutChangeListener != null) {
                onLayoutChangeListener.onLayoutChange((View)this, n, n2, n3, n4);
            }
        }

        void setAnimationMode(int n) {
            this.animationMode = n;
        }

        void setOnAttachStateChangeListener(OnAttachStateChangeListener onAttachStateChangeListener) {
            this.onAttachStateChangeListener = onAttachStateChangeListener;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            View.OnTouchListener onTouchListener = onClickListener != null ? null : consumeAllTouchListener;
            this.setOnTouchListener(onTouchListener);
            super.setOnClickListener(onClickListener);
        }

        void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
            this.onLayoutChangeListener = onLayoutChangeListener;
        }

    }

}

