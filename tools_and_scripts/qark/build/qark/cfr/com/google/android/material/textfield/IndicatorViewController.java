/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.ObjectAnimator
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.text.TextUtils
 *  android.util.Property
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.EditText
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.Space
 *  android.widget.TextView
 *  com.google.android.material.R
 *  com.google.android.material.R$dimen
 *  com.google.android.material.R$id
 */
package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import java.util.List;

final class IndicatorViewController {
    private static final int CAPTION_OPACITY_FADE_ANIMATION_DURATION = 167;
    private static final int CAPTION_STATE_ERROR = 1;
    private static final int CAPTION_STATE_HELPER_TEXT = 2;
    private static final int CAPTION_STATE_NONE = 0;
    private static final int CAPTION_TRANSLATE_Y_ANIMATION_DURATION = 217;
    static final int COUNTER_INDEX = 2;
    static final int ERROR_INDEX = 0;
    static final int HELPER_INDEX = 1;
    private Animator captionAnimator;
    private FrameLayout captionArea;
    private int captionDisplayed;
    private int captionToShow;
    private final float captionTranslationYPx;
    private int captionViewsAdded;
    private final Context context;
    private boolean errorEnabled;
    private CharSequence errorText;
    private int errorTextAppearance;
    private TextView errorView;
    private ColorStateList errorViewTextColor;
    private CharSequence helperText;
    private boolean helperTextEnabled;
    private int helperTextTextAppearance;
    private TextView helperTextView;
    private ColorStateList helperTextViewTextColor;
    private LinearLayout indicatorArea;
    private int indicatorsAdded;
    private final TextInputLayout textInputView;
    private Typeface typeface;

    public IndicatorViewController(TextInputLayout textInputLayout) {
        Context context;
        this.context = context = textInputLayout.getContext();
        this.textInputView = textInputLayout;
        this.captionTranslationYPx = context.getResources().getDimensionPixelSize(R.dimen.design_textinput_caption_translate_y);
    }

    private boolean canAdjustIndicatorPadding() {
        if (this.indicatorArea != null && this.textInputView.getEditText() != null) {
            return true;
        }
        return false;
    }

    private void createCaptionAnimators(List<Animator> list, boolean bl, TextView textView, int n, int n2, int n3) {
        if (textView != null) {
            if (!bl) {
                return;
            }
            if (n == n3 || n == n2) {
                bl = n3 == n;
                list.add((Animator)this.createCaptionOpacityAnimator(textView, bl));
                if (n3 == n) {
                    list.add((Animator)this.createCaptionTranslationYAnimator(textView));
                }
            }
            return;
        }
    }

    private ObjectAnimator createCaptionOpacityAnimator(TextView textView, boolean bl) {
        float f = bl ? 1.0f : 0.0f;
        textView = ObjectAnimator.ofFloat((Object)textView, (Property)View.ALPHA, (float[])new float[]{f});
        textView.setDuration(167L);
        textView.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
        return textView;
    }

    private ObjectAnimator createCaptionTranslationYAnimator(TextView textView) {
        textView = ObjectAnimator.ofFloat((Object)textView, (Property)View.TRANSLATION_Y, (float[])new float[]{- this.captionTranslationYPx, 0.0f});
        textView.setDuration(217L);
        textView.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
        return textView;
    }

    private TextView getCaptionViewFromDisplayState(int n) {
        if (n != 1) {
            if (n != 2) {
                return null;
            }
            return this.helperTextView;
        }
        return this.errorView;
    }

    private boolean isCaptionStateError(int n) {
        if (n == 1 && this.errorView != null && !TextUtils.isEmpty((CharSequence)this.errorText)) {
            return true;
        }
        return false;
    }

    private boolean isCaptionStateHelperText(int n) {
        if (n == 2 && this.helperTextView != null && !TextUtils.isEmpty((CharSequence)this.helperText)) {
            return true;
        }
        return false;
    }

    private void setCaptionViewVisibilities(int n, int n2) {
        TextView textView;
        if (n == n2) {
            return;
        }
        if (n2 != 0 && (textView = this.getCaptionViewFromDisplayState(n2)) != null) {
            textView.setVisibility(0);
            textView.setAlpha(1.0f);
        }
        if (n != 0 && (textView = this.getCaptionViewFromDisplayState(n)) != null) {
            textView.setVisibility(4);
            if (n == 1) {
                textView.setText(null);
            }
        }
        this.captionDisplayed = n2;
    }

    private void setTextViewTypeface(TextView textView, Typeface typeface) {
        if (textView != null) {
            textView.setTypeface(typeface);
        }
    }

    private void setViewGroupGoneIfEmpty(ViewGroup viewGroup, int n) {
        if (n == 0) {
            viewGroup.setVisibility(8);
        }
    }

    private boolean shouldAnimateCaptionView(TextView textView, CharSequence charSequence) {
        if (ViewCompat.isLaidOut((View)this.textInputView) && this.textInputView.isEnabled() && (this.captionToShow != this.captionDisplayed || textView == null || !TextUtils.equals((CharSequence)textView.getText(), (CharSequence)charSequence))) {
            return true;
        }
        return false;
    }

    private void updateCaptionViewsVisibility(int n, final int n2, boolean bl) {
        if (bl) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.captionAnimator = animatorSet;
            ArrayList<Animator> arrayList = new ArrayList<Animator>();
            this.createCaptionAnimators(arrayList, this.helperTextEnabled, this.helperTextView, 2, n, n2);
            this.createCaptionAnimators(arrayList, this.errorEnabled, this.errorView, 1, n, n2);
            AnimatorSetCompat.playTogether(animatorSet, arrayList);
            animatorSet.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this.getCaptionViewFromDisplayState(n), n, this.getCaptionViewFromDisplayState(n2)){
                final /* synthetic */ int val$captionToHide;
                final /* synthetic */ TextView val$captionViewToHide;
                final /* synthetic */ TextView val$captionViewToShow;
                {
                    this.val$captionViewToHide = textView;
                    this.val$captionToHide = n22;
                    this.val$captionViewToShow = textView2;
                }

                public void onAnimationEnd(Animator animator2) {
                    IndicatorViewController.this.captionDisplayed = n2;
                    IndicatorViewController.this.captionAnimator = null;
                    animator2 = this.val$captionViewToHide;
                    if (animator2 != null) {
                        animator2.setVisibility(4);
                        if (this.val$captionToHide == 1 && IndicatorViewController.this.errorView != null) {
                            IndicatorViewController.this.errorView.setText(null);
                        }
                        if ((animator2 = this.val$captionViewToShow) != null) {
                            animator2.setTranslationY(0.0f);
                            this.val$captionViewToShow.setAlpha(1.0f);
                        }
                    }
                }

                public void onAnimationStart(Animator animator2) {
                    animator2 = this.val$captionViewToShow;
                    if (animator2 != null) {
                        animator2.setVisibility(0);
                    }
                }
            });
            animatorSet.start();
        } else {
            this.setCaptionViewVisibilities(n, n2);
        }
        this.textInputView.updateEditTextBackground();
        this.textInputView.updateLabelState(bl);
        this.textInputView.updateTextInputBoxState();
    }

    void addIndicator(TextView textView, int n) {
        if (this.indicatorArea == null && this.captionArea == null) {
            LinearLayout linearLayout;
            this.indicatorArea = linearLayout = new LinearLayout(this.context);
            linearLayout.setOrientation(0);
            this.textInputView.addView((View)this.indicatorArea, -1, -2);
            linearLayout = new FrameLayout(this.context);
            this.captionArea = linearLayout;
            this.indicatorArea.addView((View)linearLayout, -1, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -2));
            linearLayout = new Space(this.context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0, 1.0f);
            this.indicatorArea.addView((View)linearLayout, (ViewGroup.LayoutParams)layoutParams);
            if (this.textInputView.getEditText() != null) {
                this.adjustIndicatorPadding();
            }
        }
        if (this.isCaptionView(n)) {
            this.captionArea.setVisibility(0);
            this.captionArea.addView((View)textView);
            ++this.captionViewsAdded;
        } else {
            this.indicatorArea.addView((View)textView, n);
        }
        this.indicatorArea.setVisibility(0);
        ++this.indicatorsAdded;
    }

    void adjustIndicatorPadding() {
        if (this.canAdjustIndicatorPadding()) {
            ViewCompat.setPaddingRelative((View)this.indicatorArea, ViewCompat.getPaddingStart((View)this.textInputView.getEditText()), 0, ViewCompat.getPaddingEnd((View)this.textInputView.getEditText()), 0);
        }
    }

    void cancelCaptionAnimator() {
        Animator animator2 = this.captionAnimator;
        if (animator2 != null) {
            animator2.cancel();
        }
    }

    boolean errorIsDisplayed() {
        return this.isCaptionStateError(this.captionDisplayed);
    }

    boolean errorShouldBeShown() {
        return this.isCaptionStateError(this.captionToShow);
    }

    CharSequence getErrorText() {
        return this.errorText;
    }

    int getErrorViewCurrentTextColor() {
        TextView textView = this.errorView;
        if (textView != null) {
            return textView.getCurrentTextColor();
        }
        return -1;
    }

    ColorStateList getErrorViewTextColors() {
        TextView textView = this.errorView;
        if (textView != null) {
            return textView.getTextColors();
        }
        return null;
    }

    CharSequence getHelperText() {
        return this.helperText;
    }

    ColorStateList getHelperTextViewColors() {
        TextView textView = this.helperTextView;
        if (textView != null) {
            return textView.getTextColors();
        }
        return null;
    }

    int getHelperTextViewCurrentTextColor() {
        TextView textView = this.helperTextView;
        if (textView != null) {
            return textView.getCurrentTextColor();
        }
        return -1;
    }

    boolean helperTextIsDisplayed() {
        return this.isCaptionStateHelperText(this.captionDisplayed);
    }

    boolean helperTextShouldBeShown() {
        return this.isCaptionStateHelperText(this.captionToShow);
    }

    void hideError() {
        this.errorText = null;
        this.cancelCaptionAnimator();
        if (this.captionDisplayed == 1) {
            this.captionToShow = this.helperTextEnabled && !TextUtils.isEmpty((CharSequence)this.helperText) ? 2 : 0;
        }
        this.updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, this.shouldAnimateCaptionView(this.errorView, null));
    }

    void hideHelperText() {
        this.cancelCaptionAnimator();
        if (this.captionDisplayed == 2) {
            this.captionToShow = 0;
        }
        this.updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, this.shouldAnimateCaptionView(this.helperTextView, null));
    }

    boolean isCaptionView(int n) {
        boolean bl = true;
        if (n != 0) {
            if (n == 1) {
                return true;
            }
            bl = false;
        }
        return bl;
    }

    boolean isErrorEnabled() {
        return this.errorEnabled;
    }

    boolean isHelperTextEnabled() {
        return this.helperTextEnabled;
    }

    void removeIndicator(TextView textView, int n) {
        FrameLayout frameLayout;
        if (this.indicatorArea == null) {
            return;
        }
        if (this.isCaptionView(n) && (frameLayout = this.captionArea) != null) {
            this.captionViewsAdded = n = this.captionViewsAdded - 1;
            this.setViewGroupGoneIfEmpty((ViewGroup)frameLayout, n);
            this.captionArea.removeView((View)textView);
        } else {
            this.indicatorArea.removeView((View)textView);
        }
        this.indicatorsAdded = n = this.indicatorsAdded - 1;
        this.setViewGroupGoneIfEmpty((ViewGroup)this.indicatorArea, n);
    }

    void setErrorEnabled(boolean bl) {
        if (this.errorEnabled == bl) {
            return;
        }
        this.cancelCaptionAnimator();
        if (bl) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(this.context);
            this.errorView = appCompatTextView;
            appCompatTextView.setId(R.id.textinput_error);
            appCompatTextView = this.typeface;
            if (appCompatTextView != null) {
                this.errorView.setTypeface((Typeface)appCompatTextView);
            }
            this.setErrorTextAppearance(this.errorTextAppearance);
            this.setErrorViewTextColor(this.errorViewTextColor);
            this.errorView.setVisibility(4);
            ViewCompat.setAccessibilityLiveRegion((View)this.errorView, 1);
            this.addIndicator(this.errorView, 0);
        } else {
            this.hideError();
            this.removeIndicator(this.errorView, 0);
            this.errorView = null;
            this.textInputView.updateEditTextBackground();
            this.textInputView.updateTextInputBoxState();
        }
        this.errorEnabled = bl;
    }

    void setErrorTextAppearance(int n) {
        this.errorTextAppearance = n;
        TextView textView = this.errorView;
        if (textView != null) {
            this.textInputView.setTextAppearanceCompatWithErrorFallback(textView, n);
        }
    }

    void setErrorViewTextColor(ColorStateList colorStateList) {
        this.errorViewTextColor = colorStateList;
        TextView textView = this.errorView;
        if (textView != null && colorStateList != null) {
            textView.setTextColor(colorStateList);
        }
    }

    void setHelperTextAppearance(int n) {
        this.helperTextTextAppearance = n;
        TextView textView = this.helperTextView;
        if (textView != null) {
            TextViewCompat.setTextAppearance(textView, n);
        }
    }

    void setHelperTextEnabled(boolean bl) {
        if (this.helperTextEnabled == bl) {
            return;
        }
        this.cancelCaptionAnimator();
        if (bl) {
            AppCompatTextView appCompatTextView = new AppCompatTextView(this.context);
            this.helperTextView = appCompatTextView;
            appCompatTextView.setId(R.id.textinput_helper_text);
            appCompatTextView = this.typeface;
            if (appCompatTextView != null) {
                this.helperTextView.setTypeface((Typeface)appCompatTextView);
            }
            this.helperTextView.setVisibility(4);
            ViewCompat.setAccessibilityLiveRegion((View)this.helperTextView, 1);
            this.setHelperTextAppearance(this.helperTextTextAppearance);
            this.setHelperTextViewTextColor(this.helperTextViewTextColor);
            this.addIndicator(this.helperTextView, 1);
        } else {
            this.hideHelperText();
            this.removeIndicator(this.helperTextView, 1);
            this.helperTextView = null;
            this.textInputView.updateEditTextBackground();
            this.textInputView.updateTextInputBoxState();
        }
        this.helperTextEnabled = bl;
    }

    void setHelperTextViewTextColor(ColorStateList colorStateList) {
        this.helperTextViewTextColor = colorStateList;
        TextView textView = this.helperTextView;
        if (textView != null && colorStateList != null) {
            textView.setTextColor(colorStateList);
        }
    }

    void setTypefaces(Typeface typeface) {
        if (typeface != this.typeface) {
            this.typeface = typeface;
            this.setTextViewTypeface(this.errorView, typeface);
            this.setTextViewTypeface(this.helperTextView, typeface);
        }
    }

    void showError(CharSequence charSequence) {
        this.cancelCaptionAnimator();
        this.errorText = charSequence;
        this.errorView.setText(charSequence);
        if (this.captionDisplayed != 1) {
            this.captionToShow = 1;
        }
        this.updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, this.shouldAnimateCaptionView(this.errorView, charSequence));
    }

    void showHelper(CharSequence charSequence) {
        this.cancelCaptionAnimator();
        this.helperText = charSequence;
        this.helperTextView.setText(charSequence);
        if (this.captionDisplayed != 2) {
            this.captionToShow = 2;
        }
        this.updateCaptionViewsVisibility(this.captionDisplayed, this.captionToShow, this.shouldAnimateCaptionView(this.helperTextView, charSequence));
    }

}

