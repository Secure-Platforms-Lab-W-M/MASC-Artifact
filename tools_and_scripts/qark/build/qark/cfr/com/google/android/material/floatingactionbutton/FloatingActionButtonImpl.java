/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.FloatEvaluator
 *  android.animation.ObjectAnimator
 *  android.animation.TimeInterpolator
 *  android.animation.TypeEvaluator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Matrix
 *  android.graphics.Matrix$ScaleToFit
 *  android.graphics.Paint
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.InsetDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Property
 *  android.view.View
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnPreDrawListener
 *  com.google.android.material.R
 *  com.google.android.material.R$animator
 */
package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.Property;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.ImageMatrixProperty;
import com.google.android.material.animation.MatrixEvaluator;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.animation.MotionTiming;
import com.google.android.material.floatingactionbutton.BorderDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.StateListAnimator;
import com.google.android.material.ripple.RippleDrawableCompat;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shadow.ShadowViewDelegate;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import java.util.ArrayList;
import java.util.Iterator;

class FloatingActionButtonImpl {
    static final int ANIM_STATE_HIDING = 1;
    static final int ANIM_STATE_NONE = 0;
    static final int ANIM_STATE_SHOWING = 2;
    static final long ELEVATION_ANIM_DELAY = 100L;
    static final long ELEVATION_ANIM_DURATION = 100L;
    static final TimeInterpolator ELEVATION_ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
    static final int[] EMPTY_STATE_SET;
    static final int[] ENABLED_STATE_SET;
    static final int[] FOCUSED_ENABLED_STATE_SET;
    private static final float HIDE_ICON_SCALE = 0.0f;
    private static final float HIDE_OPACITY = 0.0f;
    private static final float HIDE_SCALE = 0.0f;
    static final int[] HOVERED_ENABLED_STATE_SET;
    static final int[] HOVERED_FOCUSED_ENABLED_STATE_SET;
    static final int[] PRESSED_ENABLED_STATE_SET;
    static final float SHADOW_MULTIPLIER = 1.5f;
    private static final float SHOW_ICON_SCALE = 1.0f;
    private static final float SHOW_OPACITY = 1.0f;
    private static final float SHOW_SCALE = 1.0f;
    private int animState = 0;
    BorderDrawable borderDrawable;
    Drawable contentBackground;
    private Animator currentAnimator;
    private MotionSpec defaultHideMotionSpec;
    private MotionSpec defaultShowMotionSpec;
    float elevation;
    boolean ensureMinTouchTargetSize;
    private ArrayList<Animator.AnimatorListener> hideListeners;
    private MotionSpec hideMotionSpec;
    float hoveredFocusedTranslationZ;
    private float imageMatrixScale = 1.0f;
    private int maxImageSize;
    int minTouchTargetSize;
    private ViewTreeObserver.OnPreDrawListener preDrawListener;
    float pressedTranslationZ;
    Drawable rippleDrawable;
    private float rotation;
    boolean shadowPaddingEnabled = true;
    final ShadowViewDelegate shadowViewDelegate;
    ShapeAppearanceModel shapeAppearance;
    MaterialShapeDrawable shapeDrawable;
    private ArrayList<Animator.AnimatorListener> showListeners;
    private MotionSpec showMotionSpec;
    private final StateListAnimator stateListAnimator;
    private final Matrix tmpMatrix = new Matrix();
    private final Rect tmpRect = new Rect();
    private final RectF tmpRectF1 = new RectF();
    private final RectF tmpRectF2 = new RectF();
    private ArrayList<InternalTransformationCallback> transformationCallbacks;
    final FloatingActionButton view;

    static {
        PRESSED_ENABLED_STATE_SET = new int[]{16842919, 16842910};
        HOVERED_FOCUSED_ENABLED_STATE_SET = new int[]{16843623, 16842908, 16842910};
        FOCUSED_ENABLED_STATE_SET = new int[]{16842908, 16842910};
        HOVERED_ENABLED_STATE_SET = new int[]{16843623, 16842910};
        ENABLED_STATE_SET = new int[]{16842910};
        EMPTY_STATE_SET = new int[0];
    }

    FloatingActionButtonImpl(FloatingActionButton object, ShadowViewDelegate shadowViewDelegate) {
        this.view = object;
        this.shadowViewDelegate = shadowViewDelegate;
        this.stateListAnimator = object = new StateListAnimator();
        object.addState(PRESSED_ENABLED_STATE_SET, this.createElevationAnimator(new ElevateToPressedTranslationZAnimation()));
        this.stateListAnimator.addState(HOVERED_FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, this.createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(HOVERED_ENABLED_STATE_SET, this.createElevationAnimator(new ElevateToHoveredFocusedTranslationZAnimation()));
        this.stateListAnimator.addState(ENABLED_STATE_SET, this.createElevationAnimator(new ResetElevationAnimation()));
        this.stateListAnimator.addState(EMPTY_STATE_SET, this.createElevationAnimator(new DisabledElevationAnimation()));
        this.rotation = this.view.getRotation();
    }

    private void calculateImageMatrixFromScale(float f, Matrix matrix) {
        matrix.reset();
        Drawable drawable2 = this.view.getDrawable();
        if (drawable2 != null && this.maxImageSize != 0) {
            RectF rectF = this.tmpRectF1;
            RectF rectF2 = this.tmpRectF2;
            rectF.set(0.0f, 0.0f, (float)drawable2.getIntrinsicWidth(), (float)drawable2.getIntrinsicHeight());
            int n = this.maxImageSize;
            rectF2.set(0.0f, 0.0f, (float)n, (float)n);
            matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
            n = this.maxImageSize;
            matrix.postScale(f, f, (float)n / 2.0f, (float)n / 2.0f);
        }
    }

    private AnimatorSet createAnimator(MotionSpec motionSpec, float f, float f2, float f3) {
        ArrayList<Animator> arrayList = new ArrayList<Animator>();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat((Object)this.view, (Property)View.ALPHA, (float[])new float[]{f});
        motionSpec.getTiming("opacity").apply((Animator)objectAnimator);
        arrayList.add((Animator)objectAnimator);
        objectAnimator = ObjectAnimator.ofFloat((Object)this.view, (Property)View.SCALE_X, (float[])new float[]{f2});
        motionSpec.getTiming("scale").apply((Animator)objectAnimator);
        this.workAroundOreoBug(objectAnimator);
        arrayList.add((Animator)objectAnimator);
        objectAnimator = ObjectAnimator.ofFloat((Object)this.view, (Property)View.SCALE_Y, (float[])new float[]{f2});
        motionSpec.getTiming("scale").apply((Animator)objectAnimator);
        this.workAroundOreoBug(objectAnimator);
        arrayList.add((Animator)objectAnimator);
        this.calculateImageMatrixFromScale(f3, this.tmpMatrix);
        objectAnimator = ObjectAnimator.ofObject((Object)this.view, (Property)new ImageMatrixProperty(), (TypeEvaluator)new MatrixEvaluator(){

            @Override
            public Matrix evaluate(float f, Matrix matrix, Matrix matrix2) {
                FloatingActionButtonImpl.this.imageMatrixScale = f;
                return super.evaluate(f, matrix, matrix2);
            }
        }, (Object[])new Matrix[]{new Matrix(this.tmpMatrix)});
        motionSpec.getTiming("iconScale").apply((Animator)objectAnimator);
        arrayList.add((Animator)objectAnimator);
        motionSpec = new AnimatorSet();
        AnimatorSetCompat.playTogether((AnimatorSet)motionSpec, arrayList);
        return motionSpec;
    }

    private ValueAnimator createElevationAnimator(ShadowAnimatorImpl shadowAnimatorImpl) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(ELEVATION_ANIM_INTERPOLATOR);
        valueAnimator.setDuration(100L);
        valueAnimator.addListener((Animator.AnimatorListener)shadowAnimatorImpl);
        valueAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)shadowAnimatorImpl);
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        return valueAnimator;
    }

    private MotionSpec getDefaultHideMotionSpec() {
        if (this.defaultHideMotionSpec == null) {
            this.defaultHideMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_hide_motion_spec);
        }
        return Preconditions.checkNotNull(this.defaultHideMotionSpec);
    }

    private MotionSpec getDefaultShowMotionSpec() {
        if (this.defaultShowMotionSpec == null) {
            this.defaultShowMotionSpec = MotionSpec.createFromResource(this.view.getContext(), R.animator.design_fab_show_motion_spec);
        }
        return Preconditions.checkNotNull(this.defaultShowMotionSpec);
    }

    private ViewTreeObserver.OnPreDrawListener getOrCreatePreDrawListener() {
        if (this.preDrawListener == null) {
            this.preDrawListener = new ViewTreeObserver.OnPreDrawListener(){

                public boolean onPreDraw() {
                    FloatingActionButtonImpl.this.onPreDraw();
                    return true;
                }
            };
        }
        return this.preDrawListener;
    }

    private boolean shouldAnimateVisibilityChange() {
        if (ViewCompat.isLaidOut((View)this.view) && !this.view.isInEditMode()) {
            return true;
        }
        return false;
    }

    private void workAroundOreoBug(ObjectAnimator objectAnimator) {
        if (Build.VERSION.SDK_INT != 26) {
            return;
        }
        objectAnimator.setEvaluator((TypeEvaluator)new TypeEvaluator<Float>(){
            FloatEvaluator floatEvaluator;
            {
                this.floatEvaluator = new FloatEvaluator();
            }

            public Float evaluate(float f, Float f2, Float f3) {
                if ((f = this.floatEvaluator.evaluate(f, (Number)f2, (Number)f3).floatValue()) < 0.1f) {
                    f = 0.0f;
                }
                return Float.valueOf(f);
            }
        });
    }

    public void addOnHideAnimationListener(Animator.AnimatorListener animatorListener) {
        if (this.hideListeners == null) {
            this.hideListeners = new ArrayList();
        }
        this.hideListeners.add(animatorListener);
    }

    void addOnShowAnimationListener(Animator.AnimatorListener animatorListener) {
        if (this.showListeners == null) {
            this.showListeners = new ArrayList();
        }
        this.showListeners.add(animatorListener);
    }

    void addTransformationCallback(InternalTransformationCallback internalTransformationCallback) {
        if (this.transformationCallbacks == null) {
            this.transformationCallbacks = new ArrayList();
        }
        this.transformationCallbacks.add(internalTransformationCallback);
    }

    MaterialShapeDrawable createShapeDrawable() {
        return new MaterialShapeDrawable(Preconditions.checkNotNull(this.shapeAppearance));
    }

    final Drawable getContentBackground() {
        return this.contentBackground;
    }

    float getElevation() {
        return this.elevation;
    }

    boolean getEnsureMinTouchTargetSize() {
        return this.ensureMinTouchTargetSize;
    }

    final MotionSpec getHideMotionSpec() {
        return this.hideMotionSpec;
    }

    float getHoveredFocusedTranslationZ() {
        return this.hoveredFocusedTranslationZ;
    }

    void getPadding(Rect rect) {
        int n = this.ensureMinTouchTargetSize ? (this.minTouchTargetSize - this.view.getSizeDimension()) / 2 : 0;
        float f = this.shadowPaddingEnabled ? this.getElevation() + this.pressedTranslationZ : 0.0f;
        int n2 = Math.max(n, (int)Math.ceil(f));
        n = Math.max(n, (int)Math.ceil(1.5f * f));
        rect.set(n2, n, n2, n);
    }

    float getPressedTranslationZ() {
        return this.pressedTranslationZ;
    }

    final ShapeAppearanceModel getShapeAppearance() {
        return this.shapeAppearance;
    }

    final MotionSpec getShowMotionSpec() {
        return this.showMotionSpec;
    }

    void hide(InternalVisibilityChangedListener iterator, final boolean bl) {
        if (this.isOrWillBeHidden()) {
            return;
        }
        Object object = this.currentAnimator;
        if (object != null) {
            object.cancel();
        }
        if (this.shouldAnimateVisibilityChange()) {
            object = this.hideMotionSpec;
            if (object == null) {
                object = this.getDefaultHideMotionSpec();
            }
            object = this.createAnimator((MotionSpec)object, 0.0f, 0.0f, 0.0f);
            object.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter((InternalVisibilityChangedListener)((Object)iterator)){
                private boolean cancelled;
                final /* synthetic */ InternalVisibilityChangedListener val$listener;
                {
                    this.val$listener = internalVisibilityChangedListener;
                }

                public void onAnimationCancel(Animator animator) {
                    this.cancelled = true;
                }

                public void onAnimationEnd(Animator object) {
                    FloatingActionButtonImpl.this.animState = 0;
                    FloatingActionButtonImpl.this.currentAnimator = null;
                    if (!this.cancelled) {
                        object = FloatingActionButtonImpl.this.view;
                        int n = bl ? 8 : 4;
                        object.internalSetVisibility(n, bl);
                        object = this.val$listener;
                        if (object != null) {
                            object.onHidden();
                        }
                    }
                }

                public void onAnimationStart(Animator animator) {
                    FloatingActionButtonImpl.this.view.internalSetVisibility(0, bl);
                    FloatingActionButtonImpl.this.animState = 1;
                    FloatingActionButtonImpl.this.currentAnimator = animator;
                    this.cancelled = false;
                }
            });
            iterator = this.hideListeners;
            if (iterator != null) {
                iterator = iterator.iterator();
                while (iterator.hasNext()) {
                    object.addListener((Animator.AnimatorListener)iterator.next());
                }
            }
            object.start();
            return;
        }
        object = this.view;
        int n = bl ? 8 : 4;
        object.internalSetVisibility(n, bl);
        if (iterator != null) {
            iterator.onHidden();
        }
    }

    void initializeBackgroundDrawable(ColorStateList object, PorterDuff.Mode mode, ColorStateList colorStateList, int n) {
        MaterialShapeDrawable materialShapeDrawable;
        this.shapeDrawable = materialShapeDrawable = this.createShapeDrawable();
        materialShapeDrawable.setTintList((ColorStateList)object);
        if (mode != null) {
            this.shapeDrawable.setTintMode(mode);
        }
        this.shapeDrawable.setShadowColor(-12303292);
        this.shapeDrawable.initializeElevationOverlay(this.view.getContext());
        object = new RippleDrawableCompat(this.shapeDrawable.getShapeAppearanceModel());
        object.setTintList(RippleUtils.sanitizeRippleDrawableColor(colorStateList));
        this.rippleDrawable = object;
        this.contentBackground = new LayerDrawable(new Drawable[]{Preconditions.checkNotNull(this.shapeDrawable), object});
    }

    boolean isOrWillBeHidden() {
        int n = this.view.getVisibility();
        boolean bl = false;
        boolean bl2 = false;
        if (n == 0) {
            if (this.animState == 1) {
                bl2 = true;
            }
            return bl2;
        }
        bl2 = bl;
        if (this.animState != 2) {
            bl2 = true;
        }
        return bl2;
    }

    boolean isOrWillBeShown() {
        int n = this.view.getVisibility();
        boolean bl = false;
        boolean bl2 = false;
        if (n != 0) {
            if (this.animState == 2) {
                bl2 = true;
            }
            return bl2;
        }
        bl2 = bl;
        if (this.animState != 1) {
            bl2 = true;
        }
        return bl2;
    }

    void jumpDrawableToCurrentState() {
        this.stateListAnimator.jumpToCurrentState();
    }

    void onAttachedToWindow() {
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            MaterialShapeUtils.setParentAbsoluteElevation((View)this.view, materialShapeDrawable);
        }
        if (this.requirePreDrawListener()) {
            this.view.getViewTreeObserver().addOnPreDrawListener(this.getOrCreatePreDrawListener());
        }
    }

    void onCompatShadowChanged() {
    }

    void onDetachedFromWindow() {
        ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
        ViewTreeObserver.OnPreDrawListener onPreDrawListener = this.preDrawListener;
        if (onPreDrawListener != null) {
            viewTreeObserver.removeOnPreDrawListener(onPreDrawListener);
            this.preDrawListener = null;
        }
    }

    void onDrawableStateChanged(int[] arrn) {
        this.stateListAnimator.setState(arrn);
    }

    void onElevationsChanged(float f, float f2, float f3) {
        this.updatePadding();
        this.updateShapeElevation(f);
    }

    void onPaddingUpdated(Rect rect) {
        Preconditions.checkNotNull(this.contentBackground, "Didn't initialize content background");
        if (this.shouldAddPadding()) {
            rect = new InsetDrawable(this.contentBackground, rect.left, rect.top, rect.right, rect.bottom);
            this.shadowViewDelegate.setBackgroundDrawable((Drawable)rect);
            return;
        }
        this.shadowViewDelegate.setBackgroundDrawable(this.contentBackground);
    }

    void onPreDraw() {
        float f = this.view.getRotation();
        if (this.rotation != f) {
            this.rotation = f;
            this.updateFromViewRotation();
        }
    }

    void onScaleChanged() {
        ArrayList<InternalTransformationCallback> arrayList = this.transformationCallbacks;
        if (arrayList != null) {
            arrayList = arrayList.iterator();
            while (arrayList.hasNext()) {
                ((InternalTransformationCallback)arrayList.next()).onScaleChanged();
            }
        }
    }

    void onTranslationChanged() {
        ArrayList<InternalTransformationCallback> arrayList = this.transformationCallbacks;
        if (arrayList != null) {
            arrayList = arrayList.iterator();
            while (arrayList.hasNext()) {
                ((InternalTransformationCallback)arrayList.next()).onTranslationChanged();
            }
        }
    }

    public void removeOnHideAnimationListener(Animator.AnimatorListener animatorListener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.hideListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove((Object)animatorListener);
    }

    void removeOnShowAnimationListener(Animator.AnimatorListener animatorListener) {
        ArrayList<Animator.AnimatorListener> arrayList = this.showListeners;
        if (arrayList == null) {
            return;
        }
        arrayList.remove((Object)animatorListener);
    }

    void removeTransformationCallback(InternalTransformationCallback internalTransformationCallback) {
        ArrayList<InternalTransformationCallback> arrayList = this.transformationCallbacks;
        if (arrayList == null) {
            return;
        }
        arrayList.remove(internalTransformationCallback);
    }

    boolean requirePreDrawListener() {
        return true;
    }

    void setBackgroundTintList(ColorStateList colorStateList) {
        Drawable drawable2 = this.shapeDrawable;
        if (drawable2 != null) {
            drawable2.setTintList(colorStateList);
        }
        if ((drawable2 = this.borderDrawable) != null) {
            drawable2.setBorderTint(colorStateList);
        }
    }

    void setBackgroundTintMode(PorterDuff.Mode mode) {
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setTintMode(mode);
        }
    }

    final void setElevation(float f) {
        if (this.elevation != f) {
            this.elevation = f;
            this.onElevationsChanged(f, this.hoveredFocusedTranslationZ, this.pressedTranslationZ);
        }
    }

    void setEnsureMinTouchTargetSize(boolean bl) {
        this.ensureMinTouchTargetSize = bl;
    }

    final void setHideMotionSpec(MotionSpec motionSpec) {
        this.hideMotionSpec = motionSpec;
    }

    final void setHoveredFocusedTranslationZ(float f) {
        if (this.hoveredFocusedTranslationZ != f) {
            this.hoveredFocusedTranslationZ = f;
            this.onElevationsChanged(this.elevation, f, this.pressedTranslationZ);
        }
    }

    final void setImageMatrixScale(float f) {
        this.imageMatrixScale = f;
        Matrix matrix = this.tmpMatrix;
        this.calculateImageMatrixFromScale(f, matrix);
        this.view.setImageMatrix(matrix);
    }

    final void setMaxImageSize(int n) {
        if (this.maxImageSize != n) {
            this.maxImageSize = n;
            this.updateImageMatrixScale();
        }
    }

    void setMinTouchTargetSize(int n) {
        this.minTouchTargetSize = n;
    }

    final void setPressedTranslationZ(float f) {
        if (this.pressedTranslationZ != f) {
            this.pressedTranslationZ = f;
            this.onElevationsChanged(this.elevation, this.hoveredFocusedTranslationZ, f);
        }
    }

    void setRippleColor(ColorStateList colorStateList) {
        Drawable drawable2 = this.rippleDrawable;
        if (drawable2 != null) {
            DrawableCompat.setTintList(drawable2, RippleUtils.sanitizeRippleDrawableColor(colorStateList));
        }
    }

    void setShadowPaddingEnabled(boolean bl) {
        this.shadowPaddingEnabled = bl;
        this.updatePadding();
    }

    final void setShapeAppearance(ShapeAppearanceModel shapeAppearanceModel) {
        this.shapeAppearance = shapeAppearanceModel;
        Drawable drawable2 = this.shapeDrawable;
        if (drawable2 != null) {
            drawable2.setShapeAppearanceModel(shapeAppearanceModel);
        }
        if ((drawable2 = this.rippleDrawable) instanceof Shapeable) {
            ((Shapeable)drawable2).setShapeAppearanceModel(shapeAppearanceModel);
        }
        if ((drawable2 = this.borderDrawable) != null) {
            drawable2.setShapeAppearanceModel(shapeAppearanceModel);
        }
    }

    final void setShowMotionSpec(MotionSpec motionSpec) {
        this.showMotionSpec = motionSpec;
    }

    boolean shouldAddPadding() {
        return true;
    }

    final boolean shouldExpandBoundsForA11y() {
        if (this.ensureMinTouchTargetSize && this.view.getSizeDimension() < this.minTouchTargetSize) {
            return false;
        }
        return true;
    }

    void show(InternalVisibilityChangedListener iterator, final boolean bl) {
        if (this.isOrWillBeShown()) {
            return;
        }
        Object object = this.currentAnimator;
        if (object != null) {
            object.cancel();
        }
        if (this.shouldAnimateVisibilityChange()) {
            if (this.view.getVisibility() != 0) {
                this.view.setAlpha(0.0f);
                this.view.setScaleY(0.0f);
                this.view.setScaleX(0.0f);
                this.setImageMatrixScale(0.0f);
            }
            if ((object = this.showMotionSpec) == null) {
                object = this.getDefaultShowMotionSpec();
            }
            object = this.createAnimator((MotionSpec)object, 1.0f, 1.0f, 1.0f);
            object.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter((InternalVisibilityChangedListener)((Object)iterator)){
                final /* synthetic */ InternalVisibilityChangedListener val$listener;
                {
                    this.val$listener = internalVisibilityChangedListener;
                }

                public void onAnimationEnd(Animator object) {
                    FloatingActionButtonImpl.this.animState = 0;
                    FloatingActionButtonImpl.this.currentAnimator = null;
                    object = this.val$listener;
                    if (object != null) {
                        object.onShown();
                    }
                }

                public void onAnimationStart(Animator animator) {
                    FloatingActionButtonImpl.this.view.internalSetVisibility(0, bl);
                    FloatingActionButtonImpl.this.animState = 2;
                    FloatingActionButtonImpl.this.currentAnimator = animator;
                }
            });
            iterator = this.showListeners;
            if (iterator != null) {
                iterator = iterator.iterator();
                while (iterator.hasNext()) {
                    object.addListener((Animator.AnimatorListener)iterator.next());
                }
            }
            object.start();
            return;
        }
        this.view.internalSetVisibility(0, bl);
        this.view.setAlpha(1.0f);
        this.view.setScaleY(1.0f);
        this.view.setScaleX(1.0f);
        this.setImageMatrixScale(1.0f);
        if (iterator != null) {
            iterator.onShown();
        }
    }

    void updateFromViewRotation() {
        MaterialShapeDrawable materialShapeDrawable;
        if (Build.VERSION.SDK_INT == 19) {
            if (this.rotation % 90.0f != 0.0f) {
                if (this.view.getLayerType() != 1) {
                    this.view.setLayerType(1, null);
                }
            } else if (this.view.getLayerType() != 0) {
                this.view.setLayerType(0, null);
            }
        }
        if ((materialShapeDrawable = this.shapeDrawable) != null) {
            materialShapeDrawable.setShadowCompatRotation((int)this.rotation);
        }
    }

    final void updateImageMatrixScale() {
        this.setImageMatrixScale(this.imageMatrixScale);
    }

    final void updatePadding() {
        Rect rect = this.tmpRect;
        this.getPadding(rect);
        this.onPaddingUpdated(rect);
        this.shadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    void updateShapeElevation(float f) {
        MaterialShapeDrawable materialShapeDrawable = this.shapeDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setElevation(f);
        }
    }

    private class DisabledElevationAnimation
    extends ShadowAnimatorImpl {
        DisabledElevationAnimation() {
            super();
        }

        @Override
        protected float getTargetShadowSize() {
            return 0.0f;
        }
    }

    private class ElevateToHoveredFocusedTranslationZAnimation
    extends ShadowAnimatorImpl {
        ElevateToHoveredFocusedTranslationZAnimation() {
            super();
        }

        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.hoveredFocusedTranslationZ;
        }
    }

    private class ElevateToPressedTranslationZAnimation
    extends ShadowAnimatorImpl {
        ElevateToPressedTranslationZAnimation() {
            super();
        }

        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation + FloatingActionButtonImpl.this.pressedTranslationZ;
        }
    }

    static interface InternalTransformationCallback {
        public void onScaleChanged();

        public void onTranslationChanged();
    }

    static interface InternalVisibilityChangedListener {
        public void onHidden();

        public void onShown();
    }

    private class ResetElevationAnimation
    extends ShadowAnimatorImpl {
        ResetElevationAnimation() {
            super();
        }

        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.elevation;
        }
    }

    private abstract class ShadowAnimatorImpl
    extends AnimatorListenerAdapter
    implements ValueAnimator.AnimatorUpdateListener {
        private float shadowSizeEnd;
        private float shadowSizeStart;
        private boolean validValues;

        private ShadowAnimatorImpl() {
        }

        protected abstract float getTargetShadowSize();

        public void onAnimationEnd(Animator animator) {
            FloatingActionButtonImpl.this.updateShapeElevation((int)this.shadowSizeEnd);
            this.validValues = false;
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float f;
            if (!this.validValues) {
                f = FloatingActionButtonImpl.this.shapeDrawable == null ? 0.0f : FloatingActionButtonImpl.this.shapeDrawable.getElevation();
                this.shadowSizeStart = f;
                this.shadowSizeEnd = this.getTargetShadowSize();
                this.validValues = true;
            }
            FloatingActionButtonImpl floatingActionButtonImpl = FloatingActionButtonImpl.this;
            f = this.shadowSizeStart;
            floatingActionButtonImpl.updateShapeElevation((int)(f + (this.shadowSizeEnd - f) * valueAnimator.getAnimatedFraction()));
        }
    }

}

