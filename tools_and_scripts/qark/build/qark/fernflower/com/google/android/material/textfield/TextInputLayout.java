package com.google.android.material.textfield;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.DrawableUtils;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R.attr;
import com.google.android.material.R.color;
import com.google.android.material.R.id;
import com.google.android.material.R.string;
import com.google.android.material.R.style;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class TextInputLayout extends LinearLayout {
   public static final int BOX_BACKGROUND_FILLED = 1;
   public static final int BOX_BACKGROUND_NONE = 0;
   public static final int BOX_BACKGROUND_OUTLINE = 2;
   private static final int DEF_STYLE_RES;
   public static final int END_ICON_CLEAR_TEXT = 2;
   public static final int END_ICON_CUSTOM = -1;
   public static final int END_ICON_DROPDOWN_MENU = 3;
   public static final int END_ICON_NONE = 0;
   public static final int END_ICON_PASSWORD_TOGGLE = 1;
   private static final int INVALID_MAX_LENGTH = -1;
   private static final int LABEL_SCALE_ANIMATION_DURATION = 167;
   private static final String LOG_TAG = "TextInputLayout";
   private ValueAnimator animator;
   private MaterialShapeDrawable boxBackground;
   private int boxBackgroundColor;
   private int boxBackgroundMode;
   private final int boxCollapsedPaddingTopPx;
   private final int boxLabelCutoutPaddingPx;
   private int boxStrokeColor;
   private final int boxStrokeWidthDefaultPx;
   private final int boxStrokeWidthFocusedPx;
   private int boxStrokeWidthPx;
   private MaterialShapeDrawable boxUnderline;
   final CollapsingTextHelper collapsingTextHelper;
   boolean counterEnabled;
   private int counterMaxLength;
   private int counterOverflowTextAppearance;
   private ColorStateList counterOverflowTextColor;
   private boolean counterOverflowed;
   private int counterTextAppearance;
   private ColorStateList counterTextColor;
   private TextView counterView;
   private int defaultFilledBackgroundColor;
   private ColorStateList defaultHintTextColor;
   private final int defaultStrokeColor;
   private final int disabledColor;
   private final int disabledFilledBackgroundColor;
   EditText editText;
   private final LinkedHashSet editTextAttachedListeners;
   private final LinkedHashSet endIconChangedListeners;
   private final SparseArray endIconDelegates;
   private Drawable endIconDummyDrawable;
   private final FrameLayout endIconFrame;
   private int endIconMode;
   private OnLongClickListener endIconOnLongClickListener;
   private ColorStateList endIconTintList;
   private Mode endIconTintMode;
   private final CheckableImageButton endIconView;
   private final CheckableImageButton errorIconView;
   private int focusedStrokeColor;
   private ColorStateList focusedTextColor;
   private boolean hasEndIconTintList;
   private boolean hasEndIconTintMode;
   private boolean hasStartIconTintList;
   private boolean hasStartIconTintMode;
   private CharSequence hint;
   private boolean hintAnimationEnabled;
   private boolean hintEnabled;
   private boolean hintExpanded;
   private final int hoveredFilledBackgroundColor;
   private final int hoveredStrokeColor;
   private boolean inDrawableStateChanged;
   private final IndicatorViewController indicatorViewController;
   private final FrameLayout inputFrame;
   private boolean isProvidingHint;
   private Drawable originalEditTextEndDrawable;
   private CharSequence originalHint;
   private boolean restoringSavedState;
   private ShapeAppearanceModel shapeAppearanceModel;
   private Drawable startIconDummyDrawable;
   private OnLongClickListener startIconOnLongClickListener;
   private ColorStateList startIconTintList;
   private Mode startIconTintMode;
   private final CheckableImageButton startIconView;
   private final Rect tmpBoundsRect;
   private final Rect tmpRect;
   private final RectF tmpRectF;
   private Typeface typeface;

   static {
      DEF_STYLE_RES = style.Widget_Design_TextInputLayout;
   }

   public TextInputLayout(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TextInputLayout(Context var1, AttributeSet var2) {
      this(var1, var2, attr.textInputStyle);
   }

   public TextInputLayout(Context var1, AttributeSet var2, int var3) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   private void applyBoxAttributes() {
      MaterialShapeDrawable var2 = this.boxBackground;
      if (var2 != null) {
         var2.setShapeAppearanceModel(this.shapeAppearanceModel);
         if (this.canDrawOutlineStroke()) {
            this.boxBackground.setStroke((float)this.boxStrokeWidthPx, this.boxStrokeColor);
         }

         int var1 = this.calculateBoxBackgroundColor();
         this.boxBackgroundColor = var1;
         this.boxBackground.setFillColor(ColorStateList.valueOf(var1));
         if (this.endIconMode == 3) {
            this.editText.getBackground().invalidateSelf();
         }

         this.applyBoxUnderlineAttributes();
         this.invalidate();
      }
   }

   private void applyBoxUnderlineAttributes() {
      if (this.boxUnderline != null) {
         if (this.canDrawStroke()) {
            this.boxUnderline.setFillColor(ColorStateList.valueOf(this.boxStrokeColor));
         }

         this.invalidate();
      }
   }

   private void applyCutoutPadding(RectF var1) {
      var1.left -= (float)this.boxLabelCutoutPaddingPx;
      var1.top -= (float)this.boxLabelCutoutPaddingPx;
      var1.right += (float)this.boxLabelCutoutPaddingPx;
      var1.bottom += (float)this.boxLabelCutoutPaddingPx;
   }

   private void applyEndIconTint() {
      this.applyIconTint(this.endIconView, this.hasEndIconTintList, this.endIconTintList, this.hasEndIconTintMode, this.endIconTintMode);
   }

   private void applyIconTint(CheckableImageButton var1, boolean var2, ColorStateList var3, boolean var4, Mode var5) {
      Drawable var7 = var1.getDrawable();
      Drawable var6 = var7;
      if (var7 != null) {
         label26: {
            if (!var2) {
               var6 = var7;
               if (!var4) {
                  break label26;
               }
            }

            var7 = DrawableCompat.wrap(var7).mutate();
            if (var2) {
               DrawableCompat.setTintList(var7, var3);
            }

            var6 = var7;
            if (var4) {
               DrawableCompat.setTintMode(var7, var5);
               var6 = var7;
            }
         }
      }

      if (var1.getDrawable() != var6) {
         var1.setImageDrawable(var6);
      }

   }

   private void applyStartIconTint() {
      this.applyIconTint(this.startIconView, this.hasStartIconTintList, this.startIconTintList, this.hasStartIconTintMode, this.startIconTintMode);
   }

   private void assignBoxBackgroundByMode() {
      int var1 = this.boxBackgroundMode;
      if (var1 == 0) {
         this.boxBackground = null;
         this.boxUnderline = null;
      } else if (var1 != 1) {
         if (var1 != 2) {
            StringBuilder var2 = new StringBuilder();
            var2.append(this.boxBackgroundMode);
            var2.append(" is illegal; only @BoxBackgroundMode constants are supported.");
            throw new IllegalArgumentException(var2.toString());
         } else {
            if (this.hintEnabled && !(this.boxBackground instanceof CutoutDrawable)) {
               this.boxBackground = new CutoutDrawable(this.shapeAppearanceModel);
            } else {
               this.boxBackground = new MaterialShapeDrawable(this.shapeAppearanceModel);
            }

            this.boxUnderline = null;
         }
      } else {
         this.boxBackground = new MaterialShapeDrawable(this.shapeAppearanceModel);
         this.boxUnderline = new MaterialShapeDrawable();
      }
   }

   private int calculateBoxBackgroundColor() {
      int var1 = this.boxBackgroundColor;
      if (this.boxBackgroundMode == 1) {
         var1 = MaterialColors.layer(MaterialColors.getColor((View)this, attr.colorSurface, 0), this.boxBackgroundColor);
      }

      return var1;
   }

   private Rect calculateCollapsedTextBounds(Rect var1) {
      if (this.editText != null) {
         Rect var3 = this.tmpBoundsRect;
         var3.bottom = var1.bottom;
         int var2 = this.boxBackgroundMode;
         if (var2 != 1) {
            if (var2 != 2) {
               var3.left = var1.left + this.editText.getCompoundPaddingLeft();
               var3.top = this.getPaddingTop();
               var3.right = var1.right - this.editText.getCompoundPaddingRight();
               return var3;
            } else {
               var3.left = var1.left + this.editText.getPaddingLeft();
               var3.top = var1.top - this.calculateLabelMarginTop();
               var3.right = var1.right - this.editText.getPaddingRight();
               return var3;
            }
         } else {
            var3.left = var1.left + this.editText.getCompoundPaddingLeft();
            var3.top = var1.top + this.boxCollapsedPaddingTopPx;
            var3.right = var1.right - this.editText.getCompoundPaddingRight();
            return var3;
         }
      } else {
         throw new IllegalStateException();
      }
   }

   private int calculateExpandedLabelBottom(Rect var1, Rect var2, float var3) {
      return this.boxBackgroundMode == 1 ? (int)((float)var2.top + var3) : var1.bottom - this.editText.getCompoundPaddingBottom();
   }

   private int calculateExpandedLabelTop(Rect var1, float var2) {
      return this.isSingleLineFilledTextField() ? (int)((float)var1.centerY() - var2 / 2.0F) : var1.top + this.editText.getCompoundPaddingTop();
   }

   private Rect calculateExpandedTextBounds(Rect var1) {
      if (this.editText != null) {
         Rect var3 = this.tmpBoundsRect;
         float var2 = this.collapsingTextHelper.getExpandedTextHeight();
         var3.left = var1.left + this.editText.getCompoundPaddingLeft();
         var3.top = this.calculateExpandedLabelTop(var1, var2);
         var3.right = var1.right - this.editText.getCompoundPaddingRight();
         var3.bottom = this.calculateExpandedLabelBottom(var1, var3, var2);
         return var3;
      } else {
         throw new IllegalStateException();
      }
   }

   private int calculateLabelMarginTop() {
      if (!this.hintEnabled) {
         return 0;
      } else {
         int var1 = this.boxBackgroundMode;
         if (var1 != 0 && var1 != 1) {
            return var1 != 2 ? 0 : (int)(this.collapsingTextHelper.getCollapsedTextHeight() / 2.0F);
         } else {
            return (int)this.collapsingTextHelper.getCollapsedTextHeight();
         }
      }
   }

   private boolean canDrawOutlineStroke() {
      return this.boxBackgroundMode == 2 && this.canDrawStroke();
   }

   private boolean canDrawStroke() {
      return this.boxStrokeWidthPx > -1 && this.boxStrokeColor != 0;
   }

   private void closeCutout() {
      if (this.cutoutEnabled()) {
         ((CutoutDrawable)this.boxBackground).removeCutout();
      }

   }

   private void collapseHint(boolean var1) {
      ValueAnimator var2 = this.animator;
      if (var2 != null && var2.isRunning()) {
         this.animator.cancel();
      }

      if (var1 && this.hintAnimationEnabled) {
         this.animateToExpansionFraction(1.0F);
      } else {
         this.collapsingTextHelper.setExpansionFraction(1.0F);
      }

      this.hintExpanded = false;
      if (this.cutoutEnabled()) {
         this.openCutout();
      }

   }

   private boolean cutoutEnabled() {
      return this.hintEnabled && !TextUtils.isEmpty(this.hint) && this.boxBackground instanceof CutoutDrawable;
   }

   private void dispatchOnEditTextAttached() {
      Iterator var1 = this.editTextAttachedListeners.iterator();

      while(var1.hasNext()) {
         ((TextInputLayout.OnEditTextAttachedListener)var1.next()).onEditTextAttached(this);
      }

   }

   private void dispatchOnEndIconChanged(int var1) {
      Iterator var2 = this.endIconChangedListeners.iterator();

      while(var2.hasNext()) {
         ((TextInputLayout.OnEndIconChangedListener)var2.next()).onEndIconChanged(this, var1);
      }

   }

   private void drawBoxUnderline(Canvas var1) {
      MaterialShapeDrawable var2 = this.boxUnderline;
      if (var2 != null) {
         Rect var3 = var2.getBounds();
         var3.top = var3.bottom - this.boxStrokeWidthPx;
         this.boxUnderline.draw(var1);
      }

   }

   private void drawHint(Canvas var1) {
      if (this.hintEnabled) {
         this.collapsingTextHelper.draw(var1);
      }

   }

   private void expandHint(boolean var1) {
      ValueAnimator var2 = this.animator;
      if (var2 != null && var2.isRunning()) {
         this.animator.cancel();
      }

      if (var1 && this.hintAnimationEnabled) {
         this.animateToExpansionFraction(0.0F);
      } else {
         this.collapsingTextHelper.setExpansionFraction(0.0F);
      }

      if (this.cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout()) {
         this.closeCutout();
      }

      this.hintExpanded = true;
   }

   private EndIconDelegate getEndIconDelegate() {
      EndIconDelegate var1 = (EndIconDelegate)this.endIconDelegates.get(this.endIconMode);
      return var1 != null ? var1 : (EndIconDelegate)this.endIconDelegates.get(0);
   }

   private CheckableImageButton getEndIconToUpdateDummyDrawable() {
      if (this.errorIconView.getVisibility() == 0) {
         return this.errorIconView;
      } else {
         return this.hasEndIcon() && this.isEndIconVisible() ? this.endIconView : null;
      }
   }

   private boolean hasEndIcon() {
      return this.endIconMode != 0;
   }

   private boolean hasStartIcon() {
      return this.getStartIconDrawable() != null;
   }

   private boolean isSingleLineFilledTextField() {
      return this.boxBackgroundMode == 1 && (VERSION.SDK_INT < 16 || this.editText.getMinLines() <= 1);
   }

   private void onApplyBoxBackgroundMode() {
      this.assignBoxBackgroundByMode();
      this.setEditTextBoxBackground();
      this.updateTextInputBoxState();
      if (this.boxBackgroundMode != 0) {
         this.updateInputLayoutMargins();
      }

   }

   private void openCutout() {
      if (this.cutoutEnabled()) {
         RectF var1 = this.tmpRectF;
         this.collapsingTextHelper.getCollapsedTextActualBounds(var1);
         this.applyCutoutPadding(var1);
         var1.offset((float)(-this.getPaddingLeft()), 0.0F);
         ((CutoutDrawable)this.boxBackground).setCutout(var1);
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

   private void setEditText(EditText var1) {
      if (this.editText == null) {
         if (this.endIconMode != 3 && !(var1 instanceof TextInputEditText)) {
            Log.i("TextInputLayout", "EditText added is not a TextInputEditText. Please switch to using that class instead.");
         }

         this.editText = var1;
         this.onApplyBoxBackgroundMode();
         this.setTextInputAccessibilityDelegate(new TextInputLayout.AccessibilityDelegate(this));
         this.collapsingTextHelper.setTypefaces(this.editText.getTypeface());
         this.collapsingTextHelper.setExpandedTextSize(this.editText.getTextSize());
         int var2 = this.editText.getGravity();
         this.collapsingTextHelper.setCollapsedTextGravity(var2 & -113 | 48);
         this.collapsingTextHelper.setExpandedTextGravity(var2);
         this.editText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable var1) {
               TextInputLayout var2 = TextInputLayout.this;
               var2.updateLabelState(var2.restoringSavedState ^ true);
               if (TextInputLayout.this.counterEnabled) {
                  TextInputLayout.this.updateCounter(var1.length());
               }

            }

            public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            }
         });
         if (this.defaultHintTextColor == null) {
            this.defaultHintTextColor = this.editText.getHintTextColors();
         }

         if (this.hintEnabled) {
            if (TextUtils.isEmpty(this.hint)) {
               CharSequence var3 = this.editText.getHint();
               this.originalHint = var3;
               this.setHint(var3);
               this.editText.setHint((CharSequence)null);
            }

            this.isProvidingHint = true;
         }

         if (this.counterView != null) {
            this.updateCounter(this.editText.getText().length());
         }

         this.updateEditTextBackground();
         this.indicatorViewController.adjustIndicatorPadding();
         this.startIconView.bringToFront();
         this.endIconFrame.bringToFront();
         this.errorIconView.bringToFront();
         this.dispatchOnEditTextAttached();
         this.updateLabelState(false, true);
      } else {
         throw new IllegalArgumentException("We already have an EditText, can only have one");
      }
   }

   private void setEditTextBoxBackground() {
      if (this.shouldUseEditTextBackgroundForBoxBackground()) {
         ViewCompat.setBackground(this.editText, this.boxBackground);
      }

   }

   private void setErrorIconVisible(boolean var1) {
      CheckableImageButton var4 = this.errorIconView;
      byte var3 = 0;
      byte var2;
      if (var1) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      var4.setVisibility(var2);
      FrameLayout var5 = this.endIconFrame;
      var2 = var3;
      if (var1) {
         var2 = 8;
      }

      var5.setVisibility(var2);
      if (!this.hasEndIcon()) {
         this.updateIconDummyDrawables();
      }

   }

   private void setHintInternal(CharSequence var1) {
      if (!TextUtils.equals(var1, this.hint)) {
         this.hint = var1;
         this.collapsingTextHelper.setText(var1);
         if (!this.hintExpanded) {
            this.openCutout();
         }
      }

   }

   private static void setIconClickable(CheckableImageButton var0, OnLongClickListener var1) {
      boolean var5 = ViewCompat.hasOnClickListeners(var0);
      boolean var4 = false;
      byte var2 = 1;
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      if (var5 || var3) {
         var4 = true;
      }

      var0.setFocusable(var4);
      var0.setClickable(var5);
      var0.setPressable(var5);
      var0.setLongClickable(var3);
      if (!var4) {
         var2 = 2;
      }

      ViewCompat.setImportantForAccessibility(var0, var2);
   }

   private static void setIconOnClickListener(CheckableImageButton var0, OnClickListener var1, OnLongClickListener var2) {
      var0.setOnClickListener(var1);
      setIconClickable(var0, var2);
   }

   private static void setIconOnLongClickListener(CheckableImageButton var0, OnLongClickListener var1) {
      var0.setOnLongClickListener(var1);
      setIconClickable(var0, var1);
   }

   private boolean shouldUseEditTextBackgroundForBoxBackground() {
      EditText var1 = this.editText;
      return var1 != null && this.boxBackground != null && var1.getBackground() == null && this.boxBackgroundMode != 0;
   }

   private void tintEndIconOnError(boolean var1) {
      if (var1 && this.getEndIconDrawable() != null) {
         Drawable var2 = DrawableCompat.wrap(this.getEndIconDrawable()).mutate();
         DrawableCompat.setTint(var2, this.indicatorViewController.getErrorViewCurrentTextColor());
         this.endIconView.setImageDrawable(var2);
      } else {
         this.applyEndIconTint();
      }
   }

   private void updateBoxUnderlineBounds(Rect var1) {
      if (this.boxUnderline != null) {
         int var2 = var1.bottom;
         int var3 = this.boxStrokeWidthFocusedPx;
         this.boxUnderline.setBounds(var1.left, var2 - var3, var1.right, var1.bottom);
      }

   }

   private void updateCounter() {
      if (this.counterView != null) {
         EditText var2 = this.editText;
         int var1;
         if (var2 == null) {
            var1 = 0;
         } else {
            var1 = var2.getText().length();
         }

         this.updateCounter(var1);
      }

   }

   private static void updateCounterContentDescription(Context var0, TextView var1, int var2, int var3, boolean var4) {
      int var5;
      if (var4) {
         var5 = string.character_counter_overflowed_content_description;
      } else {
         var5 = string.character_counter_content_description;
      }

      var1.setContentDescription(var0.getString(var5, new Object[]{var2, var3}));
   }

   private void updateCounterTextAppearanceAndColor() {
      TextView var2 = this.counterView;
      if (var2 != null) {
         int var1;
         if (this.counterOverflowed) {
            var1 = this.counterOverflowTextAppearance;
         } else {
            var1 = this.counterTextAppearance;
         }

         this.setTextAppearanceCompatWithErrorFallback(var2, var1);
         ColorStateList var3;
         if (!this.counterOverflowed) {
            var3 = this.counterTextColor;
            if (var3 != null) {
               this.counterView.setTextColor(var3);
            }
         }

         if (this.counterOverflowed) {
            var3 = this.counterOverflowTextColor;
            if (var3 != null) {
               this.counterView.setTextColor(var3);
            }
         }
      }

   }

   private boolean updateEditTextHeightBasedOnIcon() {
      if (this.editText == null) {
         return false;
      } else {
         int var1 = Math.max(this.endIconView.getMeasuredHeight(), this.startIconView.getMeasuredHeight());
         if (this.editText.getMeasuredHeight() < var1) {
            this.editText.setMinimumHeight(var1);
            return true;
         } else {
            return false;
         }
      }
   }

   private boolean updateIconDummyDrawables() {
      if (this.editText == null) {
         return false;
      } else {
         boolean var4 = false;
         int var1;
         int var2;
         int var3;
         Drawable[] var6;
         Drawable var7;
         Drawable var8;
         if (this.hasStartIcon() && this.isStartIconVisible() && this.startIconView.getMeasuredWidth() > 0) {
            if (this.startIconDummyDrawable == null) {
               this.startIconDummyDrawable = new ColorDrawable();
               var1 = this.startIconView.getMeasuredWidth();
               var2 = this.editText.getPaddingLeft();
               var3 = MarginLayoutParamsCompat.getMarginEnd((MarginLayoutParams)this.startIconView.getLayoutParams());
               this.startIconDummyDrawable.setBounds(0, 0, var1 - var2 + var3, 1);
            }

            var6 = TextViewCompat.getCompoundDrawablesRelative(this.editText);
            var7 = var6[0];
            var8 = this.startIconDummyDrawable;
            if (var7 != var8) {
               TextViewCompat.setCompoundDrawablesRelative(this.editText, var8, var6[1], var6[2], var6[3]);
               var4 = true;
            }
         } else if (this.startIconDummyDrawable != null) {
            var6 = TextViewCompat.getCompoundDrawablesRelative(this.editText);
            TextViewCompat.setCompoundDrawablesRelative(this.editText, (Drawable)null, var6[1], var6[2], var6[3]);
            this.startIconDummyDrawable = null;
            var4 = true;
         }

         CheckableImageButton var9 = this.getEndIconToUpdateDummyDrawable();
         boolean var5;
         if (var9 != null && var9.getMeasuredWidth() > 0) {
            if (this.endIconDummyDrawable == null) {
               this.endIconDummyDrawable = new ColorDrawable();
               var1 = var9.getMeasuredWidth();
               var2 = this.editText.getPaddingRight();
               var3 = MarginLayoutParamsCompat.getMarginStart((MarginLayoutParams)var9.getLayoutParams());
               this.endIconDummyDrawable.setBounds(0, 0, var1 - var2 + var3, 1);
            }

            var6 = TextViewCompat.getCompoundDrawablesRelative(this.editText);
            var7 = var6[2];
            var8 = this.endIconDummyDrawable;
            var5 = var4;
            if (var7 != var8) {
               this.originalEditTextEndDrawable = var6[2];
               TextViewCompat.setCompoundDrawablesRelative(this.editText, var6[0], var6[1], var8, var6[3]);
               var5 = true;
            }
         } else {
            var5 = var4;
            if (this.endIconDummyDrawable != null) {
               var6 = TextViewCompat.getCompoundDrawablesRelative(this.editText);
               if (var6[2] == this.endIconDummyDrawable) {
                  TextViewCompat.setCompoundDrawablesRelative(this.editText, var6[0], var6[1], this.originalEditTextEndDrawable, var6[3]);
                  var4 = true;
               }

               this.endIconDummyDrawable = null;
               return var4;
            }
         }

         return var5;
      }
   }

   private void updateInputLayoutMargins() {
      if (this.boxBackgroundMode != 1) {
         LayoutParams var2 = (LayoutParams)this.inputFrame.getLayoutParams();
         int var1 = this.calculateLabelMarginTop();
         if (var1 != var2.topMargin) {
            var2.topMargin = var1;
            this.inputFrame.requestLayout();
         }
      }

   }

   private void updateLabelState(boolean var1, boolean var2) {
      boolean var5 = this.isEnabled();
      EditText var7 = this.editText;
      boolean var4 = true;
      boolean var3;
      if (var7 != null && !TextUtils.isEmpty(var7.getText())) {
         var3 = true;
      } else {
         var3 = false;
      }

      var7 = this.editText;
      if (var7 == null || !var7.hasFocus()) {
         var4 = false;
      }

      boolean var6 = this.indicatorViewController.errorShouldBeShown();
      ColorStateList var8 = this.defaultHintTextColor;
      if (var8 != null) {
         this.collapsingTextHelper.setCollapsedTextColor(var8);
         this.collapsingTextHelper.setExpandedTextColor(this.defaultHintTextColor);
      }

      if (!var5) {
         this.collapsingTextHelper.setCollapsedTextColor(ColorStateList.valueOf(this.disabledColor));
         this.collapsingTextHelper.setExpandedTextColor(ColorStateList.valueOf(this.disabledColor));
      } else if (var6) {
         this.collapsingTextHelper.setCollapsedTextColor(this.indicatorViewController.getErrorViewTextColors());
      } else {
         label60: {
            if (this.counterOverflowed) {
               TextView var9 = this.counterView;
               if (var9 != null) {
                  this.collapsingTextHelper.setCollapsedTextColor(var9.getTextColors());
                  break label60;
               }
            }

            if (var4) {
               var8 = this.focusedTextColor;
               if (var8 != null) {
                  this.collapsingTextHelper.setCollapsedTextColor(var8);
               }
            }
         }
      }

      if (var3 || this.isEnabled() && (var4 || var6)) {
         if (var2 || this.hintExpanded) {
            this.collapseHint(var1);
         }
      } else if (var2 || !this.hintExpanded) {
         this.expandHint(var1);
         return;
      }

   }

   public void addOnEditTextAttachedListener(TextInputLayout.OnEditTextAttachedListener var1) {
      this.editTextAttachedListeners.add(var1);
      if (this.editText != null) {
         var1.onEditTextAttached(this);
      }

   }

   public void addOnEndIconChangedListener(TextInputLayout.OnEndIconChangedListener var1) {
      this.endIconChangedListeners.add(var1);
   }

   public void addView(View var1, int var2, android.view.ViewGroup.LayoutParams var3) {
      if (var1 instanceof EditText) {
         android.widget.FrameLayout.LayoutParams var4 = new android.widget.FrameLayout.LayoutParams(var3);
         var4.gravity = var4.gravity & -113 | 16;
         this.inputFrame.addView(var1, var4);
         this.inputFrame.setLayoutParams(var3);
         this.updateInputLayoutMargins();
         this.setEditText((EditText)var1);
      } else {
         super.addView(var1, var2, var3);
      }
   }

   void animateToExpansionFraction(float var1) {
      if (this.collapsingTextHelper.getExpansionFraction() != var1) {
         if (this.animator == null) {
            ValueAnimator var2 = new ValueAnimator();
            this.animator = var2;
            var2.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.animator.setDuration(167L);
            this.animator.addUpdateListener(new AnimatorUpdateListener() {
               public void onAnimationUpdate(ValueAnimator var1) {
                  TextInputLayout.this.collapsingTextHelper.setExpansionFraction((Float)var1.getAnimatedValue());
               }
            });
         }

         this.animator.setFloatValues(new float[]{this.collapsingTextHelper.getExpansionFraction(), var1});
         this.animator.start();
      }
   }

   public void clearOnEditTextAttachedListeners() {
      this.editTextAttachedListeners.clear();
   }

   public void clearOnEndIconChangedListeners() {
      this.endIconChangedListeners.clear();
   }

   boolean cutoutIsOpen() {
      return this.cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout();
   }

   public void dispatchProvideAutofillStructure(ViewStructure var1, int var2) {
      if (this.originalHint != null) {
         EditText var4 = this.editText;
         if (var4 != null) {
            boolean var3 = this.isProvidingHint;
            this.isProvidingHint = false;
            CharSequence var7 = var4.getHint();
            this.editText.setHint(this.originalHint);

            try {
               super.dispatchProvideAutofillStructure(var1, var2);
            } finally {
               this.editText.setHint(var7);
               this.isProvidingHint = var3;
            }

            return;
         }
      }

      super.dispatchProvideAutofillStructure(var1, var2);
   }

   protected void dispatchRestoreInstanceState(SparseArray var1) {
      this.restoringSavedState = true;
      super.dispatchRestoreInstanceState(var1);
      this.restoringSavedState = false;
   }

   public void draw(Canvas var1) {
      super.draw(var1);
      this.drawHint(var1);
      this.drawBoxUnderline(var1);
   }

   protected void drawableStateChanged() {
      if (!this.inDrawableStateChanged) {
         boolean var2 = true;
         this.inDrawableStateChanged = true;
         super.drawableStateChanged();
         int[] var3 = this.getDrawableState();
         boolean var1 = false;
         CollapsingTextHelper var4 = this.collapsingTextHelper;
         if (var4 != null) {
            var1 = false | var4.setState(var3);
         }

         if (!ViewCompat.isLaidOut(this) || !this.isEnabled()) {
            var2 = false;
         }

         this.updateLabelState(var2);
         this.updateEditTextBackground();
         this.updateTextInputBoxState();
         if (var1) {
            this.invalidate();
         }

         this.inDrawableStateChanged = false;
      }
   }

   public int getBaseline() {
      EditText var1 = this.editText;
      return var1 != null ? var1.getBaseline() + this.getPaddingTop() + this.calculateLabelMarginTop() : super.getBaseline();
   }

   MaterialShapeDrawable getBoxBackground() {
      int var1 = this.boxBackgroundMode;
      if (var1 != 1 && var1 != 2) {
         throw new IllegalStateException();
      } else {
         return this.boxBackground;
      }
   }

   public int getBoxBackgroundColor() {
      return this.boxBackgroundColor;
   }

   public int getBoxBackgroundMode() {
      return this.boxBackgroundMode;
   }

   public float getBoxCornerRadiusBottomEnd() {
      return this.boxBackground.getBottomLeftCornerResolvedSize();
   }

   public float getBoxCornerRadiusBottomStart() {
      return this.boxBackground.getBottomRightCornerResolvedSize();
   }

   public float getBoxCornerRadiusTopEnd() {
      return this.boxBackground.getTopRightCornerResolvedSize();
   }

   public float getBoxCornerRadiusTopStart() {
      return this.boxBackground.getTopLeftCornerResolvedSize();
   }

   public int getBoxStrokeColor() {
      return this.focusedStrokeColor;
   }

   public int getCounterMaxLength() {
      return this.counterMaxLength;
   }

   CharSequence getCounterOverflowDescription() {
      if (this.counterEnabled && this.counterOverflowed) {
         TextView var1 = this.counterView;
         if (var1 != null) {
            return var1.getContentDescription();
         }
      }

      return null;
   }

   public ColorStateList getCounterOverflowTextColor() {
      return this.counterTextColor;
   }

   public ColorStateList getCounterTextColor() {
      return this.counterTextColor;
   }

   public ColorStateList getDefaultHintTextColor() {
      return this.defaultHintTextColor;
   }

   public EditText getEditText() {
      return this.editText;
   }

   public CharSequence getEndIconContentDescription() {
      return this.endIconView.getContentDescription();
   }

   public Drawable getEndIconDrawable() {
      return this.endIconView.getDrawable();
   }

   public int getEndIconMode() {
      return this.endIconMode;
   }

   CheckableImageButton getEndIconView() {
      return this.endIconView;
   }

   public CharSequence getError() {
      return this.indicatorViewController.isErrorEnabled() ? this.indicatorViewController.getErrorText() : null;
   }

   public int getErrorCurrentTextColors() {
      return this.indicatorViewController.getErrorViewCurrentTextColor();
   }

   public Drawable getErrorIconDrawable() {
      return this.errorIconView.getDrawable();
   }

   final int getErrorTextCurrentColor() {
      return this.indicatorViewController.getErrorViewCurrentTextColor();
   }

   public CharSequence getHelperText() {
      return this.indicatorViewController.isHelperTextEnabled() ? this.indicatorViewController.getHelperText() : null;
   }

   public int getHelperTextCurrentTextColor() {
      return this.indicatorViewController.getHelperTextViewCurrentTextColor();
   }

   public CharSequence getHint() {
      return this.hintEnabled ? this.hint : null;
   }

   final float getHintCollapsedTextHeight() {
      return this.collapsingTextHelper.getCollapsedTextHeight();
   }

   final int getHintCurrentCollapsedTextColor() {
      return this.collapsingTextHelper.getCurrentCollapsedTextColor();
   }

   public ColorStateList getHintTextColor() {
      return this.focusedTextColor;
   }

   @Deprecated
   public CharSequence getPasswordVisibilityToggleContentDescription() {
      return this.endIconView.getContentDescription();
   }

   @Deprecated
   public Drawable getPasswordVisibilityToggleDrawable() {
      return this.endIconView.getDrawable();
   }

   public CharSequence getStartIconContentDescription() {
      return this.startIconView.getContentDescription();
   }

   public Drawable getStartIconDrawable() {
      return this.startIconView.getDrawable();
   }

   public Typeface getTypeface() {
      return this.typeface;
   }

   public boolean isCounterEnabled() {
      return this.counterEnabled;
   }

   public boolean isEndIconCheckable() {
      return this.endIconView.isCheckable();
   }

   public boolean isEndIconVisible() {
      return this.endIconFrame.getVisibility() == 0 && this.endIconView.getVisibility() == 0;
   }

   public boolean isErrorEnabled() {
      return this.indicatorViewController.isErrorEnabled();
   }

   final boolean isHelperTextDisplayed() {
      return this.indicatorViewController.helperTextIsDisplayed();
   }

   public boolean isHelperTextEnabled() {
      return this.indicatorViewController.isHelperTextEnabled();
   }

   public boolean isHintAnimationEnabled() {
      return this.hintAnimationEnabled;
   }

   public boolean isHintEnabled() {
      return this.hintEnabled;
   }

   final boolean isHintExpanded() {
      return this.hintExpanded;
   }

   @Deprecated
   public boolean isPasswordVisibilityToggleEnabled() {
      return this.endIconMode == 1;
   }

   boolean isProvidingHint() {
      return this.isProvidingHint;
   }

   public boolean isStartIconCheckable() {
      return this.startIconView.isCheckable();
   }

   public boolean isStartIconVisible() {
      return this.startIconView.getVisibility() == 0;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      EditText var6 = this.editText;
      if (var6 != null) {
         Rect var7 = this.tmpRect;
         DescendantOffsetUtils.getDescendantRect(this, var6, var7);
         this.updateBoxUnderlineBounds(var7);
         if (this.hintEnabled) {
            this.collapsingTextHelper.setCollapsedBounds(this.calculateCollapsedTextBounds(var7));
            this.collapsingTextHelper.setExpandedBounds(this.calculateExpandedTextBounds(var7));
            this.collapsingTextHelper.recalculate();
            if (this.cutoutEnabled() && !this.hintExpanded) {
               this.openCutout();
            }
         }
      }

   }

   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      boolean var3 = this.updateEditTextHeightBasedOnIcon();
      boolean var4 = this.updateIconDummyDrawables();
      if (var3 || var4) {
         this.editText.post(new Runnable() {
            public void run() {
               TextInputLayout.this.editText.requestLayout();
            }
         });
      }

   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof TextInputLayout.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         TextInputLayout.SavedState var2 = (TextInputLayout.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.setError(var2.error);
         if (var2.isEndIconChecked) {
            this.endIconView.post(new Runnable() {
               public void run() {
                  TextInputLayout.this.endIconView.performClick();
                  TextInputLayout.this.endIconView.jumpDrawablesToCurrentState();
               }
            });
         }

         this.requestLayout();
      }
   }

   public Parcelable onSaveInstanceState() {
      TextInputLayout.SavedState var2 = new TextInputLayout.SavedState(super.onSaveInstanceState());
      if (this.indicatorViewController.errorShouldBeShown()) {
         var2.error = this.getError();
      }

      boolean var1;
      if (this.hasEndIcon() && this.endIconView.isChecked()) {
         var1 = true;
      } else {
         var1 = false;
      }

      var2.isEndIconChecked = var1;
      return var2;
   }

   @Deprecated
   public void passwordVisibilityToggleRequested(boolean var1) {
      if (this.endIconMode == 1) {
         this.endIconView.performClick();
         if (var1) {
            this.endIconView.jumpDrawablesToCurrentState();
         }
      }

   }

   public void removeOnEditTextAttachedListener(TextInputLayout.OnEditTextAttachedListener var1) {
      this.editTextAttachedListeners.remove(var1);
   }

   public void removeOnEndIconChangedListener(TextInputLayout.OnEndIconChangedListener var1) {
      this.endIconChangedListeners.remove(var1);
   }

   public void setBoxBackgroundColor(int var1) {
      if (this.boxBackgroundColor != var1) {
         this.boxBackgroundColor = var1;
         this.defaultFilledBackgroundColor = var1;
         this.applyBoxAttributes();
      }

   }

   public void setBoxBackgroundColorResource(int var1) {
      this.setBoxBackgroundColor(ContextCompat.getColor(this.getContext(), var1));
   }

   public void setBoxBackgroundMode(int var1) {
      if (var1 != this.boxBackgroundMode) {
         this.boxBackgroundMode = var1;
         if (this.editText != null) {
            this.onApplyBoxBackgroundMode();
         }

      }
   }

   public void setBoxCornerRadii(float var1, float var2, float var3, float var4) {
      if (this.boxBackground.getTopLeftCornerResolvedSize() != var1 || this.boxBackground.getTopRightCornerResolvedSize() != var2 || this.boxBackground.getBottomRightCornerResolvedSize() != var4 || this.boxBackground.getBottomLeftCornerResolvedSize() != var3) {
         this.shapeAppearanceModel = this.shapeAppearanceModel.toBuilder().setTopLeftCornerSize(var1).setTopRightCornerSize(var2).setBottomRightCornerSize(var4).setBottomLeftCornerSize(var3).build();
         this.applyBoxAttributes();
      }

   }

   public void setBoxCornerRadiiResources(int var1, int var2, int var3, int var4) {
      this.setBoxCornerRadii(this.getContext().getResources().getDimension(var1), this.getContext().getResources().getDimension(var2), this.getContext().getResources().getDimension(var4), this.getContext().getResources().getDimension(var3));
   }

   public void setBoxStrokeColor(int var1) {
      if (this.focusedStrokeColor != var1) {
         this.focusedStrokeColor = var1;
         this.updateTextInputBoxState();
      }

   }

   public void setCounterEnabled(boolean var1) {
      if (this.counterEnabled != var1) {
         if (var1) {
            AppCompatTextView var2 = new AppCompatTextView(this.getContext());
            this.counterView = var2;
            var2.setId(id.textinput_counter);
            Typeface var3 = this.typeface;
            if (var3 != null) {
               this.counterView.setTypeface(var3);
            }

            this.counterView.setMaxLines(1);
            this.indicatorViewController.addIndicator(this.counterView, 2);
            this.updateCounterTextAppearanceAndColor();
            this.updateCounter();
         } else {
            this.indicatorViewController.removeIndicator(this.counterView, 2);
            this.counterView = null;
         }

         this.counterEnabled = var1;
      }

   }

   public void setCounterMaxLength(int var1) {
      if (this.counterMaxLength != var1) {
         if (var1 > 0) {
            this.counterMaxLength = var1;
         } else {
            this.counterMaxLength = -1;
         }

         if (this.counterEnabled) {
            this.updateCounter();
         }
      }

   }

   public void setCounterOverflowTextAppearance(int var1) {
      if (this.counterOverflowTextAppearance != var1) {
         this.counterOverflowTextAppearance = var1;
         this.updateCounterTextAppearanceAndColor();
      }

   }

   public void setCounterOverflowTextColor(ColorStateList var1) {
      if (this.counterOverflowTextColor != var1) {
         this.counterOverflowTextColor = var1;
         this.updateCounterTextAppearanceAndColor();
      }

   }

   public void setCounterTextAppearance(int var1) {
      if (this.counterTextAppearance != var1) {
         this.counterTextAppearance = var1;
         this.updateCounterTextAppearanceAndColor();
      }

   }

   public void setCounterTextColor(ColorStateList var1) {
      if (this.counterTextColor != var1) {
         this.counterTextColor = var1;
         this.updateCounterTextAppearanceAndColor();
      }

   }

   public void setDefaultHintTextColor(ColorStateList var1) {
      this.defaultHintTextColor = var1;
      this.focusedTextColor = var1;
      if (this.editText != null) {
         this.updateLabelState(false);
      }

   }

   public void setEnabled(boolean var1) {
      recursiveSetEnabled(this, var1);
      super.setEnabled(var1);
   }

   public void setEndIconActivated(boolean var1) {
      this.endIconView.setActivated(var1);
   }

   public void setEndIconCheckable(boolean var1) {
      this.endIconView.setCheckable(var1);
   }

   public void setEndIconContentDescription(int var1) {
      CharSequence var2;
      if (var1 != 0) {
         var2 = this.getResources().getText(var1);
      } else {
         var2 = null;
      }

      this.setEndIconContentDescription(var2);
   }

   public void setEndIconContentDescription(CharSequence var1) {
      if (this.getEndIconContentDescription() != var1) {
         this.endIconView.setContentDescription(var1);
      }

   }

   public void setEndIconDrawable(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setEndIconDrawable(var2);
   }

   public void setEndIconDrawable(Drawable var1) {
      this.endIconView.setImageDrawable(var1);
   }

   public void setEndIconMode(int var1) {
      int var2 = this.endIconMode;
      this.endIconMode = var1;
      boolean var3;
      if (var1 != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.setEndIconVisible(var3);
      if (this.getEndIconDelegate().isBoxBackgroundModeSupported(this.boxBackgroundMode)) {
         this.getEndIconDelegate().initialize();
         this.applyEndIconTint();
         this.dispatchOnEndIconChanged(var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("The current box background mode ");
         var4.append(this.boxBackgroundMode);
         var4.append(" is not supported by the end icon mode ");
         var4.append(var1);
         throw new IllegalStateException(var4.toString());
      }
   }

   public void setEndIconOnClickListener(OnClickListener var1) {
      setIconOnClickListener(this.endIconView, var1, this.endIconOnLongClickListener);
   }

   public void setEndIconOnLongClickListener(OnLongClickListener var1) {
      this.endIconOnLongClickListener = var1;
      setIconOnLongClickListener(this.endIconView, var1);
   }

   public void setEndIconTintList(ColorStateList var1) {
      if (this.endIconTintList != var1) {
         this.endIconTintList = var1;
         this.hasEndIconTintList = true;
         this.applyEndIconTint();
      }

   }

   public void setEndIconTintMode(Mode var1) {
      if (this.endIconTintMode != var1) {
         this.endIconTintMode = var1;
         this.hasEndIconTintMode = true;
         this.applyEndIconTint();
      }

   }

   public void setEndIconVisible(boolean var1) {
      if (this.isEndIconVisible() != var1) {
         CheckableImageButton var3 = this.endIconView;
         byte var2;
         if (var1) {
            var2 = 0;
         } else {
            var2 = 4;
         }

         var3.setVisibility(var2);
         this.updateIconDummyDrawables();
      }

   }

   public void setError(CharSequence var1) {
      if (!this.indicatorViewController.isErrorEnabled()) {
         if (TextUtils.isEmpty(var1)) {
            return;
         }

         this.setErrorEnabled(true);
      }

      if (!TextUtils.isEmpty(var1)) {
         this.indicatorViewController.showError(var1);
      } else {
         this.indicatorViewController.hideError();
      }
   }

   public void setErrorEnabled(boolean var1) {
      this.indicatorViewController.setErrorEnabled(var1);
   }

   public void setErrorIconDrawable(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setErrorIconDrawable(var2);
   }

   public void setErrorIconDrawable(Drawable var1) {
      this.errorIconView.setImageDrawable(var1);
      boolean var2;
      if (var1 != null && this.indicatorViewController.isErrorEnabled()) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.setErrorIconVisible(var2);
   }

   public void setErrorIconTintList(ColorStateList var1) {
      Drawable var3 = this.errorIconView.getDrawable();
      Drawable var2 = var3;
      if (var3 != null) {
         var2 = DrawableCompat.wrap(var3).mutate();
         DrawableCompat.setTintList(var2, var1);
      }

      if (this.errorIconView.getDrawable() != var2) {
         this.errorIconView.setImageDrawable(var2);
      }

   }

   public void setErrorIconTintMode(Mode var1) {
      Drawable var3 = this.errorIconView.getDrawable();
      Drawable var2 = var3;
      if (var3 != null) {
         var2 = DrawableCompat.wrap(var3).mutate();
         DrawableCompat.setTintMode(var2, var1);
      }

      if (this.errorIconView.getDrawable() != var2) {
         this.errorIconView.setImageDrawable(var2);
      }

   }

   public void setErrorTextAppearance(int var1) {
      this.indicatorViewController.setErrorTextAppearance(var1);
   }

   public void setErrorTextColor(ColorStateList var1) {
      this.indicatorViewController.setErrorViewTextColor(var1);
   }

   public void setHelperText(CharSequence var1) {
      if (TextUtils.isEmpty(var1)) {
         if (this.isHelperTextEnabled()) {
            this.setHelperTextEnabled(false);
            return;
         }
      } else {
         if (!this.isHelperTextEnabled()) {
            this.setHelperTextEnabled(true);
         }

         this.indicatorViewController.showHelper(var1);
      }

   }

   public void setHelperTextColor(ColorStateList var1) {
      this.indicatorViewController.setHelperTextViewTextColor(var1);
   }

   public void setHelperTextEnabled(boolean var1) {
      this.indicatorViewController.setHelperTextEnabled(var1);
   }

   public void setHelperTextTextAppearance(int var1) {
      this.indicatorViewController.setHelperTextAppearance(var1);
   }

   public void setHint(CharSequence var1) {
      if (this.hintEnabled) {
         this.setHintInternal(var1);
         this.sendAccessibilityEvent(2048);
      }

   }

   public void setHintAnimationEnabled(boolean var1) {
      this.hintAnimationEnabled = var1;
   }

   public void setHintEnabled(boolean var1) {
      if (var1 != this.hintEnabled) {
         this.hintEnabled = var1;
         if (!var1) {
            this.isProvidingHint = false;
            if (!TextUtils.isEmpty(this.hint) && TextUtils.isEmpty(this.editText.getHint())) {
               this.editText.setHint(this.hint);
            }

            this.setHintInternal((CharSequence)null);
         } else {
            CharSequence var2 = this.editText.getHint();
            if (!TextUtils.isEmpty(var2)) {
               if (TextUtils.isEmpty(this.hint)) {
                  this.setHint(var2);
               }

               this.editText.setHint((CharSequence)null);
            }

            this.isProvidingHint = true;
         }

         if (this.editText != null) {
            this.updateInputLayoutMargins();
         }
      }

   }

   public void setHintTextAppearance(int var1) {
      this.collapsingTextHelper.setCollapsedTextAppearance(var1);
      this.focusedTextColor = this.collapsingTextHelper.getCollapsedTextColor();
      if (this.editText != null) {
         this.updateLabelState(false);
         this.updateInputLayoutMargins();
      }

   }

   public void setHintTextColor(ColorStateList var1) {
      if (this.focusedTextColor != var1) {
         if (this.defaultHintTextColor == null) {
            this.collapsingTextHelper.setCollapsedTextColor(var1);
         }

         this.focusedTextColor = var1;
         if (this.editText != null) {
            this.updateLabelState(false);
         }
      }

   }

   @Deprecated
   public void setPasswordVisibilityToggleContentDescription(int var1) {
      CharSequence var2;
      if (var1 != 0) {
         var2 = this.getResources().getText(var1);
      } else {
         var2 = null;
      }

      this.setPasswordVisibilityToggleContentDescription(var2);
   }

   @Deprecated
   public void setPasswordVisibilityToggleContentDescription(CharSequence var1) {
      this.endIconView.setContentDescription(var1);
   }

   @Deprecated
   public void setPasswordVisibilityToggleDrawable(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setPasswordVisibilityToggleDrawable(var2);
   }

   @Deprecated
   public void setPasswordVisibilityToggleDrawable(Drawable var1) {
      this.endIconView.setImageDrawable(var1);
   }

   @Deprecated
   public void setPasswordVisibilityToggleEnabled(boolean var1) {
      if (var1 && this.endIconMode != 1) {
         this.setEndIconMode(1);
      } else {
         if (!var1) {
            this.setEndIconMode(0);
         }

      }
   }

   @Deprecated
   public void setPasswordVisibilityToggleTintList(ColorStateList var1) {
      this.endIconTintList = var1;
      this.hasEndIconTintList = true;
      this.applyEndIconTint();
   }

   @Deprecated
   public void setPasswordVisibilityToggleTintMode(Mode var1) {
      this.endIconTintMode = var1;
      this.hasEndIconTintMode = true;
      this.applyEndIconTint();
   }

   public void setStartIconCheckable(boolean var1) {
      this.startIconView.setCheckable(var1);
   }

   public void setStartIconContentDescription(int var1) {
      CharSequence var2;
      if (var1 != 0) {
         var2 = this.getResources().getText(var1);
      } else {
         var2 = null;
      }

      this.setStartIconContentDescription(var2);
   }

   public void setStartIconContentDescription(CharSequence var1) {
      if (this.getStartIconContentDescription() != var1) {
         this.startIconView.setContentDescription(var1);
      }

   }

   public void setStartIconDrawable(int var1) {
      Drawable var2;
      if (var1 != 0) {
         var2 = AppCompatResources.getDrawable(this.getContext(), var1);
      } else {
         var2 = null;
      }

      this.setStartIconDrawable(var2);
   }

   public void setStartIconDrawable(Drawable var1) {
      this.startIconView.setImageDrawable(var1);
      if (var1 != null) {
         this.setStartIconVisible(true);
         this.applyStartIconTint();
      } else {
         this.setStartIconVisible(false);
         this.setStartIconOnClickListener((OnClickListener)null);
         this.setStartIconOnLongClickListener((OnLongClickListener)null);
         this.setStartIconContentDescription((CharSequence)null);
      }
   }

   public void setStartIconOnClickListener(OnClickListener var1) {
      setIconOnClickListener(this.startIconView, var1, this.startIconOnLongClickListener);
   }

   public void setStartIconOnLongClickListener(OnLongClickListener var1) {
      this.startIconOnLongClickListener = var1;
      setIconOnLongClickListener(this.startIconView, var1);
   }

   public void setStartIconTintList(ColorStateList var1) {
      if (this.startIconTintList != var1) {
         this.startIconTintList = var1;
         this.hasStartIconTintList = true;
         this.applyStartIconTint();
      }

   }

   public void setStartIconTintMode(Mode var1) {
      if (this.startIconTintMode != var1) {
         this.startIconTintMode = var1;
         this.hasStartIconTintMode = true;
         this.applyStartIconTint();
      }

   }

   public void setStartIconVisible(boolean var1) {
      if (this.isStartIconVisible() != var1) {
         CheckableImageButton var3 = this.startIconView;
         byte var2;
         if (var1) {
            var2 = 0;
         } else {
            var2 = 8;
         }

         var3.setVisibility(var2);
         this.updateIconDummyDrawables();
      }

   }

   void setTextAppearanceCompatWithErrorFallback(TextView var1, int var2) {
      boolean var3 = false;

      boolean var8;
      label35: {
         int var4;
         label34: {
            label39: {
               boolean var10001;
               try {
                  TextViewCompat.setTextAppearance(var1, var2);
               } catch (Exception var7) {
                  var10001 = false;
                  break label39;
               }

               var8 = var3;

               try {
                  if (VERSION.SDK_INT < 23) {
                     break label35;
                  }

                  var4 = var1.getTextColors().getDefaultColor();
                  break label34;
               } catch (Exception var6) {
                  var10001 = false;
               }
            }

            var8 = true;
            break label35;
         }

         var8 = var3;
         if (var4 == -65281) {
            var8 = true;
         }
      }

      if (var8) {
         TextViewCompat.setTextAppearance(var1, style.TextAppearance_AppCompat_Caption);
         var1.setTextColor(ContextCompat.getColor(this.getContext(), color.design_error));
      }

   }

   public void setTextInputAccessibilityDelegate(TextInputLayout.AccessibilityDelegate var1) {
      EditText var2 = this.editText;
      if (var2 != null) {
         ViewCompat.setAccessibilityDelegate(var2, var1);
      }

   }

   public void setTypeface(Typeface var1) {
      if (var1 != this.typeface) {
         this.typeface = var1;
         this.collapsingTextHelper.setTypefaces(var1);
         this.indicatorViewController.setTypefaces(var1);
         TextView var2 = this.counterView;
         if (var2 != null) {
            var2.setTypeface(var1);
         }
      }

   }

   void updateCounter(int var1) {
      boolean var3 = this.counterOverflowed;
      if (this.counterMaxLength == -1) {
         this.counterView.setText(String.valueOf(var1));
         this.counterView.setContentDescription((CharSequence)null);
         this.counterOverflowed = false;
      } else {
         if (ViewCompat.getAccessibilityLiveRegion(this.counterView) == 1) {
            ViewCompat.setAccessibilityLiveRegion(this.counterView, 0);
         }

         boolean var2;
         if (var1 > this.counterMaxLength) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.counterOverflowed = var2;
         updateCounterContentDescription(this.getContext(), this.counterView, var1, this.counterMaxLength, this.counterOverflowed);
         if (var3 != this.counterOverflowed) {
            this.updateCounterTextAppearanceAndColor();
            if (this.counterOverflowed) {
               ViewCompat.setAccessibilityLiveRegion(this.counterView, 1);
            }
         }

         this.counterView.setText(this.getContext().getString(string.character_counter_pattern, new Object[]{var1, this.counterMaxLength}));
      }

      if (this.editText != null && var3 != this.counterOverflowed) {
         this.updateLabelState(false);
         this.updateTextInputBoxState();
         this.updateEditTextBackground();
      }

   }

   void updateEditTextBackground() {
      EditText var1 = this.editText;
      if (var1 != null) {
         if (this.boxBackgroundMode == 0) {
            Drawable var2 = var1.getBackground();
            if (var2 != null) {
               Drawable var3 = var2;
               if (DrawableUtils.canSafelyMutateDrawable(var2)) {
                  var3 = var2.mutate();
               }

               if (this.indicatorViewController.errorShouldBeShown()) {
                  var3.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(this.indicatorViewController.getErrorViewCurrentTextColor(), Mode.SRC_IN));
               } else {
                  if (this.counterOverflowed) {
                     TextView var4 = this.counterView;
                     if (var4 != null) {
                        var3.setColorFilter(AppCompatDrawableManager.getPorterDuffColorFilter(var4.getCurrentTextColor(), Mode.SRC_IN));
                        return;
                     }
                  }

                  DrawableCompat.clearColorFilter(var3);
                  this.editText.refreshDrawableState();
               }
            }
         }
      }
   }

   void updateLabelState(boolean var1) {
      this.updateLabelState(var1, false);
   }

   void updateTextInputBoxState() {
      if (this.boxBackground != null) {
         if (this.boxBackgroundMode != 0) {
            boolean var1;
            boolean var3;
            boolean var4;
            EditText var5;
            label101: {
               var3 = this.isFocused();
               var4 = false;
               if (!var3) {
                  var5 = this.editText;
                  if (var5 == null || !var5.hasFocus()) {
                     var1 = false;
                     break label101;
                  }
               }

               var1 = true;
            }

            boolean var2;
            label92: {
               if (!this.isHovered()) {
                  var5 = this.editText;
                  if (var5 == null || !var5.isHovered()) {
                     var2 = false;
                     break label92;
                  }
               }

               var2 = true;
            }

            if (!this.isEnabled()) {
               this.boxStrokeColor = this.disabledColor;
            } else if (this.indicatorViewController.errorShouldBeShown()) {
               this.boxStrokeColor = this.indicatorViewController.getErrorViewCurrentTextColor();
            } else {
               label82: {
                  if (this.counterOverflowed) {
                     TextView var6 = this.counterView;
                     if (var6 != null) {
                        this.boxStrokeColor = var6.getCurrentTextColor();
                        break label82;
                     }
                  }

                  if (var1) {
                     this.boxStrokeColor = this.focusedStrokeColor;
                  } else if (var2) {
                     this.boxStrokeColor = this.hoveredStrokeColor;
                  } else {
                     this.boxStrokeColor = this.defaultStrokeColor;
                  }
               }
            }

            if (this.indicatorViewController.errorShouldBeShown() && this.getEndIconDelegate().shouldTintIconOnError()) {
               var3 = true;
            } else {
               var3 = false;
            }

            this.tintEndIconOnError(var3);
            if (this.getErrorIconDrawable() != null && this.indicatorViewController.isErrorEnabled() && this.indicatorViewController.errorShouldBeShown()) {
               var3 = true;
            } else {
               var3 = var4;
            }

            this.setErrorIconVisible(var3);
            if ((var2 || var1) && this.isEnabled()) {
               this.boxStrokeWidthPx = this.boxStrokeWidthFocusedPx;
            } else {
               this.boxStrokeWidthPx = this.boxStrokeWidthDefaultPx;
            }

            if (this.boxBackgroundMode == 1) {
               if (!this.isEnabled()) {
                  this.boxBackgroundColor = this.disabledFilledBackgroundColor;
               } else if (var2) {
                  this.boxBackgroundColor = this.hoveredFilledBackgroundColor;
               } else {
                  this.boxBackgroundColor = this.defaultFilledBackgroundColor;
               }
            }

            this.applyBoxAttributes();
         }
      }
   }

   public static class AccessibilityDelegate extends AccessibilityDelegateCompat {
      private final TextInputLayout layout;

      public AccessibilityDelegate(TextInputLayout var1) {
         this.layout = var1;
      }

      public void onInitializeAccessibilityNodeInfo(View var1, AccessibilityNodeInfoCompat var2) {
         super.onInitializeAccessibilityNodeInfo(var1, var2);
         EditText var12 = this.layout.getEditText();
         Editable var13;
         if (var12 != null) {
            var13 = var12.getText();
         } else {
            var13 = null;
         }

         CharSequence var11 = this.layout.getHint();
         CharSequence var9 = this.layout.getError();
         CharSequence var10 = this.layout.getCounterOverflowDescription();
         boolean var4 = TextUtils.isEmpty(var13) ^ true;
         boolean var5 = TextUtils.isEmpty(var11) ^ true;
         boolean var6 = TextUtils.isEmpty(var9) ^ true;
         boolean var8 = false;
         boolean var3;
         if (!var6 && TextUtils.isEmpty(var10)) {
            var3 = false;
         } else {
            var3 = true;
         }

         if (var4) {
            var2.setText(var13);
         } else if (var5) {
            var2.setText(var11);
         }

         if (var5) {
            var2.setHintText(var11);
            boolean var7 = var8;
            if (!var4) {
               var7 = var8;
               if (var5) {
                  var7 = true;
               }
            }

            var2.setShowingHintText(var7);
         }

         if (var3) {
            CharSequence var14;
            if (var6) {
               var14 = var9;
            } else {
               var14 = var10;
            }

            var2.setError(var14);
            var2.setContentInvalid(true);
         }

      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface BoxBackgroundMode {
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface EndIconMode {
   }

   public interface OnEditTextAttachedListener {
      void onEditTextAttached(TextInputLayout var1);
   }

   public interface OnEndIconChangedListener {
      void onEndIconChanged(TextInputLayout var1, int var2);
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
      boolean isEndIconChecked;

      SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.error = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
         int var3 = var1.readInt();
         boolean var4 = true;
         if (var3 != 1) {
            var4 = false;
         }

         this.isEndIconChecked = var4;
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
}
