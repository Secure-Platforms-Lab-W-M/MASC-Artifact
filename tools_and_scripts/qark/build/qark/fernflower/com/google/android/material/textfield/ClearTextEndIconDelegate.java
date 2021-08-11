package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import androidx.appcompat.content.res.AppCompatResources;
import com.google.android.material.R.drawable;
import com.google.android.material.R.string;
import com.google.android.material.animation.AnimationUtils;

class ClearTextEndIconDelegate extends EndIconDelegate {
   private static final int ANIMATION_FADE_DURATION = 100;
   private static final int ANIMATION_SCALE_DURATION = 150;
   private static final float ANIMATION_SCALE_FROM_VALUE = 0.8F;
   private final TextWatcher clearTextEndIconTextWatcher = new TextWatcher() {
      public void afterTextChanged(Editable var1) {
         if (ClearTextEndIconDelegate.hasText(var1)) {
            if (!ClearTextEndIconDelegate.this.textInputLayout.isEndIconVisible()) {
               ClearTextEndIconDelegate.this.iconOutAnim.cancel();
               ClearTextEndIconDelegate.this.iconInAnim.start();
               return;
            }
         } else {
            ClearTextEndIconDelegate.this.iconInAnim.cancel();
            ClearTextEndIconDelegate.this.iconOutAnim.start();
         }

      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   };
   private final TextInputLayout.OnEditTextAttachedListener clearTextOnEditTextAttachedListener = new TextInputLayout.OnEditTextAttachedListener() {
      public void onEditTextAttached(TextInputLayout var1) {
         EditText var2 = var1.getEditText();
         var1.setEndIconVisible(ClearTextEndIconDelegate.hasText(var2.getText()));
         var1.setEndIconCheckable(false);
         var2.removeTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
         var2.addTextChangedListener(ClearTextEndIconDelegate.this.clearTextEndIconTextWatcher);
      }
   };
   private AnimatorSet iconInAnim;
   private ValueAnimator iconOutAnim;

   ClearTextEndIconDelegate(TextInputLayout var1) {
      super(var1);
   }

   private ValueAnimator getAlphaAnimator(float... var1) {
      ValueAnimator var2 = ValueAnimator.ofFloat(var1);
      var2.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
      var2.setDuration(100L);
      var2.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1) {
            float var2 = (Float)var1.getAnimatedValue();
            ClearTextEndIconDelegate.this.endIconView.setAlpha(var2);
         }
      });
      return var2;
   }

   private ValueAnimator getScaleAnimator() {
      ValueAnimator var1 = ValueAnimator.ofFloat(new float[]{0.8F, 1.0F});
      var1.setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
      var1.setDuration(150L);
      var1.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1) {
            float var2 = (Float)var1.getAnimatedValue();
            ClearTextEndIconDelegate.this.endIconView.setScaleX(var2);
            ClearTextEndIconDelegate.this.endIconView.setScaleY(var2);
         }
      });
      return var1;
   }

   private static boolean hasText(Editable var0) {
      return var0.length() > 0;
   }

   private void initAnimators() {
      ValueAnimator var1 = this.getScaleAnimator();
      ValueAnimator var2 = this.getAlphaAnimator(0.0F, 1.0F);
      AnimatorSet var3 = new AnimatorSet();
      this.iconInAnim = var3;
      var3.playTogether(new Animator[]{var1, var2});
      this.iconInAnim.addListener(new AnimatorListenerAdapter() {
         public void onAnimationStart(Animator var1) {
            ClearTextEndIconDelegate.this.textInputLayout.setEndIconVisible(true);
         }
      });
      var1 = this.getAlphaAnimator(1.0F, 0.0F);
      this.iconOutAnim = var1;
      var1.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            ClearTextEndIconDelegate.this.textInputLayout.setEndIconVisible(false);
         }
      });
   }

   void initialize() {
      this.textInputLayout.setEndIconDrawable(AppCompatResources.getDrawable(this.context, drawable.mtrl_ic_cancel));
      this.textInputLayout.setEndIconContentDescription(this.textInputLayout.getResources().getText(string.clear_text_end_icon_content_description));
      this.textInputLayout.setEndIconOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            ClearTextEndIconDelegate.this.textInputLayout.getEditText().setText((CharSequence)null);
         }
      });
      this.textInputLayout.addOnEditTextAttachedListener(this.clearTextOnEditTextAttachedListener);
      this.initAnimators();
   }
}
