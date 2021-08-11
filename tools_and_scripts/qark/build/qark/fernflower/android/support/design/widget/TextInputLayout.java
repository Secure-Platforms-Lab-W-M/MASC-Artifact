package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.annotation.VisibleForTesting;
import android.support.design.R$id;
import android.support.design.R$layout;
import android.support.design.R$string;
import android.support.design.R$style;
import android.support.design.R$styleable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.widget.Space;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.R$color;
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
import android.view.ViewStructure;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class TextInputLayout extends LinearLayout {
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
   private Mode mPasswordToggleTintMode;
   private CheckableImageButton mPasswordToggleView;
   private boolean mPasswordToggledVisible;
   private boolean mRestoringSavedState;
   private Paint mTmpPaint;
   private final Rect mTmpRect;
   private Typeface mTypeface;

   public TextInputLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TextInputLayout(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public TextInputLayout(Context var1, AttributeSet var2, int var3) {
      super(var1, var2);
      this.mTmpRect = new Rect();
      this.mCollapsingTextHelper = new CollapsingTextHelper(this);
      ThemeUtils.checkAppCompatTheme(var1);
      this.setOrientation(1);
      this.setWillNotDraw(false);
      this.setAddStatesFromChildren(true);
      this.mInputFrame = new FrameLayout(var1);
      this.mInputFrame.setAddStatesFromChildren(true);
      this.addView(this.mInputFrame);
      this.mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
      this.mCollapsingTextHelper.setPositionInterpolator(new AccelerateInterpolator());
      this.mCollapsingTextHelper.setCollapsedTextGravity(8388659);
      TintTypedArray var6 = TintTypedArray.obtainStyledAttributes(var1, var2, R$styleable.TextInputLayout, var3, R$style.Widget_Design_TextInputLayout);
      this.mHintEnabled = var6.getBoolean(R$styleable.TextInputLayout_hintEnabled, true);
      this.setHint(var6.getText(R$styleable.TextInputLayout_android_hint));
      this.mHintAnimationEnabled = var6.getBoolean(R$styleable.TextInputLayout_hintAnimationEnabled, true);
      if (var6.hasValue(R$styleable.TextInputLayout_android_textColorHint)) {
         ColorStateList var7 = var6.getColorStateList(R$styleable.TextInputLayout_android_textColorHint);
         this.mFocusedTextColor = var7;
         this.mDefaultTextColor = var7;
      }

      if (var6.getResourceId(R$styleable.TextInputLayout_hintTextAppearance, -1) != -1) {
         this.setHintTextAppearance(var6.getResourceId(R$styleable.TextInputLayout_hintTextAppearance, 0));
      }

      this.mErrorTextAppearance = var6.getResourceId(R$styleable.TextInputLayout_errorTextAppearance, 0);
      boolean var4 = var6.getBoolean(R$styleable.TextInputLayout_errorEnabled, false);
      boolean var5 = var6.getBoolean(R$styleable.TextInputLayout_counterEnabled, false);
      this.setCounterMaxLength(var6.getInt(R$styleable.TextInputLayout_counterMaxLength, -1));
      this.mCounterTextAppearance = var6.getResourceId(R$styleable.TextInputLayout_counterTextAppearance, 0);
      this.mCounterOverflowTextAppearance = var6.getResourceId(R$styleable.TextInputLayout_counterOverflowTextAppearance, 0);
      this.mPasswordToggleEnabled = var6.getBoolean(R$styleable.TextInputLayout_passwordToggleEnabled, false);
      this.mPasswordToggleDrawable = var6.getDrawable(R$styleable.TextInputLayout_passwordToggleDrawable);
      this.mPasswordToggleContentDesc = var6.getText(R$styleable.TextInputLayout_passwordToggleContentDescription);
      if (var6.hasValue(R$styleable.TextInputLayout_passwordToggleTint)) {
         this.mHasPasswordToggleTintList = true;
         this.mPasswordToggleTintList = var6.getColorStateList(R$styleable.TextInputLayout_passwordToggleTint);
      }

      if (var6.hasValue(R$styleable.TextInputLayout_passwordToggleTintMode)) {
         this.mHasPasswordToggleTintMode = true;
         this.mPasswordToggleTintMode = ViewUtils.parseTintMode(var6.getInt(R$styleable.TextInputLayout_passwordToggleTintMode, -1), (Mode)null);
      }

      var6.recycle();
      this.setErrorEnabled(var4);
      this.setCounterEnabled(var5);
      this.applyPasswordToggleTint();
      if (ViewCompat.getImportantForAccessibility(this) == 0) {
         ViewCompat.setImportantForAccessibility(this, 1);
      }

      ViewCompat.setAccessibilityDelegate(this, new TextInputLayout.TextInputAccessibilityDelegate());
   }

   private void addIndicator(TextView var1, int var2) {
      if (this.mIndicatorArea == null) {
         this.mIndicatorArea = new LinearLayout(this.getContext());
         this.mIndicatorArea.setOrientation(0);
         this.addView(this.mIndicatorArea, -1, -2);
         Space var3 = new Space(this.getContext());
         LayoutParams var4 = new LayoutParams(0, 0, 1.0F);
         this.mIndicatorArea.addView(var3, var4);
         if (this.mEditText != null) {
            this.adjustIndicatorPadding();
         }
      }

      this.mIndicatorArea.setVisibility(0);
      this.mIndicatorArea.addView(var1, var2);
      ++this.mIndicatorsAdded;
   }

   private void adjustIndicatorPadding() {
      ViewCompat.setPaddingRelative(this.mIndicatorArea, ViewCompat.getPaddingStart(this.mEditText), 0, ViewCompat.getPaddingEnd(this.mEditText), this.mEditText.getPaddingBottom());
   }

   private void applyPasswordToggleTint() {
      if (this.mPasswordToggleDrawable != null && (this.mHasPasswordToggleTintList || this.mHasPasswordToggleTintMode)) {
         this.mPasswordToggleDrawable = DrawableCompat.wrap(this.mPasswordToggleDrawable).mutate();
         if (this.mHasPasswordToggleTintList) {
            DrawableCompat.setTintList(this.mPasswordToggleDrawable, this.mPasswordToggleTintList);
         }

         if (this.mHasPasswordToggleTintMode) {
            DrawableCompat.setTintMode(this.mPasswordToggleDrawable, this.mPasswordToggleTintMode);
         }

         CheckableImageButton var1 = this.mPasswordToggleView;
         if (var1 != null) {
            Drawable var3 = var1.getDrawable();
            Drawable var2 = this.mPasswordToggleDrawable;
            if (var3 != var2) {
               this.mPasswordToggleView.setImageDrawable(var2);
            }
         }
      }
   }

   private static boolean arrayContains(int[] var0, int var1) {
      int var3 = var0.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var0[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   private void collapseHint(boolean var1) {
      ValueAnimator var2 = this.mAnimator;
      if (var2 != null && var2.isRunning()) {
         this.mAnimator.cancel();
      }

      if (var1 && this.mHintAnimationEnabled) {
         this.animateToExpansionFraction(1.0F);
      } else {
         this.mCollapsingTextHelper.setExpansionFraction(1.0F);
      }

      this.mHintExpanded = false;
   }

   private void ensureBackgroundDrawableStateWorkaround() {
      int var1 = VERSION.SDK_INT;
      if (var1 == 21 || var1 == 22) {
         Drawable var2 = this.mEditText.getBackground();
         if (var2 != null) {
            if (!this.mHasReconstructedEditTextBackground) {
               Drawable var3 = var2.getConstantState().newDrawable();
               if (var2 instanceof DrawableContainer) {
                  this.mHasReconstructedEditTextBackground = DrawableUtils.setContainerConstantState((DrawableContainer)var2, var3.getConstantState());
               }

               if (!this.mHasReconstructedEditTextBackground) {
                  ViewCompat.setBackground(this.mEditText, var3);
                  this.mHasReconstructedEditTextBackground = true;
               }
            }
         }
      }
   }

   private void expandHint(boolean var1) {
      ValueAnimator var2 = this.mAnimator;
      if (var2 != null && var2.isRunning()) {
         this.mAnimator.cancel();
      }

      if (var1 && this.mHintAnimationEnabled) {
         this.animateToExpansionFraction(0.0F);
      } else {
         this.mCollapsingTextHelper.setExpansionFraction(0.0F);
      }

      this.mHintExpanded = true;
   }

   private boolean hasPasswordTransformation() {
      EditText var1 = this.mEditText;
      return var1 != null && var1.getTransformationMethod() instanceof PasswordTransformationMethod;
   }

   private void passwordVisibilityToggleRequested(boolean var1) {
      if (this.mPasswordToggleEnabled) {
         int var2 = this.mEditText.getSelectionEnd();
         if (this.hasPasswordTransformation()) {
            this.mEditText.setTransformationMethod((TransformationMethod)null);
            this.mPasswordToggledVisible = true;
         } else {
            this.mEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            this.mPasswordToggledVisible = false;
         }

         this.mPasswordToggleView.setChecked(this.mPasswordToggledVisible);
         if (var1) {
            this.mPasswordToggleView.jumpDrawablesToCurrentState();
         }

         this.mEditText.setSelection(var2);
      }
   }

   private static void recursiveSetEnabled(ViewGroup var0, boolean var1) {
      int var2 = 0;

      for(int var3 = var0.getChildCount(); var2 < var3; ++var2) {
         View var4 = var0.getChildAt(var2);
         var4.setEnabled(var1);
         if (var4 instanceof ViewGroup) {
            recursiveSetEnabled((ViewGroup)var4, var1);
         }
      }

   }

   private void removeIndicator(TextView var1) {
      LinearLayout var3 = this.mIndicatorArea;
      if (var3 != null) {
         var3.removeView(var1);
         int var2 = this.mIndicatorsAdded - 1;
         this.mIndicatorsAdded = var2;
         if (var2 == 0) {
            this.mIndicatorArea.setVisibility(8);
         }
      }
   }

   private void setEditText(EditText var1) {
      if (this.mEditText == null) {
         if (!(var1 instanceof TextInputEditText)) {
            Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead.");
         }

         this.mEditText = var1;
         if (!this.hasPasswordTransformation()) {
            this.mCollapsingTextHelper.setTypefaces(this.mEditText.getTypeface());
         }

         this.mCollapsingTextHelper.setExpandedTextSize(this.mEditText.getTextSize());
         int var2 = this.mEditText.getGravity();
         this.mCollapsingTextHelper.setCollapsedTextGravity(var2 & -113 | 48);
         this.mCollapsingTextHelper.setExpandedTextGravity(var2);
         this.mEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               TextInputLayout var2 = TextInputLayout.this;
               var2.updateLabelState(var2.mRestoringSavedState ^ true);
               if (TextInputLayout.this.mCounterEnabled) {
                  TextInputLayout.this.updateCounter(var1.length());
               }
            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         if (this.mDefaultTextColor == null) {
            this.mDefaultTextColor = this.mEditText.getHintTextColors();
         }

         if (this.mHintEnabled && TextUtils.isEmpty(this.mHint)) {
            this.mOriginalHint = this.mEditText.getHint();
            this.setHint(this.mOriginalHint);
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
      } else {
         throw new IllegalArgumentException("We already have an EditText, can only have one");
      }
   }

   private void setError(@Nullable final CharSequence var1, boolean var2) {
      this.mError = var1;
      if (!this.mErrorEnabled) {
         if (TextUtils.isEmpty(var1)) {
            return;
         }

         this.setErrorEnabled(true);
      }

      this.mErrorShown = TextUtils.isEmpty(var1) ^ true;
      this.mErrorView.animate().cancel();
      if (this.mErrorShown) {
         this.mErrorView.setText(var1);
         this.mErrorView.setVisibility(0);
         if (var2) {
            if (this.mErrorView.getAlpha() == 1.0F) {
               this.mErrorView.setAlpha(0.0F);
            }

            this.mErrorView.animate().alpha(1.0F).setDuration(200L).setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
               public void onAnimationStart(Animator var1) {
                  TextInputLayout.this.mErrorView.setVisibility(0);
               }
            }).start();
         } else {
            this.mErrorView.setAlpha(1.0F);
         }
      } else if (this.mErrorView.getVisibility() == 0) {
         if (var2) {
            this.mErrorView.animate().alpha(0.0F).setDuration(200L).setInterpolator(AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1x) {
                  TextInputLayout.this.mErrorView.setText(var1);
                  TextInputLayout.this.mErrorView.setVisibility(4);
               }
            }).start();
         } else {
            this.mErrorView.setText(var1);
            this.mErrorView.setVisibility(4);
         }
      }

      this.updateEditTextBackground();
      this.updateLabelState(var2);
   }

   private void setHintInternal(CharSequence var1) {
      this.mHint = var1;
      this.mCollapsingTextHelper.setText(var1);
   }

   private boolean shouldShowPasswordIcon() {
      return this.mPasswordToggleEnabled && (this.hasPasswordTransformation() || this.mPasswordToggledVisible);
   }

   private void updateEditTextBackground() {
      EditText var1 = this.mEditText;
      if (var1 != null) {
         Drawable var3 = var1.getBackground();
         if (var3 != null) {
            this.ensureBackgroundDrawableStateWorkaround();
            if (android.support.v7.widget.DrawableUtils.canSafelyMutateDrawable(var3)) {
               var3 = var3.mutate();
            }

            TextView var2;
            if (this.mErrorShown) {
               var2 = this.mErrorView;
               if (var2 != null) {
                  var3.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(var2.getCurrentTextColor(), Mode.SRC_IN));
                  return;
               }
            }

            if (this.mCounterOverflowed) {
               var2 = this.mCounterView;
               if (var2 != null) {
                  var3.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(var2.getCurrentTextColor(), Mode.SRC_IN));
                  return;
               }
            }

            DrawableCompat.clearColorFilter(var3);
            this.mEditText.refreshDrawableState();
         }
      }
   }

   private void updateInputLayoutMargins() {
      LayoutParams var2 = (LayoutParams)this.mInputFrame.getLayoutParams();
      int var1;
      if (this.mHintEnabled) {
         if (this.mTmpPaint == null) {
            this.mTmpPaint = new Paint();
         }

         this.mTmpPaint.setTypeface(this.mCollapsingTextHelper.getCollapsedTypeface());
         this.mTmpPaint.setTextSize(this.mCollapsingTextHelper.getCollapsedTextSize());
         var1 = (int)(-this.mTmpPaint.ascent());
      } else {
         var1 = 0;
      }

      if (var1 != var2.topMargin) {
         var2.topMargin = var1;
         this.mInputFrame.requestLayout();
      }
   }

   private void updatePasswordToggleView() {
      if (this.mEditText != null) {
         Drawable[] var2;
         if (this.shouldShowPasswordIcon()) {
            if (this.mPasswordToggleView == null) {
               this.mPasswordToggleView = (CheckableImageButton)LayoutInflater.from(this.getContext()).inflate(R$layout.design_text_input_password_icon, this.mInputFrame, false);
               this.mPasswordToggleView.setImageDrawable(this.mPasswordToggleDrawable);
               this.mPasswordToggleView.setContentDescription(this.mPasswordToggleContentDesc);
               this.mInputFrame.addView(this.mPasswordToggleView);
               this.mPasswordToggleView.setOnClickListener(new OnClickListener() {
                  public void onClick(View var1) {
                     TextInputLayout.this.passwordVisibilityToggleRequested(false);
                  }
               });
            }

            EditText var3 = this.mEditText;
            if (var3 != null && ViewCompat.getMinimumHeight(var3) <= 0) {
               this.mEditText.setMinimumHeight(ViewCompat.getMinimumHeight(this.mPasswordToggleView));
            }

            this.mPasswordToggleView.setVisibility(0);
            this.mPasswordToggleView.setChecked(this.mPasswordToggledVisible);
            if (this.mPasswordToggleDummyDrawable == null) {
               this.mPasswordToggleDummyDrawable = new ColorDrawable();
            }

            this.mPasswordToggleDummyDrawable.setBounds(0, 0, this.mPasswordToggleView.getMeasuredWidth(), 1);
            var2 = TextViewCompat.getCompoundDrawablesRelative(this.mEditText);
            if (var2[2] != this.mPasswordToggleDummyDrawable) {
               this.mOriginalEditTextEndDrawable = var2[2];
            }

            TextViewCompat.setCompoundDrawablesRelative(this.mEditText, var2[0], var2[1], this.mPasswordToggleDummyDrawable, var2[3]);
            this.mPasswordToggleView.setPadding(this.mEditText.getPaddingLeft(), this.mEditText.getPaddingTop(), this.mEditText.getPaddingRight(), this.mEditText.getPaddingBottom());
         } else {
            CheckableImageButton var1 = this.mPasswordToggleView;
            if (var1 != null && var1.getVisibility() == 0) {
               this.mPasswordToggleView.setVisibility(8);
            }

            if (this.mPasswordToggleDummyDrawable != null) {
               var2 = TextViewCompat.getCompoundDrawablesRelative(this.mEditText);
               if (var2[2] == this.mPasswordToggleDummyDrawable) {
                  TextViewCompat.setCompoundDrawablesRelative(this.mEditText, var2[0], var2[1], this.mOriginalEditTextEndDrawable, var2[3]);
                  this.mPasswordToggleDummyDrawable = null;
               }
            }
         }
      }
   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      if (var1 instanceof EditText) {
         android.widget.FrameLayout.LayoutParams var4 = new android.widget.FrameLayout.LayoutParams(var3);
         var4.gravity = var4.gravity & -113 | 16;
         this.mInputFrame.addView(var1, var4);
         this.mInputFrame.setLayoutParams(var3);
         this.updateInputLayoutMargins();
         this.setEditText((EditText)var1);
      } else {
         super.addView(var1, var2, var3);
      }
   }

   @VisibleForTesting
   void animateToExpansionFraction(float var1) {
      if (this.mCollapsingTextHelper.getExpansionFraction() != var1) {
         if (this.mAnimator == null) {
            this.mAnimator = new ValueAnimator();
            this.mAnimator.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
            this.mAnimator.setDuration(200L);
            this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
               public void onAnimationUpdate(ValueAnimator var1) {
                  TextInputLayout.this.mCollapsingTextHelper.setExpansionFraction((Float)var1.getAnimatedValue());
               }
            });
         }

         this.mAnimator.setFloatValues(new float[]{this.mCollapsingTextHelper.getExpansionFraction(), var1});
         this.mAnimator.start();
      }
   }

   public void dispatchProvideAutofillStructure(ViewStructure var1, int var2) {
      if (this.mOriginalHint != null) {
         EditText var3 = this.mEditText;
         if (var3 != null) {
            CharSequence var6 = var3.getHint();
            this.mEditText.setHint(this.mOriginalHint);

            try {
               super.dispatchProvideAutofillStructure(var1, var2);
            } finally {
               this.mEditText.setHint(var6);
            }

            return;
         }
      }

      super.dispatchProvideAutofillStructure(var1, var2);
   }

   protected void dispatchRestoreInstanceState(SparseArray var1) {
      this.mRestoringSavedState = true;
      super.dispatchRestoreInstanceState(var1);
      this.mRestoringSavedState = false;
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      if (this.mHintEnabled) {
         this.mCollapsingTextHelper.draw(var1);
      }
   }

   protected void drawableStateChanged() {
      if (!this.mInDrawableStateChanged) {
         boolean var2 = true;
         this.mInDrawableStateChanged = true;
         super.drawableStateChanged();
         int[] var3 = this.getDrawableState();
         boolean var1 = false;
         if (!ViewCompat.isLaidOut(this) || !this.isEnabled()) {
            var2 = false;
         }

         this.updateLabelState(var2);
         this.updateEditTextBackground();
         CollapsingTextHelper var4 = this.mCollapsingTextHelper;
         if (var4 != null) {
            var1 = false | var4.setState(var3);
         }

         if (var1) {
            this.invalidate();
         }

         this.mInDrawableStateChanged = false;
      }
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
      return this.mErrorEnabled ? this.mError : null;
   }

   @Nullable
   public CharSequence getHint() {
      return this.mHintEnabled ? this.mHint : null;
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

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (this.mHintEnabled) {
         EditText var6 = this.mEditText;
         if (var6 != null) {
            Rect var7 = this.mTmpRect;
            ViewGroupUtils.getDescendantRect(this, var6, var7);
            var2 = var7.left + this.mEditText.getCompoundPaddingLeft();
            var4 = var7.right - this.mEditText.getCompoundPaddingRight();
            this.mCollapsingTextHelper.setExpandedBounds(var2, var7.top + this.mEditText.getCompoundPaddingTop(), var4, var7.bottom - this.mEditText.getCompoundPaddingBottom());
            this.mCollapsingTextHelper.setCollapsedBounds(var2, this.getPaddingTop(), var4, var5 - var3 - this.getPaddingBottom());
            this.mCollapsingTextHelper.recalculate();
            return;
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      this.updatePasswordToggleView();
      super.onMeasure(var1, var2);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof TextInputLayout.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         TextInputLayout.SavedState var2 = (TextInputLayout.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.setError(var2.error);
         if (var2.isPasswordToggledVisible) {
            this.passwordVisibilityToggleRequested(true);
         }

         this.requestLayout();
      }
   }

   public Parcelable onSaveInstanceState() {
      TextInputLayout.SavedState var1 = new TextInputLayout.SavedState(super.onSaveInstanceState());
      if (this.mErrorShown) {
         var1.error = this.getError();
      }

      var1.isPasswordToggledVisible = this.mPasswordToggledVisible;
      return var1;
   }

   public void setCounterEnabled(boolean var1) {
      if (this.mCounterEnabled != var1) {
         if (var1) {
            this.mCounterView = new AppCompatTextView(this.getContext());
            this.mCounterView.setId(R$id.textinput_counter);
            Typeface var2 = this.mTypeface;
            if (var2 != null) {
               this.mCounterView.setTypeface(var2);
            }

            this.mCounterView.setMaxLines(1);

            try {
               TextViewCompat.setTextAppearance(this.mCounterView, this.mCounterTextAppearance);
            } catch (Exception var3) {
               TextViewCompat.setTextAppearance(this.mCounterView, android.support.v7.appcompat.R$style.TextAppearance_AppCompat_Caption);
               this.mCounterView.setTextColor(ContextCompat.getColor(this.getContext(), R$color.error_color_material));
            }

            this.addIndicator(this.mCounterView, -1);
            EditText var4 = this.mEditText;
            if (var4 == null) {
               this.updateCounter(0);
            } else {
               this.updateCounter(var4.getText().length());
            }
         } else {
            this.removeIndicator(this.mCounterView);
            this.mCounterView = null;
         }

         this.mCounterEnabled = var1;
      }
   }

   public void setCounterMaxLength(int var1) {
      if (this.mCounterMaxLength != var1) {
         if (var1 > 0) {
            this.mCounterMaxLength = var1;
         } else {
            this.mCounterMaxLength = -1;
         }

         if (this.mCounterEnabled) {
            EditText var2 = this.mEditText;
            if (var2 == null) {
               var1 = 0;
            } else {
               var1 = var2.getText().length();
            }

            this.updateCounter(var1);
         }
      }
   }

   public void setEnabled(boolean var1) {
      recursiveSetEnabled(this, var1);
      super.setEnabled(var1);
   }

   public void setError(@Nullable CharSequence var1) {
      boolean var2;
      label20: {
         if (ViewCompat.isLaidOut(this) && this.isEnabled()) {
            TextView var3 = this.mErrorView;
            if (var3 == null || !TextUtils.equals(var3.getText(), var1)) {
               var2 = true;
               break label20;
            }
         }

         var2 = false;
      }

      this.setError(var1, var2);
   }

   public void setErrorEnabled(boolean var1) {
      if (this.mErrorEnabled != var1) {
         TextView var4 = this.mErrorView;
         if (var4 != null) {
            var4.animate().cancel();
         }

         if (var1) {
            this.mErrorView = new AppCompatTextView(this.getContext());
            this.mErrorView.setId(R$id.textinput_error);
            Typeface var6 = this.mTypeface;
            if (var6 != null) {
               this.mErrorView.setTypeface(var6);
            }

            boolean var2 = false;

            label34: {
               int var3;
               try {
                  TextViewCompat.setTextAppearance(this.mErrorView, this.mErrorTextAppearance);
                  if (VERSION.SDK_INT < 23) {
                     break label34;
                  }

                  var3 = this.mErrorView.getTextColors().getDefaultColor();
               } catch (Exception var5) {
                  var2 = true;
                  break label34;
               }

               if (var3 == -65281) {
                  var2 = true;
               }
            }

            if (var2) {
               TextViewCompat.setTextAppearance(this.mErrorView, android.support.v7.appcompat.R$style.TextAppearance_AppCompat_Caption);
               this.mErrorView.setTextColor(ContextCompat.getColor(this.getContext(), R$color.error_color_material));
            }

            this.mErrorView.setVisibility(4);
            ViewCompat.setAccessibilityLiveRegion(this.mErrorView, 1);
            this.addIndicator(this.mErrorView, 0);
         } else {
            this.mErrorShown = false;
            this.updateEditTextBackground();
            this.removeIndicator(this.mErrorView);
            this.mErrorView = null;
         }

         this.mErrorEnabled = var1;
      }
   }

   public void setErrorTextAppearance(@StyleRes int var1) {
      this.mErrorTextAppearance = var1;
      TextView var2 = this.mErrorView;
      if (var2 != null) {
         TextViewCompat.setTextAppearance(var2, var1);
      }
   }

   public void setHint(@Nullable CharSequence var1) {
      if (this.mHintEnabled) {
         this.setHintInternal(var1);
         this.sendAccessibilityEvent(2048);
      }
   }

   public void setHintAnimationEnabled(boolean var1) {
      this.mHintAnimationEnabled = var1;
   }

   public void setHintEnabled(boolean var1) {
      if (var1 != this.mHintEnabled) {
         this.mHintEnabled = var1;
         CharSequence var2 = this.mEditText.getHint();
         if (!this.mHintEnabled) {
            if (!TextUtils.isEmpty(this.mHint) && TextUtils.isEmpty(var2)) {
               this.mEditText.setHint(this.mHint);
            }

            this.setHintInternal((CharSequence)null);
         } else if (!TextUtils.isEmpty(var2)) {
            if (TextUtils.isEmpty(this.mHint)) {
               this.setHint(var2);
            }

            this.mEditText.setHint((CharSequence)null);
         }

         if (this.mEditText != null) {
            this.updateInputLayoutMargins();
         }
      }
   }

   public void setHintTextAppearance(@StyleRes int var1) {
      this.mCollapsingTextHelper.setCollapsedTextAppearance(var1);
      this.mFocusedTextColor = this.mCollapsingTextHelper.getCollapsedTextColor();
      if (this.mEditText != null) {
         this.updateLabelState(false);
         this.updateInputLayoutMargins();
      }
   }

   public void setPasswordVisibilityToggleContentDescription(@StringRes int var1) {
      CharSequence var2;
      if (var1 != 0) {
         var2 = this.getResources().getText(var1);
      } else {
         var2 = null;
      }

      this.setPasswordVisibilityToggleContentDescription(var2);
   }

   public void setPasswordVisibilityToggleContentDescription(@Nullable CharSequence var1) {
      this.mPasswordToggleContentDesc = var1;
      CheckableImageButton var2 = this.mPasswordToggleView;
      if (var2 != null) {
         var2.setContentDescription(var1);
      }
   }

   public void setPasswordVisibilityToggleDrawable(@DrawableRes int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setPasswordVisibilityToggleDrawable(var2);
   }

   public void setPasswordVisibilityToggleDrawable(@Nullable Drawable var1) {
      this.mPasswordToggleDrawable = var1;
      CheckableImageButton var2 = this.mPasswordToggleView;
      if (var2 != null) {
         var2.setImageDrawable(var1);
      }
   }

   public void setPasswordVisibilityToggleEnabled(boolean var1) {
      if (this.mPasswordToggleEnabled != var1) {
         this.mPasswordToggleEnabled = var1;
         if (!var1 && this.mPasswordToggledVisible) {
            EditText var2 = this.mEditText;
            if (var2 != null) {
               var2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
         }

         this.mPasswordToggledVisible = false;
         this.updatePasswordToggleView();
      }
   }

   public void setPasswordVisibilityToggleTintList(@Nullable ColorStateList var1) {
      this.mPasswordToggleTintList = var1;
      this.mHasPasswordToggleTintList = true;
      this.applyPasswordToggleTint();
   }

   public void setPasswordVisibilityToggleTintMode(@Nullable Mode var1) {
      this.mPasswordToggleTintMode = var1;
      this.mHasPasswordToggleTintMode = true;
      this.applyPasswordToggleTint();
   }

   public void setTypeface(@Nullable Typeface var1) {
      Typeface var2 = this.mTypeface;
      if (var2 != null && !var2.equals(var1) || this.mTypeface == null && var1 != null) {
         this.mTypeface = var1;
         this.mCollapsingTextHelper.setTypefaces(var1);
         TextView var3 = this.mCounterView;
         if (var3 != null) {
            var3.setTypeface(var1);
         }

         var3 = this.mErrorView;
         if (var3 != null) {
            var3.setTypeface(var1);
         }
      }
   }

   void updateCounter(int var1) {
      boolean var4 = this.mCounterOverflowed;
      int var2 = this.mCounterMaxLength;
      if (var2 == -1) {
         this.mCounterView.setText(String.valueOf(var1));
         this.mCounterOverflowed = false;
      } else {
         boolean var3;
         if (var1 > var2) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.mCounterOverflowed = var3;
         var3 = this.mCounterOverflowed;
         if (var4 != var3) {
            TextView var5 = this.mCounterView;
            if (var3) {
               var2 = this.mCounterOverflowTextAppearance;
            } else {
               var2 = this.mCounterTextAppearance;
            }

            TextViewCompat.setTextAppearance(var5, var2);
         }

         this.mCounterView.setText(this.getContext().getString(R$string.character_counter_pattern, new Object[]{var1, this.mCounterMaxLength}));
      }

      if (this.mEditText != null && var4 != this.mCounterOverflowed) {
         this.updateLabelState(false);
         this.updateEditTextBackground();
      }
   }

   void updateLabelState(boolean var1) {
      this.updateLabelState(var1, false);
   }

   void updateLabelState(boolean var1, boolean var2) {
      boolean var4 = this.isEnabled();
      EditText var7 = this.mEditText;
      boolean var3;
      if (var7 != null && !TextUtils.isEmpty(var7.getText())) {
         var3 = true;
      } else {
         var3 = false;
      }

      boolean var5 = arrayContains(this.getDrawableState(), 16842908);
      boolean var6 = TextUtils.isEmpty(this.getError());
      ColorStateList var8 = this.mDefaultTextColor;
      if (var8 != null) {
         this.mCollapsingTextHelper.setExpandedTextColor(var8);
      }

      label70: {
         if (var4 && this.mCounterOverflowed) {
            TextView var9 = this.mCounterView;
            if (var9 != null) {
               this.mCollapsingTextHelper.setCollapsedTextColor(var9.getTextColors());
               break label70;
            }
         }

         if (var4 && var5) {
            var8 = this.mFocusedTextColor;
            if (var8 != null) {
               this.mCollapsingTextHelper.setCollapsedTextColor(var8);
               break label70;
            }
         }

         var8 = this.mDefaultTextColor;
         if (var8 != null) {
            this.mCollapsingTextHelper.setCollapsedTextColor(var8);
         }
      }

      if (var3 || this.isEnabled() && (var5 || true ^ var6)) {
         if (var2 || this.mHintExpanded) {
            this.collapseHint(var1);
         }
      } else if (var2 || !this.mHintExpanded) {
         this.expandHint(var1);
      }
   }

   static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public TextInputLayout.SavedState createFromParcel(Parcel var1) {
            return new TextInputLayout.SavedState(var1, (ClassLoader)null);
         }

         public TextInputLayout.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new TextInputLayout.SavedState(var1, var2);
         }

         public TextInputLayout.SavedState[] newArray(int var1) {
            return new TextInputLayout.SavedState[var1];
         }
      };
      CharSequence error;
      boolean isPasswordToggledVisible;

      SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.error = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
         int var3 = var1.readInt();
         boolean var4 = true;
         if (var3 != 1) {
            var4 = false;
         }

         this.isPasswordToggledVisible = var4;
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("TextInputLayout.SavedState{");
         var1.append(Integer.toHexString(System.identityHashCode(this)));
         var1.append(" error=");
         var1.append(this.error);
         var1.append("}");
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }
   }

   private class TextInputAccessibilityDelegate extends AccessibilityDelegateCompat {
      TextInputAccessibilityDelegate() {
      }

      public void onInitializeAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onInitializeAccessibilityEvent(var1, var2);
         var2.setClassName(TextInputLayout.class.getSimpleName());
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         var2.setClassName(TextInputLayout.class.getSimpleName());
         CharSequence var3 = TextInputLayout.this.mCollapsingTextHelper.getText();
         if (!TextUtils.isEmpty(var3)) {
            var2.setText(var3);
         }

         if (TextInputLayout.this.mEditText != null) {
            var2.setLabelFor(TextInputLayout.this.mEditText);
         }

         if (TextInputLayout.this.mErrorView != null) {
            var3 = TextInputLayout.this.mErrorView.getText();
         } else {
            var3 = null;
         }

         if (!TextUtils.isEmpty(var3)) {
            var2.setContentInvalid(true);
            var2.setError(var3);
         }
      }

      public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
         super.onPopulateAccessibilityEvent(var1, var2);
         CharSequence var3 = TextInputLayout.this.mCollapsingTextHelper.getText();
         if (!TextUtils.isEmpty(var3)) {
            var2.getText().add(var3);
         }
      }
   }
}
