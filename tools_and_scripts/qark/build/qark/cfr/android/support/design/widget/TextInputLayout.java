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
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Paint
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.Typeface
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.graphics.drawable.DrawableContainer
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.text.Editable
 *  android.text.TextUtils
 *  android.text.TextWatcher
 *  android.text.method.PasswordTransformationMethod
 *  android.text.method.TransformationMethod
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewPropertyAnimator
 *  android.view.ViewStructure
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.widget.EditText
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 */
package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.annotation.VisibleForTesting;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.CheckableImageButton;
import android.support.design.widget.CollapsingTextHelper;
import android.support.design.widget.DrawableUtils;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.ThemeUtils;
import android.support.design.widget.ViewGroupUtils;
import android.support.design.widget.ViewUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.Space;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintTypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewStructure;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class TextInputLayout
extends LinearLayout {
    private static final int ANIMATION_DURATION = 200;
    private static final int INVALID_MAX_LENGTH = -1;
    private static final String LOG_TAG = "TextInputLayout";
    private ValueAnimator mAnimator;
    final CollapsingTextHelper mCollapsingTextHelper;
    boolean mCounterEnabled;
    private int mCounterMaxLength;
    private int mCounterOverflowTextAppearance;
    private boolean mCounterOverflowed;
    private int mCounterTextAppearance;
    private TextView mCounterView;
    private ColorStateList mDefaultTextColor;
    EditText mEditText;
    private CharSequence mError;
    private boolean mErrorEnabled;
    private boolean mErrorShown;
    private int mErrorTextAppearance;
    TextView mErrorView;
    private ColorStateList mFocusedTextColor;
    private boolean mHasPasswordToggleTintList;
    private boolean mHasPasswordToggleTintMode;
    private boolean mHasReconstructedEditTextBackground;
    private CharSequence mHint;
    private boolean mHintAnimationEnabled;
    private boolean mHintEnabled;
    private boolean mHintExpanded;
    private boolean mInDrawableStateChanged;
    private LinearLayout mIndicatorArea;
    private int mIndicatorsAdded;
    private final FrameLayout mInputFrame;
    private Drawable mOriginalEditTextEndDrawable;
    private CharSequence mOriginalHint;
    private CharSequence mPasswordToggleContentDesc;
    private Drawable mPasswordToggleDrawable;
    private Drawable mPasswordToggleDummyDrawable;
    private boolean mPasswordToggleEnabled;
    private ColorStateList mPasswordToggleTintList;
    private PorterDuff.Mode mPasswordToggleTintMode;
    private CheckableImageButton mPasswordToggleView;
    private boolean mPasswordToggledVisible;
    private boolean mRestoringSavedState;
    private Paint mTmpPaint;
    private final Rect mTmpRect = new Rect();
    private Typeface mTypeface;

    public TextInputLayout(Context context) {
        this(context, null);
    }

    public TextInputLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TextInputLayout(Context object, AttributeSet attributeSet, int n) {
        super((Context)object, attributeSet);
        this.mCollapsingTextHelper = new CollapsingTextHelper((View)this);
        ThemeUtils.checkAppCompatTheme((Context)object);
        this.setOrientation(1);
        this.setWillNotDraw(false);
        this.setAddStatesFromChildren(true);
        this.mInputFrame = new FrameLayout((Context)object);
        this.mInputFrame.setAddStatesFromChildren(true);
        this.addView((View)this.mInputFrame);
        this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        this.mCollapsingTextHelper.setPositionInterpolator((Interpolator)new AccelerateInterpolator());
        this.mCollapsingTextHelper.setCollapsedTextGravity(8388659);
        object = TintTypedArray.obtainStyledAttributes((Context)object, attributeSet, R.styleable.TextInputLayout, n, R.style.Widget_Design_TextInputLayout);
        this.mHintEnabled = object.getBoolean(R.styleable.TextInputLayout_hintEnabled, true);
        this.setHint(object.getText(R.styleable.TextInputLayout_android_hint));
        this.mHintAnimationEnabled = object.getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
        if (object.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
            attributeSet = object.getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
            this.mFocusedTextColor = attributeSet;
            this.mDefaultTextColor = attributeSet;
        }
        if (object.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1) != -1) {
            this.setHintTextAppearance(object.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0));
        }
        this.mErrorTextAppearance = object.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
        boolean bl = object.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
        boolean bl2 = object.getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
        this.setCounterMaxLength(object.getInt(R.styleable.TextInputLayout_counterMaxLength, -1));
        this.mCounterTextAppearance = object.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
        this.mCounterOverflowTextAppearance = object.getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
        this.mPasswordToggleEnabled = object.getBoolean(R.styleable.TextInputLayout_passwordToggleEnabled, false);
        this.mPasswordToggleDrawable = object.getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable);
        this.mPasswordToggleContentDesc = object.getText(R.styleable.TextInputLayout_passwordToggleContentDescription);
        if (object.hasValue(R.styleable.TextInputLayout_passwordToggleTint)) {
            this.mHasPasswordToggleTintList = true;
            this.mPasswordToggleTintList = object.getColorStateList(R.styleable.TextInputLayout_passwordToggleTint);
        }
        if (object.hasValue(R.styleable.TextInputLayout_passwordToggleTintMode)) {
            this.mHasPasswordToggleTintMode = true;
            this.mPasswordToggleTintMode = ViewUtils.parseTintMode(object.getInt(R.styleable.TextInputLayout_passwordToggleTintMode, -1), null);
        }
        object.recycle();
        this.setErrorEnabled(bl);
        this.setCounterEnabled(bl2);
        this.applyPasswordToggleTint();
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        ViewCompat.setAccessibilityDelegate((View)this, new TextInputAccessibilityDelegate());
    }

    private void addIndicator(TextView textView, int n) {
        if (this.mIndicatorArea == null) {
            this.mIndicatorArea = new LinearLayout(this.getContext());
            this.mIndicatorArea.setOrientation(0);
            this.addView((View)this.mIndicatorArea, -1, -2);
            Space space = new Space(this.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0, 1.0f);
            this.mIndicatorArea.addView((View)space, (ViewGroup.LayoutParams)layoutParams);
            if (this.mEditText != null) {
                this.adjustIndicatorPadding();
            }
        }
        this.mIndicatorArea.setVisibility(0);
        this.mIndicatorArea.addView((View)textView, n);
        ++this.mIndicatorsAdded;
    }

    private void adjustIndicatorPadding() {
        ViewCompat.setPaddingRelative((View)this.mIndicatorArea, ViewCompat.getPaddingStart((View)this.mEditText), 0, ViewCompat.getPaddingEnd((View)this.mEditText), this.mEditText.getPaddingBottom());
    }

    private void applyPasswordToggleTint() {
        if (this.mPasswordToggleDrawable != null && (this.mHasPasswordToggleTintList || this.mHasPasswordToggleTintMode)) {
            CheckableImageButton checkableImageButton;
            this.mPasswordToggleDrawable = DrawableCompat.wrap(this.mPasswordToggleDrawable).mutate();
            if (this.mHasPasswordToggleTintList) {
                DrawableCompat.setTintList(this.mPasswordToggleDrawable, this.mPasswordToggleTintList);
            }
            if (this.mHasPasswordToggleTintMode) {
                DrawableCompat.setTintMode(this.mPasswordToggleDrawable, this.mPasswordToggleTintMode);
            }
            if ((checkableImageButton = this.mPasswordToggleView) != null) {
                Drawable drawable2;
                if ((checkableImageButton = checkableImageButton.getDrawable()) != (drawable2 = this.mPasswordToggleDrawable)) {
                    this.mPasswordToggleView.setImageDrawable(drawable2);
                    return;
                }
                return;
            }
            return;
        }
    }

    private static boolean arrayContains(int[] arrn, int n) {
        int n2 = arrn.length;
        for (int i = 0; i < n2; ++i) {
            if (arrn[i] != n) continue;
            return true;
        }
        return false;
    }

    private void collapseHint(boolean bl) {
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (bl && this.mHintAnimationEnabled) {
            this.animateToExpansionFraction(1.0f);
        } else {
            this.mCollapsingTextHelper.setExpansionFraction(1.0f);
        }
        this.mHintExpanded = false;
    }

    private void ensureBackgroundDrawableStateWorkaround() {
        int n = Build.VERSION.SDK_INT;
        if (n != 21 && n != 22) {
            return;
        }
        Drawable drawable2 = this.mEditText.getBackground();
        if (drawable2 == null) {
            return;
        }
        if (!this.mHasReconstructedEditTextBackground) {
            Drawable drawable3 = drawable2.getConstantState().newDrawable();
            if (drawable2 instanceof DrawableContainer) {
                this.mHasReconstructedEditTextBackground = DrawableUtils.setContainerConstantState((DrawableContainer)drawable2, drawable3.getConstantState());
            }
            if (!this.mHasReconstructedEditTextBackground) {
                ViewCompat.setBackground((View)this.mEditText, drawable3);
                this.mHasReconstructedEditTextBackground = true;
                return;
            }
            return;
        }
    }

    private void expandHint(boolean bl) {
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (bl && this.mHintAnimationEnabled) {
            this.animateToExpansionFraction(0.0f);
        } else {
            this.mCollapsingTextHelper.setExpansionFraction(0.0f);
        }
        this.mHintExpanded = true;
    }

    private boolean hasPasswordTransformation() {
        EditText editText = this.mEditText;
        if (editText != null && editText.getTransformationMethod() instanceof PasswordTransformationMethod) {
            return true;
        }
        return false;
    }

    private void passwordVisibilityToggleRequested(boolean bl) {
        if (this.mPasswordToggleEnabled) {
            int n = this.mEditText.getSelectionEnd();
            if (this.hasPasswordTransformation()) {
                this.mEditText.setTransformationMethod(null);
                this.mPasswordToggledVisible = true;
            } else {
                this.mEditText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
                this.mPasswordToggledVisible = false;
            }
            this.mPasswordToggleView.setChecked(this.mPasswordToggledVisible);
            if (bl) {
                this.mPasswordToggleView.jumpDrawablesToCurrentState();
            }
            this.mEditText.setSelection(n);
            return;
        }
    }

    private static void recursiveSetEnabled(ViewGroup viewGroup, boolean bl) {
        int n = viewGroup.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(bl);
            if (!(view instanceof ViewGroup)) continue;
            TextInputLayout.recursiveSetEnabled((ViewGroup)view, bl);
        }
    }

    private void removeIndicator(TextView textView) {
        LinearLayout linearLayout = this.mIndicatorArea;
        if (linearLayout != null) {
            int n;
            linearLayout.removeView((View)textView);
            this.mIndicatorsAdded = n = this.mIndicatorsAdded - 1;
            if (n == 0) {
                this.mIndicatorArea.setVisibility(8);
                return;
            }
            return;
        }
    }

    private void setEditText(EditText editText) {
        if (this.mEditText == null) {
            if (!(editText instanceof TextInputEditText)) {
                Log.i((String)"TextInputLayout", (String)"EditText added is not a TextInputEditText. Please switch to using that class instead.");
            }
            this.mEditText = editText;
            if (!this.hasPasswordTransformation()) {
                this.mCollapsingTextHelper.setTypefaces(this.mEditText.getTypeface());
            }
            this.mCollapsingTextHelper.setExpandedTextSize(this.mEditText.getTextSize());
            int n = this.mEditText.getGravity();
            this.mCollapsingTextHelper.setCollapsedTextGravity(n & -113 | 48);
            this.mCollapsingTextHelper.setExpandedTextGravity(n);
            this.mEditText.addTextChangedListener(new TextWatcher(){

                public void afterTextChanged(Editable editable) {
                    TextInputLayout textInputLayout = TextInputLayout.this;
                    textInputLayout.updateLabelState(textInputLayout.mRestoringSavedState ^ true);
                    if (TextInputLayout.this.mCounterEnabled) {
                        TextInputLayout.this.updateCounter(editable.length());
                        return;
                    }
                }

                public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3) {
                }

                public void onTextChanged(CharSequence charSequence, int n, int n2, int n3) {
                }
            });
            if (this.mDefaultTextColor == null) {
                this.mDefaultTextColor = this.mEditText.getHintTextColors();
            }
            if (this.mHintEnabled && TextUtils.isEmpty((CharSequence)this.mHint)) {
                this.mOriginalHint = this.mEditText.getHint();
                this.setHint(this.mOriginalHint);
                this.mEditText.setHint(null);
            }
            if (this.mCounterView != null) {
                this.updateCounter(this.mEditText.getText().length());
            }
            if (this.mIndicatorArea != null) {
                this.adjustIndicatorPadding();
            }
            this.updatePasswordToggleView();
            this.updateLabelState(false, true);
            return;
        }
        throw new IllegalArgumentException("We already have an EditText, can only have one");
    }

    private void setError(final @Nullable CharSequence charSequence, boolean bl) {
        this.mError = charSequence;
        if (!this.mErrorEnabled) {
            if (TextUtils.isEmpty((CharSequence)charSequence)) {
                return;
            }
            this.setErrorEnabled(true);
        }
        this.mErrorShown = TextUtils.isEmpty((CharSequence)charSequence) ^ true;
        this.mErrorView.animate().cancel();
        if (this.mErrorShown) {
            this.mErrorView.setText(charSequence);
            this.mErrorView.setVisibility(0);
            if (bl) {
                if (this.mErrorView.getAlpha() == 1.0f) {
                    this.mErrorView.setAlpha(0.0f);
                }
                this.mErrorView.animate().alpha(1.0f).setDuration(200L).setInterpolator((TimeInterpolator)AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                    public void onAnimationStart(Animator animator2) {
                        TextInputLayout.this.mErrorView.setVisibility(0);
                    }
                }).start();
            } else {
                this.mErrorView.setAlpha(1.0f);
            }
        } else if (this.mErrorView.getVisibility() == 0) {
            if (bl) {
                this.mErrorView.animate().alpha(0.0f).setDuration(200L).setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                    public void onAnimationEnd(Animator animator2) {
                        TextInputLayout.this.mErrorView.setText(charSequence);
                        TextInputLayout.this.mErrorView.setVisibility(4);
                    }
                }).start();
            } else {
                this.mErrorView.setText(charSequence);
                this.mErrorView.setVisibility(4);
            }
        }
        this.updateEditTextBackground();
        this.updateLabelState(bl);
    }

    private void setHintInternal(CharSequence charSequence) {
        this.mHint = charSequence;
        this.mCollapsingTextHelper.setText(charSequence);
    }

    private boolean shouldShowPasswordIcon() {
        if (this.mPasswordToggleEnabled && (this.hasPasswordTransformation() || this.mPasswordToggledVisible)) {
            return true;
        }
        return false;
    }

    private void updateEditTextBackground() {
        TextView textView;
        EditText editText = this.mEditText;
        if (editText == null) {
            return;
        }
        if ((editText = editText.getBackground()) == null) {
            return;
        }
        this.ensureBackgroundDrawableStateWorkaround();
        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable((Drawable)editText)) {
            editText = editText.mutate();
        }
        if (this.mErrorShown && (textView = this.mErrorView) != null) {
            editText.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(textView.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
            return;
        }
        if (this.mCounterOverflowed && (textView = this.mCounterView) != null) {
            editText.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(textView.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
            return;
        }
        DrawableCompat.clearColorFilter((Drawable)editText);
        this.mEditText.refreshDrawableState();
    }

    private void updateInputLayoutMargins() {
        int n;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)this.mInputFrame.getLayoutParams();
        if (this.mHintEnabled) {
            if (this.mTmpPaint == null) {
                this.mTmpPaint = new Paint();
            }
            this.mTmpPaint.setTypeface(this.mCollapsingTextHelper.getCollapsedTypeface());
            this.mTmpPaint.setTextSize(this.mCollapsingTextHelper.getCollapsedTextSize());
            n = (int)(- this.mTmpPaint.ascent());
        } else {
            n = 0;
        }
        if (n != layoutParams.topMargin) {
            layoutParams.topMargin = n;
            this.mInputFrame.requestLayout();
            return;
        }
    }

    private void updatePasswordToggleView() {
        if (this.mEditText == null) {
            return;
        }
        if (this.shouldShowPasswordIcon()) {
            Drawable[] arrdrawable;
            if (this.mPasswordToggleView == null) {
                this.mPasswordToggleView = (CheckableImageButton)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.design_text_input_password_icon, (ViewGroup)this.mInputFrame, false);
                this.mPasswordToggleView.setImageDrawable(this.mPasswordToggleDrawable);
                this.mPasswordToggleView.setContentDescription(this.mPasswordToggleContentDesc);
                this.mInputFrame.addView((View)this.mPasswordToggleView);
                this.mPasswordToggleView.setOnClickListener(new View.OnClickListener(){

                    public void onClick(View view) {
                        TextInputLayout.this.passwordVisibilityToggleRequested(false);
                    }
                });
            }
            if ((arrdrawable = this.mEditText) != null && ViewCompat.getMinimumHeight((View)arrdrawable) <= 0) {
                this.mEditText.setMinimumHeight(ViewCompat.getMinimumHeight((View)this.mPasswordToggleView));
            }
            this.mPasswordToggleView.setVisibility(0);
            this.mPasswordToggleView.setChecked(this.mPasswordToggledVisible);
            if (this.mPasswordToggleDummyDrawable == null) {
                this.mPasswordToggleDummyDrawable = new ColorDrawable();
            }
            this.mPasswordToggleDummyDrawable.setBounds(0, 0, this.mPasswordToggleView.getMeasuredWidth(), 1);
            arrdrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.mEditText);
            if (arrdrawable[2] != this.mPasswordToggleDummyDrawable) {
                this.mOriginalEditTextEndDrawable = arrdrawable[2];
            }
            TextViewCompat.setCompoundDrawablesRelative((TextView)this.mEditText, arrdrawable[0], arrdrawable[1], this.mPasswordToggleDummyDrawable, arrdrawable[3]);
            this.mPasswordToggleView.setPadding(this.mEditText.getPaddingLeft(), this.mEditText.getPaddingTop(), this.mEditText.getPaddingRight(), this.mEditText.getPaddingBottom());
            return;
        }
        Drawable[] arrdrawable = this.mPasswordToggleView;
        if (arrdrawable != null && arrdrawable.getVisibility() == 0) {
            this.mPasswordToggleView.setVisibility(8);
        }
        if (this.mPasswordToggleDummyDrawable != null) {
            arrdrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.mEditText);
            if (arrdrawable[2] == this.mPasswordToggleDummyDrawable) {
                TextViewCompat.setCompoundDrawablesRelative((TextView)this.mEditText, arrdrawable[0], arrdrawable[1], this.mOriginalEditTextEndDrawable, arrdrawable[3]);
                this.mPasswordToggleDummyDrawable = null;
                return;
            }
            return;
        }
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        if (view instanceof EditText) {
            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(layoutParams);
            layoutParams2.gravity = layoutParams2.gravity & -113 | 16;
            this.mInputFrame.addView(view, (ViewGroup.LayoutParams)layoutParams2);
            this.mInputFrame.setLayoutParams(layoutParams);
            this.updateInputLayoutMargins();
            this.setEditText((EditText)view);
            return;
        }
        super.addView(view, n, layoutParams);
    }

    @VisibleForTesting
    void animateToExpansionFraction(float f) {
        if (this.mCollapsingTextHelper.getExpansionFraction() == f) {
            return;
        }
        if (this.mAnimator == null) {
            this.mAnimator = new ValueAnimator();
            this.mAnimator.setInterpolator((TimeInterpolator)AnimationUtils.LINEAR_INTERPOLATOR);
            this.mAnimator.setDuration(200L);
            this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    TextInputLayout.this.mCollapsingTextHelper.setExpansionFraction(((Float)valueAnimator.getAnimatedValue()).floatValue());
                }
            });
        }
        this.mAnimator.setFloatValues(new float[]{this.mCollapsingTextHelper.getExpansionFraction(), f});
        this.mAnimator.start();
    }

    public void dispatchProvideAutofillStructure(ViewStructure viewStructure, int n) {
        Object object;
        if (this.mOriginalHint != null && (object = this.mEditText) != null) {
            object = object.getHint();
            this.mEditText.setHint(this.mOriginalHint);
            try {
                super.dispatchProvideAutofillStructure(viewStructure, n);
                return;
            }
            finally {
                this.mEditText.setHint((CharSequence)object);
            }
        }
        super.dispatchProvideAutofillStructure(viewStructure, n);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        this.mRestoringSavedState = true;
        super.dispatchRestoreInstanceState(sparseArray);
        this.mRestoringSavedState = false;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mHintEnabled) {
            this.mCollapsingTextHelper.draw(canvas);
            return;
        }
    }

    protected void drawableStateChanged() {
        if (this.mInDrawableStateChanged) {
            return;
        }
        boolean bl = true;
        this.mInDrawableStateChanged = true;
        super.drawableStateChanged();
        int[] arrn = this.getDrawableState();
        boolean bl2 = false;
        if (!ViewCompat.isLaidOut((View)this) || !this.isEnabled()) {
            bl = false;
        }
        this.updateLabelState(bl);
        this.updateEditTextBackground();
        CollapsingTextHelper collapsingTextHelper = this.mCollapsingTextHelper;
        if (collapsingTextHelper != null) {
            bl2 = false | collapsingTextHelper.setState(arrn);
        }
        if (bl2) {
            this.invalidate();
        }
        this.mInDrawableStateChanged = false;
    }

    public int getCounterMaxLength() {
        return this.mCounterMaxLength;
    }

    @Nullable
    public EditText getEditText() {
        return this.mEditText;
    }

    @Nullable
    public CharSequence getError() {
        if (this.mErrorEnabled) {
            return this.mError;
        }
        return null;
    }

    @Nullable
    public CharSequence getHint() {
        if (this.mHintEnabled) {
            return this.mHint;
        }
        return null;
    }

    @Nullable
    public CharSequence getPasswordVisibilityToggleContentDescription() {
        return this.mPasswordToggleContentDesc;
    }

    @Nullable
    public Drawable getPasswordVisibilityToggleDrawable() {
        return this.mPasswordToggleDrawable;
    }

    @NonNull
    public Typeface getTypeface() {
        return this.mTypeface;
    }

    public boolean isCounterEnabled() {
        return this.mCounterEnabled;
    }

    public boolean isErrorEnabled() {
        return this.mErrorEnabled;
    }

    public boolean isHintAnimationEnabled() {
        return this.mHintAnimationEnabled;
    }

    public boolean isHintEnabled() {
        return this.mHintEnabled;
    }

    @VisibleForTesting
    final boolean isHintExpanded() {
        return this.mHintExpanded;
    }

    public boolean isPasswordVisibilityToggleEnabled() {
        return this.mPasswordToggleEnabled;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        EditText editText;
        super.onLayout(bl, n, n2, n3, n4);
        if (this.mHintEnabled && (editText = this.mEditText) != null) {
            Rect rect = this.mTmpRect;
            ViewGroupUtils.getDescendantRect((ViewGroup)this, (View)editText, rect);
            n = rect.left + this.mEditText.getCompoundPaddingLeft();
            n3 = rect.right - this.mEditText.getCompoundPaddingRight();
            this.mCollapsingTextHelper.setExpandedBounds(n, rect.top + this.mEditText.getCompoundPaddingTop(), n3, rect.bottom - this.mEditText.getCompoundPaddingBottom());
            this.mCollapsingTextHelper.setCollapsedBounds(n, this.getPaddingTop(), n3, n4 - n2 - this.getPaddingBottom());
            this.mCollapsingTextHelper.recalculate();
            return;
        }
    }

    protected void onMeasure(int n, int n2) {
        this.updatePasswordToggleView();
        super.onMeasure(n, n2);
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.setError(parcelable.error);
        if (parcelable.isPasswordToggledVisible) {
            this.passwordVisibilityToggleRequested(true);
        }
        this.requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.mErrorShown) {
            savedState.error = this.getError();
        }
        savedState.isPasswordToggledVisible = this.mPasswordToggledVisible;
        return savedState;
    }

    public void setCounterEnabled(boolean bl) {
        if (this.mCounterEnabled != bl) {
            if (bl) {
                this.mCounterView = new AppCompatTextView(this.getContext());
                this.mCounterView.setId(R.id.textinput_counter);
                Typeface typeface = this.mTypeface;
                if (typeface != null) {
                    this.mCounterView.setTypeface(typeface);
                }
                this.mCounterView.setMaxLines(1);
                try {
                    TextViewCompat.setTextAppearance(this.mCounterView, this.mCounterTextAppearance);
                }
                catch (Exception exception) {
                    TextViewCompat.setTextAppearance(this.mCounterView, R.style.TextAppearance_AppCompat_Caption);
                    this.mCounterView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.error_color_material));
                }
                this.addIndicator(this.mCounterView, -1);
                typeface = this.mEditText;
                if (typeface == null) {
                    this.updateCounter(0);
                } else {
                    this.updateCounter(typeface.getText().length());
                }
            } else {
                this.removeIndicator(this.mCounterView);
                this.mCounterView = null;
            }
            this.mCounterEnabled = bl;
            return;
        }
    }

    public void setCounterMaxLength(int n) {
        if (this.mCounterMaxLength != n) {
            this.mCounterMaxLength = n > 0 ? n : -1;
            if (this.mCounterEnabled) {
                EditText editText = this.mEditText;
                n = editText == null ? 0 : editText.getText().length();
                this.updateCounter(n);
                return;
            }
            return;
        }
    }

    public void setEnabled(boolean bl) {
        TextInputLayout.recursiveSetEnabled((ViewGroup)this, bl);
        super.setEnabled(bl);
    }

    public void setError(@Nullable CharSequence charSequence) {
        TextView textView;
        boolean bl = ViewCompat.isLaidOut((View)this) && this.isEnabled() && ((textView = this.mErrorView) == null || !TextUtils.equals((CharSequence)textView.getText(), (CharSequence)charSequence));
        this.setError(charSequence, bl);
    }

    public void setErrorEnabled(boolean bl) {
        if (this.mErrorEnabled != bl) {
            TextView textView = this.mErrorView;
            if (textView != null) {
                textView.animate().cancel();
            }
            if (bl) {
                this.mErrorView = new AppCompatTextView(this.getContext());
                this.mErrorView.setId(R.id.textinput_error);
                textView = this.mTypeface;
                if (textView != null) {
                    this.mErrorView.setTypeface((Typeface)textView);
                }
                boolean bl2 = false;
                try {
                    int n;
                    TextViewCompat.setTextAppearance(this.mErrorView, this.mErrorTextAppearance);
                    if (Build.VERSION.SDK_INT >= 23 && (n = this.mErrorView.getTextColors().getDefaultColor()) == -65281) {
                        bl2 = true;
                    }
                }
                catch (Exception exception) {
                    bl2 = true;
                }
                if (bl2) {
                    TextViewCompat.setTextAppearance(this.mErrorView, R.style.TextAppearance_AppCompat_Caption);
                    this.mErrorView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.error_color_material));
                }
                this.mErrorView.setVisibility(4);
                ViewCompat.setAccessibilityLiveRegion((View)this.mErrorView, 1);
                this.addIndicator(this.mErrorView, 0);
            } else {
                this.mErrorShown = false;
                this.updateEditTextBackground();
                this.removeIndicator(this.mErrorView);
                this.mErrorView = null;
            }
            this.mErrorEnabled = bl;
            return;
        }
    }

    public void setErrorTextAppearance(@StyleRes int n) {
        this.mErrorTextAppearance = n;
        TextView textView = this.mErrorView;
        if (textView != null) {
            TextViewCompat.setTextAppearance(textView, n);
            return;
        }
    }

    public void setHint(@Nullable CharSequence charSequence) {
        if (this.mHintEnabled) {
            this.setHintInternal(charSequence);
            this.sendAccessibilityEvent(2048);
            return;
        }
    }

    public void setHintAnimationEnabled(boolean bl) {
        this.mHintAnimationEnabled = bl;
    }

    public void setHintEnabled(boolean bl) {
        if (bl != this.mHintEnabled) {
            this.mHintEnabled = bl;
            CharSequence charSequence = this.mEditText.getHint();
            if (!this.mHintEnabled) {
                if (!TextUtils.isEmpty((CharSequence)this.mHint) && TextUtils.isEmpty((CharSequence)charSequence)) {
                    this.mEditText.setHint(this.mHint);
                }
                this.setHintInternal(null);
            } else if (!TextUtils.isEmpty((CharSequence)charSequence)) {
                if (TextUtils.isEmpty((CharSequence)this.mHint)) {
                    this.setHint(charSequence);
                }
                this.mEditText.setHint(null);
            }
            if (this.mEditText != null) {
                this.updateInputLayoutMargins();
                return;
            }
            return;
        }
    }

    public void setHintTextAppearance(@StyleRes int n) {
        this.mCollapsingTextHelper.setCollapsedTextAppearance(n);
        this.mFocusedTextColor = this.mCollapsingTextHelper.getCollapsedTextColor();
        if (this.mEditText != null) {
            this.updateLabelState(false);
            this.updateInputLayoutMargins();
            return;
        }
    }

    public void setPasswordVisibilityToggleContentDescription(@StringRes int n) {
        CharSequence charSequence = n != 0 ? this.getResources().getText(n) : null;
        this.setPasswordVisibilityToggleContentDescription(charSequence);
    }

    public void setPasswordVisibilityToggleContentDescription(@Nullable CharSequence charSequence) {
        this.mPasswordToggleContentDesc = charSequence;
        CheckableImageButton checkableImageButton = this.mPasswordToggleView;
        if (checkableImageButton != null) {
            checkableImageButton.setContentDescription(charSequence);
            return;
        }
    }

    public void setPasswordVisibilityToggleDrawable(@DrawableRes int n) {
        Drawable drawable2 = n != 0 ? AppCompatResources.getDrawable(this.getContext(), n) : null;
        this.setPasswordVisibilityToggleDrawable(drawable2);
    }

    public void setPasswordVisibilityToggleDrawable(@Nullable Drawable drawable2) {
        this.mPasswordToggleDrawable = drawable2;
        CheckableImageButton checkableImageButton = this.mPasswordToggleView;
        if (checkableImageButton != null) {
            checkableImageButton.setImageDrawable(drawable2);
            return;
        }
    }

    public void setPasswordVisibilityToggleEnabled(boolean bl) {
        if (this.mPasswordToggleEnabled != bl) {
            EditText editText;
            this.mPasswordToggleEnabled = bl;
            if (!bl && this.mPasswordToggledVisible && (editText = this.mEditText) != null) {
                editText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            }
            this.mPasswordToggledVisible = false;
            this.updatePasswordToggleView();
            return;
        }
    }

    public void setPasswordVisibilityToggleTintList(@Nullable ColorStateList colorStateList) {
        this.mPasswordToggleTintList = colorStateList;
        this.mHasPasswordToggleTintList = true;
        this.applyPasswordToggleTint();
    }

    public void setPasswordVisibilityToggleTintMode(@Nullable PorterDuff.Mode mode) {
        this.mPasswordToggleTintMode = mode;
        this.mHasPasswordToggleTintMode = true;
        this.applyPasswordToggleTint();
    }

    public void setTypeface(@Nullable Typeface typeface) {
        block6 : {
            Typeface typeface2;
            block5 : {
                typeface2 = this.mTypeface;
                if (typeface2 != null && !typeface2.equals((Object)typeface)) break block5;
                if (this.mTypeface != null || typeface == null) break block6;
            }
            this.mTypeface = typeface;
            this.mCollapsingTextHelper.setTypefaces(typeface);
            typeface2 = this.mCounterView;
            if (typeface2 != null) {
                typeface2.setTypeface(typeface);
            }
            if ((typeface2 = this.mErrorView) != null) {
                typeface2.setTypeface(typeface);
                return;
            }
            return;
        }
    }

    void updateCounter(int n) {
        boolean bl = this.mCounterOverflowed;
        int n2 = this.mCounterMaxLength;
        if (n2 == -1) {
            this.mCounterView.setText((CharSequence)String.valueOf(n));
            this.mCounterOverflowed = false;
        } else {
            boolean bl2 = n > n2;
            this.mCounterOverflowed = bl2;
            if (bl != (bl2 = this.mCounterOverflowed)) {
                TextView textView = this.mCounterView;
                n2 = bl2 ? this.mCounterOverflowTextAppearance : this.mCounterTextAppearance;
                TextViewCompat.setTextAppearance(textView, n2);
            }
            this.mCounterView.setText((CharSequence)this.getContext().getString(R.string.character_counter_pattern, new Object[]{n, this.mCounterMaxLength}));
        }
        if (this.mEditText != null && bl != this.mCounterOverflowed) {
            this.updateLabelState(false);
            this.updateEditTextBackground();
            return;
        }
    }

    void updateLabelState(boolean bl) {
        this.updateLabelState(bl, false);
    }

    void updateLabelState(boolean bl, boolean bl2) {
        boolean bl3 = this.isEnabled();
        EditText editText = this.mEditText;
        boolean bl4 = editText != null && !TextUtils.isEmpty((CharSequence)editText.getText());
        boolean bl5 = TextInputLayout.arrayContains(this.getDrawableState(), 16842908);
        boolean bl6 = TextUtils.isEmpty((CharSequence)this.getError());
        editText = this.mDefaultTextColor;
        if (editText != null) {
            this.mCollapsingTextHelper.setExpandedTextColor((ColorStateList)editText);
        }
        if (bl3 && this.mCounterOverflowed && (editText = this.mCounterView) != null) {
            this.mCollapsingTextHelper.setCollapsedTextColor(editText.getTextColors());
        } else if (bl3 && bl5 && (editText = this.mFocusedTextColor) != null) {
            this.mCollapsingTextHelper.setCollapsedTextColor((ColorStateList)editText);
        } else {
            editText = this.mDefaultTextColor;
            if (editText != null) {
                this.mCollapsingTextHelper.setCollapsedTextColor((ColorStateList)editText);
            }
        }
        if (!(bl4 || this.isEnabled() && (bl5 || true ^ bl6))) {
            if (!bl2 && this.mHintExpanded) {
                return;
            }
            this.expandHint(bl);
            return;
        }
        if (!bl2 && !this.mHintExpanded) {
            return;
        }
        this.collapseHint(bl);
    }

    static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        CharSequence error;
        boolean isPasswordToggledVisible;

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.error = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            int n = parcel.readInt();
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            this.isPasswordToggledVisible = bl;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TextInputLayout.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" error=");
            stringBuilder.append((Object)this.error);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            RuntimeException runtimeException;
            super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
            throw runtimeException;
        }

    }

    private class TextInputAccessibilityDelegate
    extends AccessibilityDelegateCompat {
        TextInputAccessibilityDelegate() {
        }

        @Override
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)TextInputLayout.class.getSimpleName());
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View object, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo((View)object, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(TextInputLayout.class.getSimpleName());
            object = TextInputLayout.this.mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty((CharSequence)object)) {
                accessibilityNodeInfoCompat.setText((CharSequence)object);
            }
            if (TextInputLayout.this.mEditText != null) {
                accessibilityNodeInfoCompat.setLabelFor((View)TextInputLayout.this.mEditText);
            }
            object = TextInputLayout.this.mErrorView != null ? TextInputLayout.this.mErrorView.getText() : null;
            if (!TextUtils.isEmpty((CharSequence)object)) {
                accessibilityNodeInfoCompat.setContentInvalid(true);
                accessibilityNodeInfoCompat.setError((CharSequence)object);
                return;
            }
        }

        @Override
        public void onPopulateAccessibilityEvent(View object, AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent((View)object, accessibilityEvent);
            object = TextInputLayout.this.mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty((CharSequence)object)) {
                accessibilityEvent.getText().add(object);
                return;
            }
        }
    }

}

