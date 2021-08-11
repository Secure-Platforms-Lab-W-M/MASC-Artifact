// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityEvent;
import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.support.v7.content.res.AppCompatResources;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.annotation.NonNull;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ViewStructure;
import android.support.annotation.VisibleForTesting;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.widget.FrameLayout$LayoutParams;
import android.support.v4.widget.TextViewCompat;
import android.graphics.drawable.ColorDrawable;
import android.view.View$OnClickListener;
import android.view.LayoutInflater;
import android.graphics.ColorFilter;
import android.support.v7.widget.AppCompatDrawableManager;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ViewGroup;
import android.text.method.TransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.graphics.drawable.DrawableContainer;
import android.os.Build$VERSION;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.support.v4.widget.Space;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.TintTypedArray;
import android.support.design.R;
import android.view.animation.Interpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.PorterDuff$Mode;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.EditText;
import android.content.res.ColorStateList;
import android.widget.TextView;
import android.animation.ValueAnimator;
import android.widget.LinearLayout;

public class TextInputLayout extends LinearLayout
{
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
    private PorterDuff$Mode mPasswordToggleTintMode;
    private CheckableImageButton mPasswordToggleView;
    private boolean mPasswordToggledVisible;
    private boolean mRestoringSavedState;
    private Paint mTmpPaint;
    private final Rect mTmpRect;
    private Typeface mTypeface;
    
    public TextInputLayout(final Context context) {
        this(context, null);
    }
    
    public TextInputLayout(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public TextInputLayout(final Context context, final AttributeSet set, final int n) {
        super(context, set);
        this.mTmpRect = new Rect();
        this.mCollapsingTextHelper = new CollapsingTextHelper((View)this);
        ThemeUtils.checkAppCompatTheme(context);
        this.setOrientation(1);
        this.setWillNotDraw(false);
        this.setAddStatesFromChildren(true);
        (this.mInputFrame = new FrameLayout(context)).setAddStatesFromChildren(true);
        this.addView((View)this.mInputFrame);
        this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        this.mCollapsingTextHelper.setPositionInterpolator((Interpolator)new AccelerateInterpolator());
        this.mCollapsingTextHelper.setCollapsedTextGravity(8388659);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.TextInputLayout, n, R.style.Widget_Design_TextInputLayout);
        this.mHintEnabled = obtainStyledAttributes.getBoolean(R.styleable.TextInputLayout_hintEnabled, true);
        this.setHint(obtainStyledAttributes.getText(R.styleable.TextInputLayout_android_hint));
        this.mHintAnimationEnabled = obtainStyledAttributes.getBoolean(R.styleable.TextInputLayout_hintAnimationEnabled, true);
        if (obtainStyledAttributes.hasValue(R.styleable.TextInputLayout_android_textColorHint)) {
            final ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.TextInputLayout_android_textColorHint);
            this.mFocusedTextColor = colorStateList;
            this.mDefaultTextColor = colorStateList;
        }
        if (obtainStyledAttributes.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, -1) != -1) {
            this.setHintTextAppearance(obtainStyledAttributes.getResourceId(R.styleable.TextInputLayout_hintTextAppearance, 0));
        }
        this.mErrorTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.TextInputLayout_errorTextAppearance, 0);
        final boolean boolean1 = obtainStyledAttributes.getBoolean(R.styleable.TextInputLayout_errorEnabled, false);
        final boolean boolean2 = obtainStyledAttributes.getBoolean(R.styleable.TextInputLayout_counterEnabled, false);
        this.setCounterMaxLength(obtainStyledAttributes.getInt(R.styleable.TextInputLayout_counterMaxLength, -1));
        this.mCounterTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.TextInputLayout_counterTextAppearance, 0);
        this.mCounterOverflowTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.TextInputLayout_counterOverflowTextAppearance, 0);
        this.mPasswordToggleEnabled = obtainStyledAttributes.getBoolean(R.styleable.TextInputLayout_passwordToggleEnabled, false);
        this.mPasswordToggleDrawable = obtainStyledAttributes.getDrawable(R.styleable.TextInputLayout_passwordToggleDrawable);
        this.mPasswordToggleContentDesc = obtainStyledAttributes.getText(R.styleable.TextInputLayout_passwordToggleContentDescription);
        if (obtainStyledAttributes.hasValue(R.styleable.TextInputLayout_passwordToggleTint)) {
            this.mHasPasswordToggleTintList = true;
            this.mPasswordToggleTintList = obtainStyledAttributes.getColorStateList(R.styleable.TextInputLayout_passwordToggleTint);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.TextInputLayout_passwordToggleTintMode)) {
            this.mHasPasswordToggleTintMode = true;
            this.mPasswordToggleTintMode = ViewUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.TextInputLayout_passwordToggleTintMode, -1), null);
        }
        obtainStyledAttributes.recycle();
        this.setErrorEnabled(boolean1);
        this.setCounterEnabled(boolean2);
        this.applyPasswordToggleTint();
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        ViewCompat.setAccessibilityDelegate((View)this, new TextInputAccessibilityDelegate());
    }
    
    private void addIndicator(final TextView textView, final int n) {
        if (this.mIndicatorArea == null) {
            (this.mIndicatorArea = new LinearLayout(this.getContext())).setOrientation(0);
            this.addView((View)this.mIndicatorArea, -1, -2);
            this.mIndicatorArea.addView((View)new Space(this.getContext()), (ViewGroup$LayoutParams)new LinearLayout$LayoutParams(0, 0, 1.0f));
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
        if (this.mPasswordToggleDrawable == null || (!this.mHasPasswordToggleTintList && !this.mHasPasswordToggleTintMode)) {
            return;
        }
        this.mPasswordToggleDrawable = DrawableCompat.wrap(this.mPasswordToggleDrawable).mutate();
        if (this.mHasPasswordToggleTintList) {
            DrawableCompat.setTintList(this.mPasswordToggleDrawable, this.mPasswordToggleTintList);
        }
        if (this.mHasPasswordToggleTintMode) {
            DrawableCompat.setTintMode(this.mPasswordToggleDrawable, this.mPasswordToggleTintMode);
        }
        final CheckableImageButton mPasswordToggleView = this.mPasswordToggleView;
        if (mPasswordToggleView == null) {
            return;
        }
        final Drawable drawable = mPasswordToggleView.getDrawable();
        final Drawable mPasswordToggleDrawable = this.mPasswordToggleDrawable;
        if (drawable != mPasswordToggleDrawable) {
            this.mPasswordToggleView.setImageDrawable(mPasswordToggleDrawable);
        }
    }
    
    private static boolean arrayContains(final int[] array, final int n) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    private void collapseHint(final boolean b) {
        final ValueAnimator mAnimator = this.mAnimator;
        if (mAnimator != null && mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (b && this.mHintAnimationEnabled) {
            this.animateToExpansionFraction(1.0f);
        }
        else {
            this.mCollapsingTextHelper.setExpansionFraction(1.0f);
        }
        this.mHintExpanded = false;
    }
    
    private void ensureBackgroundDrawableStateWorkaround() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT != 21 && sdk_INT != 22) {
            return;
        }
        final Drawable background = this.mEditText.getBackground();
        if (background == null) {
            return;
        }
        if (this.mHasReconstructedEditTextBackground) {
            return;
        }
        final Drawable drawable = background.getConstantState().newDrawable();
        if (background instanceof DrawableContainer) {
            this.mHasReconstructedEditTextBackground = DrawableUtils.setContainerConstantState((DrawableContainer)background, drawable.getConstantState());
        }
        if (!this.mHasReconstructedEditTextBackground) {
            ViewCompat.setBackground((View)this.mEditText, drawable);
            this.mHasReconstructedEditTextBackground = true;
        }
    }
    
    private void expandHint(final boolean b) {
        final ValueAnimator mAnimator = this.mAnimator;
        if (mAnimator != null && mAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (b && this.mHintAnimationEnabled) {
            this.animateToExpansionFraction(0.0f);
        }
        else {
            this.mCollapsingTextHelper.setExpansionFraction(0.0f);
        }
        this.mHintExpanded = true;
    }
    
    private boolean hasPasswordTransformation() {
        final EditText mEditText = this.mEditText;
        return mEditText != null && mEditText.getTransformationMethod() instanceof PasswordTransformationMethod;
    }
    
    private void passwordVisibilityToggleRequested(final boolean b) {
        if (this.mPasswordToggleEnabled) {
            final int selectionEnd = this.mEditText.getSelectionEnd();
            if (this.hasPasswordTransformation()) {
                this.mEditText.setTransformationMethod((TransformationMethod)null);
                this.mPasswordToggledVisible = true;
            }
            else {
                this.mEditText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
                this.mPasswordToggledVisible = false;
            }
            this.mPasswordToggleView.setChecked(this.mPasswordToggledVisible);
            if (b) {
                this.mPasswordToggleView.jumpDrawablesToCurrentState();
            }
            this.mEditText.setSelection(selectionEnd);
        }
    }
    
    private static void recursiveSetEnabled(final ViewGroup viewGroup, final boolean enabled) {
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            final View child = viewGroup.getChildAt(i);
            child.setEnabled(enabled);
            if (child instanceof ViewGroup) {
                recursiveSetEnabled((ViewGroup)child, enabled);
            }
        }
    }
    
    private void removeIndicator(final TextView textView) {
        final LinearLayout mIndicatorArea = this.mIndicatorArea;
        if (mIndicatorArea == null) {
            return;
        }
        mIndicatorArea.removeView((View)textView);
        if (--this.mIndicatorsAdded == 0) {
            this.mIndicatorArea.setVisibility(8);
        }
    }
    
    private void setEditText(final EditText mEditText) {
        if (this.mEditText == null) {
            if (!(mEditText instanceof TextInputEditText)) {
                Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead.");
            }
            this.mEditText = mEditText;
            if (!this.hasPasswordTransformation()) {
                this.mCollapsingTextHelper.setTypefaces(this.mEditText.getTypeface());
            }
            this.mCollapsingTextHelper.setExpandedTextSize(this.mEditText.getTextSize());
            final int gravity = this.mEditText.getGravity();
            this.mCollapsingTextHelper.setCollapsedTextGravity((gravity & 0xFFFFFF8F) | 0x30);
            this.mCollapsingTextHelper.setExpandedTextGravity(gravity);
            this.mEditText.addTextChangedListener((TextWatcher)new TextWatcher() {
                public void afterTextChanged(final Editable editable) {
                    final TextInputLayout this$0 = TextInputLayout.this;
                    this$0.updateLabelState(this$0.mRestoringSavedState ^ true);
                    if (TextInputLayout.this.mCounterEnabled) {
                        TextInputLayout.this.updateCounter(editable.length());
                    }
                }
                
                public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
                
                public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                }
            });
            if (this.mDefaultTextColor == null) {
                this.mDefaultTextColor = this.mEditText.getHintTextColors();
            }
            if (this.mHintEnabled && TextUtils.isEmpty(this.mHint)) {
                this.setHint(this.mOriginalHint = this.mEditText.getHint());
                this.mEditText.setHint((CharSequence)null);
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
    
    private void setError(@Nullable final CharSequence text, final boolean b) {
        this.mError = text;
        if (!this.mErrorEnabled) {
            if (TextUtils.isEmpty(text)) {
                return;
            }
            this.setErrorEnabled(true);
        }
        this.mErrorShown = (TextUtils.isEmpty(text) ^ true);
        this.mErrorView.animate().cancel();
        if (this.mErrorShown) {
            this.mErrorView.setText(text);
            this.mErrorView.setVisibility(0);
            if (b) {
                if (this.mErrorView.getAlpha() == 1.0f) {
                    this.mErrorView.setAlpha(0.0f);
                }
                this.mErrorView.animate().alpha(1.0f).setDuration(200L).setInterpolator((TimeInterpolator)AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationStart(final Animator animator) {
                        TextInputLayout.this.mErrorView.setVisibility(0);
                    }
                }).start();
            }
            else {
                this.mErrorView.setAlpha(1.0f);
            }
        }
        else if (this.mErrorView.getVisibility() == 0) {
            if (b) {
                this.mErrorView.animate().alpha(0.0f).setDuration(200L).setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                    public void onAnimationEnd(final Animator animator) {
                        TextInputLayout.this.mErrorView.setText(text);
                        TextInputLayout.this.mErrorView.setVisibility(4);
                    }
                }).start();
            }
            else {
                this.mErrorView.setText(text);
                this.mErrorView.setVisibility(4);
            }
        }
        this.updateEditTextBackground();
        this.updateLabelState(b);
    }
    
    private void setHintInternal(final CharSequence charSequence) {
        this.mHint = charSequence;
        this.mCollapsingTextHelper.setText(charSequence);
    }
    
    private boolean shouldShowPasswordIcon() {
        return this.mPasswordToggleEnabled && (this.hasPasswordTransformation() || this.mPasswordToggledVisible);
    }
    
    private void updateEditTextBackground() {
        final EditText mEditText = this.mEditText;
        if (mEditText == null) {
            return;
        }
        Drawable drawable = mEditText.getBackground();
        if (drawable == null) {
            return;
        }
        this.ensureBackgroundDrawableStateWorkaround();
        if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(drawable)) {
            drawable = drawable.mutate();
        }
        if (this.mErrorShown) {
            final TextView mErrorView = this.mErrorView;
            if (mErrorView != null) {
                drawable.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(mErrorView.getCurrentTextColor(), PorterDuff$Mode.SRC_IN));
                return;
            }
        }
        if (this.mCounterOverflowed) {
            final TextView mCounterView = this.mCounterView;
            if (mCounterView != null) {
                drawable.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(mCounterView.getCurrentTextColor(), PorterDuff$Mode.SRC_IN));
                return;
            }
        }
        DrawableCompat.clearColorFilter(drawable);
        this.mEditText.refreshDrawableState();
    }
    
    private void updateInputLayoutMargins() {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)this.mInputFrame.getLayoutParams();
        int topMargin;
        if (this.mHintEnabled) {
            if (this.mTmpPaint == null) {
                this.mTmpPaint = new Paint();
            }
            this.mTmpPaint.setTypeface(this.mCollapsingTextHelper.getCollapsedTypeface());
            this.mTmpPaint.setTextSize(this.mCollapsingTextHelper.getCollapsedTextSize());
            topMargin = (int)(-this.mTmpPaint.ascent());
        }
        else {
            topMargin = 0;
        }
        if (topMargin != linearLayout$LayoutParams.topMargin) {
            linearLayout$LayoutParams.topMargin = topMargin;
            this.mInputFrame.requestLayout();
        }
    }
    
    private void updatePasswordToggleView() {
        if (this.mEditText == null) {
            return;
        }
        if (this.shouldShowPasswordIcon()) {
            if (this.mPasswordToggleView == null) {
                (this.mPasswordToggleView = (CheckableImageButton)LayoutInflater.from(this.getContext()).inflate(R.layout.design_text_input_password_icon, (ViewGroup)this.mInputFrame, false)).setImageDrawable(this.mPasswordToggleDrawable);
                this.mPasswordToggleView.setContentDescription(this.mPasswordToggleContentDesc);
                this.mInputFrame.addView((View)this.mPasswordToggleView);
                this.mPasswordToggleView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        TextInputLayout.this.passwordVisibilityToggleRequested(false);
                    }
                });
            }
            final EditText mEditText = this.mEditText;
            if (mEditText != null && ViewCompat.getMinimumHeight((View)mEditText) <= 0) {
                this.mEditText.setMinimumHeight(ViewCompat.getMinimumHeight((View)this.mPasswordToggleView));
            }
            this.mPasswordToggleView.setVisibility(0);
            this.mPasswordToggleView.setChecked(this.mPasswordToggledVisible);
            if (this.mPasswordToggleDummyDrawable == null) {
                this.mPasswordToggleDummyDrawable = (Drawable)new ColorDrawable();
            }
            this.mPasswordToggleDummyDrawable.setBounds(0, 0, this.mPasswordToggleView.getMeasuredWidth(), 1);
            final Drawable[] compoundDrawablesRelative = TextViewCompat.getCompoundDrawablesRelative((TextView)this.mEditText);
            if (compoundDrawablesRelative[2] != this.mPasswordToggleDummyDrawable) {
                this.mOriginalEditTextEndDrawable = compoundDrawablesRelative[2];
            }
            TextViewCompat.setCompoundDrawablesRelative((TextView)this.mEditText, compoundDrawablesRelative[0], compoundDrawablesRelative[1], this.mPasswordToggleDummyDrawable, compoundDrawablesRelative[3]);
            this.mPasswordToggleView.setPadding(this.mEditText.getPaddingLeft(), this.mEditText.getPaddingTop(), this.mEditText.getPaddingRight(), this.mEditText.getPaddingBottom());
            return;
        }
        final CheckableImageButton mPasswordToggleView = this.mPasswordToggleView;
        if (mPasswordToggleView != null && mPasswordToggleView.getVisibility() == 0) {
            this.mPasswordToggleView.setVisibility(8);
        }
        if (this.mPasswordToggleDummyDrawable == null) {
            return;
        }
        final Drawable[] compoundDrawablesRelative2 = TextViewCompat.getCompoundDrawablesRelative((TextView)this.mEditText);
        if (compoundDrawablesRelative2[2] == this.mPasswordToggleDummyDrawable) {
            TextViewCompat.setCompoundDrawablesRelative((TextView)this.mEditText, compoundDrawablesRelative2[0], compoundDrawablesRelative2[1], this.mOriginalEditTextEndDrawable, compoundDrawablesRelative2[3]);
            this.mPasswordToggleDummyDrawable = null;
        }
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams layoutParams) {
        if (view instanceof EditText) {
            final FrameLayout$LayoutParams frameLayout$LayoutParams = new FrameLayout$LayoutParams(layoutParams);
            frameLayout$LayoutParams.gravity = ((frameLayout$LayoutParams.gravity & 0xFFFFFF8F) | 0x10);
            this.mInputFrame.addView(view, (ViewGroup$LayoutParams)frameLayout$LayoutParams);
            this.mInputFrame.setLayoutParams(layoutParams);
            this.updateInputLayoutMargins();
            this.setEditText((EditText)view);
            return;
        }
        super.addView(view, n, layoutParams);
    }
    
    @VisibleForTesting
    void animateToExpansionFraction(final float n) {
        if (this.mCollapsingTextHelper.getExpansionFraction() == n) {
            return;
        }
        if (this.mAnimator == null) {
            (this.mAnimator = new ValueAnimator()).setInterpolator((TimeInterpolator)AnimationUtils.LINEAR_INTERPOLATOR);
            this.mAnimator.setDuration(200L);
            this.mAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new ValueAnimator$AnimatorUpdateListener() {
                public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                    TextInputLayout.this.mCollapsingTextHelper.setExpansionFraction((float)valueAnimator.getAnimatedValue());
                }
            });
        }
        this.mAnimator.setFloatValues(new float[] { this.mCollapsingTextHelper.getExpansionFraction(), n });
        this.mAnimator.start();
    }
    
    public void dispatchProvideAutofillStructure(final ViewStructure viewStructure, final int n) {
        if (this.mOriginalHint != null) {
            final EditText mEditText = this.mEditText;
            if (mEditText != null) {
                final CharSequence hint = mEditText.getHint();
                this.mEditText.setHint(this.mOriginalHint);
                try {
                    super.dispatchProvideAutofillStructure(viewStructure, n);
                    return;
                }
                finally {
                    this.mEditText.setHint(hint);
                }
            }
        }
        super.dispatchProvideAutofillStructure(viewStructure, n);
    }
    
    protected void dispatchRestoreInstanceState(final SparseArray<Parcelable> sparseArray) {
        this.mRestoringSavedState = true;
        super.dispatchRestoreInstanceState((SparseArray)sparseArray);
        this.mRestoringSavedState = false;
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (this.mHintEnabled) {
            this.mCollapsingTextHelper.draw(canvas);
        }
    }
    
    protected void drawableStateChanged() {
        if (this.mInDrawableStateChanged) {
            return;
        }
        boolean b = true;
        this.mInDrawableStateChanged = true;
        super.drawableStateChanged();
        final int[] drawableState = this.getDrawableState();
        boolean b2 = false;
        if (!ViewCompat.isLaidOut((View)this) || !this.isEnabled()) {
            b = false;
        }
        this.updateLabelState(b);
        this.updateEditTextBackground();
        final CollapsingTextHelper mCollapsingTextHelper = this.mCollapsingTextHelper;
        if (mCollapsingTextHelper != null) {
            b2 = (false | mCollapsingTextHelper.setState(drawableState));
        }
        if (b2) {
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
    
    protected void onLayout(final boolean b, int n, final int n2, int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (this.mHintEnabled) {
            final EditText mEditText = this.mEditText;
            if (mEditText != null) {
                final Rect mTmpRect = this.mTmpRect;
                ViewGroupUtils.getDescendantRect((ViewGroup)this, (View)mEditText, mTmpRect);
                n = mTmpRect.left + this.mEditText.getCompoundPaddingLeft();
                n3 = mTmpRect.right - this.mEditText.getCompoundPaddingRight();
                this.mCollapsingTextHelper.setExpandedBounds(n, mTmpRect.top + this.mEditText.getCompoundPaddingTop(), n3, mTmpRect.bottom - this.mEditText.getCompoundPaddingBottom());
                this.mCollapsingTextHelper.setCollapsedBounds(n, this.getPaddingTop(), n3, n4 - n2 - this.getPaddingBottom());
                this.mCollapsingTextHelper.recalculate();
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.updatePasswordToggleView();
        super.onMeasure(n, n2);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.setError(savedState.error);
        if (savedState.isPasswordToggledVisible) {
            this.passwordVisibilityToggleRequested(true);
        }
        this.requestLayout();
    }
    
    public Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.mErrorShown) {
            savedState.error = this.getError();
        }
        savedState.isPasswordToggledVisible = this.mPasswordToggledVisible;
        return (Parcelable)savedState;
    }
    
    public void setCounterEnabled(final boolean mCounterEnabled) {
        if (this.mCounterEnabled != mCounterEnabled) {
            if (mCounterEnabled) {
                (this.mCounterView = new AppCompatTextView(this.getContext())).setId(R.id.textinput_counter);
                final Typeface mTypeface = this.mTypeface;
                if (mTypeface != null) {
                    this.mCounterView.setTypeface(mTypeface);
                }
                this.mCounterView.setMaxLines(1);
                try {
                    TextViewCompat.setTextAppearance(this.mCounterView, this.mCounterTextAppearance);
                }
                catch (Exception ex) {
                    TextViewCompat.setTextAppearance(this.mCounterView, android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Caption);
                    this.mCounterView.setTextColor(ContextCompat.getColor(this.getContext(), android.support.v7.appcompat.R.color.error_color_material));
                }
                this.addIndicator(this.mCounterView, -1);
                final EditText mEditText = this.mEditText;
                if (mEditText == null) {
                    this.updateCounter(0);
                }
                else {
                    this.updateCounter(mEditText.getText().length());
                }
            }
            else {
                this.removeIndicator(this.mCounterView);
                this.mCounterView = null;
            }
            this.mCounterEnabled = mCounterEnabled;
        }
    }
    
    public void setCounterMaxLength(int length) {
        if (this.mCounterMaxLength == length) {
            return;
        }
        if (length > 0) {
            this.mCounterMaxLength = length;
        }
        else {
            this.mCounterMaxLength = -1;
        }
        if (this.mCounterEnabled) {
            final EditText mEditText = this.mEditText;
            if (mEditText == null) {
                length = 0;
            }
            else {
                length = mEditText.getText().length();
            }
            this.updateCounter(length);
        }
    }
    
    public void setEnabled(final boolean enabled) {
        recursiveSetEnabled((ViewGroup)this, enabled);
        super.setEnabled(enabled);
    }
    
    public void setError(@Nullable final CharSequence charSequence) {
        boolean b = false;
        Label_0047: {
            if (ViewCompat.isLaidOut((View)this) && this.isEnabled()) {
                final TextView mErrorView = this.mErrorView;
                if (mErrorView == null || !TextUtils.equals(mErrorView.getText(), charSequence)) {
                    b = true;
                    break Label_0047;
                }
            }
            b = false;
        }
        this.setError(charSequence, b);
    }
    
    public void setErrorEnabled(final boolean mErrorEnabled) {
        if (this.mErrorEnabled != mErrorEnabled) {
            final TextView mErrorView = this.mErrorView;
            if (mErrorView != null) {
                mErrorView.animate().cancel();
            }
            if (mErrorEnabled) {
                (this.mErrorView = new AppCompatTextView(this.getContext())).setId(R.id.textinput_error);
                final Typeface mTypeface = this.mTypeface;
                if (mTypeface != null) {
                    this.mErrorView.setTypeface(mTypeface);
                }
                boolean b = false;
                try {
                    TextViewCompat.setTextAppearance(this.mErrorView, this.mErrorTextAppearance);
                    if (Build$VERSION.SDK_INT >= 23) {
                        if (this.mErrorView.getTextColors().getDefaultColor() == -65281) {
                            b = true;
                        }
                    }
                }
                catch (Exception ex) {
                    b = true;
                }
                if (b) {
                    TextViewCompat.setTextAppearance(this.mErrorView, android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Caption);
                    this.mErrorView.setTextColor(ContextCompat.getColor(this.getContext(), android.support.v7.appcompat.R.color.error_color_material));
                }
                this.mErrorView.setVisibility(4);
                ViewCompat.setAccessibilityLiveRegion((View)this.mErrorView, 1);
                this.addIndicator(this.mErrorView, 0);
            }
            else {
                this.mErrorShown = false;
                this.updateEditTextBackground();
                this.removeIndicator(this.mErrorView);
                this.mErrorView = null;
            }
            this.mErrorEnabled = mErrorEnabled;
        }
    }
    
    public void setErrorTextAppearance(@StyleRes final int mErrorTextAppearance) {
        this.mErrorTextAppearance = mErrorTextAppearance;
        final TextView mErrorView = this.mErrorView;
        if (mErrorView != null) {
            TextViewCompat.setTextAppearance(mErrorView, mErrorTextAppearance);
        }
    }
    
    public void setHint(@Nullable final CharSequence hintInternal) {
        if (this.mHintEnabled) {
            this.setHintInternal(hintInternal);
            this.sendAccessibilityEvent(2048);
        }
    }
    
    public void setHintAnimationEnabled(final boolean mHintAnimationEnabled) {
        this.mHintAnimationEnabled = mHintAnimationEnabled;
    }
    
    public void setHintEnabled(final boolean mHintEnabled) {
        if (mHintEnabled == this.mHintEnabled) {
            return;
        }
        this.mHintEnabled = mHintEnabled;
        final CharSequence hint = this.mEditText.getHint();
        if (!this.mHintEnabled) {
            if (!TextUtils.isEmpty(this.mHint) && TextUtils.isEmpty(hint)) {
                this.mEditText.setHint(this.mHint);
            }
            this.setHintInternal(null);
        }
        else if (!TextUtils.isEmpty(hint)) {
            if (TextUtils.isEmpty(this.mHint)) {
                this.setHint(hint);
            }
            this.mEditText.setHint((CharSequence)null);
        }
        if (this.mEditText != null) {
            this.updateInputLayoutMargins();
        }
    }
    
    public void setHintTextAppearance(@StyleRes final int collapsedTextAppearance) {
        this.mCollapsingTextHelper.setCollapsedTextAppearance(collapsedTextAppearance);
        this.mFocusedTextColor = this.mCollapsingTextHelper.getCollapsedTextColor();
        if (this.mEditText != null) {
            this.updateLabelState(false);
            this.updateInputLayoutMargins();
        }
    }
    
    public void setPasswordVisibilityToggleContentDescription(@StringRes final int n) {
        CharSequence text;
        if (n != 0) {
            text = this.getResources().getText(n);
        }
        else {
            text = null;
        }
        this.setPasswordVisibilityToggleContentDescription(text);
    }
    
    public void setPasswordVisibilityToggleContentDescription(@Nullable final CharSequence charSequence) {
        this.mPasswordToggleContentDesc = charSequence;
        final CheckableImageButton mPasswordToggleView = this.mPasswordToggleView;
        if (mPasswordToggleView != null) {
            mPasswordToggleView.setContentDescription(charSequence);
        }
    }
    
    public void setPasswordVisibilityToggleDrawable(@DrawableRes final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setPasswordVisibilityToggleDrawable(drawable);
    }
    
    public void setPasswordVisibilityToggleDrawable(@Nullable final Drawable drawable) {
        this.mPasswordToggleDrawable = drawable;
        final CheckableImageButton mPasswordToggleView = this.mPasswordToggleView;
        if (mPasswordToggleView != null) {
            mPasswordToggleView.setImageDrawable(drawable);
        }
    }
    
    public void setPasswordVisibilityToggleEnabled(final boolean mPasswordToggleEnabled) {
        if (this.mPasswordToggleEnabled != mPasswordToggleEnabled) {
            this.mPasswordToggleEnabled = mPasswordToggleEnabled;
            if (!mPasswordToggleEnabled && this.mPasswordToggledVisible) {
                final EditText mEditText = this.mEditText;
                if (mEditText != null) {
                    mEditText.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
                }
            }
            this.mPasswordToggledVisible = false;
            this.updatePasswordToggleView();
        }
    }
    
    public void setPasswordVisibilityToggleTintList(@Nullable final ColorStateList mPasswordToggleTintList) {
        this.mPasswordToggleTintList = mPasswordToggleTintList;
        this.mHasPasswordToggleTintList = true;
        this.applyPasswordToggleTint();
    }
    
    public void setPasswordVisibilityToggleTintMode(@Nullable final PorterDuff$Mode mPasswordToggleTintMode) {
        this.mPasswordToggleTintMode = mPasswordToggleTintMode;
        this.mHasPasswordToggleTintMode = true;
        this.applyPasswordToggleTint();
    }
    
    public void setTypeface(@Nullable final Typeface typeface) {
        final Typeface mTypeface = this.mTypeface;
        if ((mTypeface == null || mTypeface.equals((Object)typeface)) && (this.mTypeface != null || typeface == null)) {
            return;
        }
        this.mTypeface = typeface;
        this.mCollapsingTextHelper.setTypefaces(typeface);
        final TextView mCounterView = this.mCounterView;
        if (mCounterView != null) {
            mCounterView.setTypeface(typeface);
        }
        final TextView mErrorView = this.mErrorView;
        if (mErrorView != null) {
            mErrorView.setTypeface(typeface);
        }
    }
    
    void updateCounter(final int n) {
        final boolean mCounterOverflowed = this.mCounterOverflowed;
        final int mCounterMaxLength = this.mCounterMaxLength;
        if (mCounterMaxLength == -1) {
            this.mCounterView.setText((CharSequence)String.valueOf(n));
            this.mCounterOverflowed = false;
        }
        else {
            this.mCounterOverflowed = (n > mCounterMaxLength);
            final boolean mCounterOverflowed2 = this.mCounterOverflowed;
            if (mCounterOverflowed != mCounterOverflowed2) {
                final TextView mCounterView = this.mCounterView;
                int n2;
                if (mCounterOverflowed2) {
                    n2 = this.mCounterOverflowTextAppearance;
                }
                else {
                    n2 = this.mCounterTextAppearance;
                }
                TextViewCompat.setTextAppearance(mCounterView, n2);
            }
            this.mCounterView.setText((CharSequence)this.getContext().getString(R.string.character_counter_pattern, new Object[] { n, this.mCounterMaxLength }));
        }
        if (this.mEditText != null && mCounterOverflowed != this.mCounterOverflowed) {
            this.updateLabelState(false);
            this.updateEditTextBackground();
        }
    }
    
    void updateLabelState(final boolean b) {
        this.updateLabelState(b, false);
    }
    
    void updateLabelState(final boolean b, final boolean b2) {
        final boolean enabled = this.isEnabled();
        final EditText mEditText = this.mEditText;
        final boolean b3 = mEditText != null && !TextUtils.isEmpty((CharSequence)mEditText.getText());
        final boolean arrayContains = arrayContains(this.getDrawableState(), 16842908);
        final boolean empty = TextUtils.isEmpty(this.getError());
        final ColorStateList mDefaultTextColor = this.mDefaultTextColor;
        if (mDefaultTextColor != null) {
            this.mCollapsingTextHelper.setExpandedTextColor(mDefaultTextColor);
        }
        Label_0173: {
            if (enabled && this.mCounterOverflowed) {
                final TextView mCounterView = this.mCounterView;
                if (mCounterView != null) {
                    this.mCollapsingTextHelper.setCollapsedTextColor(mCounterView.getTextColors());
                    break Label_0173;
                }
            }
            if (enabled && arrayContains) {
                final ColorStateList mFocusedTextColor = this.mFocusedTextColor;
                if (mFocusedTextColor != null) {
                    this.mCollapsingTextHelper.setCollapsedTextColor(mFocusedTextColor);
                    break Label_0173;
                }
            }
            final ColorStateList mDefaultTextColor2 = this.mDefaultTextColor;
            if (mDefaultTextColor2 != null) {
                this.mCollapsingTextHelper.setCollapsedTextColor(mDefaultTextColor2);
            }
        }
        Label_0220: {
            if (!b3) {
                if (this.isEnabled()) {
                    if (arrayContains) {
                        break Label_0220;
                    }
                    if (true ^ empty) {
                        break Label_0220;
                    }
                }
                if (!b2 && this.mHintExpanded) {
                    return;
                }
                this.expandHint(b);
                return;
            }
        }
        if (!b2 && !this.mHintExpanded) {
            return;
        }
        this.collapseHint(b);
    }
    
    static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        CharSequence error;
        boolean isPasswordToggledVisible;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.error = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            final int int1 = parcel.readInt();
            boolean isPasswordToggledVisible = true;
            if (int1 != 1) {
                isPasswordToggledVisible = false;
            }
            this.isPasswordToggledVisible = isPasswordToggledVisible;
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("TextInputLayout.SavedState{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" error=");
            sb.append((Object)this.error);
            sb.append("}");
            return sb.toString();
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        }
    }
    
    private class TextInputAccessibilityDelegate extends AccessibilityDelegateCompat
    {
        TextInputAccessibilityDelegate() {
        }
        
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)TextInputLayout.class.getSimpleName());
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(TextInputLayout.class.getSimpleName());
            final CharSequence text = TextInputLayout.this.mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty(text)) {
                accessibilityNodeInfoCompat.setText(text);
            }
            if (TextInputLayout.this.mEditText != null) {
                accessibilityNodeInfoCompat.setLabelFor((View)TextInputLayout.this.mEditText);
            }
            CharSequence text2;
            if (TextInputLayout.this.mErrorView != null) {
                text2 = TextInputLayout.this.mErrorView.getText();
            }
            else {
                text2 = null;
            }
            if (!TextUtils.isEmpty(text2)) {
                accessibilityNodeInfoCompat.setContentInvalid(true);
                accessibilityNodeInfoCompat.setError(text2);
            }
        }
        
        @Override
        public void onPopulateAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onPopulateAccessibilityEvent(view, accessibilityEvent);
            final CharSequence text = TextInputLayout.this.mCollapsingTextHelper.getText();
            if (!TextUtils.isEmpty(text)) {
                accessibilityEvent.getText().add(text);
            }
        }
    }
}
