package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AutoCompleteTextView.OnDismissListener;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.drawable;
import com.google.android.material.R.string;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

class DropdownMenuEndIconDelegate extends EndIconDelegate {
   private static final int ANIMATION_FADE_IN_DURATION = 67;
   private static final int ANIMATION_FADE_OUT_DURATION = 50;
   private static final boolean IS_LOLLIPOP;
   private final TextInputLayout.AccessibilityDelegate accessibilityDelegate;
   private AccessibilityManager accessibilityManager;
   private final TextInputLayout.OnEditTextAttachedListener dropdownMenuOnEditTextAttachedListener;
   private long dropdownPopupActivatedAt;
   private boolean dropdownPopupDirty;
   private final TextWatcher exposedDropdownEndIconTextWatcher = new TextWatcher() {
      public void afterTextChanged(Editable var1) {
         DropdownMenuEndIconDelegate var2 = DropdownMenuEndIconDelegate.this;
         final AutoCompleteTextView var3 = var2.castAutoCompleteTextViewOrThrow(var2.textInputLayout.getEditText());
         var3.post(new Runnable() {
            public void run() {
               boolean var1 = var3.isPopupShowing();
               DropdownMenuEndIconDelegate.this.setEndIconChecked(var1);
               DropdownMenuEndIconDelegate.this.dropdownPopupDirty = var1;
            }
         });
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }
   };
   private ValueAnimator fadeInAnim;
   private ValueAnimator fadeOutAnim;
   private StateListDrawable filledPopupBackground;
   private boolean isEndIconChecked;
   private MaterialShapeDrawable outlinedPopupBackground;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      IS_LOLLIPOP = var0;
   }

   DropdownMenuEndIconDelegate(TextInputLayout var1) {
      super(var1);
      this.accessibilityDelegate = new TextInputLayout.AccessibilityDelegate(this.textInputLayout) {
         public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
            super.onInitializeAccessibilityNodeInfo(var1, var2);
            var2.setClassName(Spinner.class.getName());
            if (var2.isShowingHintText()) {
               var2.setHintText((CharSequence)null);
            }

         }

         public void onPopulateAccessibilityEvent(View var1, AccessibilityEvent var2) {
            super.onPopulateAccessibilityEvent(var1, var2);
            DropdownMenuEndIconDelegate var3 = DropdownMenuEndIconDelegate.this;
            AutoCompleteTextView var4 = var3.castAutoCompleteTextViewOrThrow(var3.textInputLayout.getEditText());
            if (var2.getEventType() == 1 && DropdownMenuEndIconDelegate.this.accessibilityManager.isTouchExplorationEnabled()) {
               DropdownMenuEndIconDelegate.this.showHideDropdown(var4);
            }

         }
      };
      this.dropdownMenuOnEditTextAttachedListener = new TextInputLayout.OnEditTextAttachedListener() {
         public void onEditTextAttached(TextInputLayout var1) {
            AutoCompleteTextView var2 = DropdownMenuEndIconDelegate.this.castAutoCompleteTextViewOrThrow(var1.getEditText());
            DropdownMenuEndIconDelegate.this.setPopupBackground(var2);
            DropdownMenuEndIconDelegate.this.addRippleEffect(var2);
            DropdownMenuEndIconDelegate.this.setUpDropdownShowHideBehavior(var2);
            var2.setThreshold(0);
            var2.removeTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
            var2.addTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
            var1.setErrorIconDrawable((Drawable)null);
            var1.setTextInputAccessibilityDelegate(DropdownMenuEndIconDelegate.this.accessibilityDelegate);
            var1.setEndIconVisible(true);
         }
      };
      this.dropdownPopupDirty = false;
      this.isEndIconChecked = false;
      this.dropdownPopupActivatedAt = Long.MAX_VALUE;
   }

   private void addRippleEffect(AutoCompleteTextView var1) {
      if (var1.getKeyListener() == null) {
         int var2 = this.textInputLayout.getBoxBackgroundMode();
         MaterialShapeDrawable var4 = this.textInputLayout.getBoxBackground();
         int var3 = MaterialColors.getColor(var1, attr.colorControlHighlight);
         int[][] var5 = new int[][]{{16842919}, new int[0]};
         if (var2 == 2) {
            this.addRippleEffectOnOutlinedLayout(var1, var3, var5, var4);
         } else {
            if (var2 == 1) {
               this.addRippleEffectOnFilledLayout(var1, var3, var5, var4);
            }

         }
      }
   }

   private void addRippleEffectOnFilledLayout(AutoCompleteTextView var1, int var2, int[][] var3, MaterialShapeDrawable var4) {
      int var5 = this.textInputLayout.getBoxBackgroundColor();
      var2 = MaterialColors.layer(var2, var5, 0.1F);
      int[] var8 = new int[]{var2, var5};
      if (IS_LOLLIPOP) {
         ViewCompat.setBackground(var1, new RippleDrawable(new ColorStateList(var3, var8), var4, var4));
      } else {
         MaterialShapeDrawable var9 = new MaterialShapeDrawable(var4.getShapeAppearanceModel());
         var9.setFillColor(new ColorStateList(var3, var8));
         LayerDrawable var10 = new LayerDrawable(new Drawable[]{var4, var9});
         var2 = ViewCompat.getPaddingStart(var1);
         var5 = var1.getPaddingTop();
         int var6 = ViewCompat.getPaddingEnd(var1);
         int var7 = var1.getPaddingBottom();
         ViewCompat.setBackground(var1, var10);
         ViewCompat.setPaddingRelative(var1, var2, var5, var6, var7);
      }
   }

   private void addRippleEffectOnOutlinedLayout(AutoCompleteTextView var1, int var2, int[][] var3, MaterialShapeDrawable var4) {
      int var5 = MaterialColors.getColor(var1, attr.colorSurface);
      MaterialShapeDrawable var6 = new MaterialShapeDrawable(var4.getShapeAppearanceModel());
      var2 = MaterialColors.layer(var2, var5, 0.1F);
      var6.setFillColor(new ColorStateList(var3, new int[]{var2, 0}));
      LayerDrawable var9;
      if (IS_LOLLIPOP) {
         var6.setTint(var5);
         ColorStateList var8 = new ColorStateList(var3, new int[]{var2, var5});
         MaterialShapeDrawable var7 = new MaterialShapeDrawable(var4.getShapeAppearanceModel());
         var7.setTint(-1);
         var9 = new LayerDrawable(new Drawable[]{new RippleDrawable(var8, var6, var7), var4});
      } else {
         var9 = new LayerDrawable(new Drawable[]{var6, var4});
      }

      ViewCompat.setBackground(var1, var9);
   }

   private AutoCompleteTextView castAutoCompleteTextViewOrThrow(EditText var1) {
      if (var1 instanceof AutoCompleteTextView) {
         return (AutoCompleteTextView)var1;
      } else {
         throw new RuntimeException("EditText needs to be an AutoCompleteTextView if an Exposed Dropdown Menu is being used.");
      }
   }

   private ValueAnimator getAlphaAnimator(int var1, float... var2) {
      ValueAnimator var3 = ValueAnimator.ofFloat(var2);
      var3.setInterpolator(AnimationUtils.LINEAR_INTERPOLATOR);
      var3.setDuration((long)var1);
      var3.addUpdateListener(new AnimatorUpdateListener() {
         public void onAnimationUpdate(ValueAnimator var1) {
            float var2 = (Float)var1.getAnimatedValue();
            DropdownMenuEndIconDelegate.this.endIconView.setAlpha(var2);
         }
      });
      return var3;
   }

   private MaterialShapeDrawable getPopUpMaterialShapeDrawable(float var1, float var2, float var3, int var4) {
      ShapeAppearanceModel var5 = ShapeAppearanceModel.builder().setTopLeftCornerSize(var1).setTopRightCornerSize(var1).setBottomLeftCornerSize(var2).setBottomRightCornerSize(var2).build();
      MaterialShapeDrawable var6 = MaterialShapeDrawable.createWithElevationOverlay(this.context, var3);
      var6.setShapeAppearanceModel(var5);
      var6.setPadding(0, var4, 0, var4);
      return var6;
   }

   private void initAnimators() {
      this.fadeInAnim = this.getAlphaAnimator(67, 0.0F, 1.0F);
      ValueAnimator var1 = this.getAlphaAnimator(50, 1.0F, 0.0F);
      this.fadeOutAnim = var1;
      var1.addListener(new AnimatorListenerAdapter() {
         public void onAnimationEnd(Animator var1) {
            DropdownMenuEndIconDelegate.this.endIconView.setChecked(DropdownMenuEndIconDelegate.this.isEndIconChecked);
            DropdownMenuEndIconDelegate.this.fadeInAnim.start();
         }
      });
   }

   private boolean isDropdownPopupActive() {
      long var1 = System.currentTimeMillis() - this.dropdownPopupActivatedAt;
      return var1 < 0L || var1 > 300L;
   }

   private void setEndIconChecked(boolean var1) {
      if (this.isEndIconChecked != var1) {
         this.isEndIconChecked = var1;
         this.fadeInAnim.cancel();
         this.fadeOutAnim.start();
      }

   }

   private void setPopupBackground(AutoCompleteTextView var1) {
      if (IS_LOLLIPOP) {
         int var2 = this.textInputLayout.getBoxBackgroundMode();
         if (var2 == 2) {
            var1.setDropDownBackgroundDrawable(this.outlinedPopupBackground);
            return;
         }

         if (var2 == 1) {
            var1.setDropDownBackgroundDrawable(this.filledPopupBackground);
         }
      }

   }

   private void setUpDropdownShowHideBehavior(final AutoCompleteTextView var1) {
      var1.setOnTouchListener(new OnTouchListener() {
         public boolean onTouch(View var1x, MotionEvent var2) {
            if (var2.getAction() == 1) {
               if (DropdownMenuEndIconDelegate.this.isDropdownPopupActive()) {
                  DropdownMenuEndIconDelegate.this.dropdownPopupDirty = false;
               }

               DropdownMenuEndIconDelegate.this.showHideDropdown(var1);
               var1x.performClick();
            }

            return false;
         }
      });
      var1.setOnFocusChangeListener(new OnFocusChangeListener() {
         public void onFocusChange(View var1, boolean var2) {
            DropdownMenuEndIconDelegate.this.textInputLayout.setEndIconActivated(var2);
            if (!var2) {
               DropdownMenuEndIconDelegate.this.setEndIconChecked(false);
               DropdownMenuEndIconDelegate.this.dropdownPopupDirty = false;
            }

         }
      });
      if (IS_LOLLIPOP) {
         var1.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
               DropdownMenuEndIconDelegate.this.dropdownPopupDirty = true;
               DropdownMenuEndIconDelegate.this.dropdownPopupActivatedAt = System.currentTimeMillis();
               DropdownMenuEndIconDelegate.this.setEndIconChecked(false);
            }
         });
      }

   }

   private void showHideDropdown(AutoCompleteTextView var1) {
      if (var1 != null) {
         if (this.isDropdownPopupActive()) {
            this.dropdownPopupDirty = false;
         }

         if (!this.dropdownPopupDirty) {
            if (IS_LOLLIPOP) {
               this.setEndIconChecked(this.isEndIconChecked ^ true);
            } else {
               this.isEndIconChecked ^= true;
               this.endIconView.toggle();
            }

            if (this.isEndIconChecked) {
               var1.requestFocus();
               var1.showDropDown();
            } else {
               var1.dismissDropDown();
            }
         } else {
            this.dropdownPopupDirty = false;
         }
      }
   }

   void initialize() {
      float var1 = (float)this.context.getResources().getDimensionPixelOffset(dimen.mtrl_shape_corner_size_small_component);
      float var2 = (float)this.context.getResources().getDimensionPixelOffset(dimen.mtrl_exposed_dropdown_menu_popup_elevation);
      int var3 = this.context.getResources().getDimensionPixelOffset(dimen.mtrl_exposed_dropdown_menu_popup_vertical_padding);
      MaterialShapeDrawable var4 = this.getPopUpMaterialShapeDrawable(var1, var1, var2, var3);
      MaterialShapeDrawable var5 = this.getPopUpMaterialShapeDrawable(0.0F, var1, var2, var3);
      this.outlinedPopupBackground = var4;
      StateListDrawable var6 = new StateListDrawable();
      this.filledPopupBackground = var6;
      var6.addState(new int[]{16842922}, var4);
      this.filledPopupBackground.addState(new int[0], var5);
      if (IS_LOLLIPOP) {
         var3 = drawable.mtrl_dropdown_arrow;
      } else {
         var3 = drawable.mtrl_ic_arrow_drop_down;
      }

      this.textInputLayout.setEndIconDrawable(AppCompatResources.getDrawable(this.context, var3));
      this.textInputLayout.setEndIconContentDescription(this.textInputLayout.getResources().getText(string.exposed_dropdown_menu_content_description));
      this.textInputLayout.setEndIconOnClickListener(new OnClickListener() {
         public void onClick(View var1) {
            AutoCompleteTextView var2 = (AutoCompleteTextView)DropdownMenuEndIconDelegate.this.textInputLayout.getEditText();
            DropdownMenuEndIconDelegate.this.showHideDropdown(var2);
         }
      });
      this.textInputLayout.addOnEditTextAttachedListener(this.dropdownMenuOnEditTextAttachedListener);
      this.initAnimators();
      ViewCompat.setImportantForAccessibility(this.endIconView, 2);
      this.accessibilityManager = (AccessibilityManager)this.context.getSystemService("accessibility");
   }

   boolean isBoxBackgroundModeSupported(int var1) {
      return var1 != 0;
   }

   boolean shouldTintIconOnError() {
      return true;
   }
}
